<template>
  <div class="attachment-card" @click="triggerFileInput">
    <div class="attachment-card-header">
      <span>이미지</span>
    </div>
    <div class="attachment-card-body">
      <template v-if="state.previewUrls.length">
        <div class="attachment-image-list">
          <div
            class="attachment-image-item"
            v-for="(url, index) in state.previewUrls"
            :key="index"
          >
            <img :src="url" />
            <button class="remove-btn" @click.stop="removeImage(index)">✕</button>
          </div>
        </div>
      </template>
      <template v-else>
        <div class="attachment-placeholder">이미지를 클릭하여 업로드</div>
      </template>
    </div>
    <input
      ref="fileInput"
      type="file"
      accept="image/*"
      multiple
      @change="onFileChange"
      class="attachment-hidden-input"
    />
  </div>
</template>


<script setup>
import { reactive, ref } from 'vue';
import '@/assets/css/attachment.css';

const fileInput = ref(null)
const state = reactive({
  files : [],
  previewUrls : [],
})
const triggerFileInput = () => {
fileInput.value?.click();
};

const onFileChange = (e) => {
  const selectedFiles = Array.from(e.target.files)
  
  selectedFiles.forEach((file) => {
  state.files.push(file)
  state.previewUrls.push(URL.createObjectURL(file))
  })
  if (fileInput.value) {
  fileInput.value.value = ''
}
  
}
const removeImage = (index) => {
    state.files.splice(index,1)
    state.previewUrls.splice(index,1)
}
defineExpose({state})
</script>
