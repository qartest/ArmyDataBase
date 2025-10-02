<template>
  <div class="container mt-4">
    <h1 class="mb-4">Ракетное оружие</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="fetchMissileWeapons">Обновить</button>
      <button class="btn btn-success" @click="openCreateModal">Добавить элемент</button>
    </div>
    <table class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Серийный номер</th>
          <th>Состояние</th>
          <th>Тип</th>
          <th>Военная часть</th>
          <th>Год</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="missileWeapons.length === 0">
          <td colspan="6" class="text-center">Нет данных</td>
        </tr>
        <tr v-for="item in missileWeapons" :key="item.id">
          <td>{{ item.serialNumber }}</td>
          <td>{{ translateCondition(item.launcherStatus) }}</td>
          <td>{{ getTypeName(item.typeId) }}</td>
          <td>{{ item.formationName }}</td>
          <td>{{ formatDate(item.yearOfManufacture) }}</td>
            <td><button class="btn btn-warning btn-sm me-2" @click="openEditModal(item)">Отредактировать</button>
            <button class="btn btn-danger btn-sm" @click="deleteItem(item)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Модальное окно для создания/редактирования -->
    <div class="modal fade" id="missileWeaponModal" tabindex="-1" aria-labelledby="missileWeaponModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="missileWeaponModalLabel">{{ isEditMode ? 'Редактировать ракетное оружие' : 'Создать ракетное оружие' }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveItem">
              <div class="mb-3">
                <label for="serialNumber" class="form-label">Серийный номер</label>
                <input type="text" class="form-control" id="serialNumber" v-model="currentItem.serialNumber" required>
              </div>
              <div class="mb-3">
                <label for="launcherStatus" class="form-label">Состояние пусковой установки</label>
                <select class="form-select" id="launcherStatus" v-model="currentItem.launcherStatus" required>
                  <option value="" disabled>Выберите состояние</option>
                  <option v-for="condition in conditions" :key="condition.value" :value="condition.value">
                    {{ condition.label }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label for="typeId" class="form-label">Тип ракетного оружия</label>
                <select class="form-select" id="typeId" v-model="currentItem.typeId" required>
                  <option value="" disabled>Выберите тип</option>
                  <option v-for="type in missileWeaponTypes" :key="type.id" :value="type.id">
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
  name: 'MissileWeapon',
  data() {
    return {
      missileWeapons: [],
      missileWeaponTypes: [],
      militaryFormations: [],
      currentItem: {
        id: null,
        serialNumber: '',
        launcherStatus: '',
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
    this.fetchMissileWeapons()
    this.fetchMissileWeaponTypes()
    this.fetchMilitaryFormations()
    this.modal = new Modal(document.getElementById('missileWeaponModal'))
  },
  methods: {
    async fetchMissileWeapons() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/missileweapon/getAll')
        console.log('Полученные данные ракетного оружия:', response.data)
        this.missileWeapons = response.data.sort((a, b) => a.id - b.id)
        console.log('Обработанные ракетные системы:', this.missileWeapons)
      } catch (error) {
        console.error('Ошибка загрузки ракетного оружия:', error)
        alert('Не удалось загрузить список ракетного оружия: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchMissileWeaponTypes() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/missileweaponType/getAll')
        this.missileWeaponTypes = response.data
        console.log('Полученные типы ракетного оружия:', this.missileWeaponTypes)
      } catch (error) {
        console.error('Ошибка загрузки типов ракетного оружия:', error)
        alert('Не удалось загрузить типы ракетного оружия')
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
        launcherStatus: '',
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
        launcherStatus: item.launcherStatus,
        typeId: item.typeId,
        formationId: item.formationId,
        yearOfManufacture: item.yearOfManufacture ? item.yearOfManufacture.split('T')[0] : '',
      }
      this.modal.show()
    },
    async saveItem() {
      if (!this.currentItem.serialNumber || !this.currentItem.launcherStatus || !this.currentItem.typeId || !this.currentItem.formationId || !this.currentItem.yearOfManufacture) {
        alert('Пожалуйста, заполните все обязательные поля')
        return
      }
      try {
        const payload = {
          serialNumber: this.currentItem.serialNumber,
          launcherStatus: this.currentItem.launcherStatus,
          typeId: parseInt(this.currentItem.typeId),
          formationId: parseInt(this.currentItem.formationId),
          yearOfManufacture: this.currentItem.yearOfManufacture,
          category: 'MISSILE_WEAPON',
        }
        console.log('Отправка данных:', JSON.stringify(payload, null, 2))
        if (this.isEditMode) {
          await axios.put(`http://localhost:8080/api/equipment/missileweapon/update/${this.currentItem.id}`, payload)
        } else {
          await axios.post('http://localhost:8080/api/equipment/missileweapon/create', payload)
        }
        this.modal.hide()
        this.fetchMissileWeapons()
      } catch (error) {
        console.error('Ошибка сохранения ракетного оружия:', error)
        alert('Не удалось сохранить ракетное оружие: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteItem(item) {
      if (confirm(`Вы уверены, что хотите удалить ракетное оружие с серийным номером ${item.serialNumber}?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/equipment/missileweapon/delete/${item.id}`)
          this.fetchMissileWeapons()
        } catch (error) {
          console.error('Ошибка удаления ракетного оружия:', error)
          alert('Не удалось удалить ракетное оружие: ' + (error.response?.data?.message || error.message))
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
      const type = this.missileWeaponTypes.find(t => t.id === typeId)
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