<template>
    <div class="payment-wrapper">
      <button @click="emit('back')" class="x-btn" >x</button>
      <div class="left">
        <VisitHistoryDetail :id="props.item.treatmentId" />
      </div>
      <div class="right">
        <PaymentWindow :item="props.item" />
      </div>
    </div>
</template>
  
<script setup>
  import PaymentWindow from './PaymentWindow.vue'
  import VisitHistoryDetail from '../diagnosis/VisitHistoryDetail.vue'
  import '@/assets/css/Payment.css'
  const emit = defineEmits(['back'])
  const props = defineProps({item: Object})
  </script>
