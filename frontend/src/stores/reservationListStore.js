import { reception } from "@/reception/util/reception.js";
import { reservation } from "@/reservation/util/reservation.js";
import { status } from "@/status/util/status.js";
import { useUserStore } from "@/stores/userStore.js";
import { common } from "@/util/common.js";
import { errorMessage } from "@/util/errorMessage.js";
import { roles } from "@/util/roles.js";
import { defineStore } from "pinia";
import { computed, ref } from "vue";

export const useReservationListStore = defineStore('reservation', () =>  {

    const userInfo = computed(() => useUserStore().user);
    const doctorList = ref();
    const reservationStatusList = ref();
    const reservationList = ref();

    const getReservationStatusList = async () => {

        reservationStatusList.value = await status.getReservationStatusList();

    }

    const roleChk = async () => {

        console.log(userInfo.value.role);

        if(userInfo.value.role === roles.DOCTOR) {

            doctorList.value = [{
                name: userInfo.value.name,
                uuid: userInfo.value.uuid
            }]

        } else {

            doctorList.value = await reception.getDoctorList();

        }

    }

    const nullChk = async () => {

        if(doctorList.value == null) {

            common.alertError(errorMessage.common.retry);

        }

    }

    const promiseAll = async (date) => {

        await roleChk();

        await nullChk();

        reservationList.value = await Promise.all(

            doctorList.value.map(async (doctor) => {

                const list = await reservation.getReservationListByStaff(doctor.uuid, date);

                return {
                    doctor,
                    patientList: list
                }

            })
        );
    }

    return {
        promiseAll,
        getReservationStatusList,
        reservationList,
        reservationStatusList
    };

});