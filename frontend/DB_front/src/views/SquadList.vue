<template>
  <div class="container mt-4">
    <h1 class="mb-4">Отряды</h1>
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
          <th>Солдаты</th>
          <th>Взвод</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="squads.length === 0">
          <td colspan="6" class="text-nowrap text-center">Нет данных</td>
        </tr>
        <tr v-for="squad in squads" :key="squad.id">
          <td>{{ squad.id }}</td>
          <td>{{ squad.name }}</td>
          <td>
            <span v-if="squad.commanderMinimum">
              {{ squad.commanderMinimum.secondName }} {{ squad.commanderMinimum.firstName }} {{ squad.commanderMinimum.fatherName || '' }} ({{ squad.commanderMinimum.rankName || 'Нет звания' }})
            </span>
            <span v-else>-</span>
          </td>
          <td>
            <ul v-if="squad.soldiersMinimumDto && squad.soldiersMinimumDto.length">
              <li v-for="soldier in squad.soldiersMinimumDto" :key="soldier.id">
                <strong>ФИО:</strong> {{ soldier.secondName }} {{ soldier.firstName }} {{ soldier.fatherName || '' }}<br>
                <strong>Звание:</strong> {{ soldier.rankName || '-' }}
              </li>
            </ul>
            <span v-else>-</span>
          </td>
          <td>{{ squad.platoonName || '-' }}</td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="openEditModal(squad)">Редактировать</button>
            <button class="btn btn-danger btn-sm" @click="deleteSquad(squad)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>
    <squad-form ref="squadForm" :soldiers="soldiers" :platoons="platoons" @save="saveSquad" />
  </div>
</template>

<script>
import axios from 'axios'
import SquadForm from './SquadForm.vue'

export default {
  name: 'SquadList',
  components: { SquadForm },
  data() {
    return {
      squads: [],
      soldiers: [],
      platoons: [],
      isLoading: false,
    }
  },
  mounted() {
    this.fetchAllData()
  },
  methods: {
    async fetchAllData() {
      this.isLoading = true
      this.squads = []
      this.soldiers = []
      this.platoons = []
      try {
        await Promise.all([
          this.fetchSquads(),
          this.fetchSoldiers(),
          this.fetchPlatoons(),
        ])
      } finally {
        this.isLoading = false
      }
    },
    async fetchSquads() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/squad/getAll')
        console.log('Полученные данные отрядов:', response.data)
        this.squads = response.data.map(squad => ({
          ...squad,
          commanderId: squad.commanderMinimum ? squad.commanderMinimum.id : null,
          soldiersId: squad.soldiersMinimumDto ? squad.soldiersMinimumDto.map(s => s.id) : [],
          platoonId: squad.platoonId || null,
        }))
        console.log('Обработанные отряды:', this.squads)
      } catch (error) {
        console.error('Ошибка загрузки отрядов:', error)
        alert('Не удалось загрузить список отрядов: ' + (error.response?.data?.message || error.message))
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
    async fetchPlatoons() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/platoon/getAll')
        console.log('Полученные данные взводов:', response.data)
        this.platoons = Array.from(new Map(response.data.map(p => [p.id, p])).values())
        console.log('Уникальные взводы:', this.platoons)
      } catch (error) {
        console.error('Ошибка загрузки взводов:', error)
        alert('Не удалось загрузить список взводов: ' + (error.response?.data?.message || error.message))
      }
    },
    openCreateModal() {
      this.$refs.squadForm.openModal(false, {})
    },
    openEditModal(squad) {
      console.log('Редактирование отряда:', squad)
      this.$refs.squadForm.openModal(true, squad)
    },
    async saveSquad({ squadData, isEditMode }) {
      try {
        console.log('Отправка данных отряда:', squadData)
        if (isEditMode) {
          await axios.put(`http://localhost:8080/api/units/squad/update/${squadData.id}`, squadData)
        } else {
          await axios.post('http://localhost:8080/api/units/squad/create', squadData)
        }
        this.$refs.squadForm.closeModal()
        await this.fetchAllData()
      } catch (error) {
        console.error('Ошибка сохранения отряда:', error)
        alert('Не удалось сохранить отряд: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteSquad(squad) {
      if (confirm(`Вы уверены, что хотите удалить отряд "${squad.name}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/units/squad/delete/${squad.id}`)
          await this.fetchAllData()
        } catch (error) {
          console.error('Ошибка удаления отряда:', error)
          alert('Не удалось удалить отряд: ' + (error.response?.data?.message || error.message))
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