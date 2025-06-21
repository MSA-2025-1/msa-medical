<script setup>
import {ref, reactive, onMounted, computed} from 'vue'
import VueDatepicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'
import { customFetch } from '@/util/customFetch'
import router from '@/router'
import { ENDPOINTS } from '@/util/endpoints'

  const selectedVal = reactive({
    doctorUuid: null,
    date: new Date(),
    time: null,
    symptom: null
  });

  const doctorList = ref();

  const selectDoctor = (doctor) => {
    selectedVal.doctorUuid = doctor.uuid;
    console.log(selectedVal);
  }

  const minDate = new Date();
  const maxDate = new Date().setDate(minDate.getDate() + 7);

  const reservationTime = computed(() => {

    const start = new Date();
    const end = new Date();
    const slots = [];

    start.setHours(9, 0, 0, 0); // 9:00 AM
    end.setHours(18, 0, 0, 0); // 6:00 PM

    while (start <= end) {

      const hours = start.getHours().toString().padStart(2, '0');
      const minutes = start.getMinutes().toString().padStart(2, '0');

      slots.push(`${hours}:${minutes}`);
      start.setMinutes(start.getMinutes() + 10);

    }

    return slots;

  });

  const selectTime = (time) => {
    selectedVal.time = time;
    console.log(selectedVal);
  }

  const historyBack = () => {
    router.push({name: 'home'});
  }

  const reservation = () => {
    customFetch(
      ENDPOINTS.member.reservation
    )
    .then(
        response => {
          if(response.status === 200) alert(response.data?.message);
        }
    )
    .catch(
        err => {
          err.response?.data?.message ? alert(err.response?.data?.message) : alert("실행 중 오류가 발생했습니다. 다시 실행해주세요.");
        }
    )
  }

  onMounted(() =>
      customFetch(
        // method: 'get',
        // url: '/doctor/list'
        ENDPOINTS.doctor.list
      )
      .then(
          response => {
            if (response.status === 200) {
              doctorList.value = response.data?.list;
            }
          }
      )
      .catch(
          err => {
            err.response?.data?.message ? alert(err.response?.data?.message) : alert("실행 중 오류가 발생했습니다. 다시 실행해주세요.");
            router.push({name: 'home'});
          }
      )
  );


</script>

<template>
  <div class="container mt-4">
    <h2>예약등록</h2>
    <div class="dropdown my-3">
      <h3>의사</h3>
      <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
        {{ selectedVal?.name || '의사를 선택해주세요.' }}
      </button>
      <ul class="dropdown-menu">
          <template v-for="doctor in doctorList" :key="doctor.uuid">
            <li class="dropdown-item" @click="selectDoctor(doctor)">{{doctor.name}}</li>
          </template>
      </ul>
    </div>
    <div class="my-3">
      <h3>일자</h3>
      <VueDatepicker
          v-model="selectedVal.date" :format="'yyyy-MM-dd'" :min-date="minDate" :max-date="maxDate"
          :enable-time-picker="false"  :input-class="'form-control'" prevent-min-max-navigation
      />
    </div>
    <div class="my-3">
      <h3>시간</h3>
      <template v-for="time in reservationTime">
        <button type="button" class="btn btn-primary btn-lg" @click="selectTime(time)">{{time}}</button>
      </template>
    </div>
    <div>
      <div class="my-3 input-group">
        <span class="input-group-text">증상</span>
        <textarea class="form-control" aria-label="symptom" v-model="selectedVal.symptom"></textarea>
      </div>
    </div>
    <div>
      <button type="button" class="btn btn-outline-success" @click="reservation">예약</button>
      <button type="button" class="btn btn-outline-warning" @click="historyBack">취소</button>
    </div>
  </div>
</template>