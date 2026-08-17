<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Edit3, Eye, MapPinned, Plus, Trash2 } from 'lucide-vue-next'
import ResourceState from '@/components/common/ResourceState.vue'
import ProjectMap from './ProjectMap.vue'
import {
  createProject,
  deleteEnterpriseProject,
  getEnterpriseList,
  getEnterpriseProjectDetail,
  getProjectList,
  updateEnterpriseProject,
  type EnterpriseProjectDetail,
  type EnterpriseProjectUpdatePayload,
  type EnterpriseRecord,
  type ProjectPayload,
  type ProjectRecord,
} from '@/api/project'
import { hasPermission } from '@/api/permission'

interface ProjectEditorForm {
  id?: number
  projectCode: string
  projectName: string
  enterpriseId?: number
  supervisorOrgId?: number
  enterpriseName?: string
  supervisorName?: string
  projectType: string
  projectStatus: string
  address?: string
  projectManager?: string
  projectManagerPhone?: string
  progressPercent?: number
  description?: string
  longitude?: number
  latitude?: number
}

const data = ref<ProjectRecord[]>([])
const enterprises = ref<EnterpriseRecord[]>([])
const loading = ref(false)
const actionLoading = ref(false)
const error = ref('')
const actionError = ref('')
const dialog = ref(false)
const detailDialog = ref(false)
const mapDialog = ref(false)
const saving = ref(false)
const current = ref<EnterpriseProjectDetail>()
const page = ref(1)
const size = ref(10)
const query = reactive({ keyword: '', type: '', status: '' })
const formRef = ref<FormInstance>()

const emptyForm = (): ProjectEditorForm => ({
  projectCode: '',
  projectName: '',
  enterpriseId: undefined,
  supervisorOrgId: undefined,
  projectType: '',
  projectStatus: 'PREPARING',
  address: '',
  projectManager: '',
  projectManagerPhone: '',
  progressPercent: 0,
  description: '',
})
const form = ref<ProjectEditorForm>(emptyForm())
const isEditing = computed(() => form.value.id !== undefined)
const rules = computed<FormRules>(() => ({
  projectCode: [{ required: true, message: '请输入项目编号', trigger: 'blur' }],
  projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  enterpriseId: isEditing.value ? [] : [{ required: true, message: '请选择施工企业', trigger: 'change' }],
  supervisorOrgId: isEditing.value ? [] : [{ required: true, message: '请输入监管组织ID', trigger: 'change' }],
  projectType: [{ required: true, message: '请选择项目类型', trigger: 'change' }],
}))

const filtered = computed(() => data.value.filter((item) =>
  (!query.keyword || `${item.projectName}${item.projectCode}`.includes(query.keyword))
  && (!query.type || item.projectType === query.type)
  && (!query.status || item.projectStatus === query.status),
))
const rows = computed(() => filtered.value.slice((page.value - 1) * size.value, page.value * size.value))

const detailFields: Array<{ label: string; key: keyof EnterpriseProjectDetail; suffix?: string }> = [
  { label: '项目编号', key: 'projectCode' },
  { label: '项目名称', key: 'projectName' },
  { label: '施工企业', key: 'enterpriseName' },
  { label: '监管组织', key: 'supervisorName' },
  { label: '项目类型', key: 'projectType' },
  { label: '项目状态', key: 'projectStatus' },
  { label: '项目地址', key: 'address' },
  { label: '项目经理', key: 'projectManager' },
  { label: '联系电话', key: 'projectManagerPhone' },
  { label: '计划开工', key: 'plannedStartDate' },
  { label: '计划完工', key: 'plannedEndDate' },
  { label: '实际开工', key: 'actualStartDate' },
  { label: '实际完工', key: 'actualEndDate' },
  { label: '工程进度', key: 'progressPercent', suffix: '%' },
  { label: '创建时间', key: 'createTime' },
  { label: '项目说明', key: 'description' },
]

const load = async () => {
  loading.value = true
  error.value = ''
  try {
    const [projectResult, enterpriseResult] = await Promise.all([getProjectList(), getEnterpriseList()])
    data.value = projectResult.data ?? []
    enterprises.value = enterpriseResult.data ?? []
  } catch (reason) {
    error.value = reason instanceof Error ? reason.message : '网络异常'
  } finally {
    loading.value = false
  }
}

const openCreate = () => {
  actionError.value = ''
  form.value = emptyForm()
  dialog.value = true
}

const fetchDetail = async (row: ProjectRecord) => {
  actionLoading.value = true
  actionError.value = ''
  try {
    return (await getEnterpriseProjectDetail(row.id)).data
  } catch (reason) {
    actionError.value = reason instanceof Error ? reason.message : '项目详情加载失败'
    throw reason
  } finally {
    actionLoading.value = false
  }
}

const view = async (row: ProjectRecord) => {
  current.value = undefined
  detailDialog.value = true
  try {
    current.value = await fetchDetail(row)
  } catch {
    // 错误状态由详情弹窗统一展示。
  }
}

