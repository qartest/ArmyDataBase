<template>
  <div class="modal fade" id="soldierModal" tabindex="-1" aria-labelledby="soldierModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="soldierModalLabel">{{ isEditMode ? 'Редактировать солдата' : 'Создать солдата' }}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label for="secondName" class="form-label">Фамилия</label>
              <input type="text" class="form-control" id="secondName" v-model="currentSoldier.secondName" required>
            </div>
            <div class="mb-3">
              <label for="firstName" class="form-label">Имя</label>
              <input type="text" class="form-control" id="firstName" v-model="currentSoldier.firstName" required>
            </div>
            <div class="mb-3">
              <label for="fatherName" class="form-label">Отчество</label>
              <input type="text" class="form-control" id="fatherName" v-model="currentSoldier.fatherName">
            </div>
            <div class="mb-3">
              <label for="birthDay" class="form-label">Дата рождения</label>
              <input type="date" class="form-control" id="birthDay" v-model="currentSoldier.birthDay" required>
            </div>
            <div class="mb-3">
              <label for="rankId" class="form-label">Звание</label>
              <select class="form-select" id="rankId" ref="rankSelect" v-model="currentSoldier.rankId" required></select>
            </div>
            <div class="mb-3">
              <label for="squadId" class="form-label">Отряд</label>
              <select class="form-select" id="squadId" ref="squadSelect" v-model="currentSoldier.squadId"></select>
            </div>
            <div class="mb-3">
              <label for="MilitaryUnitId" class="form-label">Подкомандная часть</label>
              <select class="form-select" id="MilitaryUnitId" ref="militaryUnitSelect" v-model="currentSoldier.MilitaryUnitId"></select>
            </div>
            <div class="mb-3">
              <label for="specialtiesId" class="form-label">Специальности</label>
              <select class="form-select" id="specialtiesId" ref="specialtiesSelect" multiple v-model="currentSoldier.specialtiesId"></select>
            </div>

            <!-- Рядовые записи -->
            <div class="mb-3">
              <label class="form-label">Рядовые записи</label>
              <div v-for="(record, index) in currentSoldier.privateRecords" :key="'private-' + index">
                <div class="card mb-2">
                  <div class="card-body">
                    <div class="row">
                      <div class="col-md-4">
                        <label :for="'privateRank-' + index" class="form-label">Звание</label>
                        <select class="form-control" :id="'privateRank-' + index" v-model="record.rankId">
                          <option :value="null">Нет</option>
                          <option v-for="rank in ranks" :key="rank.id" :value="rank.id">
                            {{ rank.name }}
                          </option>
                        </select>
                      </div>
                      <div class="col-md-4">
                        <label :for="'privateStart-' + index" class="form-label">Начало</label>
                        <input type="date" class="form-control" :id="'privateStart-' + index" v-model="record.startDate">
                      </div>
                      <div class="col-md-4">
                        <label :for="'privateEnd-' + index" class="form-label">Конец</label>
                        <input type="date" class="form-control" :id="'privateEnd-' + index" v-model="record.endDate">
                      </div>
                    </div>
                    <button type="button" class="btn btn-danger btn-sm mt-2" @click="removePrivateRecord(index)">Удалить</button>
                  </div>
                </div>
              </div>
              <button type="button" class="btn btn-secondary mb-3" @click="addPrivateRecord">Добавить рядовую запись</button>
            </div>

            <!-- Сержантские записи -->
            <div class="mb-3">
              <label class="form-label">Сержантские записи</label>
              <div v-for="(record, index) in currentSoldier.sergeantRecords" :key="'sergeant-' + index">
                <div class="card mb-2">
                  <div class="card-body">
                    <div class="row">
                      <div class="col-md-3">
                        <label :for="'sergeantRank-' + index" class="form-label">Звание</label>
                        <select class="form-control" :id="'sergeantRank-' + index" v-model="record.rankId">
                          <option :value="null">Нет</option>
                          <option v-for="rank in ranks" :key="rank.id" :value="rank.id">
                            {{ rank.name }}
                          </option>
                        </select>
                      </div>
                      <div class="col-md-3">
                        <label :for="'sergeantStart-' + index" class="form-label">Начало</label>
                        <input type="date" class="form-control" :id="'sergeantStart-' + index" v-model="record.startDate">
                      </div>
                      <div class="col-md-3">
                        <label :for="'sergeantEnd-' + index" class="form-label">Конец</label>
                        <input type="date" class="form-control" :id="'sergeantEnd-' + index" v-model="record.endDate">
                      </div>
                      <div class="col-md-3">
                        <label :for="'sergeantTraining-' + index" class="form-label">Обучение</label>
                        <input type="date" class="form-control" :id="'sergeantTraining-' + index" v-model="record.leadershipTrainingDate">
                      </div>
                    </div>
                    <button type="button" class="btn btn-danger btn-sm mt-2" @click="removeSergeantRecord(index)">Удалить</button>
                  </div>
                </div>
              </div>
              <button type="button" class="btn btn-secondary mb-3" @click="addSergeantRecord">Добавить сержантскую запись</button>
            </div>

            <!-- Офицерские записи -->
            <div class="mb-3">
              <label class="form-label">Офицерские записи</label>
              <div v-for="(record, index) in currentSoldier.officerRecords" :key="'officer-' + index">
                <div class="card mb-2">
                  <div class="card-body">
                    <div class="row">
                      <div class="col-md-3">
                        <label :for="'officerRank-' + index" class="form-label">Звание</label>
                        <select class="form-control" :id="'officerRank-' + index" v-model="record.rankId">
                          <option :value="null">Нет</option>
                          <option v-for="rank in ranks" :key="rank.id" :value="rank.id">
                            {{ rank.name }}
                          </option>
                        </select>
                      </div>
                      <div class="col-md-3">
                        <label :for="'officerStart-' + index" class="form-label">Начало</label>
                        <input type="date" class="form-control" :id="'officerStart-' + index" v-model="record.startDate">
                      </div>
                      <div class="col-md-3">
                        <label :for="'officerEnd-' + index" class="form-label">Конец</label>
                        <input type="date" class="form-control" :id="'officerEnd-' + index" v-model="record.endDate">
                      </div>
                      <div class="col-md-3">
                        <label :for="'officerTraining-' + index" class="form-label">Обучение</label>
                        <input type="date" class="form-control" :id="'officerTraining-' + index" v-model="record.leadershipTrainingDate">
                      </div>
                    </div>
                    <div class="row mt-2">
                      <div class="col-md-12">
                        <label :for="'officerAcademy-' + index" class="form-label">Академия</label>
                        <input type="text" class="form-control" :id="'officerAcademy-' + index" v-model="record.academyName">
                      </div>
                    </div>
                    <button type="button" class="btn btn-danger btn-sm mt-2" @click="removeOfficerRecord(index)">Удалить</button>
                  </div>
                </div>
              </div>
              <button type="button" class="btn btn-secondary mb-3" @click="addOfficerRecord">Добавить офицерскую запись</button>
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
import axios from 'axios'

