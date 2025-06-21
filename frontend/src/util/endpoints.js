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
        }
    },
    member: {
        detail: (memberId) => ({
            method: 'get',
            url: `/member/${memberId}`
        }),
        reservation: (memberId) => ({
            method: 'get',
            url: `/patient/${memberId}`
        })
    },
    doctor: {
        list: {
            method: 'get',
            url: '/doctor/list'
        }
    }
}
