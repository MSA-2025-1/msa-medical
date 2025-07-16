<script setup>
import WaitingStatus from "@/shared/components/WaitingStatus.vue";
import dayjs from "dayjs";
import {reactive} from "vue";
import { inject } from 'vue'

const selectedPatientUuid = inject('selectedPatientUuid')
const selectedDoctorUuid = inject('selectedDoctorUuid')

const {value, status, date} = defineProps({
                value: Object,
                status: Array
})
const emit = defineEmits(['updateStatus', 'getPatientInfo']);

const onStatusChange = (patient, updateStatus) => {

  emit('updateStatus', {
    uuid: patient.uuid,
    patientUuid: patient.patientUuid,
    doctorUuid: patient.doctorUuid,
    updateStatus: updateStatus
  });

}

</script>

<template>

  <div class="container">
    <div class="my-3">
      <template v-for="patient in value" :key="patient.uuid">
        <div>
          <button class="btn btn-primary" type="submit" v-cloak>{{patient.name}}</button>
          <span v-cloak v-if="patient.reservationDate">
            {{ dayjs(patient.reservationDate).format("MM-DD HH:mm") }}
          </span>
          <WaitingStatus
              @update:value="(updateStatus) => onStatusChange(patient, updateStatus)"
              :status="status"
              :value="patient.status"
              :date="dayjs(patient.reservationDate)"
          />
        </div>
      </template>
    </div>
  </div>

</template>
