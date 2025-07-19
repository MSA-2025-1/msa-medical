<template>
  <div class="d-flex flex-column justify-content-center align-items-center mb-5">
    <Title>회원가입</Title>
    <SectionDivider />
    <RegisterForm />
  </div>
</template>

<script setup>
import RegisterForm from '@/patient/components/RegisterForm.vue';
import SectionDivider from '../components/SectionDivider.vue';
import Title from '../components/Title.vue';
</script>
