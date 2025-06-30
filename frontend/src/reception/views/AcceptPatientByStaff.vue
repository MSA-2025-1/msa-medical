<script setup>
import {onMounted, reactive, ref} from "vue";
import {common} from "@/util/common.js";
import {patientMethods} from "@/reservation/util/reservation.js";
import {customFetch} from "@/util/customFetch.js";
import {ENDPOINTS} from "@/util/endpoints.js";
import {omit} from "lodash";
import {errorMessage} from "@/util/errorMessage.js";
import {REGEX} from "@/util/regex.js";

const selectedVal = reactive({
  doctorUuid: null,
  patientUuid: '550e8400-e29b-41d4-a716-446655440020',
  dateTime: new Date(),
  name: null,
  patientName: '김영선',
  rrn: '950610-2111111',
  symptom: null
});

const acceptChk = reactive({
  doctorChk : false,
  symptomChk : false
});

const doctorList = ref();

const selectDoctor = (doctor) => {

  selectedVal.doctorUuid = doctor.uuid;
  selectedVal.name = doctor.name;

  acceptChk.doctorChk = selectedVal.doctorUuid !== null && REGEX.MEMBER_UUID_REGEX.test(selectedVal.doctorUuid);

}


const writeSymptom = () => {

  acceptChk.symptomChk = selectedVal.symptom !== null && selectedVal.symptom.trim().length > 0;

}

function goHome () {
  common.goHome();
}

async function getDoctorList () {
  doctorList.value = await patientMethods.getDoctorList();
}

const acceptPatientByStaff = async () => {

  for(const [key, val] of Object.entries(acceptChk)) {
    if(!val) {
      common.alertError(errorMessage.common[key]);
      return;
    }
  }

  try{

    const response = await customFetch(
      ENDPOINTS.reception.acceptPatientByStaff, {
          data: selectedVal
        }
      )

      if(response.status === 200) {
        common.alertError("접수가 완료됐습니다.");
      }

  } catch(err) {
    common.errMsg(err);
  }

}

onMounted(
    getDoctorList
)

</script>

<template>
  <div class="container">
  <div class="dropdown my-3">
    <h3>의사</h3>
    <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false" v-cloak>
      {{  selectedVal?.name || '의사를 선택해주세요.' }}
    </button>
    <ul class="dropdown-menu">
      <template v-for="doctor in doctorList" :key="doctor.uuid">
        <li class="dropdown-item" @click="selectDoctor(doctor)" v-cloak>{{doctor.name}}</li>
      </template>
    </ul>
  </div>
  <div class="my-3 input-group">
    <span class="input-group-text">증상</span>
    <textarea class="form-control" aria-label="symptom" v-model="selectedVal.symptom"
              @input = "writeSymptom" maxlength="100"
              placeholder="100자 이내로 작성해주세요.">
      </textarea>
  </div>
  <button type="button" class="btn btn-outline-success" @click="acceptPatientByStaff">접수</button>
  <button type="button" class="btn btn-outline-warning" @click="goHome">취소</button>
  </div>
</template>