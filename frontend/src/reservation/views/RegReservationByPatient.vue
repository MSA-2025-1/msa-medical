<script setup>
import {ref, reactive, onMounted, computed} from 'vue'
import VueDatepicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'
import {common} from '@/util/common.js'
import { patientMethods } from '@/reservation/util/reservation.js'
import {omit} from 'lodash'
import dayjs from "dayjs";
import {errorMessage} from "@/util/errorMessage.js";

  const selectedVal = reactive({
    doctorUuid: null,
    reservationDate: new Date(),
    time: null,
    symptom: null,
    name: null,
    isToday: false
  });

  const reservationChk = reactive({
    doctorChk : false,
    dateChk : false,
    timeChk : false,
    symptomChk : false
  });

  const doctorList = ref();
  const reservationTime = ref();
  const today = dayjs();
  const minDate = today.toDate();
  const maxDate = today.add(6, 'day').toDate();

  const selectDoctor = (doctor) => {

    selectedVal.doctorUuid = doctor.uuid;
    selectedVal.name = doctor.name;

    reservationChk.doctorChk = true;

    handleDate(selectedVal.reservationDate);

  }

  const handleDate = async (selectedDate) => {

    console.log("handleDate selectedDate:", selectedDate);
    selectedVal.reservationDate = selectedDate;

    reservationChk.dateChk = true;

    if(!reservationChk.doctorChk) {
      alert(errorMessage.common.doctorChk);
      return;
    }

    reservationTime.value = await patientMethods.getReservationTime(selectedVal);

    // 예약 정보를 가져오지 못했을 때, 타임 슬롯 생성 막기
    if(reservationTime.value === false) {
      return;
    }

    console.log("다시 프론트");
    console.log(reservationTime.value);

  }

  const disabledWeekends = (date) => {
      return dayjs(date).toDate().getDay() === 0;
  };

  const selectTime = (time) => {
    selectedVal.time = time;
    reservationChk.timeChk = true;

    patientMethods.reservationHold({
      doctorUuid: selectedVal.doctorUuid,
      dateTime :
          dayjs(`${common.dateFormatter(selectedVal.reservationDate, 'YYYY-MM-DD')}T${selectedVal.time}:00`).toDate().toISOString()
    })

  }

  const writeSymptom = () => {

    reservationChk.symptomChk = selectedVal.symptom !== null && selectedVal.symptom.trim().length > 0;

  }

  const goHome = async () => {

    if(!reservationChk.timeChk) {
      await common.goHome();
    }

    if(await patientMethods.cancelHoldingReservation()) {
      await common.goHome();
    }

  }

  const reservation = () => {
    console.log(selectedVal.reservationDate);
    console.log(selectedVal.time);

    for (const [key, value] of Object.entries(reservationChk)) {
      if (!value) {
        common.alertError(errorMessage.common[key]);
        return;
      }
    }

    console.log("예약 수행 : ", selectedVal);

    patientMethods.reservation({
      patientUuid : '550e8400-e29b-41d4-a716-446655440020', // 테스트용 환자 아이디
      ...omit(selectedVal, ['reservationDate', 'time', 'name']), // date와 time, name 속성을 제외한 나머지 속성들을 복사
      dateTime:
          dayjs(`${common.dateFormatter(selectedVal.reservationDate, 'YYYY-MM-DD')}T${selectedVal.time}:00`).toDate().toISOString()
      // `${dayjs(selectedVal.reservationDate).format('YYYY-MM-DD')}T${selectedVal.time}:00`
    });

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
    <div class="my-3">
      <h3>일자</h3>
      <VueDatepicker
          :model-value = "selectedVal.reservationDate" :format="'yyyy-MM-dd'" :min-date="minDate" :max-date="maxDate"
          :disabled-dates="disabledWeekends" :enable-time-picker="false"  :input-class="'form-control'" :esc-close = "false" :space-confirm = "false"
          @update:model-value = "handleDate" prevent-min-max-navigation
      />
    </div>
    <template v-if="reservationChk.dateChk && reservationChk.doctorChk && reservationTime">
      <div class="my-3">
        <h3>시간</h3>
        <template v-for="time in Array.from(reservationTime).sort()" :key="time">
          <button type="button" class="btn btn-primary btn-lg" @click="selectTime(time)" ref="selectedVal.time" v-cloak>{{time}}</button>
        </template>
        <template v-if="!reservationTime.size">
          <div>
            <span>예약 가능한 시간대가 없습니다.</span>
          </div>
        </template>
      </div>
    </template>
    <template v-if="reservationChk.timeChk">
      <div>
        <div class="my-3 input-group">
          <span class="input-group-text">증상</span>
          <textarea class="form-control" aria-label="symptom" v-model="selectedVal.symptom"
                    @change = "writeSymptom" maxlength="100"
                    placeholder="100자 이내로 작성해주세요.">
          </textarea>
        </div>
      </div>
    </template>
    <button type="button" class="btn btn-outline-success" @click="reservation">예약</button>
    <button type="button" class="btn btn-outline-warning" @click="goHome">취소</button>
  </div>
</template>