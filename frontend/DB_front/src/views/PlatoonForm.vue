<template>
  <div class="modal fade" id="platoonModal" tabindex="-1" aria-labelledby="platoonModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="platoonModalLabel">{{ isEditMode ? 'Редактировать взвод' : 'Создать взвод' }}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label for="name" class="form-label">Название</label>
              <input type="text" class="form-control" id="name" v-model="currentPlatoon.name" required>
            </div>
            <div class="mb-3">
              <label for="commanderId" class="form-label">Командир</label>
              <select class="form-select" id="commanderId" ref="commanderSelect" v-model="currentPlatoon.commanderId"></select>
            </div>
            <div class="mb-3">
              <label for="squadsId" class="form-label">Отряды</label>
              <select class="form-select" id="squadsId" ref="squadsSelect" multiple v-model="currentPlatoon.squadsId"></select>
            </div>
            <div class="mb-3">
              <label for="companyId" class="form-label">Рота</label>
              <select class="form-select" id="companyId" ref="companySelect" v-model="currentPlatoon.companyId"></select>
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
  name: 'PlatoonForm',
  props: {
    soldiers: {
      type: Array,
      required: true,
    },
    squads: {
      type: Array,
      required: true,
    },
    companies: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      currentPlatoon: {
        id: null,
        name: '',
        commanderId: null,
        squadsId: [],
        companyId: null,
      },
      isEditMode: false,
      modal: null,
      commanderChoices: null,
      squadsChoices: null,
      companyChoices: null,
      isChoicesInitialized: false,
    }
  },
  mounted() {
    this.modal = new Modal(document.getElementById('platoonModal'))
  },
  methods: {
    openModal(isEditMode, platoon) {
      console.log('Открытие модального окна:', { isEditMode, platoon })
      this.isEditMode = isEditMode
      this.currentPlatoon = isEditMode
        ? {
            id: platoon.id,
            name: platoon.name,
            commanderId: platoon.commanderId || null,
            squadsId: platoon.squadsId ? [...platoon.squadsId] : [], // Ensure a fresh copy of squadsId
            companyId: platoon.companyId || null,
          }
        : {
            id: null,
            name: '',
            commanderId: null,
            squadsId: [],
            companyId: null,
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
      if (this.currentPlatoon.commanderId && this.currentPlatoon.squadsId.includes(this.currentPlatoon.commanderId)) {
        alert('Командир не может быть в списке отрядов')
        return
      }
      const platoonData = {
        id: this.currentPlatoon.id,
        name: this.currentPlatoon.name,
        commanderId: this.currentPlatoon.commanderId ? parseInt(this.currentPlatoon.commanderId) : null,
        squadsId: this.currentPlatoon.squadsId.length ? this.currentPlatoon.squadsId.map(id => parseInt(id)) : null,
        companyId: this.currentPlatoon.companyId ? parseInt(this.currentPlatoon.companyId) : null,
      }
      console.log('Отправка данных в родительский компонент:', platoonData)
      this.$emit('save', { platoonData, isEditMode: this.isEditMode })
    },
    initChoices() {
      if (this.isChoicesInitialized) {
        console.log('Choices уже инициализированы, пропускаем')
        return
      }
      console.log('Инициализация Choices')
      // Очищаем DOM-элементы select
      this.$refs.commanderSelect.innerHTML = ''
      this.$refs.squadsSelect.innerHTML = ''
      this.$refs.companySelect.innerHTML = ''
      // Удаляем дубли в soldiers, squads и companies
      const uniqueSoldiers = Array.from(new Map(this.soldiers.map(s => [s.id, s])).values())
      const uniqueSquads = Array.from(new Map(this.squads.map(s => [s.id, s])).values())
      const uniqueCompanies = Array.from(new Map(this.companies.map(c => [c.id, c])).values())
      console.log('Уникальные солдаты:', uniqueSoldiers)
      console.log('Уникальные отряды:', uniqueSquads)
      console.log('Уникальные роты:', uniqueCompanies)

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

      // Инициализация Choices для отрядов
      this.squadsChoices = new Choices(this.$refs.squadsSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите отряды',
        removeItemButton: true,
        choices: uniqueSquads.map(s => ({
          value: s.id.toString(),
          label: s.name,
          selected: this.currentPlatoon.squadsId.includes(s.id.toString()), // Set selected squads
        })),
      })

      // Инициализация Choices для роты
      this.companyChoices = new Choices(this.$refs.companySelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите роту',
        choices: [
          { value: '', label: 'Нет' },
          ...uniqueCompanies.map(c => ({
            value: c.id.toString(),
            label: c.name,
          })),
        ],
      })

      // Ensure v-model syncs with Choices.js
      this.squadsChoices.setChoiceByValue(this.currentPlatoon.squadsId.map(id => id.toString()))

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
      if (this.squadsChoices) {
        this.squadsChoices.clearChoices()
        this.squadsChoices.clearStore()
        this.squadsChoices.destroy()
        this.squadsChoices = null
      }
      if (this.companyChoices) {
        this.companyChoices.clearChoices()
        this.companyChoices.clearStore()
        this.companyChoices.destroy()
        this.companyChoices = null
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