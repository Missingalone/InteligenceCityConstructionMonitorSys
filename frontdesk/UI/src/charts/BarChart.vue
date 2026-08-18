<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'
import * as echarts from 'echarts'
import type { EChartsOption } from 'echarts'

const props = defineProps<{ labels: string[]; values: number[]; horizontal?: boolean; colors?: string[] }>()
const el = ref<HTMLDivElement>()
let chart: echarts.ECharts | undefined
let observer: ResizeObserver | undefined
const render = () => {
  if (!chart) return
  const category = { type: 'category' as const, data: props.labels, axisTick: { show: false }, axisLine: { lineStyle: { color: '#244665' } }, axisLabel: { color: '#7896b2', fontSize: 10, interval: 0 } }
  const value = { type: 'value' as const, splitLine: { lineStyle: { color: 'rgba(79,126,166,.13)', type: 'dashed' as const } }, axisLabel: { color: '#607f9c', fontSize: 9 } }
  const option: EChartsOption = {
    animationDuration: 900,
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(6,24,45,.95)', borderColor: 'rgba(79,168,255,.35)', textStyle: { color: '#dceaff' } },
    grid: { left: 8, right: 14, top: 24, bottom: 5, containLabel: true },
    xAxis: props.horizontal ? value : category,
    yAxis: props.horizontal ? category : value,
    series: [{ type: 'bar', data: props.values.map((v, i) => ({ value: v, itemStyle: { color: props.colors?.[i] ?? new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#36b9ff' }, { offset: 1, color: '#1666da' }]), borderRadius: props.horizontal ? [0, 3, 3, 0] : [3, 3, 0, 0] } })), barWidth: props.horizontal ? 9 : '38%', label: { show: true, position: props.horizontal ? 'right' : 'top', color: '#94b6d5', fontSize: 10 } }],
  }
  chart.setOption(option, true)
}
onMounted(() => { if (!el.value) return; chart = echarts.init(el.value); render(); observer = new ResizeObserver(() => chart?.resize()); observer.observe(el.value) })
watch(() => [props.labels, props.values], render, { deep: true })
onBeforeUnmount(() => { observer?.disconnect(); chart?.dispose() })
</script>
<template><div ref="el" class="chart-box" /></template>
