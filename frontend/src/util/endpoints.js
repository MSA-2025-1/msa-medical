export const ENDPOINTS = {
    auth: {
        register: {
            patient: {
                method: 'post',
                url: '/auth/register/patient'
            }
        },
        checkId: {
            method: 'get',
            url: '/auth/check-id'
        },
        login: {
            method: 'post',
            url: '/auth/login'
        },
        refreshToken: {
            method: 'post',
            url: '/auth/refresh-token',
        },
        logout: {
            method: 'post',
            url: '/auth/logout'
        }
    },
    member: {
        detail: (memberId) => ({
            method: 'get',
            url: `/member/${memberId}`
        }),
        register: {
            method: 'post',
            url: '/member'
        },
        me: {
            method: 'get',
            url: '/member/me'
        }
    },
    patient: {
        register: {
            method: 'post',
            url: '/patient'
        },
        search: (searchValue) => ({
            method: 'get',
            url: `/patient/search?searchValue=${searchValue}`
        }),
        profile: {
            method: 'get',
            url: '/patient/profile'
        },
        updateProfile: {
            method: 'patch',
            url: '/patient/profile'
        },
    },
    doctor: {
        list: {
            method: 'get',
            url: '/doctor/list'
        },
    },
    staff: {
        list: (uuid) => ({
            method: 'get',
            url: '/staff/list'
        }),
        search: (searchVal) => ({
            method: 'get',
            url: `/patient/search?searchValue=${searchVal}`
        }),
        register: {
            method: 'post',
            url: '/patient'
        }
    },
    status: {
        getReceptionStatusList: {
            method: 'get',
            url: `/status/receptionList`
        },
        getReservationStatusList: {
            method: 'get',
            url: `/status/reservationList`
        },
    },
    reception: {
        acceptPatientByStaff: {
            method: 'post',
            url: '/reception/acceptPatientByStaff'
        },
        getWaitingList: (uuid) => ({
            method: 'get',
            url: `/reception/${uuid}`
        }),
        updateReceptionStatus: {
            method: 'put',
            url: `/reception/updateStatus`
        }
    },
    chat: {
        loadMemberList: (uuid) => ({
            method: 'get',
            url: `/member/memberList/${uuid}`
        })
        ,
        createChatRoom: {
            method: 'post',
            url: `/chatroom/createChatRoom`
        },
        readtime : {
            method:'post',
            url:`/chatread/readtime`
        },
        messageList: (roomId) => ({
            method:'get',
            url:`/chatmessage/messageList/${roomId}`,
        }),
        loadChatName:(roomId) =>({
            method:'get',
            url:`/chatroom/loadChatName/${roomId}`,
        }),
        updateJoinTime:{
            method:'put',
            url:`/chatjoin/updateJoinTime`,
        },
        joinreadtime:{
            method:'post',
            url:`/chatread/joinreadtime`,
        },
        updateOutTime:{
            method:'put',
            url:`/chatjoin/updateOutTime`,
        },

        chatReadList:(uuid) => ({
            method:'get',
            url:`/chatread/chatReadList/${uuid}`
        }),
        chatRoomList:(uuid) => ({
            method:'get',
            url: `/chatroom/chatRoomList/${uuid}`,
        })
    },
    reservation: {
        reservation: {
            method: 'post',
            url: '/reservation',
            // 추후 JWT에서 환자 UUID 포함해 전송.
            // 추후 팀장님과 얘기해서 patient 속성쪽으로 이동할지 결정.
        },
        reservationList: (selectedVal) => ({
            method: 'get',
            url: `/reservation/getReservationList/${selectedVal.doctorUuid}/${selectedVal.dateTime}`
        }),
        reservationHold: {
            method: 'post',
            url: '/reservation/hold'
        },
        cancelHoldingReservation: {
            method: 'put',
            url: '/reservation/cancelHoldingReservation'
        },
        getFullReservationList: (uuid, date) => ({
            method: 'get',
            url: `/reservation/${uuid}/${date}/list`
        }),
        updateReservationStatus: ({uuid, updateStatus}) => ({
            method: 'put',
            url: `/reservation/${uuid}/${updateStatus}/updateStatus`
        }),
        getReservationListPerPatient: (uuid) => ({
            method: 'get',
            url: `/reservation/${uuid}/patientlist`
        }),
        cancelReservation: {
            method: 'put',
            url: `/reservation/cancel`
        }
    },
    medicine:{
        searchlist: (search) => ({
            method:'get',
            url: `/medicine/search?k=${search}`
        })
    },
    disease:{
        searchlist: (search) => ({
            method:'get',
            url: `/disease/search?k=${search}`
        })
    },
    treatment:{
        totalTreatment:{
            method:'post',
            url:`/treatment/totalTreatment`
        },
        history: {
            method:'get',
            url:`/treatment/history`
        },
        historyDetail:(uuid) => ({
            method:'get',
            url:`/treatment/historyDetail/${uuid}`
        }),
        selectedPatientUuid:(doctorUuid) => ({
            method:'get',
            url:`/treatment/selectedPatientUuid/${doctorUuid}`
        }),
    },

    payment:{
        loadPaymentList:{
            method : 'post',
            url:`/payment/paymentList`
        },

        statusPayment: (id) => ({
            method : 'get',
            url:`/payment/statusPayment/${id}`
        }),
    },
}
