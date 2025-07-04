<template>
  <form>
    <div>
      <label for="userid">아이디</label>
      <input type="text" @input="checkIdValidity" id="userid" v-model="member.userid">
      <div v-show="invalidity.userid" v-text="invalidity.userid"></div>
    </div>
    <button type="button" @click="checkIdDuplicate">중복확인</button>
    <div>
      <label for="password">비밀번호</label>
      <input type="password" @input="checkPasswordValidity" id="password" v-model="member.password">
      <div v-show="invalidity.password" v-text="invalidity.password"></div>
    </div>
    <div>
      <label for="passwordCheck">비밀번호 확인</label>
      <input type="password" @input="checkPasswordCheckValidity" id="passwordCheck" v-model="passwordCheck">
      <div v-show="invalidity.passwordCheck" v-text="invalidity.passwordCheck"></div>
    </div>
    <div>
      <label for="name">이름</label>
      <input type="text" @input="checkNameValidity" id="name" v-model="member.name">
      <div v-show="invalidity.name" v-text="invalidity.name"></div>
    </div>
    <div>
      <label for="rrn">주민번호</label>
      <input type="text" @input="checkRrnValidity" id="rrn" v-model="member.rrn">
      <div v-show="invalidity.rrn" v-text="invalidity.rrn"></div>
    </div>
    <div>
      <label for="phone">전화번호</label>
      <input type="text" @input="checkPhoneValidity" id="phone" v-model="member.phone">
      <div v-show="invalidity.phone" v-text="invalidity.phone"></div>
    </div>
  </form>
  <button @click="registerPatient">회원가입</button>
  <RouterLink :to="{ name: 'home' }">취소</RouterLink>
</template>

<script setup>
import { customFetch } from '@/util/customFetch';
import { ENDPOINTS } from '@/util/endpoints';
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

let validId = "";

const member = reactive({
  userid: '',
  password: '',
  name: '',
  phone: '',
  rrn: ''
});
const passwordCheck = ref(''); // TODO: lodash로 처리하기
const invalidity = reactive({
  userid: '',
  password: '',
  passwordCheck: '',
  name: '',
  phone: '',
  rrn: ''
});
const touched = reactive({
  userid: false,
  password: false,
  passwordCheck: false,
  name: false,
  phone: false,
  rrn: false
});

async function registerPatient() {
  // TODO: 실패 시 이동 안 함
  checkIdValidity();
  checkPasswordValidity();
  checkPasswordCheckValidity();
  checkNameValidity();
  checkPhoneValidity();
  checkRrnValidity();

  if (Object.values(invalidity).filter((value) => value.length !== 0).length !== 0) {
    return;
  }

  const response = await customFetch(
    ENDPOINTS.auth.register.patient,
    { data: member }
  );
  router.push({ name: 'loginView' });
}

async function checkIdDuplicate() {
  if (member.userid.length !== 0) {
    const userid = member.userid;
    const response = await customFetch(ENDPOINTS.auth.checkId, {
      params: { userid }
    });
    if (!response.data.exists) {
      validId = userid;
      if (validId === member.userid) {
        invalidity.userid = "";
      }
      return;
    }
    invalidity.userid = "해당 아이디가 이미 존재합니다.";
  }
}

function checkIdValidity() {
  touched.userid = true;

  if (member.userid.length === 0) {
    invalidity.userid = "아이디를 입력해주세요.";
    return;
  }

  if (validId !== member.userid) {
    invalidity.userid = "아이디 중복을 검사해주세요.";
    return;
  }

  invalidity.userid = "";
}

function checkPasswordValidity() {
  touched.password = true;

  if (member.password.length === 0) {
    invalidity.password = "비밀번호를 입력해주세요.";
    return;
  }

  invalidity.password = "";
}

function checkPasswordCheckValidity() {
  touched.passwordCheck = true;

  if (passwordCheck.value.length === 0) {
    invalidity.passwordCheck = "비밀번호 확인이 되지 않았습니다.";
    return;
  }

  if (passwordCheck.value !== member.password) {
    invalidity.passwordCheck = "비밀번호 확인이 일치하지 않습니다.";
    return;
  }

  invalidity.passwordCheck = "";
}

function checkNameValidity() {
  touched.name = true;

  if (member.name.length === 0) {
    invalidity.name = "이름을 입력해주세요.";
    return;
  }

  invalidity.name = "";
}

function checkRrnValidity() {
  touched.rrn = true;

  if (member.rrn.length === 0) {
    invalidity.rrn = "주민번호를 입력해주세요.";
    return;
  }

  invalidity.rrn = "";
}

function checkPhoneValidity() {
  touched.phone = true;

  if (member.phone.length === 0) {
    invalidity.phone = "전화번호를 입력해주세요.";
    return;
  }

  invalidity.phone = "";
}
</script>
