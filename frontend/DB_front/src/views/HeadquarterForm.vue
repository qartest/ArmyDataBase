<template>
  <div class="modal fade" id="headquarterModal" tabindex="-1" aria-labelledby="headquarterModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="headquarterModalLabel">{{ isEditMode ? 'Редактировать штаб' : 'Создать штаб' }}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label for="name" class="form-label">Название</label>
              <input type="text" class="form-control" id="name" v-model="currentHeadquarter.name" required>
            </div>
            <div class="mb-3">
              <label for="address" class="form-label">Адрес</label>
              <input type="text" class="form-control" id="address" v-model="currentHeadquarter.address" required>
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

export default {
  name: 'HeadquarterForm',
  data() {
    return {
      currentHeadquarter: {
        id: null,
        name: '',
        address: '',
      },
      isEditMode: false,
      modal: null,
    }
  },
  mounted() {
    this.modal = new Modal(document.getElementById('headquarterModal'))
  },
  methods: {
    openModal(isEditMode, headquarter) {
      console.log('Открытие модального окна:', { isEditMode, headquarter })
      this.isEditMode = isEditMode
      this.currentHeadquarter = isEditMode
        ? {
            id: headquarter.id,
            name: headquarter.name,
            address: headquarter.address,
          }
        : {
            id: null,
            name: '',
            address: '',
          }
      this.modal.show()
    },
    closeModal() {
      this.modal.hide()
    },
    submitForm() {
      if (!this.currentHeadquarter.name || !this.currentHeadquarter.address) {
        alert('Пожалуйста, заполните все обязательные поля')
        return
      }
      const headquarterData = {
        id: this.currentHeadquarter.id,
        name: this.currentHeadquarter.name,
        address: this.currentHeadquarter.address,
      }
      console.log('Отправка данных в родительский компонент:', headquarterData)
      this.$emit('save', { headquarterData, isEditMode: this.isEditMode })
    },
  },
}
</script>

<style scoped>
/* Стили для формы */
</style>