const edit = async (row: ProjectRecord) => {
  actionError.value = ''
  try {
    const detail = await fetchDetail(row)
    form.value = {
      id: detail.id,
      projectCode: detail.projectCode,
      projectName: detail.projectName,
      enterpriseName: detail.enterpriseName,
      supervisorName: detail.supervisorName,
      projectType: detail.projectType,
      projectStatus: detail.projectStatus,
      address: detail.address,
      projectManager: detail.projectManager,
      projectManagerPhone: detail.projectManagerPhone,
      progressPercent: detail.progressPercent,
      description: detail.description,
      longitude: detail.longitude,
      latitude: detail.latitude,
    }
    dialog.value = true
  } catch {
    ElMessage.error(actionError.value || '无法加载待修改项目')
  }
}

const save = async () => {
  if (!await formRef.value?.validate()) return
  saving.value = true
  actionError.value = ''
  try {
    if (isEditing.value) {
      const payload: EnterpriseProjectUpdatePayload = {
        id: form.value.id!,
        projectCode: form.value.projectCode,
        projectName: form.value.projectName,
        enterpriseName: form.value.enterpriseName,
        supervisorName: form.value.supervisorName,
        projectType: form.value.projectType,
        projectStatus: form.value.projectStatus,
        address: form.value.address,
        projectManager: form.value.projectManager,
        projectManagerPhone: form.value.projectManagerPhone,
        progressPercent: form.value.progressPercent,
        description: form.value.description,
      }
      const result = await updateEnterpriseProject(payload)
      if (result.data !== true) throw new Error('后端未完成项目更新')
      ElMessage.success('项目修改成功')
    } else {
      const payload: ProjectPayload = {
        projectCode: form.value.projectCode,
        projectName: form.value.projectName,
        enterpriseId: form.value.enterpriseId!,
        supervisorOrgId: form.value.supervisorOrgId!,
        projectType: form.value.projectType,
        projectStatus: form.value.projectStatus,
        address: form.value.address,
        longitude: form.value.longitude,
        latitude: form.value.latitude,
        progressPercent: form.value.progressPercent,
        projectManager: form.value.projectManager,
        managerMobile: form.value.projectManagerPhone,
        description: form.value.description,
      }
      await createProject(payload)
      ElMessage.success('项目新增成功')
    }
    dialog.value = false
    await load()
  } catch (reason) {
    actionError.value = reason instanceof Error ? reason.message : '项目保存失败'
    ElMessage.error(actionError.value)
  } finally {
    saving.value = false
  }
}

const remove = async (row: ProjectRecord) => {
  await ElMessageBox.confirm(
    `删除后项目将无法在企业端继续使用，确认删除“${row.projectName}”？`,
    '删除项目',
    { type: 'warning', confirmButtonText: '确认删除', cancelButtonText: '取消' },
  )
  try {
    const result = await deleteEnterpriseProject(row.id)
    if (result.data !== true) throw new Error('后端未删除该项目')
    ElMessage.success('项目已删除')
    await load()
  } catch (reason) {
    ElMessage.error(reason instanceof Error ? reason.message : '删除失败')
  }
}

const openMap = (row: ProjectRecord) => {
  current.value = {
    ...row,
    supervisorName: undefined,
    projectManagerPhone: row.managerMobile,
  }
  mapDialog.value = true
}

onMounted(load)
</script>

