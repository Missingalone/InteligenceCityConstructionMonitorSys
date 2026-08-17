<script setup lang="ts">
import AppHeader from '@/layout/Header.vue'
import { useRoute } from 'vue-router'

const route = useRoute()
</script>

<template>
  <div class="app-shell" :class="{ 'login-shell': route.meta.public }">
    <AppHeader v-if="!route.meta.public" />
    <main :class="route.meta.public ? 'login-main' : 'app-main'">
      <RouterView v-slot="{ Component }">
        <Transition name="page" mode="out-in">
          <component :is="Component" />
        </Transition>
      </RouterView>
    </main>
  </div>
</template>
