import {computed, reactive, ref} from 'vue'
import {customFetch} from "@/util/customFetch.js";
import {ENDPOINTS} from "@/util/endpoints.js";
import router from "@/router/index.js";
import {omit} from "lodash";
import {common} from "@/util/common.js";

export const patientMethods = {
    setReservationTime : computed((reservationList) => {



        const start = new Date();
        const end = new Date();
        const slots = [];



        start.setHours(9, 0, 0, 0); // 9:00 AM
        end.setHours(18, 0, 0, 0); // 6:00 PM

        while (start <= end) {

            const hours = start.getHours().toString().padStart(2, '0');
            const minutes = start.getMinutes().toString().padStart(2, '0');

            slots.push(`${hours}:${minutes}`);
            start.setMinutes(start.getMinutes() + 10);

        }

        return slots;

    }),
    reservationTime : async (selectedVal) => {

        try{

            const response = await customFetch(
                ENDPOINTS.patient.reservationList,
                {
                    data: selectedVal
                }
            );

            if(response.status === 200) {
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

    getReservationTime: (selectedVal) => {

        const today = new Date();

        // 오늘 날짜인지 검증해
        // 오늘 날짜면 현재 시간 기준 1시간 뒤로 시간 데이터 수정해서
        // reservationTime 함수 실행.
        if(selectedVal.date === today.getDate()) {

        }

        // 오늘 날짜가 아니면 그냥 그대로 보내기
        const reservationList = this.reservationTime(selectedVal);

        return null;
    }
}