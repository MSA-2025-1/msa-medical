import { roles } from "@/util/roles"; 

const RegPatientByStaff = () => import ("@/staff/views/RegPatientByStaff.vue");
const StaffMainView = () => import("@/staff/views/StaffMainView.vue");
const RegReservationByStaff = () => import('@/reservation/views/RegReservationByStaff.vue')
const AcceptPatientByStaff = () => import('@/staff/views/AcceptPatientByStaff.vue')
const StaffLayOut = () => import('@/staff/layouts/StaffLayOut.vue')

export const staffRoutes = [
    {
        path: '/staff',
        component: StaffLayOut,
        meta: { requiresAuth: true, roles: [roles.NURSE, roles.DOCTOR] },
        children: [
            {
                path: '',
                name: 'staffMain',
                component: StaffMainView,
            },
            {
                path: 'regReservationByStaff',
                name: 'regReservationByStaff',
                component: RegReservationByStaff,
            },
            {
                path: 'regPatientByStaff',
                name: 'regPatientByStaff',
                component: RegPatientByStaff
            },
            {
                path: 'acceptPatientByStaff',
                name: 'acceptPatientByStaff',
                component: AcceptPatientByStaff
            }
        ]
    },
]