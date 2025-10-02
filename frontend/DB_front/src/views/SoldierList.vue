<template>
  <div class="container mt-4">
    <h1 class="mb-4">Солдаты</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="fetchSoldiers">Обновить/Загрузить список</button>
      <button class="btn btn-primary me-2" @click="fetchRanks">Подгрузить звания</button>
      <button class="btn btn-success" @click="openCreateModal">Создать</button>
    </div>
    <table class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Фамилия</th>
          <th>Имя</th>
          <th>Отчество</th>
          <th>Дата рождения</th>
          <th>Специальности</th>
          <th>Отряд</th>
          <th>Подкомандная часть</th>
          <th>Звание</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="soldiers.length === 0">
          <td colspan="9" class="text-nowrap text-center">Нет данных</td>
        </tr>
        <tr v-for="soldier in soldiers" :key="soldier.id">
          <td>{{ soldier.secondName }}</td>
          <td>{{ soldier.firstName }}</td>
          <td>{{ soldier.fatherName || '-' }}</td>
          <td>{{ formatDate(soldier.birthDay) }}</td>
          <td>{{ soldier.specialtyNames ? soldier.specialtyNames.join(', ') : '-' }}</td>
          <td>{{ soldier.squadName || '-' }}</td>
          <td>{{ soldier.commandedUnitName || '-' }}</td>
          <td>{{ soldier.rankName || '-' }}</td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="openEditModal(soldier)">Редактировать</button>
            <button class="btn btn-danger btn-sm me-2" @click="deleteSoldier(soldier)">Удалить</button>
            <button class="btn btn-info btn-sm" @click="openHistory(soldier)">История</button>
          </td>
        </tr>
      </tbody>
    </table>
    <soldier-form ref="soldierForm" :ranks="ranks" @save="saveSoldier" />
    <soldier-history ref="soldierHistory" />
  </div>
</template>

<script>
import axios from 'axios'
import SoldierForm from './SoldierForm.vue'
import SoldierHistory from './SoldierHistory.vue'

export default {
  name: 'SoldierList',
  components: { SoldierForm, SoldierHistory },
  data() {
    return {
      soldiers: [],
      ranks: [],
    }
  },
  mounted() {
    this.fetchSoldiers()
    this.fetchRanks()
  },
  methods: {
    async fetchSoldiers() {
      try {
        const response = await axios.get('http://localhost:8080/api/soldier/getAll')
        this.soldiers = response.data.sort((a, b) => {
          const compareSecondName = a.secondName.localeCompare(b.secondName)
          if (compareSecondName !== 0) return compareSecondName
          const compareFirstName = a.firstName.localeCompare(b.firstName)
          if (compareFirstName !== 0) return compareFirstName
          return (a.fatherName || '').localeCompare(b.fatherName || '')
        })
        console.log('Полученные солдаты:', this.soldiers)
      } catch (error) {
        console.error('Ошибка загрузки солдат:', error)
        alert('Не удалось загрузить список солдат: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchRanks() {
      try {
        const response = await axios.get('http://localhost:8080/api/rank/getAll')
        this.ranks = response.data.sort((a, b) => a.hierarchyLevel - b.hierarchyLevel)
        console.log('Загруженные звания:', this.ranks)
        if (!this.ranks.length) {
          alert('Список званий пуст. Проверьте данные на сервере.')
        }
      } catch (error) {
        console.error('Ошибка загрузки званий:', error)
        alert('Не удалось загрузить звания: ' + (error.response?.data?.message || error.message))
      }
    },
    openCreateModal() {
      this.$refs.soldierForm.openModal(false, {})
    },
    openEditModal(soldier) {
      this.$refs.soldierForm.openModal(true, soldier)
    },
    openHistory(soldier) {
      this.$refs.soldierHistory.open(soldier)
    },
    async saveSoldier({ soldierData, isEditMode }) {
      try {
        console.log('Сохранение солдата:', soldierData)
        if (isEditMode) {
          await axios.put(`http://localhost:8080/api/soldier/update/${soldierData.id}`, soldierData)
        } else {
          await axios.post('http://localhost:8080/api/soldier/create', soldierData)
        }
        this.$refs.soldierForm.closeModal()
        this.fetchSoldiers()
      } catch (error) {
        console.error('Ошибка сохранения солдата:', error)
        alert('Не удалось сохранить солдата: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteSoldier(soldier) {
      if (confirm(`Вы уверены, что хотите удалить солдата "${soldier.secondName} ${soldier.firstName}${soldier.fatherName ? ' ' + soldier.fatherName : ''}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/soldier/delete/${soldier.id}`)
          this.fetchSoldiers()
        } catch (error) {
          console.error('Ошибка удаления солдата:', error)
          alert('Не удалось удалить солдата: ' + (error.response?.data?.message || error.message))
        }
      }
    },
    formatDate(date) {
      return date ? new Date(date).toLocaleDateString('ru-RU') : '-'
    },
  },
}
</script>

<style scoped>
.table th, .table td {
  vertical-align: middle;
}
</style>