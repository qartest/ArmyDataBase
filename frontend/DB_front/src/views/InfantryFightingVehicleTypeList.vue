<template>
  <div class="container mt-4">
    <h1 class="mb-4">Тип боевой машины пехоты</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="refreshList">Обновить</button>
      <button class="btn btn-success" @click="openCreateModal">Создать</button>
    </div>
    <table class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Модель</th>
          <th>Калибр орудия (мм)</th>
          <th>Толщина брони (мм)</th>
          <th>Вместимость (чел.)</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="infantryFightingVehicleTypes.length === 0">
          <td colspan="5" class="text-center">Нет данных</td>
        </tr>
        <tr v-for="type in infantryFightingVehicleTypes" :key="type.id">
          <td>{{ type.model }}</td>
          <td>{{ type.gunCaliber }}</td>
          <td>{{ type.armorThickness }}</td>
          <td>{{ type.passengerCapacity }}</td>
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
            <h5 class="modal-title" id="typeModalLabel">{{ isEditMode ? 'Редактировать тип БМП' : 'Создать тип БМП' }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveType">
              <div class="mb-3">
                <label for="model" class="form-label">Модель</label>
                <input type="text" class="form-control" id="model" v-model="currentType.model" required>
              </div>
              <div class="mb-3">
                <label for="gunCaliber" class="form-label">Калибр орудия (мм)</label>
                <input type="number" class="form-control" id="gunCaliber" v-model="currentType.gunCaliber" min="1" required>
              </div>
              <div class="mb-3">
                <label for="armorThickness" class="form-label">Толщина брони (мм)</label>
                <input type="number" class="form-control" id="armorThickness" v-model="currentType.armorThickness" min="1" required>
              </div>
              <div class="mb-3">
                <label for="passengerCapacity" class="form-label">Вместимость (чел.)</label>
                <input type="number" class="form-control" id="passengerCapacity" v-model="currentType.passengerCapacity" min="1" required>
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
  name: 'InfantryFightingVehicleTypeList',
  data() {
    return {
      infantryFightingVehicleTypes: [],
      currentType: {
        id: null,
        model: '',
        gunCaliber: null,
        armorThickness: null,
        passengerCapacity: null,
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
        const response = await axios.get('http://localhost:8080/api/equipment/IFVType/getAll')
        console.log('Полученные данные типов БМП:', response.data)
        this.infantryFightingVehicleTypes = response.data.sort((a, b) => a.id - b.id)
        console.log('Обработанные типы БМП:', this.infantryFightingVehicleTypes)
      } catch (error) {
        console.error('Ошибка загрузки типов БМП:', error)
        alert('Не удалось загрузить список типов БМП: ' + (error.response?.data?.message || error.message))
      }
    },
    refreshList() {
      this.fetchTypes()
    },
    openCreateModal() {
      this.isEditMode = false
      this.currentType = { id: null, model: '', gunCaliber: null, armorThickness: null, passengerCapacity: null }
      this.modal.show()
    },
    openEditModal(type) {
      this.isEditMode = true
      this.currentType = { ...type }
      this.modal.show()
    },
    async saveType() {
      if (!this.currentType.model || !this.currentType.gunCaliber || !this.currentType.armorThickness || !this.currentType.passengerCapacity) {
        alert('Пожалуйста, заполните все обязательные поля')
        return
      }
      if (this.currentType.gunCaliber < 1 || this.currentType.armorThickness < 1 || this.currentType.passengerCapacity < 1) {
        alert('Числовые поля должны быть больше 0')
        return
      }
      try {
        const payload = {
          model: this.currentType.model,
          gunCaliber: parseInt(this.currentType.gunCaliber),
          armorThickness: parseInt(this.currentType.armorThickness),
          passengerCapacity: parseInt(this.currentType.passengerCapacity),
        }
        console.log('Отправка данных:', payload)
        if (this.isEditMode) {
          await axios.put(`http://localhost:8080/api/equipment/IFVType/update/${this.currentType.id}`, payload)
        } else {
          await axios.post('http://localhost:8080/api/equipment/IFVType/create', payload)
        }
        this.modal.hide()
        this.fetchTypes()
      } catch (error) {
        console.error('Ошибка сохранения типа БМП:', error)
        alert('Не удалось сохранить тип БМП: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteType(type) {
      if (confirm(`Вы уверены, что хотите удалить тип БМП "${type.model}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/equipment/IFVType/delete/${type.id}`)
          this.fetchTypes()
        } catch (error) {
          console.error('Ошибка удаления типа БМП:', error)
          alert('Не удалось удалить тип БМП: ' + (error.response?.data?.message || error.message))
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