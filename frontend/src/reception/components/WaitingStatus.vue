<script setup>
import { computed, defineProps } from "vue";

const { status, value } = defineProps({
  status: Array,
  value: String
});

const patientCurrentStatus = computed(() => value);

console.log(value);

const leftOver = computed(() => {
  return status.filter(v => v.name !== value);
});

const emit = defineEmits(["update:value"]);

const selectedStatus = (name) => {
  emit("update:value", name);
}

</script>

<template>
  <button
      class="btn btn-secondary btn-sm"
      :class="{ 'dropdown-toggle': patientCurrentStatus.trim() !== '진료 중' }"
      type="button"
      v-bind="patientCurrentStatus.trim() !== '진료 중' ? { 'data-bs-toggle': 'dropdown', 'aria-expanded': 'false' } : {}"
  >
    {{ patientCurrentStatus }}
  </button>

  <ul
      class="dropdown-menu"
      v-if="patientCurrentStatus.trim() !== '진료 중'"
  >
    <li
        v-for="left in leftOver"
        :key="left.id"
        @click="selectedStatus(left.name)"
    >
      {{ left.name }}
    </li>
  </ul>
</template>

