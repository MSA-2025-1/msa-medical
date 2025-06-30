<template>
    <div>
      <h2>채팅방 리스트</h2>
      {{uuid}}님
      
      <button class="btn" @click="showModal = true">
        알림창 <span v-if="totalCount > 0">({{ totalCount }})</span>
      </button>
      <alarm v-if="showModal" :alarms="alarmList" @close="showModal = false " :loadAlarmList="loadAlarmList" />

  
      <ul class="list-group">
        <li v-for="room in state.chatList" :key="room.roomId">
          <router-link :to="{name : 'chatroom', params:{roomId : room.roomId }}">{{ room.roomName }}</router-link>
          {{ room.content }}
          {{ room.count }}
          {{ room.finalReadTime }}
        </li>
        
      </ul>
    
      <button class="btn" @click="create = true">등록하기</button>
    <ChatCreate v-if="create" @close="create = false" @refresh="loadList" />

  </div>
  </template>
  
  <script setup>
  
import ChatCreate from './ChatCreate.vue'
import alarm from './Alarm.vue'
import { customFetch } from '@/util/customFetch';
import {onMounted, onUnmounted, provide, reactive, ref } from 'vue';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { useRouter } from 'vue-router';

  const userStore = useUserStore();
  const uuid = userStore.user?.uuid ?? ''

  const alarmList = ref([])
  const create = ref(false)
  const showModal = ref(false)
  const router = useRouter()
  const state = reactive({
    chatList : [],
  });

  import { computed } from 'vue'
import { ENDPOINTS } from '@/util/endpoints';
import { useUserStore } from '@/stores/userStore';

  const totalCount = computed(() => {
  return state.chatList.reduce((sum, room) => sum + room.count, 0)
  })

  onMounted(()=>{
    loadchatList()
    loadAlarmList()
    client.activate()
  })

  const loadchatList= async () =>{
    try {
      const response = await customFetch(ENDPOINTS.chat.chatRoomList(uuid))
    if(response.status===200){
      state.chatList = response.data
    }
    } catch (error) {
      console.error("에러:", error)
    }
};

const client = new Client({
  webSocketFactory:() => new SockJS('/ws'),
  connectHeaders:{sender:uuid},
  onConnect: () => {
    client.subscribe(`/sub/chatrooms/${uuid}`,(message) =>{
    const list = JSON.parse(message.body)
    state.chatList = list
    })
    client.subscribe(`/sub/alarms/${uuid}`, (message) => {
    const list = JSON.parse(message.body)
    alarmList.value = list
  })
  },
  onStompError: (e) =>{
  console.error('연결 실패 : ' ,e)
  }
})
const loadList = (roomId) =>{
  
  if (client.connected) {
    client.publish({
      destination: `/pub/chat/list`,
      body: JSON.stringify({ uuid,roomId })
    });
  create.value =false;
  router.push({name:'chatroom', params:{roomId}}) 
}}

const loadAlarmList = async () => {
  try {
    const response = await customFetch(ENDPOINTS.chat.chatReadList(uuid))
    if(response.status===200){
      alarmList.value = response.data
  }
  } catch (error) {
    console.error("에러",error)
  }
}
</script>


<style scoped>
.modal-overlay {
  position: fixed;
  top: 0; left: 0;
  width: 100vw; height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}
.modal-window {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  min-width: 300px;
}
.modal-footer {
  margin-top: 1rem;
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}
</style>