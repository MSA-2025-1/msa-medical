<script setup lang="ts">
  import {ref, reactive} from 'vue'

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


</script>

<template>
  <div class="container">
    <h2>예약등록</h2>
    <div class="dropdown">
      <h3>의사 : </h3>
      <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
        {{ selectedDoctor?.name || '의사를 선택해주세요.' }}
      </button>
      <ul class="dropdown-menu">
          <template v-for="doctor in doctorList" :key="doctor.uuid">
            <li class="dropdown-item" @click="selectDoctor(doctor)">{{doctor.name}}</li>
          </template>
      </ul>
    </div>
    <div class="">
      <h3>일자</h3>
      

    </div>
  </div>
</template>