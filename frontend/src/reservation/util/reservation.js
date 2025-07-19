import {computed, ref} from 'vue'
import {customFetch} from "@/util/customFetch.js";
import {ENDPOINTS} from "@/util/endpoints.js";
import {pick} from "lodash";
import {common} from "@/util/common.js";
import dayjs from "dayjs";
import {roles} from "@/util/roles.js";

export const reservation = {
    getReservationListPerPatient: async (uuid) => {

        try{

            const response = await customFetch(ENDPOINTS.reservation.getReservationListPerPatient(uuid));

            if(response.status === 200) {
                return response.data?.patientReservationList;
            }

        } catch(err) {

            common.errMsg(err);

        }

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
        getSlots: async(selectedVal) => {

        const today = dayjs(new Date());
        let reservationDate = dayjs(selectedVal.reservationDate);

        if(reservationDate.isSame(today, 'day')) {

            selectedVal.isToday = true;

            if(reservationDate.hour() > 8  && reservationDate.hour() < 18) {

                reservationDate = reservationDate.add(1, 'hour');

            }

        }else {

            selectedVal.isToday = false;

        }

        const reservatedSlots = await reservation.getReservatedSlots({
            ...pick(selectedVal, ['doctorUuid']),
            dateTime: reservationDate.toDate().toISOString()
        });

        if(reservatedSlots === false) {
            return false;
        }

        const allSlots = await reservation.getAllSlots({
            dateTime: reservationDate.toDate().toISOString()
        });

        if(allSlots === false) {
            return false;
        }

        return {reservatedSlots, allSlots}

    },
    getReservatedSlots : async (selectedVal) => {

        try{

            const response = await customFetch(
                ENDPOINTS.reservation.reservationList(selectedVal),
            );

            if(response.status === 200) {

                return response.data?.reservationList;

            }

        } catch(err) {

            common.errMsg(err);
            return false;

        }

    },
    getAllSlots: async({dateTime}) => {

        try{

            const response = await customFetch(ENDPOINTS.reservation.getAllSlots(dateTime));

            if(response.status === 200) {
                return response.data?.allSlots;
            }

        } catch(err) {

            common.errMsg(err);
            return false;

        }

    },
    getFullReservationList: async(uuid, date) => {

        try {

            const response = await customFetch(ENDPOINTS.reservation.getFullReservationList(uuid, date));

            if(response.status === 200) {
                return response.data?.reservationList;
            }

        } catch(err) {

            return common.errMsg(err);

        }

    },
    reservation : (selectedVal, router, role) => {

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

                    reservation.routeByRole(role, router);

                }
            }
        )
        .catch(
            err => {
                common.errMsg(err);
            }
        )
    },
    updateReservationStatus: async (data) => {

        try {

            const response = await customFetch(ENDPOINTS.reservation.updateReservationStatus(data));

            if(response?.status === 200) {

                if(response.data?.message !== undefined) {
                    common.alert(response.data?.message);
                }

            }

        } catch(err) {

            common.errMsg(err);

        }


    },
    cancelReservation: async (selectedListByPatient) =>  {

        try {
            console.log(selectedListByPatient);
            const response = await customFetch(
                ENDPOINTS.reservation.cancelReservation,
                {
                    data: {
                        uuidForCancel: [...selectedListByPatient]
                    }
                }
            );

            if(response.status === 200) {

                common.alert(response.data?.message);

            }

        } catch(err) {

            common.errMsg(err);

        }

    },
    routeByRole : (role, router) => {

        if(role === roles.PATIENT) {
            router.push({name: 'reservationListByPatient'});
        }

        if(role === roles.DOCTOR || role === roles.NURSE) {
            router.push({name: 'staffMain'});
        }

    },
}