<template>
  <Header />
  <RouterView />
  <Footer />
  <button @click="test">버튼</button>
</template>

<script setup>

import Footer from '@/components/home/layout/Footer.vue';
import Header from '@/components/home/layout/Header.vue';
import { customFetch } from '@/util/customFetch';
import { ENDPOINTS } from '@/util/endpoints';

async function test() {
  customFetch({
    method: 'get',
    url: '/auth'
  });
  // customFetch(ENDPOINTS.auth.checkId);
}
</script>
