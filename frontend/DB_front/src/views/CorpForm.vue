<template>
  <div class="modal fade" id="corpModal" tabindex="-1" aria-labelledby="corpModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="corpModalLabel">{{ isEditMode ? 'Редактировать корпус' : 'Создать корпус' }}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label for="name" class="form-label">Название</label>
              <input type="text" class="form-control" id="name" v-model="currentCorp.name" required>
            </div>
            <div class="mb-3">
              <label for="commanderId" class="form-label">Командир</label>
              <select class="form-select" id="commanderId" ref="commanderSelect" v-model="currentCorp.commanderId"></select>
            </div>
            <div class="mb-3">
              <label for="divisionsId" class="form-label">Дивизии</label>
              <select class="form-select" id="divisionsId" ref="divisionsSelect" multiple v-model="currentCorp.divisionsId"></select>
            </div>
            <div class="mb-3">
              <label for="armyId" class="form-label">Армия</label>
              <select class="form-select" id="armyId" ref="armySelect" v-model="currentCorp.armyId"></select>
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
  name: 'CorpForm',
  props: {
    soldiers: {
      type: Array,
      required: true,
    },
    divisions: {
      type: Array,
      required: true,
    },
    armies: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      currentCorp: {
        id: null,
        name: '',
        commanderId: null,
        divisionsId: [],
        armyId: null,
      },
      isEditMode: false,
      modal: null,
      commanderChoices: null,
      divisionsChoices: null,
      armyChoices: null,
      isChoicesInitialized: false,
    }
  },
  mounted() {
    this.modal = new Modal(document.getElementById('corpModal'))
  },
  methods: {
    openModal(isEditMode, corp) {
      console.log('Открытие модального окна:', { isEditMode, corp })
      this.isEditMode = isEditMode
      this.currentCorp = isEditMode
        ? {
            id: corp.id,
            name: corp.name,
            commanderId: corp.commanderId || null,
            divisionsId: corp.divisionsId ? [...corp.divisionsId] : [],
            armyId: corp.armyId || null,
          }
        : {
            id: null,
            name: '',
            commanderId: null,
            divisionsId: [],
            armyId: null,
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
      if (!this.currentCorp.name) {
        alert('Пожалуйста, заполните обязательное поле: Название')
        return
      }
      const corpData = {
        id: this.currentCorp.id,
        name: this.currentCorp.name,
        commanderId: this.currentCorp.commanderId ? parseInt(this.currentCorp.commanderId) : null,
        divisionsId: this.currentCorp.divisionsId.length ? this.currentCorp.divisionsId.map(id => parseInt(id)) : null,
        armyId: this.currentCorp.armyId ? parseInt(this.currentCorp.armyId) : null,
      }
      console.log('Отправка данных в родительский компонент:', corpData)
      this.$emit('save', { corpData, isEditMode: this.isEditMode })
    },
    initChoices() {
      if (this.isChoicesInitialized) {
        console.log('Choices уже инициализированы, пропускаем')
        return
      }
      console.log('Инициализация Choices')
      // Очищаем DOM-элементы select
      this.$refs.commanderSelect.innerHTML = ''
      this.$refs.divisionsSelect.innerHTML = ''
      this.$refs.armySelect.innerHTML = ''
      // Удаляем дубли в soldiers, divisions и armies
      const uniqueSoldiers = Array.from(new Map(this.soldiers.map(s => [s.id, s])).values())
      const uniqueDivisions = Array.from(new Map(this.divisions.map(d => [d.id, d])).values())
      const uniqueArmies = Array.from(new Map(this.armies.map(a => [a.id, a])).values())
      console.log('Уникальные солдаты:', uniqueSoldiers)
      console.log('Уникальные дивизии:', uniqueDivisions)
      console.log('Уникальные армии:', uniqueArmies)

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

      // Инициализация Choices для дивизий
      this.divisionsChoices = new Choices(this.$refs.divisionsSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите дивизии',
        removeItemButton: true,
        choices: uniqueDivisions.map(d => ({
          value: d.id.toString(),
          label: d.name,
          selected: this.currentCorp.divisionsId.includes(d.id.toString()),
        })),
      })

      // Инициализация Choices для армии
      this.armyChoices = new Choices(this.$refs.armySelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите армию',
        choices: [
          { value: '', label: 'Нет' },
          ...uniqueArmies.map(a => ({
            value: a.id.toString(),
            label: a.name,
          })),
        ],
      })

      // Синхронизация с v-model
      this.divisionsChoices.setChoiceByValue(this.currentCorp.divisionsId.map(id => id.toString()))

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
      if (this.divisionsChoices) {
        this.divisionsChoices.clearChoices()
        this.divisionsChoices.clearStore()
        this.divisionsChoices.destroy()
        this.divisionsChoices = null
      }
      if (this.armyChoices) {
        this.armyChoices.clearChoices()
        this.armyChoices.clearStore()
        this.armyChoices.destroy()
        this.armyChoices = null
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