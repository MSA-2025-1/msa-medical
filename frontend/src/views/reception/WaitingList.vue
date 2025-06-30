<script setup>
  import {useWaitingListStore} from "@/stores/waitingListStore.js";
  import {onBeforeMount, onMounted, onUnmounted, ref} from "vue";
  import WaitingListDoctorName from "@/views/reception/WaitingListDoctorName.vue";
  import WaitingListPatientList from "@/views/reception/WaitingListPatientList.vue";

  const waitingListStore = useWaitingListStore();
  const waitingList = ref();
  const role = 'R003'; // Role 더미 데이터

  onBeforeMount(async () => {

    // if(localStorage.getItem('role') === 'R002') {
    if(role === 'R002') {
      waitingList.value = await waitingListStore.waitingListByDoctor();
    } else {
      waitingList.value = await waitingListStore.waitingListByNurse();
    }

    console.log(waitingList.value);

  })

  onMounted(() => {
    // 웹소켓 연결부

  })

  onUnmounted(() => {
    // 웹 소켓 연결 해제부

  })


</script>

<template>
  <template v-for="list in waitingList">
    <WaitingListDoctorName :value="list.doctor"/>
    <WaitingListPatientList :value="list.patientList"/>
  </template>
</template>
