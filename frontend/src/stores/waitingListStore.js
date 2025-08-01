import { reception } from "@/reception/util/reception.js";
import { status } from "@/status/util/status.js";
import { useUserStore } from "@/stores/userStore.js";
import { common } from "@/util/common.js";
import { errorMessage } from "@/util/errorMessage.js";
import { defineStore } from "pinia";
import { computed, ref } from "vue";

export const useWaitingListStore = defineStore('waitingList', () => {

    const userInfo = computed(() => useUserStore().user);
    const doctorList = ref();
    const waitingList = ref();
    const receptionStatusList = ref();

    const getReceptionStatusList = async () => {

        receptionStatusList.value = await status.getReceptionStatusList();

    }


    const roleChk = async () => {

        console.log(userInfo.value.role);

        if(userInfo.value.role === 'DOCTOR') {

            doctorList.value = [{
                name: userInfo.value.name,
                uuid: userInfo.value.uuid
            }]

        } else {

            doctorList.value = await reception.getDoctorList();

        }

    }

    const nullChk = async () => {

        if(doctorList.value === null || doctorList.value === undefined) {

            common.alertError(errorMessage.common.retry);

        }

    }

    const promiseAll = async () => {

        await roleChk();

        await nullChk();

        console.log("실행 시 의사 정보 : ", doctorList.value);

        waitingList.value = await Promise.all(

            doctorList.value.map(async (doctor) => {

                const list = await reception.getWaitingList(doctor.uuid);

                return {
                    doctor,
                    patientList: list
                }

            })
        );
    }

    return {
        promiseAll,
        waitingList,
        getReceptionStatusList,
        receptionStatusList
    };

})