export default {
  name: 'SoldierForm',
  props: {
    ranks: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      currentSoldier: {
        id: null,
        firstName: '',
        secondName: '',
        fatherName: null,
        birthDay: null,
        specialtiesId: [],
        squadId: null,
        MilitaryUnitId: null,
        rankId: null,
        privateRecords: [],
        sergeantRecords: [],
        officerRecords: [],
      },
      isEditMode: false,
      modal: null,
      squads: [],
      militaryUnits: [],
      specialties: [],
      rankChoices: null,
      squadChoices: null,
      militaryUnitChoices: null,
      specialtiesChoices: null,
      isChoicesInitialized: false,
    }
  },
  mounted() {
    this.modal = new Modal(document.getElementById('soldierModal'))
    this.fetchSquads()
    this.fetchMilitaryUnits()
    this.fetchSpecialties()
  },
  methods: {
    async fetchSquads() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/squad/getAll/Simple')
        console.log('Полученные данные отрядов:', response.data)
        this.squads = [
          { id: null, name: 'Нет' },
          ...response.data.map(s => ({
            id: s.id,
            name: s.name,
          })),
        ]
        console.log('Обработанные отряды:', this.squads)
        if (this.isEditMode && this.currentSoldier.squadId) {
          this.$nextTick(() => this.initChoices())
        }
      } catch (error) {
        console.error('Ошибка загрузки отрядов:', error)
        alert('Не удалось загрузить список отрядов: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchMilitaryUnits() {
      try {
        const response = await axios.get('http://localhost:8080/api/help/getAllFormations')
        console.log('Полученные данные подкомандных частей:', response.data)
        this.militaryUnits = [
          { id: null, name: 'Нет' },
          ...response.data.map(u => ({
            id: u.id,
            name: u.name,
          })),
        ]
        console.log('Обработанные подкомандные части:', this.militaryUnits)
        if (this.isEditMode && this.currentSoldier.MilitaryUnitId) {
          this.$nextTick(() => this.initChoices())
        }
      } catch (error) {
        console.error('Ошибка загрузки подкомандных частей:', error)
        alert('Не удалось загрузить список подкомандных частей: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchSpecialties() {
      try {
        const response = await axios.get('http://localhost:8080/api/specialty/getAll')
        console.log('Полученные данные специальностей:', response.data)
        this.specialties = response.data.map(s => ({
          id: s.id,
          name: s.name,
        }))
        console.log('Обработанные специальности:', this.specialties)
        if (this.isEditMode && this.currentSoldier.specialtiesId.length) {
          this.$nextTick(() => this.initChoices())
        }
      } catch (error) {
        console.error('Ошибка загрузки специальностей:', error)
        alert('Не удалось загрузить список специальностей: ' + (error.response?.data?.message || error.message))
      }
    },
    openModal(isEditMode, soldier) {
      console.log('Открытие модального окна:', { isEditMode, soldier })
      this.isEditMode = isEditMode
      this.currentSoldier = isEditMode
        ? {
            id: soldier.id,
            firstName: soldier.firstName || '',
            secondName: soldier.secondName || '',
            fatherName: soldier.fatherName || null,
            birthDay: soldier.birthDay ? soldier.birthDay.split('T')[0] : null,
            specialtiesId: soldier.specialtyId ? soldier.specialtyId.map(id => id.toString()) : [],
            squadId: soldier.squadId || null,
            MilitaryUnitId: soldier.commandedUnitId || null,
            rankId: soldier.rankID || null,
            privateRecords: soldier.privateRecords ? soldier.privateRecords.map(record => ({
              rankId: record.rankId || null,
              startDate: record.startDate ? record.startDate.split('T')[0] : null,
              endDate: record.endDate ? record.endDate.split('T')[0] : null,
            })) : [],
            sergeantRecords: soldier.sergeantRecords ? soldier.sergeantRecords.map(record => ({
              rankId: record.rankId || null,
              startDate: record.startDate ? record.startDate.split('T')[0] : null,
              endDate: record.endDate ? record.endDate.split('T')[0] : null,
              leadershipTrainingDate: record.leadershipTrainingDate ? record.leadershipTrainingDate.split('T')[0] : null,
            })) : [],
            officerRecords: soldier.officerRecords ? soldier.officerRecords.map(record => ({
              rankId: record.rankId || null,
              startDate: record.startDate ? record.startDate.split('T')[0] : null,
              endDate: record.endDate ? record.endDate.split('T')[0] : null,
              leadershipTrainingDate: record.leadershipTrainingDate ? record.leadershipTrainingDate.split('T')[0] : null,
              academyName: record.academyName || null,
            })) : [],
          }
        : {
            id: null,
            firstName: '',
            secondName: '',
            fatherName: null,
            birthDay: null,
            specialtiesId: [],
            squadId: null,
            MilitaryUnitId: null,
            rankId: null,
            privateRecords: [],
            sergeantRecords: [],
            officerRecords: [],
          }
      this.isChoicesInitialized = false
      this.$nextTick(() => this.initChoices())
      this.modal.show()
    },
    closeModal() {
      this.modal.hide()
      this.destroyChoices()
      this.isChoicesInitialized = false
    },
    submitForm() {
      if (!this.currentSoldier.firstName || !this.currentSoldier.secondName || !this.currentSoldier.birthDay || !this.currentSoldier.rankId) {
        alert('Пожалуйста, заполните обязательные поля: Фамилия, Имя, Дата рождения, Звание')
        return
      }
      const soldierData = {
        id: this.currentSoldier.id,
        firstName: this.currentSoldier.firstName,
        secondName: this.currentSoldier.secondName,
        fatherName: this.currentSoldier.fatherName || null,
        birthDay: this.currentSoldier.birthDay,
        specialtiesId: this.currentSoldier.specialtiesId.length ? this.currentSoldier.specialtiesId.map(id => parseInt(id)) : null,
        squadId: this.currentSoldier.squadId ? parseInt(this.currentSoldier.squadId) : null,
        commandedUnitId: this.currentSoldier.MilitaryUnitId ? parseInt(this.currentSoldier.MilitaryUnitId) : null,
        rankId: this.currentSoldier.rankId ? parseInt(this.currentSoldier.rankId) : null,
        privateRecords: this.currentSoldier.privateRecords.map(record => ({
          rankId: record.rankId ? parseInt(record.rankId) : null,
          startDate: record.startDate || null,
          endDate: record.endDate || null,
        })),
        sergeantRecords: this.currentSoldier.sergeantRecords.map(record => ({
          rankId: record.rankId ? parseInt(record.rankId) : null,
          startDate: record.startDate || null,
          endDate: record.endDate || null,
          leadershipTrainingDate: record.leadershipTrainingDate || null,
        })),
        officerRecords: this.currentSoldier.officerRecords.map(record => ({
          rankId: record.rankId ? parseInt(record.rankId) : null,
          startDate: record.startDate || null,
          endDate: record.endDate || null,
          leadershipTrainingDate: record.leadershipTrainingDate || null,
          academyName: record.academyName || null,
        })),
      }
      console.log('Отправка данных солдата:', soldierData)
      this.$emit('save', { soldierData, isEditMode: this.isEditMode })
      this.closeModal()
    },
    initChoices() {
      if (this.isChoicesInitialized) {
        console.log('Choices уже инициализированы, пропускаем')
        return
      }
      console.log('Инициализация Choices')
      this.$nextTick(() => {
        // Очищаем DOM-элементы select
        this.$refs.rankSelect.innerHTML = ''
        this.$refs.squadSelect.innerHTML = ''
        this.$refs.militaryUnitSelect.innerHTML = ''
        this.$refs.specialtiesSelect.innerHTML = ''

        // Удаляем существующие Choices
        this.destroyChoices()

        // Инициализация Choices для звания
        this.rankChoices = new Choices(this.$refs.rankSelect, {
          searchEnabled: true,
          itemSelectText: '',
          placeholderValue: 'Выберите звание',
          searchFields: ['label'],
          choices: this.ranks.map(r => ({
            value: r.id.toString(),
            label: r.name,
          })),
        })

        // Инициализация Choices для отряда
        this.squadChoices = new Choices(this.$refs.squadSelect, {
          searchEnabled: true,
          itemSelectText: '',
          placeholderValue: 'Выберите отряд',
          choices: this.squads.map(s => ({
            value: s.id === null ? '' : s.id.toString(),
            label: s.name,
          })),
        })

        // Инициализация Choices для подкомандной части
        this.militaryUnitChoices = new Choices(this.$refs.militaryUnitSelect, {
          searchEnabled: true,
          itemSelectText: '',
          placeholderValue: 'Выберите подкомандную часть',
          choices: this.militaryUnits.map(u => ({
            value: u.id === null ? '' : u.id.toString(),
            label: u.name,
          })),
        })

        // Инициализация Choices для специальностей
        this.specialtiesChoices = new Choices(this.$refs.specialtiesSelect, {
          searchEnabled: true,
          itemSelectText: '',
          placeholderValue: 'Выберите специальности',
          removeItemButton: true,
          choices: this.specialties.map(s => ({
            value: s.id.toString(),
            label: s.name,
            selected: this.currentSoldier.specialtiesId.includes(s.id.toString()),
          })),
        })

        // Синхронизация с v-model
        this.rankChoices.setChoiceByValue(this.currentSoldier.rankId ? this.currentSoldier.rankId.toString() : '')
        this.squadChoices.setChoiceByValue(this.currentSoldier.squadId ? this.currentSoldier.squadId.toString() : '')
        this.militaryUnitChoices.setChoiceByValue(this.currentSoldier.MilitaryUnitId ? this.currentSoldier.MilitaryUnitId.toString() : '')
        this.specialtiesChoices.setChoiceByValue(this.currentSoldier.specialtiesId.map(id => id.toString()))

        this.isChoicesInitialized = true
        console.log('Choices успешно инициализированы')
      })
    },
    destroyChoices() {
      console.log('Очистка Choices')
      if (this.rankChoices) {
        this.rankChoices.clearChoices()
        this.rankChoices.clearStore()
        this.rankChoices.destroy()
        this.rankChoices = null
      }
      if (this.squadChoices) {
        this.squadChoices.clearChoices()
        this.squadChoices.clearStore()
        this.squadChoices.destroy()
        this.squadChoices = null
      }
      if (this.militaryUnitChoices) {
        this.militaryUnitChoices.clearChoices()
        this.militaryUnitChoices.clearStore()
        this.militaryUnitChoices.destroy()
        this.militaryUnitChoices = null
      }
      if (this.specialtiesChoices) {
        this.specialtiesChoices.clearChoices()
        this.specialtiesChoices.clearStore()
        this.specialtiesChoices.destroy()
        this.specialtiesChoices = null
      }
    },
    addPrivateRecord() {
      this.currentSoldier.privateRecords.push({
        rankId: null,
        startDate: null,
        endDate: null,
      })
    },
    removePrivateRecord(index) {
      this.currentSoldier.privateRecords.splice(index, 1)
    },
    addSergeantRecord() {
      this.currentSoldier.sergeantRecords.push({
        rankId: null,
        startDate: null,
        endDate: null,
        leadershipTrainingDate: null,
      })
    },
    removeSergeantRecord(index) {
      this.currentSoldier.sergeantRecords.splice(index, 1)
    },
    addOfficerRecord() {
      this.currentSoldier.officerRecords.push({
        rankId: null,
        startDate: null,
        endDate: null,
        leadershipTrainingDate: null,
        academyName: null,
      })
    },
    removeOfficerRecord(index) {
      this.currentSoldier.officerRecords.splice(index, 1)
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
.card-body {
  padding: 1rem;
}
</style>