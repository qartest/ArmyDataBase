<template>
  <div class="container mt-4">
    <h1 class="mb-4">Тип артиллерии</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="refreshList">Обновить</button>
      <button class="btn btn-success" @click="openCreateModal">Создать</button>
    </div>
    <table class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Модель</th>
          <th>Калибр (мм)</th>
          <th>Дальность (км)</th>
          <th>Скорострельность (выстр./мин)</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="artilleryTypes.length === 0">
          <td colspan="5" class="text-center">Нет данных</td>
        </tr>
        <tr v-for="type in artilleryTypes" :key="type.id">
          <td>{{ type.model }}</td>
          <td>{{ type.caliber }}</td>
          <td>{{ type.range }}</td>
          <td>{{ type.rateOfFire }}</td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="openEditModal(type)">Изменить</button>
            <button class="btn btn-danger btn-sm" @click="deleteType(type)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Модальное окно для создания/редактирования -->
    <div class="modal fade" id="typeModal" tabindex="-1" aria-labelledby="typeModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="typeModalLabel">{{ isEditMode ? 'Редактировать тип артиллерии' : 'Создать тип артиллерии' }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveType">
              <div class="mb-3">
                <label for="model" class="form-label">Модель</label>
                <input type="text" class="form-control" id="model" v-model="currentType.model" required>
              </div>
              <div class="mb-3">
                <label for="caliber" class="form-label">Калибр (мм)</label>
                <input type="number" class="form-control" id="caliber" v-model="currentType.caliber" min="1" required>
              </div>
              <div class="mb-3">
                <label for="range" class="form-label">Дальность (км)</label>
                <input type="number" class="form-control" id="range" v-model="currentType.range" min="1" required>
              </div>
              <div class="mb-3">
                <label for="rateOfFire" class="form-label">Скорострельность (выстр./мин)</label>
                <input type="number" class="form-control" id="rateOfFire" v-model="currentType.rateOfFire" min="1" required>
              </div>
              <button type="submit" class="btn btn-primary">{{ isEditMode ? 'Сохранить' : 'Создать' }}</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { Modal } from 'bootstrap'

export default {
  name: 'ArtilleryTypeList',
  data() {
    return {
      artilleryTypes: [],
      currentType: {
        id: null,
        model: '',
        caliber: null,
        range: null,
        rateOfFire: null,
      },
      isEditMode: false,
      modal: null,
    }
  },
  mounted() {
    this.fetchTypes()
    this.modal = new Modal(document.getElementById('typeModal'))
  },
  methods: {
    async fetchTypes() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/artilleryType/getAll')
        console.log('Полученные данные типов артиллерии:', response.data)
        this.artilleryTypes = response.data.sort((a, b) => a.id - b.id)
        console.log('Обработанные типы артиллерии:', this.artilleryTypes)
      } catch (error) {
        console.error('Ошибка загрузки типов артиллерии:', error)
        alert('Не удалось загрузить список типов артиллерии: ' + (error.response?.data?.message || error.message))
      }
    },
    refreshList() {
      this.fetchTypes()
    },
    openCreateModal() {
      this.isEditMode = false
      this.currentType = { id: null, model: '', caliber: null, range: null, rateOfFire: null }
      this.modal.show()
    },
    openEditModal(type) {
      this.isEditMode = true
      this.currentType = { ...type }
      this.modal.show()
    },
    async saveType() {
      if (!this.currentType.model || !this.currentType.caliber || !this.currentType.range || !this.currentType.rateOfFire) {
        alert('Пожалуйста, заполните все обязательные поля')
        return
      }
      if (this.currentType.caliber < 1 || this.currentType.range < 1 || this.currentType.rateOfFire < 1) {
        alert('Числовые поля должны быть больше 0')
        return
      }
      try {
        const payload = {
          model: this.currentType.model,
          caliber: parseInt(this.currentType.caliber),
          range: parseInt(this.currentType.range),
          rateOfFire: parseInt(this.currentType.rateOfFire),
        }
        console.log('Отправка данных:', payload)
        if (this.isEditMode) {
          await axios.put(`http://localhost:8080/api/equipment/artilleryType/update/${this.currentType.id}`, payload)
        } else {
          await axios.post('http://localhost:8080/api/equipment/artilleryType/create', payload)
        }
        this.modal.hide()
        this.fetchTypes()
      } catch (error) {
        console.error('Ошибка сохранения типа артиллерии:', error)
        alert('Не удалось сохранить тип артиллерии: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteType(type) {
      if (confirm(`Вы уверены, что хотите удалить тип артиллерии "${type.model}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/equipment/artilleryType/delete/${type.id}`)
          this.fetchTypes()
        } catch (error) {
          console.error('Ошибка удаления типа артиллерии:', error)
          alert('Не удалось удалить тип артиллерии: ' + (error.response?.data?.message || error.message))
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
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
input[type="number"] {
  -moz-appearance: textfield;
}
</style>