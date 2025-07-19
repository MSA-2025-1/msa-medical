<script setup>
import { computed, ref, onMounted, onBeforeUnmount } from "vue";
import dayjs from "dayjs";
import "@/assets/css/WaitingStatus.css";

  const { status, value, date } = defineProps({
    status: Array,
    value: String,
    date: dayjs
  });
  const isDropdownOpen = ref(false);
  const patientCurrentStatus = computed(() => value);
  const emit = defineEmits(["update:value"]);

  const filterStages = {
    left: { 
      condition: ({v, value}) => {
        return (v.name !== value)
      }
    },
    excludeWaiting: {
      condition: ({v, date}) => {
        const isToday = dayjs().isSame(date, 'day');
        return !(!isToday && v.name === '대기');
      }
    }
  };

  const leftOver = computed(() => {
    return Object.values(filterStages).reduce((acc, stage) => {
      return acc.filter(v => stage.condition({v, date, value}));
    }, status);
  });

  const selectedStatus = (name) => {
    console.log("제발 동작 좀");
    emit("update:value", name);
  }

  function toggleDropdown() {
    isDropdownOpen.value = !isDropdownOpen.value;
  }

  function closeDropdown() {
    isDropdownOpen.value = false;
  }

  function handleClickOutside(event) {
    const wrapper = event.target.closest('.ws-dropdown-wrapper');
    if (!wrapper) {
      closeDropdown();
    }
  }

  onMounted(() => {
    document.addEventListener('mousedown', handleClickOutside);
  });

  onBeforeUnmount(() => {
    document.removeEventListener('mousedown', handleClickOutside);
  });

</script>

<template>
  <div class="ws-dropdown-wrapper">
    <button
      class="btn btn-secondary btn-sm"
      type="button"
      @click="toggleDropdown"
      v-bind="patientCurrentStatus.trim() !== '진료 중' ? { 'aria-expanded': 'false' } : {}"
      :class="[
        { 'dropdown-toggle': patientCurrentStatus.trim() !== '진료 중' },
        'btn-status-' + patientCurrentStatus.trim().replace(/\s/g, '')
      ]"
    >
      {{ patientCurrentStatus }}
    </button>

    <ul
      class="ws-dropdown-menu"
      v-if="patientCurrentStatus.trim() !== '진료 중' && isDropdownOpen"
    >
      <li
        v-for="left in leftOver"
        :key="left.id"
        @click="selectedStatus(left.name); closeDropdown()"
      >
        <span v-cloak>{{ left.name }}</span>
      </li>
    </ul>
  </div>
</template>


