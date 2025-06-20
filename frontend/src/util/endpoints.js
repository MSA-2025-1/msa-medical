export const ENDPOINTS = {
    member: {
        detail: (memberId) => ({
            method: 'get',
            url: `/member/${memberId}`
        }),
        register: {
            method: 'post',
            url: '/member'
        },
        reservation: {
            method: 'post',
            url: 'patient/reservation',
            // 추후 JWT에서 환자 UUID 포함해 전송.
            // 추후 팀장님과 얘기해서 patient 속성쪽으로 이동할지 결정.
        }
    },
    doctor: {
        list: {
            method: 'get',
            url: '/doctor/list'
        }
    },
    patient: {
        reservationList : {
            method: 'post',
            url: 'patient/getReservationList'
        }
    }
}
