<script setup>
import {ref, reactive, onMounted, computed, onBeforeUnmount} from 'vue'
import VueDatepicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'
import {common} from '@/util/common.js'
import { reservation } from '@/reservation/util/reservation.js'
import {omit} from 'lodash'
import dayjs from "dayjs";
import {errorMessage} from "@/util/errorMessage.js";
import {useUserStore} from "@/stores/userStore.js";
import {useRouter, useRoute} from "vue-router";
import {roles} from "@/util/roles.js";
import '@/assets/css/RegReservation.css';
import '@/assets/css/icons.css';

  const userInfo = computed(() => useUserStore().user);
  
  const allSlots = computed(() => {
    if (!reservationTime.value?.allSlots) return [];
    return reservationTime.value.allSlots;
  });
  
  const reservedSlotIds = computed(() => {
    if (!reservationTime.value?.reservatedSlots) return [];
    return reservationTime.value.reservatedSlots.map(slot => slot.slotId);
  });
  
  const isSlotReserved = computed(() => {
    return (slotId) => {
      return reservedSlotIds.value.includes(String(slotId));
    };
  });
  
  const hasTimeSlots = computed(() => {
    return reservationTime.value?.allSlots !== undefined;
  });
  
  const hasAvailableSlots = computed(() => {
    if (!allSlots.value || allSlots.value.length === 0) return false;
    
    const allSlotIds = allSlots.value.map(slot => String(slot.id));
    const allReserved = allSlotIds.every(id => reservedSlotIds.value.includes(id));
    
    return !allReserved;

  });

  const router = useRouter();
  const route = useRoute();
  const isDropdownOpen = ref(false);
  const doctorList = ref();
  const reservationTime = ref();
  const today = dayjs();
  const minDate = today.toDate();
  const maxDate = today.add(6, 'day').toDate();

  const selectedVal = reactive({
    doctorUuid: null,
    patientUuid: null,
    reservationDate: new Date(),
    slotId: null,
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

  const selectDoctor = (doctor) => {

    selectedVal.doctorUuid = doctor.uuid;
    selectedVal.name = doctor.name;
    reservationChk.doctorChk = true;

    handleDate(selectedVal.reservationDate);

  }

  const handleDate = async (selectedDate) => {

    selectedVal.reservationDate = selectedDate;
    reservationChk.dateChk = true;

    if(!reservationChk.doctorChk) {
      alert(errorMessage.common.doctorChk);
      return;
    }

    reservationTime.value = await reservation.getSlots(selectedVal);

    if(reservationTime.value === false) {
      return;
    }

  }

  const disabledWeekends = (date) => {
      return dayjs(date).toDate().getDay() === 0;
  };

  const selectTime = (time) => {

    selectedVal.time = time.slot;
    selectedVal.slotId = time.id;
    reservationChk.timeChk = true;

  }

  const writeSymptom = () => {

    reservationChk.symptomChk = selectedVal.symptom !== null && selectedVal.symptom.trim().length > 0;

  }

  const goHome = async () => {

    if(!reservationChk.timeChk) {
      await routeToHome();
    }

  }

  const routeToHome = async() => {

    if(userInfo.value.role === roles.PATIENT) {
      await router.push({name: 'home'});
    } else {
      await router.push({name: 'staffMain'});
    }

  }

  const makeReservation = () => {

    for (const [key, value] of Object.entries(reservationChk)) {
      if (!value) {
        common.alertError(errorMessage.common[key]);
        return;
      }
    }

    reservation.reservation({

      ...omit(selectedVal, ['reservationDate', 'time', 'name', 'isToday'])

    }, router, userInfo.value.role);

  }

  async function getDoctorList () {
    doctorList.value = await reservation.getDoctorList();
  }

  const checkRole = () => {

    if(userInfo.value.role === roles.PATIENT) {

      selectedVal.patientUuid = userInfo.value.uuid;

    }

    if(userInfo.value.role === roles.DOCTOR || userInfo.value.role === roles.NURSE) {

      selectedVal.patientUuid = route.query.patientUuid;

    }

  }

  const toggleDropdown = () => {
    isDropdownOpen.value = !isDropdownOpen.value;
  };

  const closeDropdown = () => {
    isDropdownOpen.value = false;
  };

  const handleClickOutside = (event) => {
    const dropdown = document.querySelector('.reg-dropdown-group');
    if (dropdown && !dropdown.contains(event.target)) {
      closeDropdown();
    }
  };

  onMounted(() => {
    getDoctorList();
    checkRole();
    document.addEventListener('mousedown', handleClickOutside);
  });

  onBeforeUnmount(() => {
    document.removeEventListener('mousedown', handleClickOutside);
  });


</script>

<template>
  <div class="reg-reservation-card">
    <div class="reg-reservation-title">
      <img src="@/assets/icons/reservation.png" alt="예약" class="icon"/>
      예약등록
    </div>
    <form class="reg-reservation-form" @submit.prevent>
      <div class="reg-form-row">
        <label class="reg-form-label">의사<span class="reg-required">*</span></label>
        <div class="reg-dropdown-group">
          <button
            class="reg-dropdown-btn"
            type="button"
            @click="toggleDropdown"
            :aria-expanded="isDropdownOpen"
          >
            {{ selectedVal?.name || '의사를 선택해주세요.' }}
          </button>
          <ul class="reg-dropdown-menu" v-if="isDropdownOpen">
            <template v-for="doctor in doctorList" :key="doctor.uuid">
              <li class="reg-dropdown-item" @click="selectDoctor(doctor); closeDropdown()">{{doctor.name}}</li>
            </template>
          </ul>
        </div>
      </div>
      <div class="reg-form-row">
        <label class="reg-form-label">일자<span class="reg-required">*</span></label>
        <VueDatepicker
          :model-value="selectedVal.reservationDate"
          :format="'yyyy-MM-dd'"
          :min-date="minDate"
          :max-date="maxDate"
          :disabled-dates="disabledWeekends"
          :enable-time-picker="false"
          :input-class="'reg-form-input'"
          :esc-close="false"
          :space-confirm="false"
          @update:model-value="handleDate"
          prevent-min-max-navigation
        />
      </div>
      <div class="reg-form-row" v-if="reservationChk.dateChk && reservationChk.doctorChk && hasTimeSlots">
        <label class="reg-form-label">시간<span class="reg-required">*</span></label>
        <div class="reg-time-group">
          <template v-for="time in allSlots" :key="time.id">
            <button 
              type="button" 
              class="reg-btn-time" 
              :class="{ active: dayjs(selectedVal.time).isSame(dayjs(time.slot))}"
              :disabled="isSlotReserved(time.id)"
              @click="selectTime(time)" 
              v-cloak
            >
              {{dayjs(time.slot).format('HH:mm')}}
            </button>
          </template>
          <template v-if="!hasAvailableSlots">
            <span class="reg-helper-danger">예약 가능한 시간대가 없습니다.</span>
          </template>
        </div>
      </div>
      <div class="reg-form-row" v-if="reservationChk.timeChk">
        <label class="reg-form-label">증상<span class="reg-required">*</span></label>
        <textarea 
          class="reg-form-input" 
          aria-label="symptom" 
          v-model="selectedVal.symptom"
          @change="writeSymptom" maxlength="100"
          placeholder="100자 이내로 작성해주세요."
        >
        </textarea>
      </div>
      <div class="reg-form-actions">
        <button type="button" class="reg-btn-main" @click="makeReservation">예약</button>
        <button type="button" class="reg-btn-sub" @click="goHome">취소</button>
      </div>
    </form>
  </div>
</template>