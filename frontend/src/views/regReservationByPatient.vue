<script setup>
import {ref, reactive, onMounted, computed} from 'vue'
import VueDatepicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'
import {common} from '@/util/common.js'
import { patientMethods } from '@/util/reservation.js'
import {omit} from 'lodash'

const selectedVal = reactive({
    doctorUuid: null,
    reservationDate: new Date(),
    time: null,
    symptom: null,
    name: null
  });

  const reservationChk = reactive({
    doctorChk : false,
    dateChk : false,
    timeChk : false,
    symptomChk : false
  });

  const doctorList = ref();
  const reservationTime = ref();

  const selectDoctor = (doctor) => {
    selectedVal.doctorUuid = doctor.uuid;
    selectedVal.name = doctor.name;

    reservationChk.doctorChk = true;

  }

  const handleDate = async (selectedDate) => {

    console.log("handleDate selectedDate:", selectedDate);
    selectedVal.reservationDate = selectedDate;

    reservationChk.dateChk = true;

    reservationTime.value = await patientMethods.getReservationTime(selectedVal);
    console.log("다시 프론트");
    console.log(reservationTime.value);

  }

  const minDate = new Date();
  const maxDate = new Date().setDate(minDate.getDate() + 7);



  const selectTime = (time) => {

    selectedVal.time = time;

    reservationChk.timeChk = true;

  }

  const writeSymptom = (symptom) => {

    if(symptom !== null) {

      reservationChk.symptomChk = true;

    }

  }

  function goHome () {

    common.goHome();

  }

  function reservation (selectedValO) {
    console.log(selectedValO.date);
    console.log(selectedValO.time);



    // 전송 전 데이터 있는지 확인하는 검증 로직 추가하기




    const selectedVal = reactive({
      patientUuid : '550e8400-e29b-41d4-a716-446655440020', // 테스트용 환자 아이디
      ...omit(selectedValO, ['date', 'time', 'name']), // date와 time 속성을 제외한 나머지 속성들을 복사
      dateTime:
        `${selectedValO.reservationDate.toISOString().slice(0, 10)}T${selectedValO.time}:00`
      // 서버로 보낼 때는 ISOString 문자열로 보내는 것이 안정적이다.
    });

    console.log(selectedVal);

    patientMethods.reservation(selectedVal);
  }

  async function getDoctorList () {
    doctorList.value = await patientMethods.getDoctorList();
  }

  onMounted(
      getDoctorList
  );


</script>

<template>
  <div class="container mt-4">
    <h2>예약등록</h2>
    <div class="dropdown my-3">
      <h3>의사</h3>
      <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false" v-cloak>
        {{ selectedVal?.name || '의사를 선택해주세요.' }}
      </button>
      <ul class="dropdown-menu">
          <template v-for="doctor in doctorList" :key="doctor.uuid">
            <li class="dropdown-item" @click="selectDoctor(doctor)" v-cloak>{{doctor.name}}</li>
          </template>
      </ul>
    </div>
    <template v-if="reservationChk.doctorChk">
      <div class="my-3">
        <h3>일자</h3>
        <VueDatepicker
            :model-value = "selectedVal.reservationDate" :format="'yyyy-MM-dd'" :min-date="minDate" :max-date="maxDate"
            :enable-time-picker="false"  :input-class="'form-control'" :esc-close = "false" :space-confirm = "false"
            @update:model-value = "handleDate" prevent-min-max-navigation
        />
      </div>
    </template>
    <template v-if="reservationChk.dateChk && reservationTime">
      <div class="my-3">
        <h3>시간</h3>
        <template v-for="time in Array.from(reservationTime).sort()" :key="time">
          <button type="button" class="btn btn-primary btn-lg" @click="selectTime(time)" ref="selectedVal.time" v-cloak>{{time}}</button>
        </template>
      </div>
    </template>
    <template v-if="reservationChk.timeChk">
      <div>
        <div class="my-3 input-group">
          <span class="input-group-text">증상</span>
          <textarea class="form-control" aria-label="symptom" v-model="selectedVal.symptom"
                    @change = "writeSymptom(selectedVal.symptom)" maxlength="100"
                    placeholder="100자 이내로 작성해주세요.">
          </textarea>
        </div>
      </div>
    </template>
    <template v-if="reservationChk.symptomChk">
        <button type="button" class="btn btn-outline-success" @click="reservation(selectedVal)">예약</button>
    </template>
    <button type="button" class="btn btn-outline-warning" @click="goHome">취소</button>
  </div>
</template>