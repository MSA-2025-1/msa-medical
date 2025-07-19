<template>
  <div class="card p-4 w-100" style="max-width: 720px;">
    <form>
      <div class="mb-3">
        <Label for="userid">아이디</Label>
        <div class="input-group">
          <Input @input="checkIdValidity" id="userid" v-model="member.userid" />
          <button type="button" @click="checkIdDuplicate" class="btn btn-outline-primary">중복확인</button>
        </div>
        <div v-show="invalidity.userid" v-text="invalidity.userid" class="invalid-feedback d-block"></div>
      </div>
      <div class="mb-3">
        <Label for="password">비밀번호</Label>
        <Input type="password" @input="checkPasswordValidity" id="password" v-model="member.password" />
        <InvalidityMessage v-show="invalidity.password" v-text="invalidity.password" />
      </div>
      <div class="mb-3">
        <Label for="passwordCheck">비밀번호 확인</Label>
        <Input type="password" @input="checkPasswordCheckValidity" id="passwordCheck" v-model="passwordCheck" />
        <InvalidityMessage v-show="invalidity.passwordCheck" v-text="invalidity.passwordCheck" />
      </div>
      <div class="mb-3">
        <Label for="name">이름</Label>
        <Input @input="checkNameValidity" id="name" v-model="member.name" />
        <InvalidityMessage v-show="invalidity.name" v-text="invalidity.name" />
      </div>
      <div class="mb-3">
        <Label for="rrn">주민번호</Label>
        <Input @input="checkRrnValidity" id="rrn" v-model="member.rrn" />
        <InvalidityMessage v-show="invalidity.rrn" v-text="invalidity.rrn" />
      </div>
      <div class="mb-3">
        <Label for="phone">전화번호</Label>
        <Input @input="checkPhoneValidity" id="phone" v-model="member.phone" />
        <InvalidityMessage v-show="invalidity.phone" v-text="invalidity.phone" />
      </div>
    </form>
    <div class="d-flex justify-content-end gap-2">
      <button @click="registerPatient" class="btn btn-primary">회원가입</button>
      <RouterLink :to="{ name: 'home' }" class="btn btn-danger">취소</RouterLink>
    </div>
  </div>
</template>

<script setup>
import Input from '@/common/components/Input.vue';
import InvalidityMessage from '@/common/components/InvalidityMessage.vue';
import Label from '@/common/components/Label.vue';
import { customFetch } from '@/util/customFetch';
import { ENDPOINTS } from '@/util/endpoints';
import { REGEX_PATTERN } from '@/util/RegexPattern';
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
const passwordCheck = ref('');
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
  checkIdValidity();
  checkPasswordValidity();
  checkPasswordCheckValidity();
  checkNameValidity();
  checkPhoneValidity();
  checkRrnValidity();

  if (Object.values(invalidity).filter((value) => value.length !== 0).length !== 0) {
    return;
  }

  try {
    const response = await customFetch(
      ENDPOINTS.auth.register.patient,
      { data: member }
    );
    alert("회원가입에 성공했습니다.");
    router.push({ name: 'loginView' });
  } catch (err) {
    alert(err.response.data.message);
  }
}

async function checkIdDuplicate() {
  touched.userid = true;

  if (!REGEX_PATTERN.USERID.test(member.userid)) {
    invalidity.userid = "아이디는 영문, 숫자, 밑줄(_)로만 구성해주세요.";
    return;
  }

  if (member.userid.length < 5 || member.userid.length > 20) {
    invalidity.userid = "아이디 길이는 5~20자입니다.";
    return;
  }

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

function checkIdValidity() {
  touched.userid = true;

  if (!REGEX_PATTERN.USERID.test(member.userid)) {
    invalidity.userid = "아이디는 영문, 숫자, 밑줄(_)로만 구성해주세요.";
    return;
  }

  if (member.userid.length < 5 || member.userid.length > 20) {
    invalidity.userid = "아이디 길이는 5~20자입니다.";
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

  if (!REGEX_PATTERN.PASSWORD.test(member.password)) {
    invalidity.password = "비밀번호는 대문자, 소문자, 숫자, 특수문자(@$!%*?&)를 포함해주세요.";
    return;
  }

  if (member.password.length < 8 || member.password.length > 20) {
    invalidity.password = "비밀번호 길이는 8~20자입니다.";
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

  if (member.name.length < 1 || member.name.length > 20) {
    invalidity.name = "이름 길이는 1~20자입니다.";
    return;
  }

  invalidity.name = "";
}

function checkRrnValidity() {
  touched.rrn = true;

  if (!REGEX_PATTERN.RRN.test(member.rrn)) {
    invalidity.rrn = "주민번호는 하이픈(-)을 포함하여 입력해주세요.";
    return;
  }

  invalidity.rrn = "";
}

function checkPhoneValidity() {
  touched.phone = true;

  if (!REGEX_PATTERN.PHONE.test(member.phone)) {
    invalidity.phone = "전화번호는 하이픈(-)을 포함하여 입력해주세요.";
    return;
  }

  invalidity.phone = "";
}
</script>
