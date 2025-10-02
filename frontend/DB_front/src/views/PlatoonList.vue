<template>
  <div class="container mt-4">
    <h1 class="mb-4">Взводы</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="fetchAllData">Обновить/Загрузить список</button>
      <button class="btn btn-success" @click="openCreateModal">Создать</button>
    </div>
    <div v-if="isLoading" class="text-center">Загрузка...</div>
    <table v-else class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>ID</th>
          <th>Название</th>
          <th>Командир</th>
          <th>Отряды</th>
          <th>Рота</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="platoons.length === 0">
          <td colspan="6" class="text-nowrap text-center">Нет данных</td>
        </tr>
        <tr v-for="platoon in platoons" :key="platoon.id">
          <td>{{ platoon.id }}</td>
          <td>{{ platoon.name }}</td>
          <td>
            <span v-if="platoon.commanderMinimum">
              {{ platoon.commanderMinimum.secondName }} {{ platoon.commanderMinimum.firstName }} {{ platoon.commanderMinimum.fatherName || '' }} ({{ platoon.commanderMinimum.rankName || 'Нет звания' }})
            </span>
            <span v-else>-</span>
          </td>
          <td>
            <ul v-if="platoon.squads && platoon.squads.length">
              <li v-for="squad in platoon.squads" :key="squad.id">
                {{ squad.name }}
              </li>
            </ul>
            <span v-else>-</span>
          </td>
          <td>{{ platoon.companyName || '-' }}</td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="openEditModal(platoon)">Редактировать</button>
            <button class="btn btn-danger btn-sm" @click="deletePlatoon(platoon)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>
    <platoon-form ref="platoonForm" :soldiers="soldiers" :squads="squads" :companies="companies" @save="savePlatoon" />
  </div>
</template>

<script>
import axios from 'axios'
import PlatoonForm from './PlatoonForm.vue'

export default {
  name: 'PlatoonList',
  components: { PlatoonForm },
  data() {
    return {
      platoons: [],
      soldiers: [],
      squads: [],
      companies: [],
      isLoading: false,
    }
  },
  mounted() {
    this.fetchAllData()
  },
  methods: {
    async fetchAllData() {
      this.isLoading = true
      this.platoons = []
      this.soldiers = []
      this.squads = []
      this.companies = []
      try {
        await Promise.all([
          this.fetchPlatoons(),
          this.fetchSoldiers(),
          this.fetchSquads(),
          this.fetchCompanies(),
        ])
      } finally {
        this.isLoading = false
      }
    },
    async fetchPlatoons() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/platoon/getAll')
        console.log('Полученные данные взводов:', response.data)
        this.platoons = response.data.map(platoon => ({
          ...platoon,
          commanderId: platoon.commanderMinimum ? platoon.commanderMinimum.id : null,
          squadsId: platoon.squads ? platoon.squads.map(s => s.id) : [],
          companyId: platoon.companyId || null,
        }))
        console.log('Обработанные взводы:', this.platoons)
      } catch (error) {
        console.error('Ошибка загрузки взводов:', error)
        alert('Не удалось загрузить список взводов: ' + (error.response?.data?.message || error.message))
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
    async fetchSquads() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/squad/getAll/Simple')
        console.log('Полученные данные отрядов:', response.data)
        this.squads = Array.from(new Map(response.data.map(s => [s.id, s])).values())
        console.log('Уникальные отряды:', this.squads)
      } catch (error) {
        console.error('Ошибка загрузки отрядов:', error)
        alert('Не удалось загрузить список отрядов: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchCompanies() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/company/getAll/Simple')
        console.log('Полученные данные рот:', response.data)
        this.companies = Array.from(new Map(response.data.map(c => [c.id, c])).values())
        console.log('Уникальные роты:', this.companies)
      } catch (error) {
        console.error('Ошибка загрузки рот:', error)
        alert('Не удалось загрузить список рот: ' + (error.response?.data?.message || error.message))
      }
    },
    openCreateModal() {
      this.$refs.platoonForm.openModal(false, {})
    },
    openEditModal(platoon) {
      console.log('Редактирование взвода:', platoon)
      this.$refs.platoonForm.openModal(true, platoon)
    },
    async savePlatoon({ platoonData, isEditMode }) {
      try {
        console.log('Отправка данных взвода:', platoonData)
        if (isEditMode) {
          await axios.put(`http://localhost:8080/api/units/platoon/update/${platoonData.id}`, platoonData)
        } else {
          await axios.post('http://localhost:8080/api/units/platoon/create', platoonData)
        }
        this.$refs.platoonForm.closeModal()
        await this.fetchAllData()
      } catch (error) {
        console.error('Ошибка сохранения взвода:', error)
        alert('Не удалось сохранить взвод: ' + (error.response?.data?.message || error.message))
      }
    },
    async deletePlatoon(platoon) {
      if (confirm(`Вы уверены, что хотите удалить взвод "${platoon.name}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/units/platoon/delete/${platoon.id}`)
          await this.fetchAllData()
        } catch (error) {
          console.error('Ошибка удаления взвода:', error)
          alert('Не удалось удалить взвод: ' + (error.response?.data?.message || error.message))
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