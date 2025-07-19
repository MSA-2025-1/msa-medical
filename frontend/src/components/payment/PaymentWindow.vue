<template>
    <div class="paymentwindow">
      <h4 class="mb-3">수납 내역</h4>
  
      <p class="fw-bold">{{ props.item.patientName }}님</p>
      <p>받을 금액: <strong>{{ props.item.payment.toLocaleString() }}</strong>원</p>
  
      <div 
        v-for="(pay, index) in state.paymentList" 
        :key="index"
        class="text-secondary small"
      >
        {{ pay.method }} : {{ pay.amount.toLocaleString() }}원 ({{ pay.installment }})
      </div>
  
      <p class="mt-2">남은 금액: <strong>{{ reloadAmount.toLocaleString() }}</strong>원</p>
  
      <button class="btn btn-outline-primary btn-sm mt-2" @click="paymentModal = true">수납추가</button>
  
      <!-- 모달 -->
      <div v-if="paymentModal" class="modal">
        <div class="modal-box bg-white p-4 rounded shadow-sm">
          <h5 class="mb-3">수납추가</h5>
          <p>남은 금액 : <strong>{{ reloadAmount.toLocaleString() }}</strong>원</p>
  
          <!-- 결제수단 버튼 -->
      <div class="btn-group mb-2" >
        <button 
        class="btn btn-outline-secondary btn-sm"
        :class="{ active: state.paymentType === '카드' }"
        @click="state.paymentType = '카드'" >
        카드 </button>

        <button 
        class="btn btn-outline-secondary btn-sm"
        :class="{ active: state.paymentType === '현금' }"
        @click="state.paymentType = '현금'" >
        현금 </button>
      </div>
  
          <div class="mb-2">
            <label class="form-label">결제금액</label>
            <input type="number" class="form-control form-control-sm" v-model.number="state.paymentAmount" />
          </div>
  
          <div class="mb-3">
            <label class="form-label">할부선택</label>
            <select class="form-select form-select-sm" v-model="state.installment">
              <option>일시불</option>
              <option>3개월</option>
              <option>6개월</option>
              <option>12개월</option>
            </select>
          </div>
  
          <div class="d-flex justify-content-between">
            <button class="btn btn-success btn-sm" @click="addPayment">추가</button>
            <button class="btn btn-secondary btn-sm" @click="paymentModal = false">취소</button>
          </div>
        </div>
      </div>
  
      <button
        v-if="reloadAmount === 0 && !isSubmit"
        class="btn btn-success mt-3"
        @click="submit">
        결제완료
      </button>
    </div>
  </template>
  
<script setup>
import { customFetch } from '@/util/customFetch';
import { ENDPOINTS } from '@/util/endpoints';
import { computed, reactive, ref } from 'vue';
import '@/assets/css/Payment.css'

const isSubmit = ref(false) // 결재완료 버튼 누를시 없어짐
const props = defineProps({
    item: Object
})
const paymentModal = ref(false)
const state = reactive({
    paymentList :[],
    paymentType: '카드',
    paymentAmount : 0,
    installment : '일시불',
})
const reloadAmount = computed(()=> {
    const total =state.paymentList.reduce((sum,item) => sum + item.amount,0)
    return props.item.payment-total
})

function addPayment(){
    if(state.paymentAmount <=0) return;
    if(reloadAmount.value < state.paymentAmount){
        alert("지불 금액이 남은 금액보다 큽니다")
        return
    }
    state.paymentList.push({
        method: state.paymentType,
        amount: state.paymentAmount,
        installment:state.installment,
    })
    state.paymentAmount = 0
    state.paymentType = '카드'
    state.installment = '일시불'
    paymentModal.value = false
}

const submit = async () => {
    try {
        const response = await customFetch(ENDPOINTS.payment.statusPayment(props.item.id))
    if(response.status===200){
        isSubmit.value = true
    }    
    } catch (error) {
        console.error("에러",error)   
    }
}

</script>