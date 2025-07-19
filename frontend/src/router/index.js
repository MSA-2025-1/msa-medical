import { createRouter, createWebHistory } from 'vue-router'
import { patientRoutes } from './patientRoutes';
import { adminRoutes } from './adminRoutes';
import { staffRoutes } from './staffRoutes';
import { useUserStore } from '@/stores/userStore';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    ...patientRoutes,
    ...adminRoutes,
    ...staffRoutes
  ],
})

router.beforeEach((to, from) => {
  const user = useUserStore().user;

  if (to.meta.requiresAuth && !user) {
    return { name: 'loginView' }
  }

  if (to.meta.roles && !to.meta.roles.includes(user.role)) {
    return { name: 'home' }
  }
});

export default router
