<template>
  <div class="paymentList">
    <div class="payment-card" v-for="item in state.list" :key="item.id">
      <div class="payment-name" @click="namePayment(item)">
        {{ item.patientName }}
      </div>
      <div
        class="payment-badge"
        :class="item.paymentStatus === 'Y' ? 'badge-paid' : 'badge-wait'">
        {{ item.paymentStatus === 'Y' ? '결제완료' : '결제대기 중' }}
      </div>
    </div>
  </div>
</template>
  
  
<script setup>
import { customFetch } from '@/util/customFetch';
import { ENDPOINTS } from '@/util/endpoints';
import { onMounted, reactive } from 'vue';
import { getStompClient, subscribeChannel } from '@/util/stompMethod';
import { usePatientViewStore } from '@/stores/patientViewStore'
import '@/assets/css/Payment.css'
const store = usePatientViewStore()

let client ;
const state = reactive({
    list : [],
})

onMounted(()=>{
    loadPaymentList()
    client = getStompClient((client) => {
    statusSub(client)
})
})

const statusSub = (client) => {
    subscribeChannel(client,`/sub/status`,() => {
    loadPaymentList()
})
}

const loadPaymentList = async () => {
    try {
        const response = await customFetch(ENDPOINTS.payment.loadPaymentList)
        if(response.status===200){
            state.list = response.data.list
        }
    } catch (error) {
        console.error("에러",error)
    }
    
}
const namePayment = (item) => {
    store.setPaymentView(item);
}
</script>