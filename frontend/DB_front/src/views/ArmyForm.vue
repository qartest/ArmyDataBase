<template>
  <div class="modal fade" id="armyModal" tabindex="-1" aria-labelledby="armyModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="armyModalLabel">{{ isEditMode ? 'Редактировать армию' : 'Создать армию' }}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label for="name" class="form-label">Название</label>
              <input type="text" class="form-control" id="name" v-model="currentArmy.name" required>
            </div>
            <div class="mb-3">
              <label for="commanderId" class="form-label">Командир</label>
              <select class="form-select" id="commanderId" ref="commanderSelect" v-model="currentArmy.commanderId"></select>
            </div>
            <div class="mb-3">
              <label for="corpsId" class="form-label">Корпуса</label>
              <select class="form-select" id="corpsId" ref="corpsSelect" multiple v-model="currentArmy.corpsId"></select>
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
  name: 'ArmyForm',
  props: {
    soldiers: {
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
      currentArmy: {
        id: null,
        name: '',
        commanderId: null,
        corpsId: [],
      },
      isEditMode: false,
      modal: null,
      commanderChoices: null,
      corpsChoices: null,
      isChoicesInitialized: false,
    }
  },
  mounted() {
    this.modal = new Modal(document.getElementById('armyModal'))
  },
  methods: {
    openModal(isEditMode, army) {
      console.log('Открытие модального окна:', { isEditMode, army })
      this.isEditMode = isEditMode
      this.currentArmy = isEditMode
        ? {
            id: army.id,
            name: army.name,
            commanderId: army.commanderId || null,
            corpsId: army.corpsId ? [...army.corpsId] : [],
          }
        : {
            id: null,
            name: '',
            commanderId: null,
            corpsId: [],
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
      if (!this.currentArmy.name) {
        alert('Пожалуйста, заполните обязательное поле: Название')
        return
      }
      const armyData = {
        id: this.currentArmy.id,
        name: this.currentArmy.name,
        commanderId: this.currentArmy.commanderId ? parseInt(this.currentArmy.commanderId) : null,
        corpsId: this.currentArmy.corpsId.length ? this.currentArmy.corpsId.map(id => parseInt(id)) : null,
      }
      console.log('Отправка данных в родительский компонент:', armyData)
      this.$emit('save', { armyData, isEditMode: this.isEditMode })
    },
    initChoices() {
      if (this.isChoicesInitialized) {
        console.log('Choices уже инициализированы, пропускаем')
        return
      }
      console.log('Инициализация Choices')
      // Очищаем DOM-элементы select
      this.$refs.commanderSelect.innerHTML = ''
      this.$refs.corpsSelect.innerHTML = ''
      // Удаляем дубли в soldiers и corps
      const uniqueSoldiers = Array.from(new Map(this.soldiers.map(s => [s.id, s])).values())
      const uniqueCorps = Array.from(new Map(this.corps.map(c => [c.id, c])).values())
      console.log('Уникальные солдаты:', uniqueSoldiers)
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

      // Инициализация Choices для корпусов
      this.corpsChoices = new Choices(this.$refs.corpsSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите корпуса',
        removeItemButton: true,
        choices: uniqueCorps.map(c => ({
          value: c.id.toString(),
          label: c.name,
          selected: this.currentArmy.corpsId.includes(c.id.toString()),
        })),
      })

      // Синхронизация с v-model
      this.corpsChoices.setChoiceByValue(this.currentArmy.corpsId.map(id => id.toString()))

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
      if (this.corpsChoices) {
        this.corpsChoices.clearChoices()
        this.corpsChoices.clearStore()
        this.corpsChoices.destroy()
        this.corpsChoices = null
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