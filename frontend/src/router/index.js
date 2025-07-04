import { createRouter, createWebHistory } from 'vue-router'
import ChatRooms from '@/components/chat/ChatRooms.vue'
import ChatRoom from '@/components/chat/ChatRoom.vue'
import { patientRoutes } from './patientRoutes';
import { adminRoutes } from './adminRoutes';
import MedicalTreatment from '@/components/diagnosis/MedicalTreatment.vue';
import {receptionRoutes} from "@/router/receptionRoutes.js";
import {reservationRoutes} from "@/router/reservationRoutes.js";

const HomeView = () => import('@/views/HomeView.vue');
const MainView = () => import('@/views/home/MainView.vue');
const OtherView = () => import('@/views/other/OtherView.vue');
const LoginView = () => import('@/views/auth/LoginView.vue');
const Reception = () => import('@/reception/views/WaitingList.vue')

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: HomeView,
      children: [
        {
          path: '/',
          name: 'home',
          component: MainView
        },
        {
          path: '/login',
          name: 'loginView',
          component: LoginView
        },
        ...patientRoutes
      ]
    },
    ...adminRoutes,
    {
      path: '/other',
      name: 'other',
      component: OtherView
    },

    {
      path:'/chatrooms',
      name: 'chatrooms',
      component:ChatRooms,
    },
    {
      path: '/chatroom/:roomId',
      name : 'chatroom',
      component:ChatRoom
    },
    {
      path: '/reception',
      name: 'reception',
      component: Reception,
      children: [
          ...receptionRoutes
      ]
    },
    {
      path: '/medicalTreatment',
      name : 'medicalTreatment',
      component:MedicalTreatment
    },
    {
      path: '/reservation',
      children: [
          ...reservationRoutes
      ]
    }
  ],
})

export default router
