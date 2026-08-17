<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'
import * as echarts from 'echarts'
import type { EChartsOption } from 'echarts'

const props = defineProps<{ data: Array<{ name: string; value: number; color?: string }>; centerText?: string; centerSubtext?: string }>()
const el = ref<HTMLDivElement>()
let chart: echarts.ECharts | undefined
let observer: ResizeObserver | undefined
const render = () => {
  if (!chart) return
  const option: EChartsOption = {
    tooltip: { trigger: 'item', backgroundColor: 'rgba(6,24,45,.95)', borderColor: 'rgba(79,168,255,.35)', textStyle: { color: '#dceaff' } },
    legend: { bottom: 0, textStyle: { color: '#6f91b1', fontSize: 9 }, icon: 'circle' },
    graphic: props.centerText ? [{ type: 'text', left: 'center', top: '39%', style: { text: props.centerText, fill: '#e8f4ff', font: '700 22px sans-serif' } }, { type: 'text', left: 'center', top: '51%', style: { text: props.centerSubtext ?? '', fill: '#6687a5', font: '10px sans-serif' } }] : [],
    series: [{ type: 'pie', radius: ['53%', '72%'], center: ['50%', '44%'], avoidLabelOverlap: true, itemStyle: { borderRadius: 3, borderColor: '#0a223d', borderWidth: 3 }, label: { show: false }, data: props.data.map((item) => ({ name: item.name, value: item.value, itemStyle: { color: item.color } })) }],
  }
  chart.setOption(option, true)
}
onMounted(() => { if (!el.value) return; chart = echarts.init(el.value); render(); observer = new ResizeObserver(() => chart?.resize()); observer.observe(el.value) })
watch(() => props.data, render, { deep: true })
onBeforeUnmount(() => { observer?.disconnect(); chart?.dispose() })
</script>
<template><div ref="el" class="chart-box" /></template>
