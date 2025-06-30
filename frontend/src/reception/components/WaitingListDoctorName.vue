<script setup>

defineProps({
  value: Object
})

</script>

<template>

  <div class="container">
    <div class="my-3">
      <span v-cloak>{{value.name}}</span>
    </div>
  </div>

</template>
