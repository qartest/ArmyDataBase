<template>
  <div class="container mt-4">
    <h1 class="mb-4">Дивизии</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="fetchAllData">Обновить/Загрузить список</button>
      <button class="btn btn-success" @click="openCreateModal">Создать</button>
    </div>
    <div v-if="isLoading" class="text-center">Загрузка...</div>
    <table v-else class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Название</th>
          <th>Командир</th>
          <th>Военные соединения</th>
          <th>Корпус</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="divisions.length === 0">
          <td colspan="5" class="text-nowrap text-center">Нет данных</td>
        </tr>
        <tr v-for="division in divisions" :key="division.id">
          <td>{{ division.name }}</td>
          <td>
            <span v-if="division.commanderMinimum">
              {{ division.commanderMinimum.secondName }} {{ division.commanderMinimum.firstName }} {{ division.commanderMinimum.fatherName || '' }} ({{ division.commanderMinimum.rankName || 'Нет звания' }})
            </span>
            <span v-else>-</span>
          </td>
          <td>
            <ul v-if="division.formations && division.formations.length">
              <li v-for="formation in division.formations" :key="formation.id">
                {{ formation.name }}
              </li>
            </ul>
            <span v-else>-</span>
          </td>
          <td>{{ division.corpName || '-' }}</td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="openEditModal(division)">Редактировать</button>
            <button class="btn btn-danger btn-sm" @click="deleteDivision(division)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>
    <division-form ref="divisionForm" :soldiers="soldiers" :formations="formations" :corps="corps" @save="saveDivision" />
  </div>
</template>

<script>
import axios from 'axios'
import DivisionForm from './DivisionForm.vue'

export default {
  name: 'DivisionList',
  components: { DivisionForm },
  data() {
    return {
      divisions: [],
      soldiers: [],
      formations: [],
      corps: [],
      isLoading: false,
    }
  },
  mounted() {
    this.fetchAllData()
  },
  methods: {
    async fetchAllData() {
      this.isLoading = true
      this.divisions = []
      this.soldiers = []
      this.formations = []
      this.corps = []
      try {
        await Promise.all([
          this.fetchDivisions(),
          this.fetchSoldiers(),
          this.fetchFormations(),
          this.fetchCorps(),
        ])
      } finally {
        this.isLoading = false
      }
    },
    async fetchDivisions() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/division/getAll')
        console.log('Полученные данные дивизий:', response.data)
        this.divisions = response.data.map(division => ({
          ...division,
          commanderId: division.commanderMinimum ? division.commanderMinimum.id : null,
          formationsId: division.formations ? division.formations.map(f => f.id) : [],
          corpId: division.corpId || null,
        }))
        console.log('Обработанные дивизии:', this.divisions)
      } catch (error) {
        console.error('Ошибка загрузки дивизий:', error)
        alert('Не удалось загрузить список дивизий: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchSoldiers() {
      try {
        const response = await axios.get('http://localhost:8080/api/soldier/getAll')
        console.log('Полученные данные солдат:', response.data)
        this.soldiers = Array.from(new Map(response.data.map(s => [s.id, s])).values())
        console.log('Уникальные солдаты:', this.soldiers)
      } catch (error) {
        console.error('Ошибка загрузки солдат:', error)
        alert('Не удалось загрузить список солдат: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchFormations() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/militaryFormation/getAll/Simple')
        console.log('Полученные данные военных соединений:', response.data)
        this.formations = Array.from(new Map(response.data.map(f => [f.id, f])).values())
        console.log('Уникальные военные соединения:', this.formations)
      } catch (error) {
        console.error('Ошибка загрузки военных соединений:', error)
        alert('Не удалось загрузить список военных соединений: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchCorps() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/corp/getAll/Simple')
        console.log('Полученные данные корпусов:', response.data)
        this.corps = Array.from(new Map(response.data.map(c => [c.id, c])).values())
        console.log('Уникальные корпуса:', this.corps)
      } catch (error) {
        console.error('Ошибка загрузки корпусов:', error)
        alert('Не удалось загрузить список корпусов: ' + (error.response?.data?.message || error.message))
      }
    },
    openCreateModal() {
      this.$refs.divisionForm.openModal(false, {})
    },
    openEditModal(division) {
      console.log('Редактирование дивизии:', division)
      this.$refs.divisionForm.openModal(true, division)
    },
    async saveDivision({ divisionData, isEditMode }) {
      try {
        console.log('Отправка данных дивизии:', divisionData)
        if (isEditMode) {
          await axios.put(`http://localhost:8080/api/units/division/update/${divisionData.id}`, divisionData)
        } else {
          await axios.post('http://localhost:8080/api/units/division/create', divisionData)
        }
        this.$refs.divisionForm.closeModal()
        await this.fetchAllData()
      } catch (error) {
        console.error('Ошибка сохранения дивизии:', error)
        alert('Не удалось сохранить дивизию: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteDivision(division) {
      if (confirm(`Вы уверены, что хотите удалить дивизию "${division.name}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/units/division/delete/${division.id}`)
          await this.fetchAllData()
        } catch (error) {
          console.error('Ошибка удаления дивизии:', error)
          alert('Не удалось удалить дивизию: ' + (error.response?.data?.message || error.message))
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
ul {
  margin: 0;
  padding-left: 20px;
}
li {
  margin-bottom: 8px;
}
</style>