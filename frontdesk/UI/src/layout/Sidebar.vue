<script setup lang="ts">
import type { Component } from 'vue'

defineProps<{ items: Array<{ label: string; key: string; icon?: Component }>; active: string }>()
defineEmits<{ change: [key: string] }>()
</script>

<template>
  <aside class="side-rail tech-panel">
    <button v-for="item in items" :key="item.key" :class="{ active: active === item.key }" @click="$emit('change', item.key)">
      <component :is="item.icon" v-if="item.icon" :size="19" />
      <span>{{ item.label }}</span>
    </button>
  </aside>
</template>

<style scoped>
.side-rail { display: flex; flex-direction: column; gap: 7px; padding: 10px; }
.side-rail button { display: flex; align-items: center; gap: 10px; min-height: 43px; padding: 0 12px; border: 1px solid transparent; border-radius: 6px; color: #7897b4; background: transparent; cursor: pointer; text-align: left; }
.side-rail button:hover { color: #a9d6ff; background: rgba(22,119,255,.08); }
.side-rail button.active { color: #e6f4ff; border-color: rgba(46,167,255,.32); background: linear-gradient(90deg, rgba(22,119,255,.28), rgba(22,119,255,.05)); box-shadow: inset 3px 0 #2db8ff; }
</style>
