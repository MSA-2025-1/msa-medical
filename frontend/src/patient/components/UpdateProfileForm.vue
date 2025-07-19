<template>
  <div class="card p-4 w-100" style="max-width: 720px;">
    <Loading v-if="loading" />
    <template v-else>
      <form>
        <div class="mb-3">
          <Label>아이디</Label>
          <Input v-model="member.userid" disabled />
        </div>
        <div class="mb-3">
          <Label for="name">이름</Label>
          <Input @input="checkNameValidity" v-model="member.name" id="name" />
          <InvalidityMessage v-show="invalidity.name" v-text="invalidity.name" />
        </div>
        <div class="mb-3">
          <Label for="phone">전화번호</Label>
          <Input @input="checkPhoneValidity" v-model="member.phone" id="phone" />
          <InvalidityMessage v-show="invalidity.phone" v-text="invalidity.phone" />
        </div>
      </form>
      <div class="d-flex justify-content-end gap-2">
        <button @click="updateProfile" class="btn btn-primary">수정</button>
        <button @click="goBack" class="btn btn-danger">취소</button>
      </div>
    </template>
  </div>
</template>

<script setup>
import Input from '@/common/components/Input.vue';
import InvalidityMessage from '@/common/components/InvalidityMessage.vue';
import Label from '@/common/components/Label.vue';
import Loading from '@/common/components/Loading.vue';
import { customFetch } from '@/util/customFetch';
import { ENDPOINTS } from '@/util/endpoints';
import { REGEX_PATTERN } from '@/util/RegexPattern';
import { pick } from 'lodash';
import { onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const loading = ref(true);
const member = ref({});
const invalidity = reactive({
  name: '',
  phone: '',
});
const touched = reactive({
  name: false,
  phone: false,
});

onMounted(async () => {
  const response = await customFetch(ENDPOINTS.patient.profile);
  member.value = response.data.member;
  loading.value = false;
});

async function updateProfile() {
  checkNameValidity();
  checkPhoneValidity();

  if (Object.values(invalidity).filter((value) => value.length !== 0).length !== 0) {
    return;
  }

  try {
    const response = await customFetch(ENDPOINTS.patient.updateProfile, {
      data: pick(member.value, ["name", "phone"])
    });
    alert("정보 수정에 성공했습니다.");
    router.push({ name: 'mypage' });
  } catch (err) {
    alert(err.response.data.message);
  }
}

async function goBack() {
  router.back();
}

function checkNameValidity() {
  touched.name = true;

  if (member.value.name.length < 1 || member.value.name.length > 20) {
    invalidity.name = "이름 길이는 1~20자입니다.";
    return;
  }

  invalidity.name = "";
}

function checkPhoneValidity() {
  touched.phone = true;

  if (!REGEX_PATTERN.PHONE.test(member.value.phone)) {
    invalidity.phone = "전화번호는 하이픈(-)을 포함하여 입력해주세요.";
    return;
  }

  invalidity.phone = "";
}

</script>
