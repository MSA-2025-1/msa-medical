import {ref} from 'vue'
import {customFetch} from "@/util/customFetch.js";
import {ENDPOINTS} from "@/util/endpoints.js";
import { useRouter } from "vue-router";
import {omit} from "lodash";
import {common} from "@/util/common.js";
import dayjs from "dayjs";

export const patientMethods = {
    cancelHoldingReservation: async () => {

        try{

            const response = await customFetch(ENDPOINTS.reservation.cancelHoldingReservation);

            if(response.status === 200) {
                return true;
            }

        } catch(err) {
            
            common.errMsg(err);
            return false;

        }

    },
    makeSlots : (date, loc, data) => {
        return `${date.hour().toString().padStart(loc, data)}:${date.minute().toString().padStart(loc, data)}`
    },
    generateTimeSlots : (reservationList, {reservationDate, isToday}) => {

        console.log("예약 데이터 처리 부분");
        console.log(reservationList);

        const alreadyReservatedSlots = new Set(
            reservationList.value.map(item => {
                return patientMethods.makeSlots(
                    dayjs(item.reservationDate),
                    2,
                    '0'
                );
            })
        );

        let start = reservationDate;
        let end = reservationDate;
        const availableSlots = new Set();

        console.log("당일 여부 : ", isToday);
        console.log("start : ", start);

        if(isToday && start.hour() >= 9) {
            start = start.minute(Math.floor(start.minute() / 10) * 10).second(0); // 초와 밀리초도 0으로 초기화
        } else {
            start = start.hour(9).minute(0).second(0);
        }

        console.log("시작 시각 : ", start);

        if(reservationDate.day() === 6) {
            end = end.hour(14).minute(0).second(0);
        } else {
            end = end.hour(18).minute(0).second(0);
        }

        console.log("종료 시각 : ", end);

        while (start <= end) {

            availableSlots.add(patientMethods.makeSlots(start, 2, '0'));
            start = start.minute(start.minute() + 10);

        }

        console.log("예약된 시간 목록", [...alreadyReservatedSlots]);
        console.log("전체 가능한 시간 목록", [...availableSlots]);


        const diff = new Set(
            [...availableSlots].filter(slot => {
                const [hh] = slot.split(':');
                return !alreadyReservatedSlots.has(slot) && hh !== '12';
            })
        );

        console.log("최종 목록 ", diff);

        return diff;

    },
    reservationTime : async (selectedVal) => {

        try{
            console.log("fetch 전송");
            const response = await customFetch(
                ENDPOINTS.reservation.reservationList(selectedVal),
            );

            if(response.status === 200) {

                console.log(response.data?.reservationList);
                return response.data?.reservationList;

            }

        } catch(err) {
            common.errMsg(err);
            return false;
        }

    },
    reservation : (selectedVal) => {

        console.log(selectedVal);

        customFetch(
            ENDPOINTS.reservation.reservation,
            {
                data: selectedVal
            }
        )
        .then(
            response => {
                if(response.status === 200) {
                    alert(response.data?.message);
                }
            }
        )
        .catch(
            err => {
                common.errMsg(err);
            }
        )
    },
    getReservationTime: async(selectedVal) => {

        const today = dayjs(new Date());
        const reservationList = ref([]);
        let reservationDate = dayjs(selectedVal.reservationDate);

        console.log("fetch에 전송하는 일시 : " + reservationDate);

        if(reservationDate.isSame(today, 'day')) {

            selectedVal.isToday = true;

            if(reservationDate.hour() > 8  && reservationDate.hour() < 18) {

                reservationDate = reservationDate.add(1, 'hour');
                console.log("당일 1시간 뒤 시간 : ", reservationDate);

            }

        }else {

            selectedVal.isToday = false;

        }

        console.log("시간 : ", reservationDate);
        console.log("인코딩 후 시간 : ", reservationDate);

        const response = await patientMethods.reservationTime({
            patientUuid : '550e8400-e29b-41d4-a716-446655440020', // 테스트용 환자 아이디
            ...omit(selectedVal, ['reservationDate', 'time', 'name']), // date와 time, name 속성을 제외한 나머지 속성들을 복사
            dateTime:
                reservationDate.toDate().toISOString()
                // `${dayjs(reservationDate).format('YYYY-MM-DDTHH:mm:00')}`
        });

        if(response === false) {
            return false;
        }

        reservationList.value = Array.isArray(response) ? response : [];

        return patientMethods.generateTimeSlots(
            reservationList, {reservationDate : reservationDate, isToday: selectedVal.isToday}
        );

    },
    getDoctorList : async () => {

        try {

            const response = await customFetch(ENDPOINTS.doctor.list); // 일반함수와 비동기 함수 완료 타이밍이 다르기 때문에 async와 await 추가

            if (response.status === 200) {
                return response.data?.list;
            }

        } catch (err) {
            common.errMsg(err);
            await useRouter().push({name: 'home'});
        }

    },
    reservationHold: async (selectedVal) => {

        console.log("예약 홀딩 데이터 : ", selectedVal);

        try{

            const response = await customFetch(ENDPOINTS.reservation.reservationHold, {
                data: selectedVal
            });

            if(response?.status === 200) {
                return;
            }

        } catch(err) {
            return common.errMsg(err);
        }
    }
}