import {ENDPOINTS} from "@/util/endpoints.js";
import {customFetch} from "@/util/customFetch.js";
import {common} from "@/util/common.js";
import {successMessage} from "@/util/successMessage.js";

export const reception = {
    getDoctorList: async () => {

        try {

            const response = await customFetch(ENDPOINTS.doctor.list);

            if(response?.status === 200) {
                return response.data?.list;
            }

        } catch(err) {

            common.errMsg(err);

        }

    },
    getWaitingList: async (uuid) => {

        try {

            const response = await customFetch(ENDPOINTS.reception.getWaitingList(uuid));

            if(response?.status === 200) {
                return response.data?.waitingList;
            }

        } catch(err) {
            console.log("f");
            common.errMsg(err);
        }


    },
    getDoctorName : async () => {

        try {
            
            const response = await customFetch(ENDPOINTS.doctor.name('bbf7cf49-971c-47be-82cd-f613c633aa57')); // 테스트용 의사 번호 데이터
                // await customFetch(ENDPOINTS.doctor.name(localStorage.getItem('uuid')));

            if(response?.status === 200) {

                return response.data?.name;

            }

        } catch(err) {

            common.errMsg(err);

        }

    },
    getReceptionStatusList : async () =>  {

        try {

            const response = await customFetch(ENDPOINTS.reception.getReceptionStatusList());

            if(response?.status === 200) {
                return response.data?.statusList;
            }

        } catch(err) {

            common.errMsg(err);

        }

    },
    updateReceptionStatus : async ({uuid, updateStatus}) => {

        try {

            const response = await customFetch(ENDPOINTS.reception.updateReceptionStatus({uuid, updateStatus}));

            if(response?.status === 200) {

                if(response.data?.message !== undefined) {

                    common.alertError(response.data?.message);

                }

            }

        } catch(err) {

            common.errMsg(err);

        }



    }
}