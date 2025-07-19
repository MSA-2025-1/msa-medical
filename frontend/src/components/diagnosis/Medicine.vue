<template>
  <div>
    <div class="medicine-search-wrapper">
      <div class="medicine-search-input-group">
        <input
          class="medicine-search-input"
          type="text"
          :value="state.search"
          @input="onInput"
          @focus="state.isFocus = true"
          @blur="onBlur"
          @keyup.enter="submit"
          placeholder="약 코드 및 이름을 입력해주세요."
        />
        <button class="btn btn-primary custom-btn" @click="clearSearch">지움</button>
        <button class="btn btn-primary custom-btn" @click="submit">확인</button>
      </div>

      <ul v-if="state.isFocus" class="medicine-search-search-results">
        <li v-if="state.medicineList.length === 0">검색 결과가 없습니다.</li>
        <li
          v-else
          v-for="item in state.medicineList"
          :key="item.code"
          @click="selectMedicine(item)"
        >
          {{ item.code }} - {{ item.name }}
        </li>
      </ul>
    </div>
    <div class="scroll-area">
      <ul class="medicine-search-selected-list">
        <li v-for="(item, index) in state.inputText" :key="index">
          {{ item.code }}
          <input type="number" v-model.number="item.volume" min="1" class="volume-input"  placeholder="용량"/>
          <input type="number" v-model.number="item.timesPerDay" min="1" class="volume-input"  placeholder="1회 복용"/>회
          <input type="number" v-model.number="item.perDay" min="1" class="volume-input" placeholder="1일 복용" />일
          <input type="text" v-model="item.instructions" class="volume-input"placeholder="복용 방법" />
          <button @click="removeMedicine(index)" class="medicine-remove-btn" >x</button>
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
  medicineList: [],
  selectedMedicine: null,
  inputText : []
})

let debounceTimer;

const onInput = (e) => {
  state.search = e.target.value;

  if (debounceTimer) clearTimeout(debounceTimer);

  debounceTimer = setTimeout(() => {
    searchMedicine();
  }, 300);
};

  const searchMedicine = async () =>{
    try {
      const response = await customFetch(ENDPOINTS.medicine.searchlist(state.search)
      )
      if (response.status === 200) {
        state.medicineList = response.data
      }
    } catch (error) {
      console.error('에러:', error)
    }
  }

const selectMedicine = (item) => {
  state.selectedMedicine = item
  state.search = item.name
  state.medicineList = []
  state.isFocus = false
}

const clearSearch = () => {
  state.search = ''
  state.medicineList = []
  state.selectedMedicine = null
}
const onBlur = () => {
setTimeout(() => {
  state.isFocus = false
}, 100)
}

const submit = () => {
  if (state.search.trim() !== '' && state.selectedMedicine) {
      state.inputText.push({
      code: state.selectedMedicine.code,
      name: state.selectedMedicine.name,
      volume: 1,
      timesPerDay: 1,
      perDay: 1,
      instructions: ''
  })
  state.search = ''
  state.selectedMedicine = null
}
}
const removeMedicine = (index) => {
  state.inputText.splice(index,1)
}

defineExpose({
  inputText: () => state.inputText
})
</script>
