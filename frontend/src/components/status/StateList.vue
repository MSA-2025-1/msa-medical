<template>
    <div>
      <div v-for="(item,index) in state.list" :key="item[0]">
        {{ item[0] }} <br/>
        <button @click="change(index)">{{item[1]}}</button><br/>
        <div v-if="item[1] === '예약'">
            <button>예약 취소</button><br/>
        </div>
        <div v-else-if="item[1] === '대기'">
            <button>접수 취소</button><br/>
        </div>
        
      </div>
      
    </div>
  </template>
  
  <script setup>
  import { Client } from '@stomp/stompjs';
  import SockJS from 'sockjs-client';
  import { reactive, onMounted} from 'vue'
  const state = reactive({
    list : [
    ["a", "예약"],
    ["a2", "대기"],
    ["a3", "예약"],
    ["a4", "예약"],
    ["a5", "진료 중"],
    ["a6", "진료 중"],
    ["b", "대기"],
    ["c", "대기"],
    ["d", "예약"],
    ["e", "예약"],
    ["f", "예약"]
  ]
  });

  onMounted(() => {
    state.list
    client.activate()
  })

  function change(index) {
  const current = state.list[index][1]

  if (current === "예약") {
    state.list[index][1] = "대기"
  } else if (current === "대기") {
    state.list[index][1] = "진료 중"
  }else if (current === "진료 중") {
    state.list[index][1] = "진료 완료"
  }else if (current === "진료 완료") {
    state.list[index][1] = "예약"
  }
  const status = statusUpdate(index)
  client.publish({
      destination: `/pub/status/message`,
      body:JSON.stringify(status)
    })
}

  const client = new Client({
    webSocketFactory:() => new SockJS('/ws'),
    //connected:{sender:uuid}, //로그인 후에 이제 이걸가지고 JTW 인증 연결 해야함
  onConnect: () =>{
    console.log("연결성공")
  },
  onStompError:(error) =>{
    console.error('연결 실패 ',error)
  }
})

  const statusUpdate = (index) => ({
    name: state.list[index][0],
    groupId: state.list[index][1],
    })

    

  
  </script>
  
  