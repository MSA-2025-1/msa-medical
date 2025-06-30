import {defineStore} from "pinia";
import {computed, ref} from "vue";
import {reception} from "@/reception/util/reception.js";
import {errorMessage} from "@/util/errorMessage.js";
import {common} from "@/util/common.js";
import {useUserStore} from "@/stores/userStore.js";

export const useWaitingListStore = defineStore('waitingList', () => {

    const userInfo = computed(() => useUserStore().user);
    const doctorList = ref();
    const waitingList = ref();
    const receptionStatusList = ref();

    const getReceptionStatusList = async () => {

        receptionStatusList.value = await reception.getReceptionStatusList();

    }


    const roleChk = async () => {

        console.log(userInfo.value.role);

        if(userInfo.value.role === 'DOCTOR') {

            doctorList.value = [{
                name: userInfo.value.name
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

    const promiseAll = async () => {

        await roleChk();

        await nullChk();

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