<template>
  <div>
    <div class="medicine-search-wrapper">
      <div class="medicine-search-input-group">
        <input
          class="medicine-search-input" type="text" :value="state.search" @input="onInput" 
          @focus="state.isFocus = true" @blur="onBlur" @keyup.enter="submit"
          placeholder="코드 및 병명을 입력해주세요."/>
          <button class="btn btn-primary custom-btn" @click="clearSearch">지움</button>
          <button class="btn btn-primary custom-btn" @click="submit">확인</button>
      </div>
  
      <ul v-if="state.isFocus" class="medicine-search-search-results">
        <li v-if="state.diseaseList.length === 0">검색 결과가 없습니다.</li>
        <li v-else v-for="item in state.diseaseList" :key="item.id" 
        @click="selectedDisease(item)">
          {{ item.code }} - {{ item.name }}
        </li>
      </ul>
    </div>
    <div class="scroll-area">
      <ul class="medicine-search-selected-list">
        <li v-for="(item, index) in state.inputText" :key="index">
          {{ item.code }} - {{ item.name }}
          <button @click="removeDisease(index)"class="medicine-remove-btn" >x</button>
        </li>
      </ul>
    </div>
  </div>
</template>

  <script setup>
import { reactive } from 'vue'
import { customFetch } from '@/util/customFetch'
import { ENDPOINTS } from '@/util/endpoints'
import '@/assets/css/medicine.css';
const state = reactive({
  search: '',
  isFocus: false,
  diseaseList: [],
  selectedDisease: null,
  inputText : [],
})

let debounceTimer;

const onInput = (e) => {
  state.search = e.target.value;

  if (debounceTimer) clearTimeout(debounceTimer);

  debounceTimer = setTimeout(() => {
    searchDisease();
  }, 300);
};

  const searchDisease = async () =>{
    try {
      const response = await customFetch(ENDPOINTS.disease.searchlist(state.search)
      )
      if (response.status === 200) {
        state.diseaseList = response.data
      }
    } catch (error) {
      console.error('에러:', error)
    }
  }

const selectedDisease = (item) => {
  state.selectedDisease = item
  state.search = item.name
  state.diseaseList = []
  state.isFocus = false
}

const clearSearch = () => {
  state.search = ''
  state.diseaseList = []
  state.selectedDisease = null
}
const onBlur = () => {
setTimeout(() => {
  state.isFocus = false
}, 150)
}

const submit = () => {
  if (state.search.trim() !== '' && state.selectedDisease) {
      state.inputText.push(state.selectedDisease)
      state.search = ''
      state.selectedDisease = null
}
}

const removeDisease=  (index) => {
  state.inputText.splice(index,1)
}

defineExpose({
  inputText: () => state.inputText})
</script>