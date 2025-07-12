<script setup>
import '@/css/staff/StaffMenuBar.css'
import LogoutButton from "@/auth/components/LogoutButton.vue";

</script>

<template>
  <div class="my-3">

    <div class="menuContainer">
      <div class="my-3">
        <router-link :to="{name: 'regPatientByStaff'}">환자 등록</router-link>
      </div>
      <div class="my-3">
        <router-link :to="{name: 'search'}">환자 검색</router-link>
      </div>
      <div class="my-3">
        <router-link :to="{name: ''}">결제</router-link>
      </div>
      <div class="my-3">
        <router-link :to="{name: ''}">설정</router-link>
      </div>
      <LogoutButton/>
    </div>
  </div>
</template>