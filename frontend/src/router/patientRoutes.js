import { useUserStore } from '@/stores/userStore';
import { roles } from '@/util/roles';

const MainView = () => import('@/patient/views/MainView.vue');
const MyPageView = () => import("@/patient/views/MyPageView.vue");
const PatientLayout = () => import('@/patient/layouts/PatientLayout.vue');
const RegisterView = () => import("@/patient/views/RegisterView.vue");
const UpdateProfile = () => import("@/patient/views/UpdateProfile.vue");
const LoginView = () => import('@/patient/views/LoginView.vue');
const RegReservationByPatient = () => import('@/reservation/views/RegReservationByPatient.vue')
const ReservationListByPatient = () => import('@/reservation/views/ReservationListByPatient.vue')

export const patientRoutes = [
  {
    path: '/',
    component: PatientLayout,
    beforeEnter: (to, from, next) => {
      const role = useUserStore().user?.role;
      if ([roles.NURSE, roles.DOCTOR].includes(role)) {
        return next({ name: 'staffMain' });
      }
      next();
    },
    children: [
      {
        path: '',
        name: 'home',
        component: MainView,
      },
      {
        path: 'login',
        name: 'loginView',
        component: LoginView,
      },
      {
        path: 'register',
        name: 'patientRegister',
        component: RegisterView,
      },
      {
        path: 'mypage',
        name: 'mypage',
        component: MyPageView,
        meta: { requiresAuth: true,  roles: [roles.PATIENT] },
      },
      {
        path: 'profile/update',
        name: 'updateProfile',
        component: UpdateProfile,
        meta: { requiresAuth: true,  roles: [roles.PATIENT] },
      },
      {
        path: 'regReservationByPatient',
        name: 'regReservationByPatient',
        component: RegReservationByPatient,
        meta: { requiresAuth: true,  roles: [roles.PATIENT] },
      },
      {
        path: 'reservationListByPatient',
        name: 'reservationListByPatient',
        component: ReservationListByPatient,
        meta: { requiresAuth: true,  roles: [roles.PATIENT] },
      },
    ]
  },
];
