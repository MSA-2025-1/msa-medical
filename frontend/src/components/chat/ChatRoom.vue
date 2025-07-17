<template>
    <div class="chat-room-container">
        <div class="chat-room-header" >
            <h3>{{state.roomName}} </h3>
            <button class="btn-close" aria-label="Close" @click="exit"></button>
        </div>
        <div class="chat-room-box">
            <div v-for="(msg, idx) in state.messages" :key="msg.messageId || idx">
                <div v-if="shouldShowDate(idx)" class="date-divider">
                    {{ dayjs.utc(msg.createDate).tz('Asia/Seoul').format('YYYY.MM.DD')  }}    
                </div>
            <div :class="msg.uuid === uuid ? 'my-msg' : 'other-msg'">
                <div v-if="shouldShowName(idx, msg)">
                    <strong>{{ msg.name }}</strong>
                </div>
                {{ msg.content }}<br />
                <small>{{  msg.createDate ? dayjs.utc(msg.createDate).tz('Asia/Seoul').format('A h:mm') : dayjs().format('A h:mm') }}</small>
            </div>
        </div>
        </div>
        <div class="chat-room-input">
            <input v-model="state.content" class="form-control" @keyup.enter="sendMessage" @keyup.esc ="exit" placeholder="메시지를 입력하세요" />
            <button class="btn btn-warning"  @click="sendMessage">전송</button>
        </div>
    </div>
</template>

<script setup>
import '@/assets/css/chat.css';
import { useUserStore } from '@/stores/userStore';
import { customFetch } from '@/util/customFetch';
import { ENDPOINTS } from '@/util/endpoints';
import { getStompClient, sendMsg, subscribeChannel } from '@/util/stompMethod';
import dayjs from 'dayjs';
import 'dayjs/locale/ko'
import { computed, nextTick, onMounted, reactive, watch } from 'vue';
import utc from 'dayjs/plugin/utc'
import timezone from 'dayjs/plugin/timezone'
dayjs.locale('ko') // 오후 || 오전 표시 해주는거
dayjs.extend(utc)
dayjs.extend(timezone)

const props = defineProps({
    roomId : Number
})
const back = defineEmits(['back']);
const userStore = useUserStore();
const uuid = computed(() => userStore.user?.uuid ?? '');
const state = reactive({
    content:'',
    messages:[],
    roomName: ''
})
let client;
onMounted(() => {
    client = getStompClient((client) => {
    chatSub(client)
})  
loadChatName()
loadChatMessage()
scrollToBottom()
})
let chatRoomSub;

const chatSub =  (client) => {
        chatRoomSub = subscribeChannel(client,`/sub/chatroom/${props.roomId}`,async (message) => {
        state.messages.push(message)
        if(message.uuid !== uuid.value){
            //읽음처리
            try {
                await customFetch(ENDPOINTS.chat.readtime,{
                data:{
                    roomId :props.roomId,
                    uuid: uuid.value,
                    messageId: message.messageId
            }})
            } catch (error) {
                console.error("에러:", error)
            }
        }
    })
    }
//메세지 전송
const sendMessage = () =>{
    if (state.content.trim() && client.connected) {
        sendMsg(client,`/pub/chat/message`,{
        roomId :props.roomId,
        uuid :uuid.value,
        content:state.content
        });
    }
        state.content=''
}
// 채팅기록 가져오기
const loadChatMessage = async () => {
    try {
        const response = await customFetch(ENDPOINTS.chat.messageList(props.roomId))
        if(response.status===200 && response.data.length >0){
            state.messages=response.data
            // 안읽은거 있을시에 읽었다고 해주기
            JoinTimeRead()
        }
    } catch (error) {
        console.error("에러:", error)
    }
}
// 채팅방 이름 가져오기
const loadChatName = async () => {
    try {
        const response = await customFetch(ENDPOINTS.chat.loadChatName(props.roomId))
        if(response.status===200 ){
            state.roomName=response.data 
        }
    } catch (error) {
        console.error("에러:", error)
    }
}

//입장시 안읽은 메세지 읽음처리하기
const JoinTimeRead= async () =>{
    try {
        await customFetch(ENDPOINTS.chat.joinreadtime,{ data:{ roomId:props.roomId, uuid :uuid.value }})
    } catch (error) {
        console.error("에러:", error)
    }
}

// 나가기
const exit = () => {
    if (chatRoomSub) {
        chatRoomSub.unsubscribe();
        chatRoomSub = null;
    }
    back('back')
}
function scrollToBottom() {
    nextTick(() => {
    const box = document.querySelector('.chat-room-box');
    if (box) {
    box.scrollTop = box.scrollHeight;
    }
});
}
function shouldShowName(index, msg) {
if (msg.uuid === uuid.value) return false;
if (index === 0) return true;
const prev = state.messages[index - 1];
return prev.uuid !== msg.uuid;
}

watch(() => state.messages, () => {
scrollToBottom();}, { deep: true });

function shouldShowDate(index) {
  if (index === 0) return true;

  const getKSTDate = (msg) => {
    const date = msg.createDate ?? dayjs();
    return dayjs.utc(date).tz('Asia/Seoul').format('YYYY-MM-DD');
  }

  const current = getKSTDate(state.messages[index]);
  const prev = getKSTDate(state.messages[index - 1]);

  return current !== prev;
}
</script>