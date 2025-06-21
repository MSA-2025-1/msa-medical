import {computed, reactive, ref} from 'vue'
import {customFetch} from "@/util/customFetch.js";
import {ENDPOINTS} from "@/util/endpoints.js";
import router from "@/router/index.js";
import {omit} from "lodash";
import {common} from "@/util/common.js";

export const patientMethods = {
    generateTimeSlots : reservationList => {

        console.log("예약 데이터 처리 부분");
        console.log(reservationList);

        const alreadyReservatedSlots = new Set(
            reservationList.value.map(item => {
                const date = new Date(item.reservationDate);
                const hours = date.getHours().toString().padStart(2, '0');
                const minutes = date.getMinutes().toString().padStart(2, '0');
                return `${hours}:${minutes}`;
            })
        );

        const start = new Date();
        const end = new Date();
        const availableSlots = new Set();

        start.setHours(9, 0, 0, 0); // 9:00 AM
        end.setHours(18, 0, 0, 0); // 6:00 PM

        while (start <= end) {

            const hours = start.getHours().toString().padStart(2, '0');
            const minutes = start.getMinutes().toString().padStart(2, '0');

            availableSlots.add(`${hours}:${minutes}`);
            start.setMinutes(start.getMinutes() + 10);

        }

        const diff = new Set(
            [...availableSlots].filter(slot => !alreadyReservatedSlots.has(slot))
        );

        console.log(diff);

        return diff;

    },
    reservationTime : async (selectedVal) => {

        try{
            console.log("fetch 전송");
            const response = await customFetch(
                ENDPOINTS.patient.reservationList,
                {
                    data: selectedVal
                }
            );

            if(response.status === 200) {

                console.log(response.data?.reservationList);
                return response.data?.reservationList;

            }

        } catch(err) {
            common.errMsg(err);
        }

    },
    reservation : async (selectedVal) => {

        console.log(selectedVal);

        await customFetch(
            ENDPOINTS.member.reservation,
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
    getDoctorList : async () => {

        try {

            const response = await customFetch(ENDPOINTS.doctor.list); // 일반함수와 비동기 함수 완료 타이밍이 다르기 때문에 async와 await 추가

            if (response.status === 200) {
                return response.data?.list;
            }

        } catch (err) {
            common.errMsg(err);
            await router.push({name: 'home'});
        }
    },

    getReservationTime: async(selectedVal) => {

        const today = new Date();
        const reservationList = ref([]);

        console.log("fetch에 전송하는 일시 : " + selectedVal.reservationDate);
        // 오늘 날짜인지 검증해
        // 오늘 날짜가 아니면 그냥 그대로 보내기
        if(selectedVal.reservationDate.toDateString() === today.toDateString()) {

            // 오늘 날짜면 현재 시간 기준 1시간 뒤로 시간 데이터 수정해서
            // reservationTime 함수 실행.
            if(selectedVal.reservationDate.getHours() > 8  && selectedVal.reservationDate.getHours() < 19) {
                console.log("a");
                selectedVal.reservationDate.setHours(selectedVal.reservationDate.getHours() + 1);
            }
            
        }

        const response = await patientMethods.reservationTime(selectedVal);
        reservationList.value = Array.isArray(response) ? response : [];
        
        return patientMethods.generateTimeSlots(
            reservationList
        );

    }
}