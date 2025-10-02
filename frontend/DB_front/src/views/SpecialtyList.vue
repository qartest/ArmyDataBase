<template>
  <div class="container mt-4">
    <h1 class="mb-4">Специальности</h1>
    <div class="mb-3">
      <button class="btn btn-success" @click="openCreateModal">Создать</button>
    </div>
    <div v-if="isLoading" class="text-center">Загрузка...</div>
    <table v-else class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Название</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="specialties.length === 0">
          <td colspan="2" class="text-nowrap text-center">Нет данных</td>
        </tr>
        <tr v-for="specialty in specialties" :key="specialty.id">
          <td>{{ specialty.name }}</td>
          <td>
            <button class="btn btn-danger btn-sm" @click="deleteSpecialty(specialty)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>
    <specialty-form ref="specialtyForm" @save="saveSpecialty" />
  </div>
</template>

<script>
import axios from 'axios'
import SpecialtyForm from './SpecialtyForm.vue'

export default {
  name: 'SpecialtyList',
  components: { SpecialtyForm },
  data() {
    return {
      specialties: [],
      isLoading: false,
    }
  },
  mounted() {
    this.fetchSpecialties()
  },
  methods: {
    async fetchSpecialties() {
      this.isLoading = true
      this.specialties = []
      try {
        const response = await axios.get('http://localhost:8080/api/specialty/getAll')
        console.log('Полученные данные специальностей:', response.data)
        this.specialties = response.data
        console.log('Обработанные специальности:', this.specialties)
      } catch (error) {
        console.error('Ошибка загрузки специальностей:', error)
        alert('Не удалось загрузить список специальностей: ' + (error.response?.data?.message || error.message))
      } finally {
        this.isLoading = false
      }
    },
    openCreateModal() {
      this.$refs.specialtyForm.openModal()
    },
    async saveSpecialty(specialtyData) {
      try {
        console.log('Отправка данных специальности:', specialtyData)
        await axios.post('http://localhost:8080/api/specialty/create', specialtyData)
        this.$refs.specialtyForm.closeModal()
        await this.fetchSpecialties()
      } catch (error) {
        console.error('Ошибка сохранения специальности:', error)
        alert('Не удалось сохранить специальность: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteSpecialty(specialty) {
      if (confirm(`Вы уверены, что хотите удалить специальность "${specialty.name}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/specialty/delete/${specialty.id}`)
          await this.fetchSpecialties()
        } catch (error) {
          console.error('Ошибка удаления специальности:', error)
          alert('Не удалось удалить специальность: ' + (error.response?.data?.message || error.message))
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