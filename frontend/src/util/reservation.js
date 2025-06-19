import {computed, reactive, ref} from 'vue'
import {customFetch} from "@/util/customFetch.js";
import {ENDPOINTS} from "@/util/endpoints.js";
import router from "@/router/index.js";
import {omit} from "lodash";

export const patientMethods = {
    reservationTime : computed(() => {

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
    reservation : async (selectedVal) => {
        await customFetch(
            ENDPOINTS.member.reservation, {
                data: omit(selectedVal, ['name'])
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
                err.response?.data?.message ? alert(err.response?.data?.message) : alert("실행 중 오류가 발생했습니다. 다시 실행해주세요.");
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
            err.response?.data?.message ? alert(err.response?.data?.message) : alert("실행 중 오류가 발생했습니다. 다시 실행해주세요.");
            await router.push({name: 'home'});
        }
    }

}