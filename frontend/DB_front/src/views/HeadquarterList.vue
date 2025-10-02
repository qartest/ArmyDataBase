<template>
  <div class="container mt-4">
    <h1 class="mb-4">Штабы</h1>
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
          <th>Здания</th>
          <th>Военные соединения</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="headquarters.length === 0">
          <td colspan="5" class="text-nowrap text-center">Нет данных</td>
        </tr>
        <tr v-for="headquarter in headquarters" :key="headquarter.id">
          <td>{{ headquarter.name }}</td>
          <td>{{ headquarter.address }}</td>
          <td>
            <ul v-if="headquarter.buildings && headquarter.buildings.length">
              <li v-for="building in headquarter.buildings" :key="building.id">
                {{ building.name }} ({{ building.type || 'Неизвестно' }})
              </li>
            </ul>
            <span v-else>-</span>
          </td>
          <td>
            <ul v-if="headquarter.formations && headquarter.formations.length">
              <li v-for="formation in headquarter.formations" :key="formation.id">
                {{ formation.name }}
              </li>
            </ul>
            <span v-else>-</span>
          </td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="openEditModal(headquarter)">Редактировать</button>
            <button class="btn btn-danger btn-sm" @click="deleteHeadquarter(headquarter)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>
    <headquarter-form ref="headquarterForm" @save="saveHeadquarter" />
  </div>
</template>

<script>
import axios from 'axios'
import HeadquarterForm from './HeadquarterForm.vue'

export default {
  name: 'HeadquarterList',
  components: { HeadquarterForm },
  data() {
    return {
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
      this.headquarters = []
      try {
        await this.fetchHeadquarters()
      } finally {
        this.isLoading = false
      }
    },
    async fetchHeadquarters() {
      try {
        const response = await axios.get('http://localhost:8080/api/headquarter/getAll')
        console.log('Полученные данные штабов:', response.data)
        this.headquarters = response.data
        console.log('Обработанные штабы:', this.headquarters)
      } catch (error) {
        console.error('Ошибка загрузки штабов:', error)
        alert('Не удалось загрузить список штабов: ' + (error.response?.data?.message || error.message))
      }
    },
    openCreateModal() {
      this.$refs.headquarterForm.openModal(false, {})
    },
    openEditModal(headquarter) {
      console.log('Редактирование штаба:', headquarter)
      this.$refs.headquarterForm.openModal(true, headquarter)
    },
    async saveHeadquarter({ headquarterData, isEditMode }) {
      try {
        console.log('Отправка данных штаба:', headquarterData)
        if (isEditMode) {
          await axios.put(`http://localhost:8080/api/headquarter/update/${headquarterData.id}`, headquarterData)
        } else {
          await axios.post('http://localhost:8080/api/headquarter/create', headquarterData)
        }
        this.$refs.headquarterForm.closeModal()
        await this.fetchAllData()
      } catch (error) {
        console.error('Ошибка сохранения штаба:', error)
        alert('Не удалось сохранить штаб: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteHeadquarter(headquarter) {
      if (confirm(`Вы уверены, что хотите удалить штаб "${headquarter.name}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/headquarter/delete/${headquarter.id}`)
          await this.fetchAllData()
        } catch (error) {
          console.error('Ошибка удаления штаба:', error)
          alert('Не удалось удалить штаб: ' + (error.response?.data?.message || error.message))
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