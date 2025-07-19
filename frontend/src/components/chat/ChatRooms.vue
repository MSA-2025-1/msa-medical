<template>
  <div v-if="!selectedRoomId" class="chat-rooms">
    <div class = "chat-rooms-header">
      <div class="btn-total">
        <button class="btn btn-primary" @click="create = true" >
          방 만들기
        </button>
        <button class="alarm-button" @click="showModal = true">
          <span class="icon-wrapper">
            🔔
            <span v-if="totalCount > 0" class="alarm-badge">
              {{ totalCount }}
            </span>
          </span>
        </button>
      </div>
  </div>
  <div >
    <Alarm v-if="showModal" class="alarm" :alarms="alarmList" 
    @close="showModal = false " @open="clickRoomId" :loadAlarmList="loadAlarmList" />
  </div>
  <ChatCreate v-if="create" @close="create = false" @open="clickRoomId" @refresh="loadList" />
    <ul  class="chat-rooms-list">
      <li v-for="room in state.chatList" :key="room.roomId" class="chat-rooms-item"
        @click="clickRoomId(room.roomId)">
    <div class="chat-rooms-core">
      <span class="chat-rooms-name" >{{ room.roomName }}</span>
      <span class="chat-rooms-content" >{{ room.content }}</span>
      <span v-if="room.count > 0" class="chat-rooms-count">{{ room.count }}</span>
    </div>
    <div class="chat-rooms-date">
      {{ dayjs.utc(room.lastMessageTime).tz('Asia/Seoul').format('A h:mm') }}
    </div>
</li>
</ul>
  
</div>
    <ChatRoom @open="clickRoomId(roomId)" v-if="selectedRoomId"  
    :roomId="selectedRoomId" @back = "goBack" :key="selectedRoomId"/>
</template>

<script setup>  
import '@/assets/css/chat.css';
import ChatCreate from './ChatCreate.vue'
import { customFetch } from '@/util/customFetch';
import {onMounted, computed, reactive, ref } from 'vue';
import { ENDPOINTS } from '@/util/endpoints';
import { useUserStore } from '@/stores/userStore';
import dayjs from 'dayjs';
import 'dayjs/locale/ko'
import { getStompClient, sendMsg, subscribeChannel } from '@/util/stompMethod';
import ChatRoom from './ChatRoom.vue';
import Alarm from './Alarm.vue';
import utc from 'dayjs/plugin/utc'
import timezone from 'dayjs/plugin/timezone'
dayjs.locale('ko') // 오후 || 오전 표시 해주는거
dayjs.extend(utc)
dayjs.extend(timezone)

const opne = defineEmits('open')
const userStore = useUserStore();
const uuid = computed(() => userStore.user?.uuid ?? '');
const selectedRoomId = ref(false);


const alarmList = ref([])
const create = ref(false)
const showModal = ref(false)
const state = reactive({
  chatList : [],
});

const totalCount = computed(() => {
return state.chatList.reduce((sum, room) => sum + room.count, 0)
})
  let client;
onMounted(()=>{
  client = getStompClient((client) => {
    subList(client)
    subAlarm(client)
  })  
  loadchatList()
  loadAlarmList()
})
const loadchatList= async () =>{
  try {
    const response = await customFetch(ENDPOINTS.chat.chatRoomList(uuid.value))
  if(response.status===200){
    state.chatList = response.data
  }
  } catch (error) {
    console.error("에러:", error)
  }
};
  const subList = (client) => {
      subscribeChannel(client,`/sub/chatrooms/${uuid.value}`,(message) => {
      state.chatList = message
    })
  }
  const subAlarm = (client) => {
      subscribeChannel(client,`/sub/alarms/${uuid.value}`,(message)=>{
      alarmList.value = message
    })
  }

  const loadList = (roomId) =>{
    if (client.connected) {
      sendMsg(client,`/pub/chat/list`,{
      uuid:uuid.value,roomId 
    });
    create.value =false;
}}

const loadAlarmList = async ()=> {
try {
  const response = await customFetch(ENDPOINTS.chat.chatReadList(uuid.value))
  if(response.status===200){
    alarmList.value = response.data
}
} catch (error) {
  console.error("에러",error)
}
}
const goBack = () =>{
selectedRoomId.value = null
loadchatList();  
loadAlarmList();
}
const clickRoomId = (roomId) => {
selectedRoomId.value = roomId;
}
</script>