<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'
import * as echarts from 'echarts'
import type { EChartsOption } from 'echarts'

const props = withDefaults(defineProps<{ labels: string[]; series: Array<{ name: string; data: number[]; color?: string; area?: boolean }>; smooth?: boolean; unit?: string }>(), { smooth: true, unit: '' })
const el = ref<HTMLDivElement>()
let chart: echarts.ECharts | undefined
let observer: ResizeObserver | undefined

const render = () => {
  if (!chart) return
  const option: EChartsOption = {
    animationDuration: 800,
    color: props.series.map((s) => s.color ?? '#2f93ff'),
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(6,24,45,.95)', borderColor: 'rgba(79,168,255,.35)', textStyle: { color: '#dceaff' } },
    legend: { show: props.series.length > 1, bottom: 0, textStyle: { color: '#6f91b1', fontSize: 10 }, icon: 'circle' },
    grid: { left: 10, right: 14, top: 22, bottom: props.series.length > 1 ? 34 : 21, containLabel: true },
    xAxis: { type: 'category', boundaryGap: false, data: props.labels, axisLine: { lineStyle: { color: '#244665' } }, axisTick: { show: false }, axisLabel: { color: '#6f91b1', fontSize: 10 } },
    yAxis: { type: 'value', name: props.unit, nameTextStyle: { color: '#557795', fontSize: 9 }, splitLine: { lineStyle: { color: 'rgba(79,126,166,.13)', type: 'dashed' } }, axisLabel: { color: '#607f9c', fontSize: 9 } },
    series: props.series.map((s) => ({ name: s.name, type: 'line', smooth: props.smooth, showSymbol: true, symbolSize: 5, data: s.data, lineStyle: { width: 2 }, itemStyle: { borderWidth: 2, borderColor: '#071a30' }, areaStyle: s.area ? { opacity: .18, color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: s.color ?? '#2f93ff' }, { offset: 1, color: 'rgba(0,0,0,0)' }]) } : undefined })),
  }
  chart.setOption(option, true)
}
onMounted(() => { if (!el.value) return; chart = echarts.init(el.value); render(); observer = new ResizeObserver(() => chart?.resize()); observer.observe(el.value) })
watch(() => [props.labels, props.series], render, { deep: true })
onBeforeUnmount(() => { observer?.disconnect(); chart?.dispose() })
</script>
<template><div ref="el" class="chart-box" /></template>
