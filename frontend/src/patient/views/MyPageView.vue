<template>
  <div class="d-flex flex-column justify-content-center align-items-center mb-5">
    <Title>마이페이지</Title>
    <SectionDivider />
    <Profile />
  </div>
</template>

<script setup>
import Profile from '../components/Profile.vue';
import SectionDivider from '../components/SectionDivider.vue';
import Title from '../components/Title.vue';
</script>
