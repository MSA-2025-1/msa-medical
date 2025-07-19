import { useUserStore } from '@/stores/userStore';

const MainView = () => import('@/patient/views/MainView.vue');
const MyPageView = () => import("@/patient/views/MyPageView.vue");
const PatientLayout = () => import('@/patient/layouts/PatientLayout.vue');
const RegisterView = () => import("@/patient/views/RegisterView.vue");
const UpdateProfile = () => import("@/patient/views/UpdateProfile.vue");
const LoginView = () => import('@/patient/views/LoginView.vue');
const RegReservationByPatient = () => import('@/reservation/views/RegReservationByPatient.vue')
const ReservationListByPatient = () => import('@/patient/views/ReservationListByPatient.vue')

export const patientRoutes = [
  {
    path: '/',
    component: PatientLayout,
    beforeEnter: (to, from, next) => {
      const role = useUserStore().user?.role;
      if (['NURSE', 'DOCTOR'].includes(role)) {
        return next({ name: 'staffMain' });
      }
      next();
    },
    children: [
      {
        path: '',
        name: 'home',
        component: MainView
      },
      {
        path: 'login',
        name: 'loginView',
        component: LoginView
      },
      {
        path: 'register',
        name: 'patientRegister',
        component: RegisterView
      },
      {
        path: 'mypage',
        name: 'mypage',
        component: MyPageView
      },
      {
        path: 'profile/update',
        name: 'updateProfile',
        component: UpdateProfile
      },
      {
        path: 'regReservationByPatient',
        name: 'regReservationByPatient',
        component: RegReservationByPatient,
      },
      {
        path: 'reservationListByPatient',
        name: 'reservationListByPatient',
        component: ReservationListByPatient
      },
    ]
  },
];
