<template>
  <div class="card p-4 w-100" style="max-width: 720px;">
    <form>
      <div class="mb-3">
        <Label class="form-label">아이디</Label>
        <input type="text" :value="member.userid" class="form-control" disabled>
      </div>
      <div class="mb-3">
        <Label for="name" class="form-label">이름</Label>
        <Input v-model="member.name" id="name" />
      </div>
      <div class="mb-3">
        <Label for="phone" class="form-label">전화번호</Label>
        <Input v-model="member.phone" id="phone" />
      </div>
    </form>
    <div class="d-flex justify-content-end gap-2">
      <button @click="updateProfile" class="btn btn-primary">수정</button>
      <button @click="goBack" class="btn btn-danger">취소</button>
    </div>
  </div>
</template>

<script setup>
import Input from '@/common/components/Input.vue';
import Label from '@/common/components/Label.vue';
import { customFetch } from '@/util/customFetch';
import { ENDPOINTS } from '@/util/endpoints';
import { pick } from 'lodash';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const member = ref({});

onMounted(async () => {
  const response = await customFetch(ENDPOINTS.patient.profile);
  member.value = response.data.member;
});

async function updateProfile() {
  const response = await customFetch(ENDPOINTS.patient.updateProfile, {
    data: pick(member.value, ["name", "phone"])
  });
  router.push({ name: 'mypage' });
}

async function goBack() {
  router.back();
}
</script>
