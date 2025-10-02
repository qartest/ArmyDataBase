<template>
  <div class="modal fade" id="specialtyModal" tabindex="-1" aria-labelledby="specialtyModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="specialtyModalLabel">Создать специальность</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label for="name" class="form-label">Название</label>
              <select class="form-select" id="name" ref="nameSelect" v-model="currentSpecialty.name" required></select>
            </div>
            <button type="submit" class="btn btn-primary">Создать</button>
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
  name: 'SpecialtyForm',
  data() {
    return {
      currentSpecialty: {
        name: null,
      },
      modal: null,
      nameChoices: null,
      isChoicesInitialized: false,
    }
  },
  mounted() {
    this.modal = new Modal(document.getElementById('specialtyModal'))
  },
  methods: {
    openModal() {
      console.log('Открытие модального окна')
      this.currentSpecialty = { name: null }
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
      if (!this.currentSpecialty.name) {
        alert('Пожалуйста, выберите специальность')
        return
      }
      const specialtyData = {
        name: this.currentSpecialty.name,
      }
      console.log('Отправка данных в родительский компонент:', specialtyData)
      this.$emit('save', specialtyData)
    },
    initChoices() {
      if (this.isChoicesInitialized) {
        console.log('Choices уже инициализированы, пропускаем')
        return
      }
      console.log('Инициализация Choices')
      this.$refs.nameSelect.innerHTML = ''
      this.destroyChoices()

      // Список специальностей из перечисления MilitarySpecialty
      const specialtyOptions = [
        { value: 'RIFLEMAN', label: 'Стрелок' },
        { value: 'DRIVER', label: 'Водитель' },
        { value: 'SAPPER', label: 'Сапёр' },
        { value: 'SIGNALMAN', label: 'Связист' },
        { value: 'MEDIC', label: 'Санитар' },
        { value: 'GUNNER', label: 'Наводчик' },
        { value: 'SNIPER', label: 'Снайпер' },
        { value: 'MECHANIC', label: 'Механик' },
        { value: 'INSTRUCTOR', label: 'Инструктор' },
        { value: 'SQUAD_LEADER', label: 'Командир отделения' },
        { value: 'STAFF_OFFICER', label: 'Штабной офицер' },
      ]

      this.nameChoices = new Choices(this.$refs.nameSelect, {
        searchEnabled: true,
        itemSelectText: '',
        placeholderValue: 'Выберите специальность',
        choices: specialtyOptions,
      })

      this.isChoicesInitialized = true
      console.log('Choices успешно инициализированы')
    },
    destroyChoices() {
      console.log('Очистка Choices')
      if (this.nameChoices) {
        this.nameChoices.clearChoices()
        this.nameChoices.clearStore()
        this.nameChoices.destroy()
        this.nameChoices = null
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