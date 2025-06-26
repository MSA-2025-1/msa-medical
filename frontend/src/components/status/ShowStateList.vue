<template>
    <div>
      <div v-for="(item,index) in state.list" :key="item[0]">
        <div v-if="item[1] !=='예약' && item[1] !=='진료 완료'">
            {{ item[0] }} 
            {{item[1]}}
            
        </div>
      </div>
      
    </div>
  </template>
  
  <script setup>
  import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { onMounted, reactive } from 'vue'
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

  const client = new Client({
    webSocketFactory: () => new SockJS('/ws'),
    onConnect: () => {
      client.subscribe(`/sub/status` , (message) => {
        const msg = JSON.parse(message.body)
        const target = state.list.find(item => item[0] === msg.name)
        if(target) {
          console.log("업데이트 대상 찾음:", target)
          target[1]  = msg.groupId
        }
      })
      console.log("연결성공")
    }
  })

  onMounted(()=> {
    state.list
    client.activate()
  }) 

  </script>
  
  