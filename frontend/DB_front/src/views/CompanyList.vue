<template>
  <div class="container mt-4">
    <h1 class="mb-4">Роты</h1>
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
          <th>Взводы</th>
          <th>Военное соединение</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="companies.length === 0">
          <td colspan="5" class="text-nowrap text-center">Нет данных</td>
        </tr>
        <tr v-for="company in companies" :key="company.id">
          <td>{{ company.name }}</td>
          <td>
            <span v-if="company.commanderMinimum">
              {{ company.commanderMinimum.secondName }} {{ company.commanderMinimum.firstName }} {{ company.commanderMinimum.fatherName || '' }} ({{ company.commanderMinimum.rankName || 'Нет звания' }})
            </span>
            <span v-else>-</span>
          </td>
          <td>
            <ul v-if="company.platoons && company.platoons.length">
              <li v-for="platoon in company.platoons" :key="platoon.id">
                {{ platoon.name }}
              </li>
            </ul>
            <span v-else>-</span>
          </td>
          <td>{{ company.formationName || '-' }}</td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="openEditModal(company)">Редактировать</button>
            <button class="btn btn-danger btn-sm" @click="deleteCompany(company)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>
    <company-form ref="companyForm" :soldiers="soldiers" :platoons="platoons" :formations="formations" @save="saveCompany" />
  </div>
</template>

<script>
import axios from 'axios'
import CompanyForm from './CompanyForm.vue'

export default {
  name: 'CompanyList',
  components: { CompanyForm },
  data() {
    return {
      companies: [],
      soldiers: [],
      platoons: [],
      formations: [],
      isLoading: false,
    }
  },
  mounted() {
    this.fetchAllData()
  },
  methods: {
    async fetchAllData() {
      this.isLoading = true
      this.companies = []
      this.soldiers = []
      this.platoons = []
      this.formations = []
      try {
        await Promise.all([
          this.fetchCompanies(),
          this.fetchSoldiers(),
          this.fetchPlatoons(),
          this.fetchFormations(),
        ])
      } finally {
        this.isLoading = false
      }
    },
    async fetchCompanies() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/company/getAll')
        console.log('Полученные данные рот:', response.data)
        this.companies = response.data.map(company => ({
          ...company,
          commanderId: company.commanderMinimum ? company.commanderMinimum.id : null,
          platoonsId: company.platoons ? company.platoons.map(p => p.id) : [],
          formationId: company.formationId || null,
        }))
        console.log('Обработанные роты:', this.companies)
      } catch (error) {
        console.error('Ошибка загрузки рот:', error)
        alert('Не удалось загрузить список рот: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchSoldiers() {
      try {
        const response = await axios.get('http://localhost:8080/api/soldier/getAll')
        console.log('Полученные данные солдат:', response.data)
        this.soldiers = Array.from(new Map(response.data.map(s => [s.id, s])).values())
        console.log('Уникальные солдаты:', this.soldiers)
        console.log('Данные officerRecords для солдат:', this.soldiers.map(s => ({
          id: s.id,
          officerRecords: s.officerRecords
        })))
      } catch (error) {
        console.error('Ошибка загрузки солдат:', error)
        alert('Не удалось загрузить список солдат: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchPlatoons() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/platoon/getAll/Simple')
        console.log('Полученные данные взводов:', response.data)
        this.platoons = Array.from(new Map(response.data.map(p => [p.id, p])).values())
        console.log('Уникальные взводы:', this.platoons)
      } catch (error) {
        console.error('Ошибка загрузки взводов:', error)
        alert('Не удалось загрузить список взводов: ' + (error.response?.data?.message || error.message))
      }
    },
    async fetchFormations() {
      try {
        const response = await axios.get('http://localhost:8080/api/units/militaryFormation/getAll/Simple')
        console.log('Полученные данные военных соединений:', response.data)
        this.formations = Array.from(new Map(response.data.map(f => [f.id, f])).values())
        console.log('Уникальные военные соединения:', this.formations)
      } catch (error) {
        console.error('Ошибка загрузки военных соединений:', error)
        alert('Не удалось загрузить список военных соединений: ' + (error.response?.data?.message || error.message))
      }
    },
    openCreateModal() {
      this.$refs.companyForm.openModal(false, {})
    },
    openEditModal(company) {
      console.log('Редактирование роты:', company)
      this.$refs.companyForm.openModal(true, company)
    },
    async saveCompany({ companyData, isEditMode }) {
      try {
        console.log('Отправка данных роты:', companyData)
        if (isEditMode) {
          await axios.put(`http://localhost:8080/api/units/company/update/${companyData.id}`, companyData)
        } else {
          await axios.post('http://localhost:8080/api/units/company/create', companyData)
        }
        this.$refs.companyForm.closeModal()
        await this.fetchAllData()
      } catch (error) {
        console.error('Ошибка сохранения роты:', error)
        alert('Не удалось сохранить роту: ' + (error.response?.data?.message || error.message))
      }
    },
    async deleteCompany(company) {
      if (confirm(`Вы уверены, что хотите удалить роту "${company.name}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/units/company/delete/${company.id}`)
          await this.fetchAllData()
        } catch (error) {
          console.error('Ошибка удаления роты:', error)
          alert('Не удалось удалить роту: ' + (error.response?.data?.message || error.message))
        }
      }
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