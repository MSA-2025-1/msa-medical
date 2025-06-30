<template>
    <div>
      <h2>내원이력</h2>
      <template v-if="!selectedId">
      <ul>
        <li class="history-box" v-for="(item, index) in state.list" :key="index">
            <p class="date" @click="selectedId = item.id" style="cursor: pointer;">
                {{ dayjs(item.treatWriteDate).format('YYYY-MM-DD HH:mm:ss') }}</p>
        </li>
      </ul>

      <div v-if="state.pageInfo.totalPage > 1">
        <button v-if="state.pageInfo.prev" @click="changePage(state.pageInfo.startPage - 1)">
            이전
        </button>

        <button v-for="page in pageNumbers" :key="page" @click="changePage(page)">
        {{ page }}
        </button>

        <button v-if="state.pageInfo.next" @click="changePage(state.pageInfo.endPage + 1)">
            다음
        </button>
    </div>
      </template>
      
    

    <template v-else>
        <VisitHistoryDetail :visitId="selectedId" @back="selectedId = null" />
    </template>
    </div>
  </template>
  
  <script setup>
import { customFetch } from '@/util/customFetch';
import { ENDPOINTS } from '@/util/endpoints';
import dayjs from 'dayjs';
import { computed, onMounted, reactive, ref } from 'vue';
import VisitHistoryDetail from './VisitHistoryDetail.vue';
    onMounted(() => {
        historyList()
    })
    const selectedId = ref(null)
    const patientUuid = 'a' //환자 이건 상태전환 되는것까지 보고 빼겠습니다.
    const doctorUuid = 'b'//의사 이건 상태전환 되는것까지 보고 빼겠습니다.
    const state = reactive({
        list:[],
        pageInfo: {},
        pageNo: 1,
        size: 5,
  })

  const historyList = async () => {
    try {
        const response = await customFetch(ENDPOINTS.treatment.history,
        {params:{
            doctorUuid,
            patientUuid,
            pageNo: state.pageNo,
            size: state.size,
        }})
        if(response.status===200){
            state.list =  response.data.list
            state.pageInfo =  response.data.pageInfo
            console.log('pageInfo:', response.data.pageInfo)
        }
    } catch (error) {
    console.error("에러",error)
    }}

    const changePage = (page) => {
    state.pageNo = page;
    historyList();
    };
    
    const pageNumbers = computed(() => {
    const pages = [];
    for (let i = state.pageInfo.startPage; i <= state.pageInfo.endPage; i++) {
        pages.push(i);
    }
    return pages;
    });
  </script>
  
  <style scoped>
  .history-box {
    margin-bottom: 20px;
  }
  
  .date {
    font-weight: bold;
    margin-bottom: 5px;
  }
  
  .box {
    background-color: #e0e0e0;
    padding: 15px;
    border-radius: 5px;
  }
  </style>
  