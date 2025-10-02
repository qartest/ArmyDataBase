<template>
  <div class="container mt-4">
    <h1 class="mb-4">Тип автоматического оружия</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="refreshList">Обновить</button>
      <button class="btn btn-success" @click="openCreateModal">Создать</button>
    </div>
    <table class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Модель</th>
          <th>Калибр (мм)</th>
          <th>Скорострельность (выстр./мин)</th>
          <th>Емкость магазина (патр.)</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="automaticWeaponTypes.length === 0">
          <td colspan="5" class="text-center">Нет данных</td>
        </tr>
        <tr v-for="type in automaticWeaponTypes" :key="type.id">
          <td>{{ type.model }}</td>
          <td>{{ type.caliber }}</td>
          <td>{{ type.fireRate }}</td>
          <td>{{ type.magazineCapacity }}</td>
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
            <h5 class="modal-title" id="typeModalLabel">{{ isEditMode ? 'Редактировать тип автоматического оружия' : 'Создать тип автоматического оружия' }}</h5>
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
                <input type="number" class="form-control" id="caliber" v-model="currentType.caliber" min="0.1" step="0.1" required>
              </div>
              <div class="mb-3">
                <label for="fireRate" class="form-label">Скорострельность (выстр./мин)</label>
                <input type="number" class="form-control" id="fireRate" v-model="currentType.fireRate" min="1" required>
              </div>
              <div class="mb-3">
                <label for="magazineCapacity" class="form-label">Емкость магазина (патр.)</label>
                <input type="number" class="form-control" id="magazineCapacity" v-model="currentType.magazineCapacity" min="1" required>
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
  name: 'AutomaticWeaponTypeList',
  data() {
    return {
      automaticWeaponTypes: [],
      currentType: {
        id: null,
        model: '',
        caliber: null,
        fireRate: null,
        magazineCapacity: null,
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
        const response = await axios.get('http://localhost:8080/api/equipment/automaticweaponType/getAll')
        console.log('Полученные данные типов автоматического оружия:', response.data)
        this.automaticWeaponTypes = response.data.sort((a, b) => a.id - b.id)
        console.log('Обработанные типы автоматического оружия:', this.automaticWeaponTypes)
      } catch (error) {
        console.error('Ошибка загрузки типов автоматического оружия:', error)
        alert('Не удалось загрузить список типов автоматического оружия: ' + (error.response?.data?.message || error.message))
      }
    },
    refreshList() {
      this.fetchTypes()
    },
    openCreateModal() {
      this.isEditMode = false
      this.currentType = { id: null, model: '', caliber: null, fireRate: null, magazineCapacity: null }
      this.modal.show()
    },
    openEditModal(type) {
      this.isEditMode = true
      this.currentType = { ...type }
      this.modal.show()
    },
    async saveType() {
      if (!this.currentType.model || !this.currentType.caliber || !this.currentType.fireRate || !this.currentType.magazineCapacity) {
        alert('Пожалуйста, заполните все обязательные поля')
        return
      }
      if (this.currentType.caliber <= 0 || this.currentType.fireRate < 1 || this.currentType.magazineCapacity < 1) {
        alert('Числовые поля должны быть больше 0')
        return
      }
      try {
        const payload = {
          model: this.currentType.model,
          caliber: parseFloat(this.currentType.caliber),
          fireRate: parseInt(this.currentType.fireRate),
          magazineCapacity: parseInt(this.currentType.magazineCapacity),
        }
        console.log('Отправка данных:', payload)
        if (this.isEditMode) {
          await axios.put(`http://localhost:8080/api/equipment/automaticweaponType/update/${this.currentType.id}`, payload)
        } else {
          await axios.post('http://localhost:8080/api/equipment/automaticweaponType/create', payload)
        }
        this.modal.hide()
        this.fetchTypes()
      } catch (error) {
        console.error('Ошибка сохранения типа автоматического оружия:', error)
        alert('Не удалось сохранить тип автоматического оружия: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteType(type) {
      if (confirm(`Вы уверены, что хотите удалить тип автоматического оружия "${type.model}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/equipment/automaticweaponType/delete/${type.id}`)
          this.fetchTypes()
        } catch (error) {
          console.error('Ошибка удаления типа автоматического оружия:', error)
          alert('Не удалось удалить тип автоматического оружия: ' + (error.response?.data?.message || error.message))
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