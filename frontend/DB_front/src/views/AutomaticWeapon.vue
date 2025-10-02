<template>
  <div class="container mt-4">
    <h1 class="mb-4">Автоматическое оружие</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="fetchWeapons">Обновить</button>
      <button class="btn btn-success" @click="openCreateModal">Добавить оружие</button>
    </div>
    <table class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Серийный номер</th>
          <th>Состояние приклада</th>
          <th>Тип</th>
          <th>Военная часть</th>
          <th>Год производства</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="weapons.length === 0">
          <td colspan="6" class="text-center">Нет данных</td>
        </tr>
        <tr v-for="item in weapons" :key="item.id">
          <td>{{ item.serialNumber }}</td>
          <td>{{ translateCondition(item.stockCondition) }}</td>
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
    <div class="modal fade" id="weaponModal" tabindex="-1" aria-labelledby="weaponModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="weaponModalLabel">{{ isEditMode ? 'Редактировать оружие' : 'Создать оружие' }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveItem">
              <div class="mb-3">
                <label for="serialNumber" class="form-label">Серийный номер</label>
                <input type="text" class="form-control" id="serialNumber" v-model="currentItem.serialNumber" required>
              </div>
              <div class="mb-3">
                <label for="stockCondition" class="form-label">Состояние приклада</label>
                <select class="form-select" id="stockCondition" v-model="currentItem.stockCondition" required>
                  <option value="" disabled>Выберите состояние</option>
                  <option v-for="condition in conditions" :key="condition.value" :value="condition.value">
                    {{ condition.label }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label for="typeId" class="form-label">Тип оружия</label>
                <select class="form-select" id="typeId" v-model="currentItem.typeId" required>
                  <option value="" disabled>Выберите тип</option>
                  <option v-for="type in weaponTypes" :key="type.id" :value="type.id">
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
  name: 'AutomaticWeapon',
  data() {
    return {
      weapons: [],
      weaponTypes: [],
      militaryFormations: [],
      currentItem: {
        id: null,
        serialNumber: '',
        stockCondition: '',
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
    this.fetchWeapons()
    this.fetchWeaponTypes()
    this.fetchMilitaryFormations()
    this.modal = new Modal(document.getElementById('weaponModal'))
  },
  methods: {
    async fetchWeapons() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/automaticweapon/getAll')
        console.log('Полученные данные оружия:', response.data)
        this.weapons = response.data.sort((a, b) => a.id - b.id)
        console.log('Обработанное оружие:', this.weapons)
      } catch (error) {
        console.error('Ошибка загрузки оружия:', error)
        alert('Не удалось загрузить список оружия: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchWeaponTypes() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/automaticweaponType/getAll')
        this.weaponTypes = response.data
        console.log('Полученные типы оружия:', this.weaponTypes)
      } catch (error) {
        console.error('Ошибка загрузки типов оружия:', error)
        alert('Не удалось загрузить типы оружия')
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
        stockCondition: '',
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
        stockCondition: item.stockCondition,
        typeId: item.typeId,
        formationId: item.formationId,
        yearOfManufacture: item.yearOfManufacture ? item.yearOfManufacture.split('T')[0] : '',
      }
      this.modal.show()
    },
    async saveItem() {
      if (!this.currentItem.serialNumber || !this.currentItem.stockCondition || !this.currentItem.typeId || !this.currentItem.formationId || !this.currentItem.yearOfManufacture) {
        alert('Пожалуйста, заполните все обязательные поля')
        return
      }
      try {
        const payload = {
          serialNumber: this.currentItem.serialNumber,
          stockCondition: this.currentItem.stockCondition,
          typeId: parseInt(this.currentItem.typeId),
          formationId: parseInt(this.currentItem.formationId),
          yearOfManufacture: this.currentItem.yearOfManufacture,
          category: 'AUTOMATIC_WEAPON',
        }
        console.log('Отправка данных:', JSON.stringify(payload, null, 2))
        if (this.isEditMode) {
          await axios.put(`http://localhost:8080/api/equipment/automaticweapon/update/${this.currentItem.id}`, payload)
        } else {
          await axios.post('http://localhost:8080/api/equipment/automaticweapon/create', payload)
        }
        this.modal.hide()
        this.fetchWeapons()
      } catch (error) {
        console.error('Ошибка сохранения оружия:', error)
        alert('Не удалось сохранить оружие: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteItem(item) {
      if (confirm(`Вы уверены, что хотите удалить оружие с серийным номером ${item.serialNumber}?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/equipment/automaticweapon/delete/${item.id}`)
          this.fetchWeapons()
        } catch (error) {
          console.error('Ошибка удаления оружия:', error)
          alert('Не удалось удалить оружие: ' + (error.response?.data?.message || error.message))
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
      const type = this.weaponTypes.find(t => t.id === typeId)
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