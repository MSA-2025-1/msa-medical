<script setup lang="ts">
  import {ref, reactive} from 'vue'
  import Datepicker from '@vuepic/vue-datepicker'
  import '@vuepic/vue-datepicker/dist/main.css'

  const doctorList = ref(
[
      {uuid: "1", name: "김수영"},
      {uuid: "2", name: "박민수"}
    ]
  );
  const selectedDoctor = ref({
    uuid: '',
    name: ''
  });

  const selectDoctor = (doctor) => {
    selectedDoctor.value.uuid = doctor.uuid;
    selectedDoctor.value.name = doctor.name;
  }
  const today = new Date();
  let maxDate = new Date();
  maxDate.setDate(today.getDate() + 7);

  let selectedTime = ref();

  let selectedDate = ref(today);


</script>

<template>
  <div class="container mt-4">
    <h2>예약등록</h2>
    <div class="dropdown my-3">
      <h3>의사</h3>
      <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
        {{ selectedDoctor?.name || '의사를 선택해주세요.' }}
      </button>
      <ul class="dropdown-menu">
          <template v-for="doctor in doctorList" :key="doctor.uuid">
            <li class="dropdown-item" @click="selectDoctor(doctor)">{{doctor.name}}</li>
          </template>
      </ul>
    </div>
    <div class="my-3">
      <h3>일자</h3>
      <Datepicker v-model="selectedDate" :format="'yyyy-MM-dd'" :min-date="today" :max-date="maxDate" :input-class="'form-control'"/>
    </div>
    <div class="my-3">
      <h3>시간</h3>

    </div>
  </div>
</template>