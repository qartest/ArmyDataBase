<template>
  <div class="container mt-4">
    <h1 class="mb-4">Артиллерия</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="fetchArtillery">Обновить</button>
      <button class="btn btn-success" @click="openCreateModal">Добавить элемент</button>
    </div>
    <table class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Серийный номер</th>
          <th>Количество выстрелов</th>
          <th>Состояние ствола</th>
          <th>Тип</th>
          <th>Военная часть</th>
          <th>Год производства</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="artillery.length === 0">
          <td colspan="7" class="text-center">Нет данных</td>
        </tr>
        <tr v-for="item in artillery" :key="item.id">
          <td>{{ item.serialNumber }}</td>
          <td>{{ item.shots }}</td>
          <td>{{ translateCondition(item.barrelStatus) }}</td>
          <td>{{ getTypeName(item.typeId) }}</td>
          <td>{{ item.formationName }}</td>
          <td>{{ formatDate(item.yearOfManufacture) }}</td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="openEditModal(item)">Отредактировать</button>
            <button class="btn btn-danger btn-sm" @click="deleteItem(item)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Модальное окно для создания/редактирования -->
    <div class="modal fade" id="artilleryModal" tabindex="-1" aria-labelledby="artilleryModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="artilleryModalLabel">{{ isEditMode ? 'Редактировать артиллерию' : 'Создать артиллерию' }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveItem">
              <div class="mb-3">
                <label for="serialNumber" class="form-label">Серийный номер</label>
                <input type="text" class="form-control" id="serialNumber" v-model="currentItem.serialNumber" required>
              </div>
              <div class="mb-3">
                <label for="shots" class="form-label">Количество выстрелов</label>
                <input type="number" class="form-control" id="shots" v-model="currentItem.shots" min="0" required>
              </div>
              <div class="mb-3">
                <label for="barrelStatus" class="form-label">Состояние ствола</label>
                <select class="form-select" id="barrelStatus" v-model="currentItem.barrelStatus" required>
                  <option value="" disabled>Выберите состояние</option>
                  <option v-for="condition in conditions" :key="condition.value" :value="condition.value">
                    {{ condition.label }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label for="typeId" class="form-label">Тип артиллерии</label>
                <select class="form-select" id="typeId" v-model="currentItem.typeId" required>
                  <option value="" disabled>Выберите тип</option>
                  <option v-for="type in artilleryTypes" :key="type.id" :value="type.id">
                    {{ type.model }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label for="formationId" class="form-label">Военная часть</label>
                <select class="form-select" id="formationId" v-model="currentItem.formationId" required>
                  <option value="" disabled>Выберите часть</option>
                  <option v-for="formation in militaryFormations" :key="formation.id" :value="formation.id">
                    {{ formation.name }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label for="yearOfManufacture" class="form-label">Год производства</label>
                <input type="date" class="form-control" id="yearOfManufacture" v-model="currentItem.yearOfManufacture" required>
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
  name: 'ArtilleryList',
  data() {
    return {
      artillery: [],
      artilleryTypes: [],
      militaryFormations: [],
      currentItem: {
        id: null,
        serialNumber: '',
        shots: null,
        barrelStatus: '',
        typeId: '',
        formationId: '',
        yearOfManufacture: '',
      },
      isEditMode: false,
      modal: null,
    conditions: [
        { value: 'GOOD', label: 'Хорошее' },
        { value: 'WORN', label: 'Изношенное' },
        { value: 'BROKEN', label: 'Сломанное' },
    ],
    }
  },
  mounted() {
    this.fetchArtillery()
    this.fetchArtilleryTypes()
    this.fetchMilitaryFormations()
    this.modal = new Modal(document.getElementById('artilleryModal'))
  },
  methods: {
    async fetchArtillery() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/artillery/getAll')
        console.log('Полученные данные артиллерии:', response.data)
        this.artillery = response.data.sort((a, b) => a.id - b.id)
        console.log('Обработанные артиллерийские системы:', this.artillery)
      } catch (error) {
        console.error('Ошибка загрузки артиллерии:', error)
        alert('Не удалось загрузить список артиллерии: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchArtilleryTypes() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/artilleryType/getAll')
        this.artilleryTypes = response.data
        console.log('Полученные типы артиллерии:', this.artilleryTypes)
      } catch (error) {
        console.error('Ошибка загрузки типов артиллерии:', error)
        alert('Не удалось загрузить типы артиллерии')
      }
    },
    async fetchMilitaryFormations() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/militaryFormation/getAll/Simple')
        this.militaryFormations = response.data
        console.log('Полученные военные формирования:', this.militaryFormations)
      } catch (error) {
        console.error('Ошибка загрузки военных формирований:', error)
        alert('Не удалось загрузить военные формирования')
      }
    },
    openCreateModal() {
      this.isEditMode = false
      this.currentItem = {
        id: null,
        serialNumber: '',
        shots: null,
        barrelStatus: '',
        typeId: '',
        formationId: '',
        yearOfManufacture: '',
      }
      this.modal.show()
    },
    openEditModal(item) {
      this.isEditMode = true
      this.currentItem = {
        id: item.id,
        serialNumber: item.serialNumber,
        shots: item.shots,
        barrelStatus: item.barrelStatus,
        typeId: item.typeId,
        formationId: item.formationId,
        yearOfManufacture: item.yearOfManufacture ? item.yearOfManufacture.split('T')[0] : '',
      }
      this.modal.show()
    },
    async saveItem() {
      if (!this.currentItem.serialNumber || this.currentItem.shots === null || !this.currentItem.barrelStatus || !this.currentItem.typeId || !this.currentItem.formationId || !this.currentItem.yearOfManufacture) {
        alert('Пожалуйста, заполните все обязательные поля')
        return
      }
      if (this.currentItem.shots < 0) {
        alert('Количество выстрелов не может быть отрицательным')
        return
      }
      try {
        const payload = {
          serialNumber: this.currentItem.serialNumber,
          shots: parseInt(this.currentItem.shots),
          barrelStatus: this.currentItem.barrelStatus,
          typeId: parseInt(this.currentItem.typeId),
          formationId: parseInt(this.currentItem.formationId),
          yearOfManufacture: this.currentItem.yearOfManufacture,
          category: 'ARTILLERY',
        }
        console.log('Отправка данных:', payload)
        if (this.isEditMode) {
          await axios.put(`http://localhost:8080/api/equipment/artillery/update/${this.currentItem.id}`, payload)
        } else {
          await axios.post('http://localhost:8080/api/equipment/artillery/create', payload)
        }
        this.modal.hide()
        this.fetchArtillery()
      } catch (error) {
        console.error('Ошибка сохранения артиллерии:', error)
        alert('Не удалось сохранить артиллерию: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteItem(item) {
      if (confirm(`Вы уверены, что хотите удалить артиллерию с серийным номером ${item.serialNumber}?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/equipment/artillery/delete/${item.id}`)
          this.fetchArtillery()
        } catch (error) {
          console.error('Ошибка удаления артиллерии:', error)
          alert('Не удалось удалить артиллерию: ' + (error.response?.data?.message || error.message))
        }
      }
    },
    translateCondition(condition) {
        const map = {
            GOOD: 'Хорошее',
            WORN: 'Изношенное',
            BROKEN: 'Сломанное',
        }
        return map[condition] || condition
    },
    getTypeName(typeId) {
      const type = this.artilleryTypes.find(t => t.id === typeId)
      return type ? type.model : 'Неизвестно'
    },
    formatDate(date) {
      return date ? date.split('T')[0] : ''
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