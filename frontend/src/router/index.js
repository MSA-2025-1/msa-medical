import { createRouter, createWebHistory } from 'vue-router'
import regReservationByPatient from '@/views/regReservationByPatient.vue'
import HomeView from "@/views/HomeView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/regReservationByPatient',
      name: 'regReservationByPatient',
      component: regReservationByPatient,
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue'),
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView
    }
  ],
})

export default router
