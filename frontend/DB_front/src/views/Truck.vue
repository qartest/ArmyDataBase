<template>
  <div class="container mt-4">
    <h1 class="mb-4">Грузовики</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="fetchTrucks">Обновить</button>
      <button class="btn btn-success" @click="openCreateModal">Добавить грузовик</button>
    </div>
    <table class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Серийный номер</th>
          <th>Пробег (км)</th>
          <th>Тип</th>
          <th>Военная часть</th>
          <th>Год производства</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="trucks.length === 0">
          <td colspan="6" class="text-center">Нет данных</td>
        </tr>
        <tr v-for="item in trucks" :key="item.id">
          <td>{{ item.serialNumber }}</td>
          <td>{{ item.mileage }}</td>
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
    <div class="modal fade" id="truckModal" tabindex="-1" aria-labelledby="truckModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="truckModalLabel">{{ isEditMode ? 'Редактировать грузовик' : 'Создать грузовик' }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveItem">
              <div class="mb-3">
                <label for="serialNumber" class="form-label">Серийный номер</label>
                <input type="text" class="form-control" id="serialNumber" v-model="currentItem.serialNumber" required>
              </div>
              <div class="mb-3">
                <label for="mileage" class="form-label">Пробег (км)</label>
                <input type="number" class="form-control" id="mileage" v-model="currentItem.mileage" min="0" required>
              </div>
              <div class="mb-3">
                <label for="typeId" class="form-label">Тип грузовика</label>
                <select class="form-select" id="typeId" v-model="currentItem.typeId" required>
                  <option value="" disabled>Выберите тип</option>
                  <option v-for="type in truckTypes" :key="type.id" :value="type.id">
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
  name: 'Truck',
  data() {
    return {
      trucks: [],
      truckTypes: [],
      militaryFormations: [],
      currentItem: {
        id: null,
        serialNumber: '',
        mileage: null,
        typeId: '',
        formationId: '',
        yearOfManufacture: '',
      },
      isEditMode: false,
      modal: null,
    }
  },
  mounted() {
    this.fetchTrucks()
    this.fetchTruckTypes()
    this.fetchMilitaryFormations()
    this.modal = new Modal(document.getElementById('truckModal'))
  },
  methods: {
    async fetchTrucks() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/truck/getAll')
        console.log('Полученные данные грузовиков:', response.data)
        this.trucks = response.data.sort((a, b) => a.id - b.id)
        console.log('Обработанные грузовики:', this.trucks)
      } catch (error) {
        console.error('Ошибка загрузки грузовиков:', error)
        alert('Не удалось загрузить список грузовиков: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchTruckTypes() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/truckType/getAll')
        this.truckTypes = response.data
        console.log('Полученные типы грузовиков:', this.truckTypes)
      } catch (error) {
        console.error('Ошибка загрузки типов грузовиков:', error)
        alert('Не удалось загрузить типы грузовиков')
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
        mileage: null,
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
        mileage: item.mileage,
        typeId: item.typeId,
        formationId: item.formationId,
        yearOfManufacture: item.yearOfManufacture ? item.yearOfManufacture.split('T')[0] : '',
      }
      this.modal.show()
    },
    async saveItem() {
      if (!this.currentItem.serialNumber || this.currentItem.mileage === null || !this.currentItem.typeId || !this.currentItem.formationId || !this.currentItem.yearOfManufacture) {
        alert('Пожалуйста, заполните все обязательные поля')
        return
      }
      if (this.currentItem.mileage < 0) {
        alert('Пробег не может быть отрицательным')
        return
      }
      try {
        const payload = {
          serialNumber: this.currentItem.serialNumber,
          mileage: parseInt(this.currentItem.mileage),
          typeId: parseInt(this.currentItem.typeId),
          formationId: parseInt(this.currentItem.formationId),
          yearOfManufacture: this.currentItem.yearOfManufacture,
          category: 'TRUCK',
        }
        console.log('Отправка данных:', JSON.stringify(payload, null, 2))
        if (this.isEditMode) {
          await axios.put(`http://localhost:8080/api/equipment/truck/update/${this.currentItem.id}`, payload)
        } else {
          await axios.post('http://localhost:8080/api/equipment/truck/create', payload)
        }
        this.modal.hide()
        this.fetchTrucks()
      } catch (error) {
        console.error('Ошибка сохранения грузовика:', error)
        alert('Не удалось сохранить грузовик: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteItem(item) {
      if (confirm(`Вы уверены, что хотите удалить грузовик с серийным номером ${item.serialNumber}?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/equipment/truck/delete/${item.id}`)
          this.fetchTrucks()
        } catch (error) {
          console.error('Ошибка удаления грузовика:', error)
          alert('Не удалось удалить грузовик: ' + (error.response?.data?.message || error.message))
        }
      }
    },
    getTypeName(typeId) {
      const type = this.truckTypes.find(t => t.id === typeId)
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