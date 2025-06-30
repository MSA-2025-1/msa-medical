const WaitingListPatientList = () => import("@/reception/components/WaitingListPatientList.vue")
const AcceptPatientByStaff = () => import("@/reception/views/AcceptPatientByStaff.vue");

export const receptionRoutes = [
    {
        path: '/acceptPatientByStaff',
        name: 'acceptPatientByStaff',
        component: AcceptPatientByStaff
    },
    {
        path: 'waitingPatientList',
        name: 'waitingPatientList',
        component: WaitingListPatientList
    }
]