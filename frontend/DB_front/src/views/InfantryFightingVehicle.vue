<template>
  <div class="container mt-4">
    <h1 class="mb-4">Боевая машина пехоты</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="fetchVehicles">Обновить</button>
      <button class="btn btn-success" @click="openCreateModal">Добавить элемент</button>
    </div>
    <table class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Серийный номер</th>
          <th>Состояние</th>
          <th>Тип</th>
          <th>Военная часть</th>
          <th>Год производства</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="vehicles.length === 0">
          <td colspan="6" class="text-center">Нет данных</td>
        </tr>
        <tr v-for="item in vehicles" :key="item.id">
          <td>{{ item.serialNumber }}</td>
          <td>{{ translateCondition(item.vehicleCondition) }}</td>
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
    <div class="modal fade" id="vehicleModal" tabindex="-1" aria-labelledby="vehicleModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="vehicleModalLabel">{{ isEditMode ? 'Редактировать БМП' : 'Создать БМП' }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveItem">
              <div class="mb-3">
                <label for="serialNumber" class="form-label">Серийный номер</label>
                <input type="text" class="form-control" id="serialNumber" v-model="currentItem.serialNumber" required>
              </div>
              <div class="mb-3">
                <label for="vehicleCondition" class="form-label">Состояние машины</label>
                <select class="form-select" id="vehicleCondition" v-model="currentItem.vehicleCondition" required>
                  <option value="" disabled>Выберите состояние</option>
                  <option v-for="condition in conditions" :key="condition.value" :value="condition.value">
                    {{ condition.label }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label for="typeId" class="form-label">Тип БМП</label>
                <select class="form-select" id="typeId" v-model="currentItem.typeId" required>
                  <option value="" disabled>Выберите тип</option>
                  <option v-for="type in vehicleTypes" :key="type.id" :value="type.id">
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
  name: 'InfantryFightingVehicle',
  data() {
    return {
      vehicles: [],
      vehicleTypes: [],
      militaryFormations: [],
      currentItem: {
        id: null,
        serialNumber: '',
        vehicleCondition: '',
        typeId: '',
        formationId: '',
        yearOfManufacture: '',
      },
      isEditMode: false,
      modal: null,
      conditions: [
        { value: 'GOOD', label: 'Хорошее состояние' },
        { value: 'WORN', label: 'Изношенное состояние' },
        { value: 'BROKEN', label: 'Сломанное состояние' },
      ],
    }
  },
  mounted() {
    this.fetchVehicles()
    this.fetchVehicleTypes()
    this.fetchMilitaryFormations()
    this.modal = new Modal(document.getElementById('vehicleModal'))
  },
  methods: {
    async fetchVehicles() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/IFV/getAll')
        console.log('Полученные данные БМП:', response.data)
        this.vehicles = response.data.sort((a, b) => a.id - b.id)
        console.log('Обработанные БМП:', this.vehicles)
      } catch (error) {
        console.error('Ошибка загрузки БМП:', error)
        alert('Не удалось загрузить список БМП: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchVehicleTypes() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/IFVType/getAll')
        this.vehicleTypes = response.data
        console.log('Полученные типы БМП:', this.vehicleTypes)
      } catch (error) {
        console.error('Ошибка загрузки типов БМП:', error)
        alert('Не удалось загрузить типы БМП')
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
        vehicleCondition: '',
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
        vehicleCondition: item.vehicleCondition,
        typeId: item.typeId,
        formationId: item.formationId,
        yearOfManufacture: item.yearOfManufacture ? item.yearOfManufacture.split('T')[0] : '',
      }
      this.modal.show()
    },
    async saveItem() {
      if (!this.currentItem.serialNumber || !this.currentItem.vehicleCondition || !this.currentItem.typeId || !this.currentItem.formationId || !this.currentItem.yearOfManufacture) {
        alert('Пожалуйста, заполните все обязательные поля')
        return
      }
      try {
        const payload = {
          serialNumber: this.currentItem.serialNumber,
          vehicleCondition: this.currentItem.vehicleCondition,
          typeId: parseInt(this.currentItem.typeId),
          formationId: parseInt(this.currentItem.formationId),
          yearOfManufacture: this.currentItem.yearOfManufacture,
          category: 'INFANTRY_FIGHTING_VEHICLE',
        }
        console.log('Отправка данных:', JSON.stringify(payload, null, 2))
        if (this.isEditMode) {
          await axios.put(`http://localhost:8080/api/equipment/IFV/update/${this.currentItem.id}`, payload)
        } else {
          await axios.post('http://localhost:8080/api/equipment/IFV/create', payload)
        }
        this.modal.hide()
        this.fetchVehicles()
      } catch (error) {
        console.error('Ошибка сохранения БМП:', error)
        alert('Не удалось сохранить БМП: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteItem(item) {
      if (confirm(`Вы уверены, что хотите удалить БМП с серийным номером ${item.serialNumber}?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/equipment/IFV/delete/${item.id}`)
          this.fetchVehicles()
        } catch (error) {
          console.error('Ошибка удаления БМП:', error)
          alert('Не удалось удалить БМП: ' + (error.response?.data?.message || error.message))
        }
      }
    },
    translateCondition(condition) {
      const map = {
        GOOD: 'Хорошее состояние',
        WORN: 'Изношенное состояние',
        BROKEN: 'Сломанное состояние',
      }
      return map[condition] || condition
    },
    getTypeName(typeId) {
      const type = this.vehicleTypes.find(t => t.id === typeId)
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