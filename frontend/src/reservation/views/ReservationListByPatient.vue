<script setup>

import {computed, onMounted, reactive, ref} from "vue";
import dayjs from "dayjs";
import {reservation} from "@/reservation/util/reservation.js";
import {useUserStore} from "@/stores/userStore.js";
import '@/assets/css/reservation.css'
import {errorMessage} from "@/util/errorMessage.js";
import {common} from "@/util/common.js";

  const selectedListByPatient = new Set();
  const reservationList = ref([]);
  const userInfo = computed(() => useUserStore().user);

  const checked = (e) => {

    if (e.target.checked) {
      selectedListByPatient.add(e.target.value);
    } else {
      selectedListByPatient.delete(e.target.value);
    }

  }

  const reservationListPerPatient = async () => {

    reservationList.value = await reservation.getReservationListPerPatient(userInfo.value.uuid);

  }

  const cancelReservation = async () => {

    if(selectedListByPatient.size === 0) {
      common.alertError(errorMessage.reservation.noDataForCancel);
      return;
    }

    if(!window.confirm("예약 취소하시겠습니까?")) {
      return;
    }

    await reservation.cancelReservation(selectedListByPatient);
    selectedListByPatient.clear();
    await reservationListPerPatient();

  }

  onMounted(
      reservationListPerPatient
  )

</script>

<template>

  <div class="container reservation-list-by-patient">
    <div class="reservationBox">
      <table>
        <thead>
          <tr>
            <th></th>
            <th>진료 의사</th>
            <th>진료 예약 일시</th>
          </tr>
        </thead>
        <tbody>
          <template v-if="reservationList?.length > 0">
            <tr v-for="reservation in reservationList" :key="reservation.uuid">
              <td><input class="patient-reservation-check-input" type="checkbox" @change="checked" :value="reservation.uuid"></td>
              <td>{{reservation.name}}</td>
              <td><span v-cloak>{{dayjs(reservation.slot).format('MM월 DD일 HH시 mm분')}}</span></td>
            </tr>
          </template>
          <template v-else>
            <tr>
              <td colspan="3">진료 예약이 없습니다.</td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
  <button type="button" class="patient-reservation-btn-cancel" @click="cancelReservation">예약 취소</button>
  </div>

</template>
