<template>
  <div class="modal fade" id="formationModal" tabindex="-1" aria-labelledby="formationModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="formationModalLabel">{{ isEditMode ? 'Редактировать военное соединение' : 'Создать военное соединение' }}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label for="name" class="form-label">Название</label>
              <input type="text" class="form-control" id="name" v-model="currentFormation.name" required>
            </div>
            <div class="mb-3">
              <label for="commanderId" class="form-label">Командир</label>
              <select class="form-select" id="commanderId" ref="commanderSelect" v-model="currentFormation.commanderId"></select>
            </div>
            <div class="mb-3">
              <label for="companiesId" class="form-label">Роты</label>
              <select class="form-select" id="companiesId" ref="companiesSelect" multiple v-model="currentFormation.companiesId"></select>
            </div>
            <div class="mb-3">
              <label for="divisionId" class="form-label">Дивизия</label>
              <select class="form-select" id="divisionId" ref="divisionSelect" v-model="currentFormation.divisionId"></select>
            </div>
            <div class="mb-3">
              <label for="headquarterId" class="form-label">Штаб</label>
              <select class="form-select" id="headquarterId" ref="headquarterSelect" v-model="currentFormation.headquarterId" required></select>
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
  name: 'MilitaryFormationForm',
  props: {
    soldiers: {
      type: Array,
      required: true,
    },
    companies: {
      type: Array,
      required: true,
    },
    divisions: {
      type: Array,
      required: true,
    },
    headquarters: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      currentFormation: {
        id: null,
        name: '',
        commanderId: null,
        companiesId: [],
        divisionId: null,
        headquarterId: null,
        equipmentsId: null,
      },
      isEditMode: false,
      modal: null,
      commanderChoices: null,
      companiesChoices: null,
      divisionChoices: null,
      headquarterChoices: null,
      isChoicesInitialized: false,
    }
  },
  mounted() {
    this.modal = new Modal(document.getElementById('formationModal'))
  },
  methods: {
    openModal(isEditMode, formation) {
      console.log('Открытие модального окна:', { isEditMode, formation })
      this.isEditMode = isEditMode
      this.currentFormation = isEditMode
        ? {
            id: formation.id,
            name: formation.name,
            commanderId: formation.commanderId || null,
            companiesId: formation.companiesId ? [...formation.companiesId] : [],
            divisionId: formation.divisionId || null,
            headquarterId: formation.headquarterId || null,
            equipmentsId: null,
          }
        : {
            id: null,
            name: '',
            commanderId: null,
            companiesId: [],
            divisionId: null,
            headquarterId: null,
            equipmentsId: null,
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
      if (!this.currentFormation.headquarterId) {
        alert('Пожалуйста, выберите штаб')
        return
      }
      const formationData = {
        id: this.currentFormation.id,
        name: this.currentFormation.name,
        commanderId: this.currentFormation.commanderId ? parseInt(this.currentFormation.commanderId) : null,
        companiesId: this.currentFormation.companiesId.length ? this.currentFormation.companiesId.map(id => parseInt(id)) : null,
        divisionId: this.currentFormation.divisionId ? parseInt(this.currentFormation.divisionId) : null,
        headquarterId: parseInt(this.currentFormation.headquarterId),
        equipmentsId: null,
      }
      console.log('Отправка данных в родительский компонент:', formationData)
      this.$emit('save', { formationData, isEditMode: this.isEditMode })
    },
    initChoices() {
      if (this.isChoicesInitialized) {
        console.log('Choices уже инициализированы, пропускаем')
        return
      }
      console.log('Инициализация Choices')
      // Очищаем DOM-элементы select
      this.$refs.commanderSelect.innerHTML = ''
      this.$refs.companiesSelect.innerHTML = ''
      this.$refs.divisionSelect.innerHTML = ''
      this.$refs.headquarterSelect.innerHTML = ''
      // Удаляем дубли в soldiers, companies, divisions и headquarters
      const uniqueSoldiers = Array.from(new Map(this.soldiers.map(s => [s.id, s])).values())
      const uniqueCompanies = Array.from(new Map(this.companies.map(c => [c.id, c])).values())
      const uniqueDivisions = Array.from(new Map(this.divisions.map(d => [d.id, d])).values())
      const uniqueHeadquarters = Array.from(new Map(this.headquarters.map(h => [h.id, h])).values())
      console.log('Уникальные солдаты:', uniqueSoldiers)
      console.log('Уникальные роты:', uniqueCompanies)
      console.log('Уникальные дивизии:', uniqueDivisions)
      console.log('Уникальные штабы:', uniqueHeadquarters)

      // Очищаем существующие Choices
      this.destroyChoices()

      // Инициализация Choices для командира (без фильтра офицеров)
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

      // Инициализация Choices для рот
      this.companiesChoices = new Choices(this.$refs.companiesSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите роты',
        removeItemButton: true,
        choices: uniqueCompanies.map(c => ({
          value: c.id.toString(),
          label: c.name,
          selected: this.currentFormation.companiesId.includes(c.id.toString()),
        })),
      })

      // Инициализация Choices для дивизии
      this.divisionChoices = new Choices(this.$refs.divisionSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите дивизию',
        choices: [
          { value: '', label: 'Нет' },
          ...uniqueDivisions.map(d => ({
            value: d.id.toString(),
            label: d.name,
          })),
        ],
      })

      // Инициализация Choices для штаба
      this.headquarterChoices = new Choices(this.$refs.headquarterSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите штаб',
        choices: uniqueHeadquarters.map(h => ({
          value: h.id.toString(),
          label: h.name || `Штаб ${h.id}`,
        })),
      })

      // Синхронизация с v-model
      this.companiesChoices.setChoiceByValue(this.currentFormation.companiesId.map(id => id.toString()))

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
      if (this.companiesChoices) {
        this.companiesChoices.clearChoices()
        this.companiesChoices.clearStore()
        this.companiesChoices.destroy()
        this.companiesChoices = null
      }
      if (this.divisionChoices) {
        this.divisionChoices.clearChoices()
        this.divisionChoices.clearStore()
        this.divisionChoices.destroy()
        this.divisionChoices = null
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