<template>
  <article class="tech-panel resource-panel">
    <div class="panel-head">
      <h2 class="panel-title">施工项目档案</h2>
      <button v-if="hasPermission('supervisor:project:add')" class="tech-button" @click="openCreate">
        <Plus :size="14" />新增项目
      </button>
    </div>

    <div class="resource-toolbar">
      <el-input v-model="query.keyword" clearable placeholder="项目名称 / 编号" />
      <el-select v-model="query.type" clearable placeholder="项目类型">
        <el-option label="房屋建筑" value="BUILDING" />
        <el-option label="市政工程" value="MUNICIPAL" />
      </el-select>
      <el-select v-model="query.status" clearable placeholder="项目状态">
        <el-option label="筹备中" value="PREPARING" />
        <el-option label="施工中" value="CONSTRUCTING" />
        <el-option label="已完工" value="COMPLETED" />
      </el-select>
      <button class="tech-button secondary" :disabled="loading" @click="load">查询</button>
    </div>

    <ResourceState :loading="loading" :error="error" :empty="!rows.length" @retry="load">
      <el-table :data="rows">
        <el-table-column prop="projectCode" label="项目编号" />
        <el-table-column prop="projectName" label="项目名称" min-width="150" />
        <el-table-column prop="enterpriseName" label="施工企业" min-width="140" />
        <el-table-column prop="projectType" label="类型" />
        <el-table-column prop="projectStatus" label="状态">
          <template #default="{ row }"><span class="status-pill status-running">{{ row.projectStatus }}</span></template>
        </el-table-column>
        <el-table-column prop="progressPercent" label="进度">
          <template #default="{ row }">{{ row.progressPercent || 0 }}%</template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button link @click="view(row)"><Eye :size="13" />详情</el-button>
            <el-button link @click="openMap(row)"><MapPinned :size="13" />地图</el-button>
            <el-button link @click="edit(row)"><Edit3 :size="13" />修改</el-button>
            <el-button link type="danger" @click="remove(row)"><Trash2 :size="13" />删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="resource-pagination">
        <span>共 {{ filtered.length }} 个项目</span>
        <el-pagination v-model:current-page="page" v-model:page-size="size" layout="prev,pager,next,sizes" :total="filtered.length" />
      </div>
    </ResourceState>
  </article>

  <el-dialog v-model="dialog" :title="isEditing ? '修改施工项目' : '新增施工项目'" width="min(760px, 92vw)" destroy-on-close>
    <el-alert v-if="actionError" :title="actionError" type="error" show-icon :closable="false" />
    <el-form ref="formRef" v-loading="actionLoading" :model="form" :rules="rules" label-width="100px" class="dialog-form">
      <el-form-item label="项目编号" prop="projectCode"><el-input v-model="form.projectCode" /></el-form-item>
      <el-form-item label="项目名称" prop="projectName"><el-input v-model="form.projectName" /></el-form-item>
      <template v-if="isEditing">
        <el-form-item label="施工企业"><el-input v-model="form.enterpriseName" disabled /></el-form-item>
        <el-form-item label="监管组织"><el-input v-model="form.supervisorName" disabled /></el-form-item>
      </template>
      <template v-else>
        <el-form-item label="施工企业" prop="enterpriseId">
          <el-select v-model="form.enterpriseId" placeholder="请选择施工企业">
            <el-option v-for="enterprise in enterprises" :key="enterprise.id" :label="enterprise.enterpriseName" :value="enterprise.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="监管组织ID" prop="supervisorOrgId">
          <el-input-number v-model="form.supervisorOrgId" :min="1" />
        </el-form-item>
      </template>
      <el-form-item label="项目类型" prop="projectType">
        <el-select v-model="form.projectType"><el-option label="房屋建筑" value="BUILDING" /><el-option label="市政工程" value="MUNICIPAL" /></el-select>
      </el-form-item>
      <el-form-item label="项目状态">
        <el-select v-model="form.projectStatus"><el-option label="筹备中" value="PREPARING" /><el-option label="施工中" value="CONSTRUCTING" /><el-option label="已完工" value="COMPLETED" /></el-select>
      </el-form-item>
      <el-form-item label="项目地址"><el-input v-model="form.address" /></el-form-item>
      <el-form-item label="项目经理"><el-input v-model="form.projectManager" /></el-form-item>
      <el-form-item label="联系电话"><el-input v-model="form.projectManagerPhone" /></el-form-item>
      <el-form-item label="工程进度"><el-input-number v-model="form.progressPercent" :min="0" :max="100" /></el-form-item>
      <el-form-item label="项目说明" class="span-2"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
    </el-form>
    <template #footer>
      <button class="tech-button secondary" :disabled="saving" @click="dialog = false">取消</button>
      <button class="tech-button" :disabled="saving || actionLoading" @click="save">{{ saving ? '保存中...' : '保存' }}</button>
    </template>
  </el-dialog>

  <el-dialog v-model="detailDialog" title="项目详情" width="min(850px, 92vw)" destroy-on-close>
    <ResourceState :loading="actionLoading" :error="actionError" :empty="!current">
      <div v-if="current" class="detail-grid">
        <div v-for="field in detailFields" :key="field.key">
          <small>{{ field.label }}</small>
          <b>{{ current[field.key] ?? '—' }}{{ current[field.key] != null ? field.suffix : '' }}</b>
        </div>
      </div>
    </ResourceState>
  </el-dialog>

  <el-dialog v-model="mapDialog" title="项目地图位置" width="min(850px, 92vw)">
    <ProjectMap :name="current?.projectName" :longitude="current?.longitude" :latitude="current?.latitude" />
  </el-dialog>
</template>

<style scoped>
.resource-panel { overflow: visible; }
.resource-toolbar { display: grid; grid-template-columns: 1.4fr repeat(2, 1fr) auto; gap: 8px; padding: 12px; }
.resource-pagination { display: flex; align-items: center; justify-content: space-between; padding: 10px; color: #6f8da9; font-size: 10px; }
.dialog-form { display: grid; grid-template-columns: 1fr 1fr; margin-top: 12px; }
.dialog-form .span-2 { grid-column: 1 / -1; }
.detail-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; }
.detail-grid > div { padding: 10px; border: 1px solid rgba(79, 168, 255, .13); border-radius: 5px; background: rgba(9, 31, 56, .5); }
.detail-grid small { display: block; color: #6584a0; }
.detail-grid b { display: block; margin-top: 5px; color: #d4e6f5; font-size: 11px; overflow-wrap: anywhere; }
@media (max-width: 700px) { .resource-toolbar, .dialog-form, .detail-grid { grid-template-columns: 1fr; } }
</style>
