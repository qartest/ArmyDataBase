<template>
  <div class="modal fade" id="buildingModal" tabindex="-1" aria-labelledby="buildingModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="buildingModalLabel">{{ isEditMode ? 'Редактировать здание' : 'Создать здание' }}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label for="name" class="form-label">Название</label>
              <input type="text" class="form-control" id="name" v-model="currentBuilding.name" required>
            </div>
            <div class="mb-3">
              <label for="address" class="form-label">Адрес</label>
              <input type="text" class="form-control" id="address" v-model="currentBuilding.address" required>
            </div>
            <div class="mb-3">
              <label for="type" class="form-label">Тип</label>
              <select class="form-select" id="type" ref="typeSelect" v-model="currentBuilding.type" required></select>
            </div>
            <div class="mb-3">
              <label for="headquarterId" class="form-label">Штаб</label>
              <select class="form-select" id="headquarterId" ref="headquarterSelect" v-model="currentBuilding.headquarterId"></select>
            </div>
            <button type="submit" class="btn btn-primary">{{ isEditMode ? 'Сохранить' : 'Создать' }}</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Modal } from 'bootstrap'
import Choices from 'choices.js'
import 'choices.js/public/assets/styles/choices.min.css'

export default {
  name: 'BuildingForm',
  props: {
    headquarters: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      currentBuilding: {
        id: null,
        name: '',
        address: '',
        type: null,
        headquarterId: null,
      },
      isEditMode: false,
      modal: null,
      typeChoices: null,
      headquarterChoices: null,
      isChoicesInitialized: false,
    }
  },
  mounted() {
    this.modal = new Modal(document.getElementById('buildingModal'))
  },
  methods: {
    openModal(isEditMode, building) {
      console.log('Открытие модального окна:', { isEditMode, building })
      this.isEditMode = isEditMode
      this.currentBuilding = isEditMode
        ? {
            id: building.id,
            name: building.name,
            address: building.address,
            type: building.type,
            headquarterId: building.headquarterId || null,
          }
        : {
            id: null,
            name: '',
            address: '',
            type: null,
            headquarterId: null,
          }
      this.isChoicesInitialized = false
      this.initChoices()
      this.modal.show()
    },
    closeModal() {
      this.modal.hide()
      this.destroyChoices()
      this.isChoicesInitialized = false
    },
    submitForm() {
      if (!this.currentBuilding.name || !this.currentBuilding.address || !this.currentBuilding.type) {
        alert('Пожалуйста, заполните обязательные поля: Название, Адрес, Тип')
        return
      }
      const buildingData = {
        id: this.currentBuilding.id,
        name: this.currentBuilding.name,
        address: this.currentBuilding.address,
        type: this.currentBuilding.type,
        headquarterId: this.currentBuilding.headquarterId ? parseInt(this.currentBuilding.headquarterId) : null,
      }
      console.log('Отправка данных в родительский компонент:', buildingData)
      this.$emit('save', { buildingData, isEditMode: this.isEditMode })
    },
    initChoices() {
      if (this.isChoicesInitialized) {
        console.log('Choices уже инициализированы, пропускаем')
        return
      }
      console.log('Инициализация Choices')
      // Очищаем DOM-элементы select
      this.$refs.typeSelect.innerHTML = ''
      this.$refs.headquarterSelect.innerHTML = ''
      // Удаляем дубли в headquarters
      const uniqueHeadquarters = Array.from(new Map(this.headquarters.map(h => [h.id, h])).values())
      console.log('Уникальные штабы:', uniqueHeadquarters)

      // Очищаем существующие Choices
      this.destroyChoices()

      // Инициализация Choices для типа здания
      this.typeChoices = new Choices(this.$refs.typeSelect, {
        searchEnabled: false,
        itemSelectText: '',
        placeholderValue: 'Выберите тип здания',
        choices: [
          { value: 'BARRACKS', label: 'Казармы' },
          { value: 'WAREHOUSE', label: 'Склад' },
          { value: 'TRAINING', label: 'Учебный корпус' },
          { value: 'HANGAR', label: 'Ангар' },
        ],
      })

      // Инициализация Choices для штаба
      this.headquarterChoices = new Choices(this.$refs.headquarterSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите штаб',
        choices: [
          { value: '', label: 'Нет' },
          ...uniqueHeadquarters.map(h => ({
            value: h.id.toString(),
            label: h.name || `Штаб ${h.id}`,
          })),
        ],
      })

      this.isChoicesInitialized = true
      console.log('Choices успешно инициализированы')
    },
    destroyChoices() {
      console.log('Очистка Choices')
      if (this.typeChoices) {
        this.typeChoices.clearChoices()
        this.typeChoices.clearStore()
        this.typeChoices.destroy()
        this.typeChoices = null
      }
      if (this.headquarterChoices) {
        this.headquarterChoices.clearChoices()
        this.headquarterChoices.clearStore()
        this.headquarterChoices.destroy()
        this.headquarterChoices = null
      }
    },
  },
  beforeDestroy() {
    this.destroyChoices()
  },
}
</script>

<style scoped>
.choices__inner {
  min-height: 38px;
  padding: 7px 10px;
  font-size: 14px;
}
.choices__list--dropdown {
  z-index: 1000;
}
</style>