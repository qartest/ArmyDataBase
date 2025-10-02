<template>
  <div class="container mt-4">
    <h1 class="mb-4">Тип ПВО</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="refreshList">Обновить</button>
      <button class="btn btn-success" @click="openCreateModal">Создать</button>
    </div>
    <table class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Модель</th>
          <th>Дальность (км)</th>
          <th>Высота цели (м)</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="antiaircraftTypes.length === 0">
          <td colspan="4" class="text-center">Нет данных</td>
        </tr>
        <tr v-for="type in antiaircraftTypes" :key="type.id">
          <td>{{ type.model }}</td>
          <td>{{ type.range }}</td>
          <td>{{ type.targetAltitude }}</td>
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
            <h5 class="modal-title" id="typeModalLabel">{{ isEditMode ? 'Редактировать тип ПВО' : 'Создать тип ПВО' }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveType">
              <div class="mb-3">
                <label for="model" class="form-label">Модель</label>
                <input type="text" class="form-control" id="model" v-model="currentType.model" required>
              </div>
              <div class="mb-3">
                <label for="range" class="form-label">Дальность (км)</label>
                <input type="number" class="form-control" id="range" v-model="currentType.range" min="0" required>
              </div>
              <div class="mb-3">
                <label for="targetAltitude" class="form-label">Высота цели (м)</label>
                <input type="number" class="form-control" id="targetAltitude" v-model="currentType.targetAltitude" min="0" required>
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
  name: 'AntiaircraftTypeList',
  data() {
    return {
      antiaircraftTypes: [],
      currentType: {
        id: null,
        model: '',
        range: null,
        targetAltitude: null,
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
        const response = await axios.get('http://localhost:8080/api/equipment/antiaircraftType/getAll')
        this.antiaircraftTypes = response.data.sort((a, b) => a.id - b.id)
      } catch (error) {
        console.error('Ошибка загрузки типов ПВО:', error)
        alert('Не удалось загрузить список типов ПВО: ' + error.message)
      }
    },
    refreshList() {
      this.fetchTypes()
    },
    openCreateModal() {
      this.isEditMode = false
      this.currentType = { id: null, model: '', range: null, targetAltitude: null }
      this.modal.show()
    },
    openEditModal(type) {
      this.isEditMode = true
      this.currentType = { ...type }
      this.modal.show()
    },
    async saveType() {
      try {
        if (this.isEditMode) {
          await axios.put(`http://localhost:8080/api/equipment/antiaircraftType/update/${this.currentType.id}`, {
            model: this.currentType.model,
            range: this.currentType.range,
            targetAltitude: this.currentType.targetAltitude,
          })
        } else {
          await axios.post('http://localhost:8080/api/equipment/antiaircraftType/create', {
            model: this.currentType.model,
            range: this.currentType.range,
            targetAltitude: this.currentType.targetAltitude,
          })
        }
        this.modal.hide()
        this.fetchTypes()
      } catch (error) {
        console.error('Ошибка сохранения типа ПВО:', error)
        alert('Не удалось сохранить тип ПВО: ' + error.message)
      }
    },
    async deleteType(type) {
      if (confirm(`Вы уверены, что хотите удалить тип ПВО "${type.model}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/equipment/antiaircraftType/delete/${type.id}`)
          this.fetchTypes()
        } catch (error) {
          console.error('Ошибка удаления типа ПВО:', error)
          alert('Не удалось удалить тип ПВО: ' + error.message)
        }
      }
    },
  },
}
</script>

<style scoped>
/* Дополнительные стили */
</style>