<template>
  <div class="diagnosis-wrapper">
    <div class="top-section">
      <div class="left">
        <VisitHistory />
      </div>
      <div class="center">
        <Treatment ref ="treatmentRef"/>
        <Disease ref="diseaseRef"/>
      </div>
      <div class="right">
        <Attachment ref ="attachmentRef"/>
      </div>
    </div>
    <div class="bottom-section">
      <Medicine ref="medicineRef"/>
    </div>
    <button @click="submit">진료 완료</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Attachment from './Attachment.vue'
import Disease from './Disease.vue'
import Medicine from './Medicine.vue'
import VisitHistory from './VisitHistory.vue'
import { customFetch } from '@/util/customFetch'
import { ENDPOINTS } from '@/util/endpoints'
import Treatment from './Treatment.vue'

const treatmentRef= ref()
const diseaseRef = ref()
const attachmentRef = ref()
const medicineRef= ref()

const submit = async ()  => {
  const treatment = treatmentRef.value?.state //진단 증상
  const disease = diseaseRef.value?.inputText() || [] // 질병 리스트 
  const attachment = attachmentRef.value?.state || [] // 첨부파일
  const medicine = medicineRef.value?.inputText() || []// 약 리스트

  const isMissingDiagnosis = !treatment?.treatContent || treatment.treatContent.trim() === '';
  const isMissingMedicine = medicine.length === 0;
  const isMissingDisease = disease.length === 0;

  if (isMissingDiagnosis || isMissingMedicine || isMissingDisease) {
    alert('진단, 약, 질병은 모두 입력해야 합니다.');
    return;
  }

  const formData = new FormData();

  const data ={
    treatment,
    prescriptions: medicine.map(item => ({
    code: item.code,
    volume: item.volume
    })),
    diagnosis: disease.map(item => ({
      id: item.id
    }))
  }
  formData.append('data', new Blob([JSON.stringify(data)],{type:'application/json'}))

  if (attachment.files.length > 0) {
    attachment.files.forEach(file => {
      formData.append('files', file)
    })
  }
  try {
      const response = await customFetch(ENDPOINTS.treatment.totalTreatment, {
      data: formData,})
      
      if(response.status ===200){
        alert("진료 작성이 등록되었습니다.")
      }
  } catch (error) {
    console.error('진료 전체 등록 실패', error)
  }
}
</script>

<style scoped>
.diagnosis-wrapper {
  height: 100vh;
  overflow-y: auto;
  padding: 20px;
  background-color: #f5f5f5;
}

.top-section {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  overflow-x: auto; /* 가로 스크롤 */
}

.left {
  background: white;
  border: 1px solid #ccc;
  padding: 80px;
  height: 400px;
  overflow-y: auto; /* 이 안에서 스크롤 */
}

.center{
  background: white;
  border: 1px solid #ccc;
  padding: 80px;
  height: 400px;
  overflow-y: auto; /* 이 안에서 스크롤 */
} 
.right{
  background: white;
  border: 1px solid #ccc;
  padding: 80px;
  height: 400px;
  overflow-y: auto; /* 이 안에서 스크롤 */
}


.bottom-section {
  background: white;
  border: 1px solid #ccc;
  padding: 80px;
  height: 400px;
  
}

</style>
