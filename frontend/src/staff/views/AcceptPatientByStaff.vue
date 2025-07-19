<script setup>
import {onMounted, reactive, ref} from "vue";
import {common} from "@/util/common.js";
import {reservation} from "@/reservation/util/reservation.js";
import {customFetch} from "@/util/customFetch.js";
import {ENDPOINTS} from "@/util/endpoints.js";
import {omit} from "lodash";
import {errorMessage} from "@/util/errorMessage.js";
import {REGEX_PATTERN} from "@/util/RegexPattern.js";
import {useRoute, useRouter } from "vue-router";
import {successMessage} from "@/util/successMessage.js";
import '@/assets/css/AcceptPatientByStaff.css'
import '@/assets/css/icons.css'

  const selectedVal = reactive({
    doctorUuid: null,
    name: null,
    patientUuid: null,
    symptom: null
  });

  const acceptChk = reactive({
    doctorChk : false,
    symptomChk : false
  });

  const doctorList = ref();
  const route = useRoute();
  const router = useRouter();
  const isDropdownOpen = ref(false);

  const selectDoctor = (doctor) => {

    selectedVal.doctorUuid = doctor.uuid;
    selectedVal.name = doctor.name;
    acceptChk.doctorChk = selectedVal.doctorUuid !== null && REGEX_PATTERN.MEMBER_UUID_REGEX.test(selectedVal.doctorUuid);

  }

  const writeSymptom = () => {

    acceptChk.symptomChk = selectedVal.symptom !== null && selectedVal.symptom.trim().length > 0;

  }

  async function goHome () {
    await router.push({name: 'staffMain'});
  }

  async function getDoctorList () {
    doctorList.value = await reservation.getDoctorList();
  }

  const acceptPatientByStaff = async () => {

    for(const [key, val] of Object.entries(acceptChk)) {
      if(!val) {
        common.alertError(errorMessage.common[key]);
        return;
      }
    }

    try{

      const response = await customFetch(
        ENDPOINTS.reception.acceptPatientByStaff, {
            data: {
              ...omit(selectedVal, ['name']),
            }
          }
        )

        if(response.status === 201) {
          common.alert(successMessage.reception.acceptSuccess);
          await router.push({name: 'staffMain'});
        }

    } catch(err) {

      common.errMsg(err);

      selectedVal.name = null;
      selectedVal.doctorUuid = null;
      selectedVal.symptom = null;

    }

  }

  const insertSelectedValData = () => {

    selectedVal.patientUuid = route.query.patientUuid;

  }


  const toggleDropdown = () => { isDropdownOpen.value = !isDropdownOpen.value; };
  const closeDropdown = () => { isDropdownOpen.value = false; };

  onMounted(() => {
        getDoctorList();
        insertSelectedValData();
  });

</script>

<template>
  <div class="reg-accept-card">
    <div class="reg-accept-title">
      <img src="@/assets/icons/waiting.png" alt="접수" class="icon"/>
      환자 접수
    </div>
    <form class="reg-accept-form" @submit.prevent>
      <!-- 의사 선택 -->
      <div class="reg-form-row">
        <label class="reg-form-label">의사<span class="reg-required">*</span></label>
        <div class="reg-dropdown-group">
          <button
            class="reg-dropdown-btn"
            type="button"
            @click="toggleDropdown"
            :aria-expanded="isDropdownOpen"
            v-cloak
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
      <!-- 증상 입력 -->
      <div class="reg-form-row">
        <label class="reg-form-label">증상<span class="reg-required">*</span></label>
        <textarea
          class="reg-form-input"
          aria-label="symptom"
          v-model="selectedVal.symptom"
          @input="writeSymptom"
          maxlength="100"
          placeholder="100자 이내로 작성해주세요."
        ></textarea>
      </div>
      <!-- 버튼 -->
      <div class="reg-form-actions">
        <button type="button" class="reg-btn-main" @click="acceptPatientByStaff">접수</button>
        <button type="button" class="reg-btn-sub" @click="goHome">취소</button>
      </div>
    </form>
  </div>
</template>