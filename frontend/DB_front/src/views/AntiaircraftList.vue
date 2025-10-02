<template>
  <div class="container mt-4 mt-5">
    <h1 class="mb-4">ПВО</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="fetchAntiaircrafts">Обновить</button>
      <button class="btn btn-success" @click="openCreateModal">Добавить элемент</button>
    </div>
    <table class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Серийный номер</th>
          <td>Статус радара</td>
          <th>Тип</th>
          <th>Военная часть</th>
          <th>Год производства</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="antiaircrafts.length === 0">
          <td colspan="6" class="text-center">Нет данных</td>
        </tr>
        <tr v-for="antiaircraft in antiaircrafts" :key="antiaircraft.id">
          <td>{{ antiaircraft.serialNumber }}</td>
          <td>{{ translateCondition(antiaircraft.radarStatus) }}</td>
          <td>{{ getTypeName(antiaircraft.typeId) }}</td>
          <td>{{ antiaircraft.formationName }}</td>
          <td>{{ formatDate(antiaircraft.yearOfManufacture) }}</td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="openEditModal(antiaircraft)">Отредактировать</button>
            <button class="btn btn-danger btn-sm" @click="deleteItem(antiaircraft)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Модальное окно для создания/редактирования -->
    <div class="modal fade" id="antiaircraftModal" tabindex="-1" aria-labelledby="antiaircraftModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="antiaircraftModalLabel">{{ isEditMode ? 'Редактировать ПВО' : 'Создать ПВО' }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveItem">
              <div class="mb-3">
                <label for="serialNumber" class="form-label">Серийный номер</label>
                <input type="text" class="form-control" id="serialNumber" v-model="currentItem.serialNumber" required>
              </div>
              <div class="mb-3">
                <label for="radarStatus" class="form-label">Статус радара</label>
                <select class="form-select" id="radarStatus" v-model="currentItem.radarStatus" required>
                  <option value="" disabled>Выберите статус</option>
                  <option v-for="condition in conditions" :key="condition.value" :value="condition.value">
                    {{ condition.label }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label for="typeId" class="form-label">Тип ПВО</label>
                <select class="form-select" id="typeId" v-model="currentItem.typeId" required>
                  <option value="" disabled>Выберите тип</option>
                  <option v-for="type in antiaircraftTypes" :key="type.id" :value="type.id">
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
  name: 'AntiaircraftList',
  data() {
    return {
      antiaircrafts: [],
      antiaircraftTypes: [],
      militaryFormations: [],
      currentItem: {
        id: null,
        serialNumber: '',
        radarStatus: '',
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
    this.fetchAntiaircrafts()
    this.fetchAntiaircraftTypes()
    this.fetchMilitaryFormations()
    this.modal = new Modal(document.getElementById('antiaircraftModal'))
  },
  methods: {
    async fetchAntiaircrafts() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/antiaircraft/getAll')
        console.log('Полученные данные ПВО:', response.data)
        this.antiaircrafts = response.data.sort((a, b) => a.id - b.id)
        console.log('Обработанные ПВО:', this.antiaircrafts)
      } catch (error) {
        console.error('Ошибка загрузки ПВО:', error)
        alert('Не удалось загрузить список ПВО: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchAntiaircraftTypes() {
      try {
        const response = await axios.get('http://localhost:8080/api/equipment/antiaircraftType/getAll')
        this.antiaircraftTypes = response.data
        console.log('Полученные типы ПВО:', this.antiaircraftTypes)
      } catch (error) {
        console.error('Ошибка загрузки типов ПВО:', error)
        alert('Не удалось загрузить типы ПВО')
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
        radarStatus: '',
        typeId: '',
        formationId: '',
        yearOfManufacture: '',
      }
      this.modal.show()
    },
    openEditModal(antiaircraft) {
      this.isEditMode = true
      this.currentItem = {
        id: antiaircraft.id,
        serialNumber: antiaircraft.serialNumber,
        radarStatus: antiaircraft.radarStatus,
        typeId: antiaircraft.typeId,
        formationId: antiaircraft.formationId,
        yearOfManufacture: antiaircraft.yearOfManufacture ? antiaircraft.yearOfManufacture.split('T')[0] : '',
      }
      this.modal.show()
    },
    async saveItem() {
      if (!this.currentItem.serialNumber || !this.currentItem.radarStatus || !this.currentItem.typeId || !this.currentItem.formationId || !this.currentItem.yearOfManufacture) {
        alert('Пожалуйста, заполните все обязательные поля')
        return
      }
      try {
        const payload = {
          serialNumber: this.currentItem.serialNumber,
          radarStatus: this.currentItem.radarStatus,
          typeId: parseInt(this.currentItem.typeId),
          formationId: parseInt(this.currentItem.formationId),
          yearOfManufacture: this.currentItem.yearOfManufacture,
          category: 'ANTIAIRCRAFT',
        }
        console.log('Отправка данных:', payload)
        if (this.isEditMode) {
          await axios.put(`http://localhost:8080/api/equipment/antiaircraft/update/${this.currentItem.id}`, payload)
        } else {
          await axios.post('http://localhost:8080/api/equipment/antiaircraft/create', payload)
        }
        this.modal.hide()
        this.fetchAntiaircrafts()
      } catch (error) {
        console.error('Ошибка сохранения ПВО:', error)
        alert('Не удалось сохранить ПВО: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteItem(antiaircraft) {
      if (confirm(`Вы уверены, что хотите удалить ПВО с серийным номером ${antiaircraft.serialNumber}?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/equipment/antiaircraft/delete/${antiaircraft.id}`)
          this.fetchAntiaircrafts()
        } catch (error) {
          console.error('Ошибка удаления ПВО:', error)
          alert('Не удалось удалить ПВО: ' + (error.response?.data?.message || error.message))
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
      const type = this.antiaircraftTypes.find(t => t.id === typeId)
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