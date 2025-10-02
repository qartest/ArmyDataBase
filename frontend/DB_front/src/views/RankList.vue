<template>
  <div class="container mt-4">
    <h1 class="mb-4">Звание</h1>
    <div class="mb-3">
      <button class="btn btn-primary me-2" @click="refreshList">Обновить</button>
      <button class="btn btn-success" @click="openCreateModal">Создать</button>
    </div>
    <table class="table table-bordered table-hover">
      <thead class="table-light">
        <tr>
          <th>Название</th>
          <th>Категория</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="ranks.length === 0">
          <td colspan="3" class="text-center">Нет данных</td>
        </tr>
        <tr v-for="rank in ranks" :key="rank.id">
          <td>{{ formatRankName(rank.name) }}</td>
          <td>{{ formatCategory(rank.category) }}</td>
          <td>
            <button class="btn btn-warning btn-sm me-2" @click="openEditModal(rank)">Изменить</button>
            <button class="btn btn-danger btn-sm" @click="deleteRank(rank)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Модальное окно для создания/редактирования -->
    <div class="modal fade" id="rankModal" tabindex="-1" aria-labelledby="rankModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="rankModalLabel">{{ isEditMode ? 'Редактировать звание' : 'Создать звание' }}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveRank">
              <div class="mb-3">
                <label for="name" class="form-label">Название</label>
                <select class="form-select" id="name" v-model="currentRank.name" required>
                  <option v-for="rank in russianArmyRanks" :key="rank" :value="rank">
                    {{ formatRankName(rank) }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label for="category" class="form-label">Категория</label>
                <select class="form-select" id="category" v-model="currentRank.category" required>
                  <option v-for="category in rankCategories" :key="category" :value="category">
                    {{ formatCategory(category) }}
                  </option>
                </select>
              </div>
              <button type="submit" class="btn btn-primary">{{ isEditMode ? 'Сохранить' : 'Создать' }}</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { Modal } from 'bootstrap'

export default {
  name: 'RankList',
  data() {
    return {
      ranks: [],
      currentRank: {
        id: null,
        name: null,
        category: null,
      },
      isEditMode: false,
      modal: null,
      russianArmyRanks: [
        'PRIVATE', 'CORPORAL', 'JUNIOR_SERGEANT', 'SERGEANT', 'SENIOR_SERGEANT', 'STARSHINA',
        'WARRANT_OFFICER', 'SENIOR_WARRANT_OFFICER', 'JUNIOR_LIEUTENANT', 'LIEUTENANT',
        'SENIOR_LIEUTENANT', 'CAPTAIN', 'MAJOR', 'LIEUTENANT_COLONEL', 'COLONEL',
        'MAJOR_GENERAL', 'LIEUTENANT_GENERAL', 'COLONEL_GENERAL', 'GENERAL_OF_THE_ARMY'
      ],
      rankCategories: ['PRIVATE', 'SERGEANT', 'OFFICER'],
    }
  },
  mounted() {
    this.fetchRanks()
    this.modal = new Modal(document.getElementById('rankModal'))
  },
  methods: {
    async fetchRanks() {
      try {
        const response = await axios.get('http://localhost:8080/api/rank/getAll')
        // Сортировка по hierarchyLevel (предполагаем, что порядок в russianArmyRanks соответствует hierarchyLevel)
        this.ranks = response.data.sort((a, b) => 
          this.russianArmyRanks.indexOf(a.name) - this.russianArmyRanks.indexOf(b.name))
      } catch (error) {
        console.error('Ошибка загрузки званий:', error)
        alert('Не удалось загрузить список званий: ' + error.message)
      }
    },
    refreshList() {
      this.fetchRanks()
    },
    openCreateModal() {
      this.isEditMode = false
      this.currentRank = { id: null, name: null, category: null }
      this.modal.show()
    },
    openEditModal(rank) {
      this.isEditMode = true
      this.currentRank = { ...rank }
      this.modal.show()
    },
    async saveRank() {
      try {
        if (this.isEditMode) {
          await axios.put(`http://localhost:8080/api/rank/update/${this.currentRank.id}`, {
            name: this.currentRank.name,
            category: this.currentRank.category,
          })
        } else {
          await axios.post('http://localhost:8080/api/rank/create', {
            name: this.currentRank.name,
            category: this.currentRank.category,
          })
        }
        this.modal.hide()
        this.fetchRanks()
      } catch (error) {
        console.error('Ошибка сохранения звания:', error)
        alert('Не удалось сохранить звание: ' + error.message)
      }
    },
    async deleteRank(rank) {
      if (confirm(`Вы уверены, что хотите удалить звание "${this.formatRankName(rank.name)}"?`)) {
        try {
          await axios.delete(`http://localhost:8080/api/rank/delete/${rank.id}`)
          this.fetchRanks()
        } catch (error) {
          console.error('Ошибка удаления звания:', error)
          alert('Не удалось удалить звание: ' + error.message)
        }
      }
    },
    formatRankName(name) {
      const rankNames = {
        PRIVATE: 'Рядовой',
        CORPORAL: 'Ефрейтор',
        JUNIOR_SERGEANT: 'Младший сержант',
        SERGEANT: 'Сержант',
        SENIOR_SERGEANT: 'Старший сержант',
        STARSHINA: 'Старшина',
        WARRANT_OFFICER: 'Прапорщик',
        SENIOR_WARRANT_OFFICER: 'Старший прапорщик',
        JUNIOR_LIEUTENANT: 'Младший лейтенант',
        LIEUTENANT: 'Лейтенант',
        SENIOR_LIEUTENANT: 'Старший лейтенант',
        CAPTAIN: 'Капитан',
        MAJOR: 'Майор',
        LIEUTENANT_COLONEL: 'Подполковник',
        COLONEL: 'Полковник',
        MAJOR_GENERAL: 'Генерал-майор',
        LIEUTENANT_GENERAL: 'Генерал-лейтенант',
        COLONEL_GENERAL: 'Генерал-полковник',
        GENERAL_OF_THE_ARMY: 'Генерал армии',
      }
      return rankNames[name] || name
    },
    formatCategory(category) {
      const categoryNames = {
        PRIVATE: 'Рядовой',
        SERGEANT: 'Сержант',
        OFFICER: 'Офицер',
      }
      return categoryNames[category] || category
    },
  },
}
</script>

<style scoped>
/* Дополнительные стили */
</style>