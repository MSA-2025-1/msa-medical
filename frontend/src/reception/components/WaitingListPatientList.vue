<script setup>
import WaitingStatus from "@/reception/components/WaitingStatus.vue";

const {value, status} = defineProps({
                value: Object,
                status: Array
              })

const emit = defineEmits(['updateStatus', 'getPatientInfo']);

const onStatusChange = (patient, updateStatus) => {
  patient.status = updateStatus;
  emit('updateStatus', {uuid: patient.uuid, updateStatus});
}

const getPatientInfo = (patient) => {
  emit('getPatientInfo', {uuid: patient.uuid});
}


</script>

<template>

  <div class="container" v-if="value">
    <div class="my-3">
      <template v-for="patient in value" :key="patient.uuid">
        <div>
          <button class="btn btn-primary" type="submit" @click="getPatientInfo(patient)" v-cloak>{{patient.name}}</button>
          <WaitingStatus @update:value="(updateStatus) => onStatusChange(patient, updateStatus)" :status="status" v-model:value="patient.status"/>
        </div>
      </template>
    </div>
  </div>

</template>
