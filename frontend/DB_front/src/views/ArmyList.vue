<template>
  <div class="container mt-4">
    <h1 class="mb-4">Армии</h1>
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
          <th>Корпуса</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="armies.length === 0">
          <td colspan="4" class="text-nowrap text-center">Нет данных</td>
        </tr>
        <tr v-for="army in armies" :key="army.id">
          <td>{{ army.name }}</td>
          <td>
            <span v-if="army.commanderMinimum">
              {{ army.commanderMinimum.secondName }} {{ army.commanderMinimum.firstName }} {{ army.commanderMinimum.fatherName || '' }} ({{ army.commanderMinimum.rankName || 'Нет звания' }})
            </span>
            <span v-else>-</span>
          </td>
          <td>
            <ul v-if="army.corps && army.corps.length">
              <li v-for="corp in army.corps" :key="corp.id">
                {{ corp.name }}
              </li>
            </ul>
            <span v-else>-</span>
          </td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="openEditModal(army)">Редактировать</button>
            <button class="btn btn-danger btn-sm" @click="deleteArmy(army)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>
    <army-form ref="armyForm" :soldiers="soldiers" :corps="corps" @save="saveArmy" />
  </div>
</template>

<script>
import axios from 'axios'
import ArmyForm from './ArmyForm.vue'

export default {
  name: 'ArmyList',
  components: { ArmyForm },
  data() {
    return {
      armies: [],
      soldiers: [],
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
      this.armies = []
      this.soldiers = []
      this.corps = []
      try {
        await Promise.all([
          this.fetchArmies(),
          this.fetchSoldiers(),
          this.fetchCorps(),
        ])
      } finally {
        this.isLoading = false
      }
    },
    async fetchArmies() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/army/getAll')
        console.log('Полученные данные армий:', response.data)
        this.armies = response.data.map(army => ({
          ...army,
          commanderId: army.commanderMinimum ? army.commanderMinimum.id : null,
          corpsId: army.corps ? army.corps.map(c => c.id) : [],
        }))
        console.log('Обработанные армии:', this.armies)
      } catch (error) {
        console.error('Ошибка загрузки армий:', error)
        alert('Не удалось загрузить список армий: ' + (error.response?.data?.message || error.message))
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
      this.$refs.armyForm.openModal(false, {})
    },
    openEditModal(army) {
      console.log('Редактирование армии:', army)
      this.$refs.armyForm.openModal(true, army)
    },
    async saveArmy({ armyData, isEditMode }) {
      try {
        console.log('Отправка данных армии:', armyData)
        if (isEditMode) {
          await axios.put(`http://localhost:8080/api/units/army/update/${armyData.id}`, armyData)
        } else {
          await axios.post('http://localhost:8080/api/units/army/create', armyData)
        }
        this.$refs.armyForm.closeModal()
        await this.fetchAllData()
      } catch (error) {
        console.error('Ошибка сохранения армии:', error)
        alert('Не удалось сохранить армию: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteArmy(army) {
      if (confirm(`Вы уверены, что хотите удалить армию "${army.name}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/units/army/delete/${army.id}`)
          await this.fetchAllData()
        } catch (error) {
          console.error('Ошибка удаления армии:', error)
          alert('Не удалось удалить армию: ' + (error.response?.data?.message || error.message))
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