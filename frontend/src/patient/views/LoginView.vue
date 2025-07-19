<template>
  <div class="d-flex flex-column justify-content-center align-items-center mb-5">
    <Title>로그인</Title>
    <SectionDivider />
    <LoginForm />
  </div>
</template>

<script setup>
import LoginForm from '@/patient/components/LoginForm.vue';
import SectionDivider from '../components/SectionDivider.vue';
import Title from '../components/Title.vue';
</script>
