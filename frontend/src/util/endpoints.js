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
        }
    },
    doctor: {
        list: {
            method: 'get',
            url: '/doctor/list'
        }
    }
}
