<template>
  <div class="container mt-4">
    <h1 class="mb-4">Военные соединения</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="fetchAllData">Обновить/Загрузить список</button>
      <button class="btn btn-success" @click="openCreateModal">Создать</button>
    </div>
    <div v-if="isLoading" class="text-center">Загрузка...</div>
    <table v-else class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Название</th>
          <th>Командир</th>
          <th>Роты</th>
          <th>Дивизия</th>
          <th>Штаб</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="formations.length === 0">
          <td colspan="6" class="text-nowrap text-center">Нет данных</td>
        </tr>
        <tr v-for="formation in formations" :key="formation.id">
          <td>{{ formation.name }}</td>
          <td>
            <span v-if="formation.commanderMinimum">
              {{ formation.commanderMinimum.secondName }} {{ formation.commanderMinimum.firstName }} {{ formation.commanderMinimum.fatherName || '' }} ({{ formation.commanderMinimum.rankName || 'Нет звания' }})
            </span>
            <span v-else>-</span>
          </td>
          <td>
            <ul v-if="formation.companies && formation.companies.length">
              <li v-for="company in formation.companies" :key="company.id">
                {{ company.name }}
              </li>
            </ul>
            <span v-else>-</span>
          </td>
          <td>{{ formation.divisionName || '-' }}</td>
          <td>{{ formation.headquarterName || '-' }}</td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="openEditModal(formation)">Редактировать</button>
            <button class="btn btn-danger btn-sm me-2" @click="deleteFormation(formation)">Удалить</button>
            <button class="btn btn-info btn-sm" @click="showEquipment(formation)">Показать снаряжение</button>
          </td>
        </tr>
      </tbody>
    </table>
    <military-formation-form ref="formationForm" :soldiers="soldiers" :companies="companies" :divisions="divisions" :headquarters="headquarters" @save="saveFormation" />

    <!-- Модальное окно для снаряжения -->
    <div class="modal fade" id="equipmentModal" tabindex="-1" aria-labelledby="equipmentModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="equipmentModalLabel">Снаряжение: {{ selectedFormation?.name }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div v-if="!selectedFormation?.subType?.length" class="text-center">
              Нет данных о снаряжении
            </div>
            <table v-else class="table table-bordered">
              <thead>
                <tr>
                  <th>Тип</th>
                  <th>Подтип</th>
                  <th>Количество</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="type in selectedFormation.subType" :key="type.typeName">
                  <td>{{ type.typeName }}</td>
                  <td>
                    <ul>
                      <li v-for="subType in type.subTypes" :key="subType.name">
                        {{ subType.name }}
                      </li>
                    </ul>
                  </td>
                  <td>
                    <ul>
                      <li v-for="subType in type.subTypes" :key="subType.name">
                        {{ subType.quantity }}
                      </li>
                    </ul>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { Modal } from 'bootstrap'
import MilitaryFormationForm from './MilitaryFormationForm.vue'

export default {
  name: 'MilitaryFormationList',
  components: { MilitaryFormationForm },
  data() {
    return {
      formations: [],
      soldiers: [],
      companies: [],
      divisions: [],
      headquarters: [],
      isLoading: false,
      selectedFormation: null,
      equipmentModal: null,
    }
  },
  mounted() {
    this.fetchAllData()
    this.equipmentModal = new Modal(document.getElementById('equipmentModal'))
  },
  methods: {
    async fetchAllData() {
      this.isLoading = true
      this.formations = []
      this.soldiers = []
      this.companies = []
      this.divisions = []
      this.headquarters = []
      try {
        await Promise.all([
          this.fetchFormations(),
          this.fetchSoldiers(),
          this.fetchCompanies(),
          this.fetchDivisions(),
          this.fetchHeadquarters(),
        ])
      } finally {
        this.isLoading = false
      }
    },
    async fetchFormations() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/militaryFormation/getAll')
        console.log('Полученные данные военных соединений:', response.data)
        this.formations = response.data.map(formation => ({
          ...formation,
          commanderId: formation.commanderMinimum ? formation.commanderMinimum.id : null,
          companiesId: formation.companies ? formation.companies.map(c => c.id) : [],
          divisionId: formation.divisionId || null,
          headquarterId: formation.headquarterId || null,
        }))
        console.log('Обработанные военные соединения:', this.formations)
      } catch (error) {
        console.error('Ошибка загрузки военных соединений:', error)
        alert('Не удалось загрузить список военных соединений: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchSoldiers() {
      try {
        const response = await axios.get('http://localhost:8080/api/soldier/getAll')
        console.log('Полученные данные солдат:', response.data)
        this.soldiers = Array.from(new Map(response.data.map(s => [s.id, s])).values())
        console.log('Уникальные солдаты:', this.soldiers)
      } catch (error) {
        console.error('Ошибка загрузки солдат:', error)
        alert('Не удалось загрузить список солдат: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchCompanies() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/company/getAll/Simple')
        console.log('Полученные данные рот:', response.data)
        this.companies = Array.from(new Map(response.data.map(c => [c.id, c])).values())
        console.log('Уникальные роты:', this.companies)
      } catch (error) {
        console.error('Ошибка загрузки рот:', error)
        alert('Не удалось загрузить список рот: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchDivisions() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/division/getAll/Simple')
        console.log('Полученные данные дивизий:', response.data)
        this.divisions = Array.from(new Map(response.data.map(d => [d.id, d])).values())
        console.log('Уникальные дивизии:', this.divisions)
      } catch (error) {
        console.error('Ошибка загрузки дивизий:', error)
        alert('Не удалось загрузить список дивизий: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchHeadquarters() {
      try {
        const response = await axios.get('http://localhost:8080/api/headquarter/getAll')
        console.log('Полученные данные штабов:', response.data)
        this.headquarters = Array.from(new Map(response.data.map(h => [h.id, h])).values())
        console.log('Уникальные штабы:', this.headquarters)
      } catch (error) {
        console.error('Ошибка загрузки штабов:', error)
        alert('Не удалось загрузить список штабов: ' + (error.response?.data?.message || error.message))
      }
    },
    openCreateModal() {
      this.$refs.formationForm.openModal(false, {})
    },
    openEditModal(formation) {
      console.log('Редактирование военного соединения:', formation)
      this.$refs.formationForm.openModal(true, formation)
    },
    async saveFormation({ formationData, isEditMode }) {
      try {
        console.log('Отправка данных военного соединения:', formationData)
        if (isEditMode) {
          await axios.put(`http://localhost:8080/api/units/militaryFormation/update/${formationData.id}`, formationData)
        } else {
          await axios.post('http://localhost:8080/api/units/militaryFormation/create', formationData)
        }
        this.$refs.formationForm.closeModal()
        await this.fetchAllData()
      } catch (error) {
        console.error('Ошибка сохранения военного соединения:', error)
        alert('Не удалось сохранить военное соединение: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteFormation(formation) {
      if (confirm(`Вы уверены, что хотите удалить военное соединение "${formation.name}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/units/militaryFormation/delete/${formation.id}`)
          await this.fetchAllData()
        } catch (error) {
          console.error('Ошибка удаления военного соединения:', error)
          alert('Не удалось удалить военное соединение: ' + (error.response?.data?.message || error.message))
        }
      }
    },
    showEquipment(formation) {
      this.selectedFormation = formation
      this.equipmentModal.show()
    },
  },
}
</script>

<style scoped>
.table th,
.table td {
  vertical-align: middle;
}
ul {
  margin: 0;
  padding-left: 20px;
}
li {
  margin-bottom: 8px;
}
</style>