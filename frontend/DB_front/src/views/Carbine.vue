<template>
  <div class="container mt-4">
    <h1 class="mb-4">Карабины</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="fetchCarbines">Обновить</button>
      <button class="btn btn-success" @click="openCreateModal">Добавить карабин</button>
    </div>
    <table class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Серийный номер</th>
          <th>Состояние прицела</th>
          <th>Уничтожено целей</th>
          <th>Тип</th>
          <th>Военная часть</th>
          <th>Год производства</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="carbines.length === 0">
          <td colspan="7" class="text-center">Нет данных</td>
        </tr>
        <tr v-for="item in carbines" :key="item.id">
          <td>{{ item.serialNumber }}</td>
          <td>{{ translateCondition(item.sightCondition) }}</td>
          <td>{{ item.kills }}</td>
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
    <div class="modal fade" id="carbineModal" tabindex="-1" aria-labelledby="carbineModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="carbineModalLabel">{{ isEditMode ? 'Редактировать карабин' : 'Создать карабин' }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveItem">
              <div class="mb-3">
                <label for="serialNumber" class="form-label">Серийный номер</label>
                <input type="text" class="form-control" id="serialNumber" v-model="currentItem.serialNumber" required>
              </div>
              <div class="mb-3">
                <label for="sightCondition" class="form-label">Состояние прицела</label>
                <select class="form-select" id="sightCondition" v-model="currentItem.sightCondition" required>
                  <option value="" disabled>Выберите состояние</option>
                  <option v-for="condition in conditions" :key="condition.value" :value="condition.value">
                    {{ condition.label }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label for="kills" class="form-label">Уничтожено целей</label>
                <input type="number" class="form-control" id="kills" v-model="currentItem.kills" min="0" required>
              </div>
              <div class="mb-3">
                <label for="typeId" class="form-label">Тип карабина</label>
                <select class="form-select" id="typeId" v-model="currentItem.typeId" required>
                  <option value="" disabled>Выберите тип</option>
                  <option v-for="type in carbineTypes" :key="type.id" :value="type.id">
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
  name: 'Carbine',
  data() {
    return {
      carbines: [],
      carbineTypes: [],
      militaryFormations: [],
      currentItem: {
        id: null,
        serialNumber: '',
        sightCondition: '',
        kills: null,
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
    this.fetchCarbines()
    this.fetchCarbineTypes()
    this.fetchMilitaryFormations()
    this.modal = new Modal(document.getElementById('carbineModal'))
  },
  methods: {
    async fetchCarbines() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/carbinecontroller/getAll')
        console.log('Полученные данные карабинов:', response.data)
        this.carbines = response.data.sort((a, b) => a.id - b.id)
        console.log('Обработанные карабины:', this.carbines)
      } catch (error) {
        console.error('Ошибка загрузки карабинов:', error)
        alert('Не удалось загрузить список карабинов: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchCarbineTypes() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/carbineType/getAll')
        this.carbineTypes = response.data
        console.log('Полученные типы карабинов:', this.carbineTypes)
      } catch (error) {
        console.error('Ошибка загрузки типов карабинов:', error)
        alert('Не удалось загрузить типы карабинов')
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
        sightCondition: '',
        kills: null,
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
        sightCondition: item.sightCondition,
        kills: item.kills,
        typeId: item.typeId,
        formationId: item.formationId,
        yearOfManufacture: item.yearOfManufacture ? item.yearOfManufacture.split('T')[0] : '',
      }
      this.modal.show()
    },
    async saveItem() {
      if (!this.currentItem.serialNumber || !this.currentItem.sightCondition || this.currentItem.kills === null || !this.currentItem.typeId || !this.currentItem.formationId || !this.currentItem.yearOfManufacture) {
        alert('Пожалуйста, заполните все обязательные поля')
        return
      }
      if (this.currentItem.kills < 0) {
        alert('Количество уничтоженных целей не может быть отрицательным')
        return
      }
      try {
        const payload = {
          serialNumber: this.currentItem.serialNumber,
          sightCondition: this.currentItem.sightCondition,
          kills: parseInt(this.currentItem.kills),
          typeId: parseInt(this.currentItem.typeId),
          formationId: parseInt(this.currentItem.formationId),
          yearOfManufacture: this.currentItem.yearOfManufacture,
          category: 'CARBINE',
        }
        console.log('Отправка данных:', JSON.stringify(payload, null, 2))
        if (this.isEditMode) {
          await axios.put(`http://localhost:8080/api/equipment/carbinecontroller/update/${this.currentItem.id}`, payload)
        } else {
          await axios.post('http://localhost:8080/api/equipment/carbinecontroller/create', payload)
        }
        this.modal.hide()
        this.fetchCarbines()
      } catch (error) {
        console.error('Ошибка сохранения карабина:', error)
        alert('Не удалось сохранить карабин: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteItem(item) {
      if (confirm(`Вы уверены, что хотите удалить карабин с серийным номером ${item.serialNumber}?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/equipment/carbinecontroller/delete/${item.id}`)
          this.fetchCarbines()
        } catch (error) {
          console.error('Ошибка удаления карабина:', error)
          alert('Не удалось удалить карабин: ' + (error.response?.data?.message || error.message))
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
      const type = this.carbineTypes.find(t => t.id === typeId)
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