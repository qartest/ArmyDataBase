<template>
  <div class="container mt-4">
    <h1 class="mb-4">Корпуса</h1>
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
          <th>Дивизии</th>
          <th>Армия</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="corps.length === 0">
          <td colspan="5" class="text-nowrap text-center">Нет данных</td>
        </tr>
        <tr v-for="corp in corps" :key="corp.id">
          <td>{{ corp.name }}</td>
          <td>
            <span v-if="corp.commanderMinimum">
              {{ corp.commanderMinimum.secondName }} {{ corp.commanderMinimum.firstName }} {{ corp.commanderMinimum.fatherName || '' }} ({{ corp.commanderMinimum.rankName || 'Нет звания' }})
            </span>
            <span v-else>-</span>
          </td>
          <td>
            <ul v-if="corp.divisions && corp.divisions.length">
              <li v-for="division in corp.divisions" :key="division.id">
                {{ division.name }}
              </li>
            </ul>
            <span v-else>-</span>
          </td>
          <td>{{ corp.armyName || '-' }}</td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="openEditModal(corp)">Редактировать</button>
            <button class="btn btn-danger btn-sm" @click="deleteCorp(corp)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>
    <corp-form ref="corpForm" :soldiers="soldiers" :divisions="divisions" :armies="armies" @save="saveCorp" />
  </div>
</template>

<script>
import axios from 'axios'
import CorpForm from './CorpForm.vue'

export default {
  name: 'CorpList',
  components: { CorpForm },
  data() {
    return {
      corps: [],
      soldiers: [],
      divisions: [],
      armies: [],
      isLoading: false,
    }
  },
  mounted() {
    this.fetchAllData()
  },
  methods: {
    async fetchAllData() {
      this.isLoading = true
      this.corps = []
      this.soldiers = []
      this.divisions = []
      this.armies = []
      try {
        await Promise.all([
          this.fetchCorps(),
          this.fetchSoldiers(),
          this.fetchDivisions(),
          this.fetchArmies(),
        ])
      } finally {
        this.isLoading = false
      }
    },
    async fetchCorps() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/corp/getAll')
        console.log('Полученные данные корпусов:', response.data)
        this.corps = response.data.map(corp => ({
          ...corp,
          commanderId: corp.commanderMinimum ? corp.commanderMinimum.id : null,
          divisionsId: corp.divisions ? corp.divisions.map(d => d.id) : [],
          armyId: corp.armyId || null,
        }))
        console.log('Обработанные корпуса:', this.corps)
      } catch (error) {
        console.error('Ошибка загрузки корпусов:', error)
        alert('Не удалось загрузить список корпусов: ' + (error.response?.data?.message || error.message))
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
    async fetchDivisions() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/division/getAll/Simple')
        console.log('Полученные данные дивизий:', response.data)
        this.divisions = Array.from(new Map(response.data.map(d => [d.id, d])).values())
        console.log('Уникальные дивизии:', this.divisions)
      } catch (error) {
        console.error('Ошибка загрузки дивизий:', error)
        alert('Не удалось загрузить список дивизий: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchArmies() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/army/getAll/Simple')
        console.log('Полученные данные армий:', response.data)
        this.armies = Array.from(new Map(response.data.map(a => [a.id, a])).values())
        console.log('Уникальные армии:', this.armies)
      } catch (error) {
        console.error('Ошибка загрузки армий:', error)
        alert('Не удалось загрузить список армий: ' + (error.response?.data?.message || error.message))
      }
    },
    openCreateModal() {
      this.$refs.corpForm.openModal(false, {})
    },
    openEditModal(corp) {
      console.log('Редактирование корпуса:', corp)
      this.$refs.corpForm.openModal(true, corp)
    },
    async saveCorp({ corpData, isEditMode }) {
      try {
        console.log('Отправка данных корпуса:', corpData)
        if (isEditMode) {
          await axios.put(`http://localhost:8080/api/units/corp/update/${corpData.id}`, corpData)
        } else {
          await axios.post('http://localhost:8080/api/units/corp/create', corpData)
        }
        this.$refs.corpForm.closeModal()
        await this.fetchAllData()
      } catch (error) {
        console.error('Ошибка сохранения корпуса:', error)
        alert('Не удалось сохранить корпус: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteCorp(corp) {
      if (confirm(`Вы уверены, что хотите удалить корпус "${corp.name}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/units/corp/delete/${corp.id}`)
          await this.fetchAllData()
        } catch (error) {
          console.error('Ошибка удаления корпуса:', error)
          alert('Не удалось удалить корпус: ' + (error.response?.data?.message || error.message))
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