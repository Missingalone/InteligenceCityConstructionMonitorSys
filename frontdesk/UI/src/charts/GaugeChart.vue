<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'
import * as echarts from 'echarts'
import type { EChartsOption } from 'echarts'

const props = withDefaults(defineProps<{ value: number; max?: number; name: string; unit?: string; color?: string }>(), { max: 100, unit: '', color: '#26b8ff' })
const el = ref<HTMLDivElement>()
let chart: echarts.ECharts | undefined
let observer: ResizeObserver | undefined
const render = () => {
  if (!chart) return
  const option: EChartsOption = {
    series: [{ type: 'gauge', startAngle: 210, endAngle: -30, min: 0, max: props.max, splitNumber: 5, radius: '95%', center: ['50%', '55%'], progress: { show: true, roundCap: true, width: 9, itemStyle: { color: props.color } }, axisLine: { lineStyle: { width: 9, color: [[1, 'rgba(88,137,177,.16)']] } }, axisTick: { show: false }, splitLine: { distance: -14, length: 6, lineStyle: { color: '#557794', width: 1 } }, axisLabel: { distance: 13, color: '#607f9c', fontSize: 8 }, pointer: { show: false }, anchor: { show: false }, title: { offsetCenter: [0, '47%'], color: '#7595b2', fontSize: 11 }, detail: { valueAnimation: true, offsetCenter: [0, '7%'], formatter: `{value}${props.unit}`, color: '#eaf5ff', fontSize: 18, fontWeight: 700 }, data: [{ value: props.value, name: props.name }] }],
  }
  chart.setOption(option, true)
}
onMounted(() => { if (!el.value) return; chart = echarts.init(el.value); render(); observer = new ResizeObserver(() => chart?.resize()); observer.observe(el.value) })
watch(() => props.value, render)
onBeforeUnmount(() => { observer?.disconnect(); chart?.dispose() })
</script>
<template><div ref="el" class="chart-box" /></template>
