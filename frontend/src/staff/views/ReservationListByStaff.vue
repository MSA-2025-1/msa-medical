<script setup>

import '@/assets/css/ReservationListByStaff.css';
import { reservation } from "@/reservation/util/reservation.js";
import WaitingListDoctorName from "@/shared/components/WaitingListDoctorName.vue";
import WaitingListPatientList from "@/shared/components/WaitingListPatientList.vue";
import { useReservationListStore } from "@/stores/reservationListStore.js";
import { getStompClient, subscribeChannel } from "@/util/stompMethod";
import VueDatepicker from "@vuepic/vue-datepicker";
import '@vuepic/vue-datepicker/dist/main.css';
import dayjs from "dayjs";
import { computed, onBeforeMount, onMounted, reactive, ref } from "vue";


  const reservationListStore = useReservationListStore();
  const reservationStatusList = ref();
  const fullReservationList = ref();
  const today = dayjs(new Date);
  const selectedDate = reactive({
    date: today
  });
  const minDate = today;
  const maxDate = today.add(6, 'day');
  const showCalendar = ref(false);
  let client;

  const setBeforeDate = async () => {

    handleDate(selectedDate.date.subtract(1, 'day'));

    await reservationListStore.promiseAll(selectedDate.date.toISOString());

    fullReservationList.value = reservationListStore.reservationList;

  }

  const setAfterDate = async () => {

    handleDate(selectedDate.date.add(1, 'day'));

    await reservationListStore.promiseAll(selectedDate.date.toISOString());

    fullReservationList.value = reservationListStore.reservationList;

  }

  const toggleCalendar = () => {

    showCalendar.value = !showCalendar.value;

  }

  const hideBeforeDateMovement = computed(() => {
    return (selectedDate.date.isAfter(minDate));
  });

  const hideAfterDateMovement = computed(() => {
    return (selectedDate.date.isBefore(maxDate));
  })

  const handleUpdateStatus = async ({uuid, updateStatus}) => {

      await reservation.updateReservationStatus({
        uuid,
        updateStatus
      });

    await reservationListStore.promiseAll(selectedDate.date.toISOString());
    fullReservationList.value = reservationListStore.reservationList;

  }

  const disabledWeekends = (date) => {
    return dayjs(date).toDate().getDay() === 0;
  };

  const handleDate = (date) => {

    selectedDate.date = dayjs(date);

  }

  const refreshWaitingList = async () => {

    await Promise.all([

        reservationListStore.promiseAll(selectedDate.date.toISOString()),
        reservationListStore.getReservationStatusList()

      ]);

    fullReservationList.value = reservationListStore.reservationList;
    reservationStatusList.value = reservationListStore.reservationStatusList;

  }

  onBeforeMount(async () => {
    await refreshWaitingList()

  });

  const statusSub = (client) => {
      setTimeout(() => {
        subscribeChannel(client, `/sub/status`, () => {
          refreshWaitingList();
        });
      }, 100); 
  };

  onMounted(() => {
    client = getStompClient((client) => {
        statusSub(client)
      });
  })

</script>

<template>

  <div class="container">
    <div class="date-nav">
      <img v-show="hideBeforeDateMovement" @click="setBeforeDate" src="@/assets/icons/datebefore.png" alt="이전 날짜" class="nav-arrow" />
      <span class="date-display" @click="toggleCalendar">{{selectedDate.date.format("M월 D일")}}</span>
      <img v-show="hideAfterDateMovement" @click="setAfterDate" src="@/assets/icons/nextdate.png" alt="다음 날짜" class="nav-arrow" />
    </div>
    <div v-if="showCalendar" @blur="toggleCalendar" class="datepicker-popup">
      <VueDatepicker
        :model-value="selectedDate.date"
        :format="'yyyy-MM-dd'"
        :min-date="minDate.toISOString()"
        :max-date="maxDate.toISOString()"
        :disabled-dates="disabledWeekends"
        :enable-time-picker="false"
        :input-class="'form-control'"
        :esc-close="false"
        :space-confirm="false"
        @update:model-value="handleDate"
        prevent-min-max-navigation
      />
    </div>
    <div class="wr-card-list">
      <div v-for="list in fullReservationList" :key="list.doctor?.uuid" class="wr-card">
        <WaitingListDoctorName :value="list.doctor" :count="list.patientList?.length || 0" />
        <div class="wr-card-body">
          <div v-if="!list.patientList || list.patientList.length < 1" class="no-patient">
            <span>예약 환자가 없습니다.</span>
          </div>
          <div v-else>
            <WaitingListPatientList
              @updateStatus="handleUpdateStatus"
              :value="list.patientList"
              :status="reservationStatusList"
              :date="selectedDate.date"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
