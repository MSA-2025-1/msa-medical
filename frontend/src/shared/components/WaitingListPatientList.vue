<script setup>
import WaitingStatus from "@/shared/components/WaitingStatus.vue";
import dayjs from "dayjs";
import "@/assets/css/waitingreservaion.css"
import { usePatientViewStore } from '@/stores/patientViewStore'
import { computed } from "vue";
const store = usePatientViewStore()

  const {value, status, date} = defineProps({
      value: Object,
      status: Array
  })

  const emit = defineEmits(['updateStatus', 'getUuid']);

  const onStatusChange = (patient, updateStatus) => {

    emit('updateStatus', {
      uuid: patient.uuid,
      patientUuid: patient.patientUuid,
      doctorUuid: patient.doctorUuid,
      updateStatus: updateStatus
    });

  }

  const getUuid = (patient) => {

    store.setVisitView({
        patientUuid: patient.patientUuid,
        name : patient.name
    })

  }

</script>

<template>

  <div class="patient-item" v-for="patient in value" :key="patient.uuid">
    <div class="patient-info" > 
      <span class="patient-name" @click="getUuid(patient)"v-cloak>{{ patient.name }}님</span>
      <span class="patient-meta" v-cloak v-if="patient?.slot">{{ dayjs(patient.slot).format("HH:mm") }}</span>
      <WaitingStatus
        @update:value="(updateStatus) => onStatusChange(patient, updateStatus)"
        :status="status"
        :value="patient.status"
        :date="dayjs(patient.slot)"
      />
    </div>
  </div>

</template>
