<template>
  <div class="container mt-4">
    <h1 class="mb-4">Здания</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="fetchAllData">Обновить/Загрузить список</button>
      <button class="btn btn-success" @click="openCreateModal">Создать</button>
    </div>
    <div v-if="isLoading" class="text-center">Загрузка...</div>
    <table v-else class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Название</th>
          <th>Адрес</th>
          <th>Тип</th>
          <th>Штаб</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="buildings.length === 0">
          <td colspan="5" class="text-nowrap text-center">Нет данных</td>
        </tr>
        <tr v-for="building in buildings" :key="building.id">
          <td>{{ building.name }}</td>
          <td>{{ building.address }}</td>
          <td>{{ translateBuildingType(building.type) }}</td>
          <td>{{ building.headquarterName || '-' }}</td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="openEditModal(building)">Редактировать</button>
            <button class="btn btn-danger btn-sm" @click="deleteBuilding(building)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>
    <building-form ref="buildingForm" :headquarters="headquarters" @save="saveBuilding" />
  </div>
</template>

<script>
import axios from 'axios'
import BuildingForm from './BuildingForm.vue'

export default {
  name: 'BuildingList',
  components: { BuildingForm },
  data() {
    return {
      buildings: [],
      headquarters: [],
      isLoading: false,
    }
  },
  mounted() {
    this.fetchAllData()
  },
  methods: {
    async fetchAllData() {
      this.isLoading = true
      this.buildings = []
      this.headquarters = []
      try {
        await Promise.all([
          this.fetchBuildings(),
          this.fetchHeadquarters(),
        ])
      } finally {
        this.isLoading = false
      }
    },
    async fetchBuildings() {
      try {
        const response = await axios.get('http://localhost:8080/api/building/getAll')
        console.log('Полученные данные зданий:', response.data)
        this.buildings = response.data
        console.log('Обработанные здания:', this.buildings)
      } catch (error) {
        console.error('Ошибка загрузки зданий:', error)
        alert('Не удалось загрузить список зданий: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchHeadquarters() {
      try {
        const response = await axios.get('http://localhost:8080/api/headquarter/getAll')
        console.log('Полученные данные штабов:', response.data)
        this.headquarters = Array.from(new Map(response.data.map(h => [h.id, h])).values())
        console.log('Уникальные штабы:', this.headquarters)
      } catch (error) {
        console.error('Ошибка загрузки штабов:', error)
        alert('Не удалось загрузить список штабов: ' + (error.response?.data?.message || error.message))
      }
    },
    translateBuildingType(type) {
      const types = {
        BARRACKS: 'Казармы',
        WAREHOUSE: 'Склад',
        TRAINING: 'Учебный корпус',
        HANGAR: 'Ангар',
      }
      return types[type] || type
    },
    openCreateModal() {
      this.$refs.buildingForm.openModal(false, {})
    },
    openEditModal(building) {
      console.log('Редактирование здания:', building)
      this.$refs.buildingForm.openModal(true, building)
    },
    async saveBuilding({ buildingData, isEditMode }) {
      try {
        console.log('Отправка данных здания:', buildingData)
        if (isEditMode) {
          await axios.put(`http://localhost:8080/api/building/update/${buildingData.id}`, buildingData)
        } else {
          await axios.post('http://localhost:8080/api/building/create', buildingData)
        }
        this.$refs.buildingForm.closeModal()
        await this.fetchAllData()
      } catch (error) {
        console.error('Ошибка сохранения здания:', error)
        alert('Не удалось сохранить здание: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteBuilding(building) {
      if (confirm(`Вы уверены, что хотите удалить здание "${building.name}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/building/delete/${building.id}`)
          await this.fetchAllData()
        } catch (error) {
          console.error('Ошибка удаления здания:', error)
          alert('Не удалось удалить здание: ' + (error.response?.data?.message || error.message))
        }
      }
    },
  },
}
</script>

<style scoped>
.table th,
.table td {
  vertical-align: middle;
}
</style>