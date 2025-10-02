<template>
  <div class="modal fade" id="divisionModal" tabindex="-1" aria-labelledby="divisionModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="divisionModalLabel">{{ isEditMode ? 'Редактировать дивизию' : 'Создать дивизию' }}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label for="name" class="form-label">Название</label>
              <input type="text" class="form-control" id="name" v-model="currentDivision.name" required>
            </div>
            <div class="mb-3">
              <label for="commanderId" class="form-label">Командир</label>
              <select class="form-select" id="commanderId" ref="commanderSelect" v-model="currentDivision.commanderId"></select>
            </div>
            <div class="mb-3">
              <label for="formationsId" class="form-label">Военные соединения</label>
              <select class="form-select" id="formationsId" ref="formationsSelect" multiple v-model="currentDivision.formationsId"></select>
            </div>
            <div class="mb-3">
              <label for="corpId" class="form-label">Корпус</label>
              <select class="form-select" id="corpId" ref="corpSelect" v-model="currentDivision.corpId"></select>
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
  name: 'DivisionForm',
  props: {
    soldiers: {
      type: Array,
      required: true,
    },
    formations: {
      type: Array,
      required: true,
    },
    corps: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      currentDivision: {
        id: null,
        name: '',
        commanderId: null,
        formationsId: [],
        corpId: null,
      },
      isEditMode: false,
      modal: null,
      commanderChoices: null,
      formationsChoices: null,
      corpChoices: null,
      isChoicesInitialized: false,
    }
  },
  mounted() {
    this.modal = new Modal(document.getElementById('divisionModal'))
  },
  methods: {
    openModal(isEditMode, division) {
      console.log('Открытие модального окна:', { isEditMode, division })
      this.isEditMode = isEditMode
      this.currentDivision = isEditMode
        ? {
            id: division.id,
            name: division.name,
            commanderId: division.commanderId || null,
            formationsId: division.formationsId ? [...division.formationsId] : [],
            corpId: division.corpId || null,
          }
        : {
            id: null,
            name: '',
            commanderId: null,
            formationsId: [],
            corpId: null,
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
      if (!this.currentDivision.name) {
        alert('Пожалуйста, заполните обязательное поле: Название')
        return
      }
      const divisionData = {
        id: this.currentDivision.id,
        name: this.currentDivision.name,
        commanderId: this.currentDivision.commanderId ? parseInt(this.currentDivision.commanderId) : null,
        formationsId: this.currentDivision.formationsId.length ? this.currentDivision.formationsId.map(id => parseInt(id)) : null,
        corpId: this.currentDivision.corpId ? parseInt(this.currentDivision.corpId) : null,
      }
      console.log('Отправка данных в родительский компонент:', divisionData)
      this.$emit('save', { divisionData, isEditMode: this.isEditMode })
    },
    initChoices() {
      if (this.isChoicesInitialized) {
        console.log('Choices уже инициализированы, пропускаем')
        return
      }
      console.log('Инициализация Choices')
      // Очищаем DOM-элементы select
      this.$refs.commanderSelect.innerHTML = ''
      this.$refs.formationsSelect.innerHTML = ''
      this.$refs.corpSelect.innerHTML = ''
      // Удаляем дубли в soldiers, formations и corps
      const uniqueSoldiers = Array.from(new Map(this.soldiers.map(s => [s.id, s])).values())
      const uniqueFormations = Array.from(new Map(this.formations.map(f => [f.id, f])).values())
      const uniqueCorps = Array.from(new Map(this.corps.map(c => [c.id, c])).values())
      console.log('Уникальные солдаты:', uniqueSoldiers)
      console.log('Уникальные военные соединения:', uniqueFormations)
      console.log('Уникальные корпуса:', uniqueCorps)

      // Очищаем существующие Choices
      this.destroyChoices()

      // Инициализация Choices для командира
      this.commanderChoices = new Choices(this.$refs.commanderSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите командира',
        choices: [
          { value: '', label: 'Нет' },
          ...uniqueSoldiers.map(s => ({
            value: s.id.toString(),
            label: `${s.secondName} ${s.firstName} ${s.fatherName || ''} (${s.rankName || 'Нет звания'})`,
          })),
        ],
      })

      // Инициализация Choices для военных соединений
      this.formationsChoices = new Choices(this.$refs.formationsSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите военные соединения',
        removeItemButton: true,
        choices: uniqueFormations.map(f => ({
          value: f.id.toString(),
          label: f.name,
          selected: this.currentDivision.formationsId.includes(f.id.toString()),
        })),
      })

      // Инициализация Choices для корпуса
      this.corpChoices = new Choices(this.$refs.corpSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите корпус',
        choices: [
          { value: '', label: 'Нет' },
          ...uniqueCorps.map(c => ({
            value: c.id.toString(),
            label: c.name,
          })),
        ],
      })

      // Синхронизация с v-model
      this.formationsChoices.setChoiceByValue(this.currentDivision.formationsId.map(id => id.toString()))

      this.isChoicesInitialized = true
      console.log('Choices успешно инициализированы')
    },
    destroyChoices() {
      console.log('Очистка Choices')
      if (this.commanderChoices) {
        this.commanderChoices.clearChoices()
        this.commanderChoices.clearStore()
        this.commanderChoices.destroy()
        this.commanderChoices = null
      }
      if (this.formationsChoices) {
        this.formationsChoices.clearChoices()
        this.formationsChoices.clearStore()
        this.formationsChoices.destroy()
        this.formationsChoices = null
      }
      if (this.corpChoices) {
        this.corpChoices.clearChoices()
        this.corpChoices.clearStore()
        this.corpChoices.destroy()
        this.corpChoices = null
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