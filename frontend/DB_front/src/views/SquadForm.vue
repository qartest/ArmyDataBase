<template>
  <div class="modal fade" id="squadModal" tabindex="-1" aria-labelledby="squadModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="squadModalLabel">{{ isEditMode ? 'Редактировать отряд' : 'Создать отряд' }}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label for="name" class="form-label">Название</label>
              <input type="text" class="form-control" id="name" v-model="currentSquad.name" required>
            </div>
            <div class="mb-3">
              <label for="commanderId" class="form-label">Командир</label>
              <select class="form-select" id="commanderId" ref="commanderSelect" v-model="currentSquad.commanderId"></select>
            </div>
            <div class="mb-3">
              <label for="soldiersId" class="form-label">Солдаты</label>
              <select class="form-select" id="soldiersId" ref="soldiersSelect" multiple v-model="currentSquad.soldiersId"></select>
            </div>
            <div class="mb-3">
              <label for="platoonId" class="form-label">Взвод</label>
              <select class="form-select" id="platoonId" ref="platoonSelect" v-model="currentSquad.platoonId"></select>
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
  name: 'SquadForm',
  props: {
    soldiers: {
      type: Array,
      required: true,
    },
    platoons: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      currentSquad: {
        id: null,
        name: '',
        commanderId: null,
        soldiersId: [],
        platoonId: null,
      },
      isEditMode: false,
      modal: null,
      commanderChoices: null,
      soldiersChoices: null,
      platoonChoices: null,
      isChoicesInitialized: false,
    }
  },
  mounted() {
    this.modal = new Modal(document.getElementById('squadModal'))
  },
  methods: {
    openModal(isEditMode, squad) {
      console.log('Открытие модального окна:', { isEditMode, squad })
      this.isEditMode = isEditMode
      this.currentSquad = isEditMode
        ? {
            id: squad.id,
            name: squad.name,
            commanderId: squad.commanderId || null,
            soldiersId: squad.soldiersId || [],
            platoonId: squad.platoonId || null,
          }
        : {
            id: null,
            name: '',
            commanderId: null,
            soldiersId: [],
            platoonId: null,
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
      if (this.currentSquad.commanderId && this.currentSquad.soldiersId.includes(this.currentSquad.commanderId)) {
        alert('Командир не может быть в списке солдат')
        return
      }
      const squadData = {
        id: this.currentSquad.id,
        name: this.currentSquad.name,
        commanderId: this.currentSquad.commanderId ? parseInt(this.currentSquad.commanderId) : null,
        soldiersId: this.currentSquad.soldiersId.length ? this.currentSquad.soldiersId.map(id => parseInt(id)) : null,
        platoonId: this.currentSquad.platoonId ? parseInt(this.currentSquad.platoonId) : null,
      }
      console.log('Отправка данных в родительский компонент:', squadData)
      this.$emit('save', { squadData, isEditMode: this.isEditMode })
    },
    initChoices() {
      if (this.isChoicesInitialized) {
        console.log('Choices уже инициализированы, пропускаем')
        return
      }
      console.log('Инициализация Choices')
      // Очищаем DOM-элементы select
      this.$refs.commanderSelect.innerHTML = ''
      this.$refs.soldiersSelect.innerHTML = ''
      this.$refs.platoonSelect.innerHTML = ''
      // Удаляем дубли в soldiers и platoons
      const uniqueSoldiers = Array.from(new Map(this.soldiers.map(s => [s.id, s])).values())
      const uniquePlatoons = Array.from(new Map(this.platoons.map(p => [p.id, p])).values())
      // Исключаем командира из списка солдат
      const filteredSoldiers = uniqueSoldiers.filter(s => s.id.toString() !== this.currentSquad.commanderId)
      console.log('Уникальные солдаты (без командира):', filteredSoldiers)
      console.log('Уникальные взводы:', uniquePlatoons)

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

      // Инициализация Choices для солдат
      this.soldiersChoices = new Choices(this.$refs.soldiersSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите солдат',
        removeItemButton: true,
        choices: filteredSoldiers.map(s => ({
          value: s.id.toString(),
          label: `${s.secondName} ${s.firstName} ${s.fatherName || ''} (${s.rankName || 'Нет звания'})`,
        })),
      })

      // Инициализация Choices для взводов
      this.platoonChoices = new Choices(this.$refs.platoonSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите взвод',
        choices: [
          { value: '', label: 'Нет' },
          ...uniquePlatoons.map(p => ({
            value: p.id.toString(),
            label: p.name,
          })),
        ],
      })

      this.isChoicesInitialized = true
      console.log('Choices успешно инициализированы')
    },
    updateSoldiersChoices() {
      if (!this.soldiersChoices) return
      console.log('Обновление списка солдат')
      const uniqueSoldiers = Array.from(new Map(this.soldiers.map(s => [s.id, s])).values())
      const filteredSoldiers = uniqueSoldiers.filter(s => s.id.toString() !== this.currentSquad.commanderId)
      this.soldiersChoices.clearChoices()
      this.soldiersChoices.setChoices(
        filteredSoldiers.map(s => ({
          value: s.id.toString(),
          label: `${s.secondName} ${s.firstName} ${s.fatherName || ''} (${s.rankName || 'Нет звания'})`,
        })),
        'value',
        'label',
        true
      )
      console.log('Список солдат обновлён:', filteredSoldiers)
    },
    destroyChoices() {
      console.log('Очистка Choices')
      if (this.commanderChoices) {
        this.commanderChoices.clearChoices()
        this.commanderChoices.clearStore()
        this.commanderChoices.destroy()
        this.commanderChoices = null
      }
      if (this.soldiersChoices) {
        this.soldiersChoices.clearChoices()
        this.soldiersChoices.clearStore()
        this.soldiersChoices.destroy()
        this.soldiersChoices = null
      }
      if (this.platoonChoices) {
        this.platoonChoices.clearChoices()
        this.platoonChoices.clearStore()
        this.platoonChoices.destroy()
        this.platoonChoices = null
      }
    },
  },
  watch: {
    'currentSquad.commanderId': function() {
      this.updateSoldiersChoices()
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