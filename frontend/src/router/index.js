import { createRouter, createWebHistory } from 'vue-router'
import ChatRooms from '@/components/chat/ChatRooms.vue'
import ChatRoom from '@/components/chat/ChatRoom.vue'
import { patientRoutes } from './patientRoutes';
import StateList from '@/components/status/StateList.vue';
import ShowStateList from '@/components/status/ShowStateList.vue';



const HomeView = () => import('@/views/HomeView.vue');
const MainView = () => import('@/views/home/MainView.vue');
const OtherView = () => import('@/views/other/OtherView.vue');
// const acceptPatientByStaff = () => import('@/views/acceptPatientByStaff.vue')
const regReservationByPatient = () => import('@/views/reservation/RegReservationByPatient.vue')

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
        ...patientRoutes
      ]
    },
    {
      path: '/other',
      name: 'other',
      component: OtherView
    },
    {
      path: '/regReservationByPatient',
      name: 'regReservationByPatient',
      component: regReservationByPatient,
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
    // {
    //   path: '/acceptPatientByStaff',
    //   name: 'acceptPatientByStaff',
    //   component: acceptPatientByStaff
    // },
    {
      path:'/stateList',
      name: 'stateList',
      component:StateList,
    },
    {
      path: '/showStateList',
      name : 'showStateList',
      component:ShowStateList
    },


  ],
})

export default router
