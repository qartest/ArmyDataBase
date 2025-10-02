<template>
  <div class="modal fade" id="companyModal" tabindex="-1" aria-labelledby="companyModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="companyModalLabel">{{ isEditMode ? 'Редактировать роту' : 'Создать роту' }}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label for="name" class="form-label">Название</label>
              <input type="text" class="form-control" id="name" v-model="currentCompany.name" required>
            </div>
            <div class="mb-3">
              <label for="commanderId" class="form-label">Командир</label>
              <select class="form-select" id="commanderId" ref="commanderSelect" v-model="currentCompany.commanderId"></select>
            </div>
            <div class="mb-3">
              <label for="platoonsId" class="form-label">Взводы</label>
              <select class="form-select" id="platoonsId" ref="platoonsSelect" multiple v-model="currentCompany.platoonsId"></select>
            </div>
            <div class="mb-3">
              <label for="formationId" class="form-label">Военное соединение</label>
              <select class="form-select" id="formationId" ref="formationSelect" v-model="currentCompany.formationId"></select>
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
  name: 'CompanyForm',
  props: {
    soldiers: {
      type: Array,
      required: true,
    },
    platoons: {
      type: Array,
      required: true,
    },
    formations: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      currentCompany: {
        id: null,
        name: '',
        commanderId: null,
        platoonsId: [],
        formationId: null,
      },
      isEditMode: false,
      modal: null,
      commanderChoices: null,
      platoonsChoices: null,
      formationChoices: null,
      isChoicesInitialized: false,
    }
  },
  mounted() {
    this.modal = new Modal(document.getElementById('companyModal'))
  },
  methods: {
    openModal(isEditMode, company) {
      console.log('Открытие модального окна:', { isEditMode, company })
      this.isEditMode = isEditMode
      this.currentCompany = isEditMode
        ? {
            id: company.id,
            name: company.name,
            commanderId: company.commanderId || null,
            platoonsId: company.platoonsId ? [...company.platoonsId] : [],
            formationId: company.formationId || null,
          }
        : {
            id: null,
            name: '',
            commanderId: null,
            platoonsId: [],
            formationId: null,
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
      const companyData = {
        id: this.currentCompany.id,
        name: this.currentCompany.name,
        commanderId: this.currentCompany.commanderId ? parseInt(this.currentCompany.commanderId) : null,
        platoonsId: this.currentCompany.platoonsId.length ? this.currentCompany.platoonsId.map(id => parseInt(id)) : null,
        formationId: this.currentCompany.formationId ? parseInt(this.currentCompany.formationId) : null,
      }
      console.log('Отправка данных в родительский компонент:', companyData)
      this.$emit('save', { companyData, isEditMode: this.isEditMode })
    },
    initChoices() {
      if (this.isChoicesInitialized) {
        console.log('Choices уже инициализированы, пропускаем')
        return
      }
      console.log('Инициализация Choices')
      // Очищаем DOM-элементы select
      this.$refs.commanderSelect.innerHTML = ''
      this.$refs.platoonsSelect.innerHTML = ''
      this.$refs.formationSelect.innerHTML = ''
      // Удаляем дубли в soldiers, platoons и formations
      const uniqueSoldiers = Array.from(new Map(this.soldiers.map(s => [s.id, s])).values())
      const uniquePlatoons = Array.from(new Map(this.platoons.map(p => [p.id, p])).values())
      const uniqueFormations = Array.from(new Map(this.formations.map(f => [f.id, f])).values())
      console.log('Уникальные солдаты:', uniqueSoldiers)
      console.log('Уникальные взводы:', uniquePlatoons)
      console.log('Уникальные военные соединения:', uniqueFormations)

      // Логируем данные officerRecords
      console.log('Данные officerRecords для солдат:', uniqueSoldiers.map(s => ({
        id: s.id,
        officerRecords: s.officerRecords
      })))

      // Очищаем существующие Choices
      this.destroyChoices()

      // Фильтруем офицеров
      const officerSoldiers = uniqueSoldiers//.filter(s => s.officerRecords && s.officerRecords.length > 0) // Временно отключено для теста
      console.log('Отфильтрованные офицеры:', officerSoldiers)

      // Инициализация Choices для командира
      this.commanderChoices = new Choices(this.$refs.commanderSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите командира',
        choices: [
          { value: '', label: 'Нет' },
          ...officerSoldiers.map(s => ({
            value: s.id.toString(),
            label: `${s.secondName} ${s.firstName} ${s.fatherName || ''} (${s.rankName || 'Нет звания'})`,
          })),
        ],
      })

      // Инициализация Choices для взводов
      this.platoonsChoices = new Choices(this.$refs.platoonsSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите взводы',
        removeItemButton: true,
        choices: uniquePlatoons.map(p => ({
          value: p.id.toString(),
          label: p.name,
          selected: this.currentCompany.platoonsId.includes(p.id.toString()),
        })),
      })

      // Инициализация Choices для военного соединения
      this.formationChoices = new Choices(this.$refs.formationSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите военное соединение',
        choices: [
          { value: '', label: 'Нет' },
          ...uniqueFormations.map(f => ({
            value: f.id.toString(),
            label: f.name,
          })),
        ],
      })

      // Синхронизация с v-model
      this.platoonsChoices.setChoiceByValue(this.currentCompany.platoonsId.map(id => id.toString()))

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
      if (this.platoonsChoices) {
        this.platoonsChoices.clearChoices()
        this.platoonsChoices.clearStore()
        this.platoonsChoices.destroy()
        this.platoonsChoices = null
      }
      if (this.formationChoices) {
        this.formationChoices.clearChoices()
        this.formationChoices.clearStore()
        this.formationChoices.destroy()
        this.formationChoices = null
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