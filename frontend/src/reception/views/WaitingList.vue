<script setup>
  import {useWaitingListStore} from "@/stores/waitingListStore.js";
  import {computed, onBeforeMount, onMounted, onUnmounted, ref} from "vue";
  import WaitingListDoctorName from "@/shared/components/WaitingListDoctorName.vue";
  import WaitingListPatientList from "@/shared/components/WaitingListPatientList.vue";
  import {reception} from "@/reception/util/reception.js";
import { getStompClient, sendMsg, subscribeChannel } from "@/util/stompMethod";
import { useUserStore } from "@/stores/userStore";
import { getAccessToken } from "@/auth/accessToken";
import { createWebSocketModuleRunnerTransport } from "vite/module-runner";

  const waitingListStore = useWaitingListStore();
  const waitingList = ref();
  const receptionStatusList = ref();
  const userStore = useUserStore();
  const uuid = computed(() => userStore.user?.uuid ?? '');
  const token = getAccessToken()
  let client;


  const refreshWaitingList = async () => {

    await Promise.all([
      waitingListStore.promiseAll(),
      waitingListStore.getReceptionStatusList()
    ]);

    waitingList.value = waitingListStore.waitingList;
    receptionStatusList.value = waitingListStore.receptionStatusList;

  };

  // 의료진이 대기 리스트에서 이름을 누른 환자 UUID 가져오는 함수
  const getPatientInfo = ({uuid}) => {

  }

  // 상태 변경 시 동작하는 함수
  const handleUpdateStatus = async (patient) => {

    await reception.updateReceptionStatus(patient);

  }

  onBeforeMount(async () => {
    await refreshWaitingList()
  });

  const statusSub = (client) => {
    if(client?.connected){
            subscribeChannel(client,`/sub/status`,() => {
            refreshWaitingList()
            console.log("성공")
        })
      }
  }


  onMounted(() => {
    client = getStompClient(uuid.value,token,(client) => {
      statusSub(client)
    })
    // 웹소켓 연결부
  })

  onUnmounted(() => {
    // 웹 소켓 연결 해제부

  })


</script>

<template>
  <template v-for="list in waitingList" :key="list.doctor?.uuid">
      <WaitingListDoctorName
          :value="list.doctor"/>
      <WaitingListPatientList
          @updateStatus="handleUpdateStatus"
          @getPatientInfo="getPatientInfo"
          :value="list.patientList"
          :status="receptionStatusList"/>
    </template>
</template>
