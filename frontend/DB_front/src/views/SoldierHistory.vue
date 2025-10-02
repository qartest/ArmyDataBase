<template>
  <div class="offcanvas offcanvas-start" tabindex="-1" id="historyOffcanvas" aria-labelledby="historyOffcanvasLabel">
    <div class="offcanvas-header">
      <h5 class="offcanvas-title" id="historyOffcanvasLabel">История записей: {{ currentSoldier.secondName }} {{ currentSoldier.firstName }}</h5>
      <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
    </div>
    <div class="offcanvas-body">
      <h6>Рядовые записи</h6>
      <ul v-if="currentSoldier.privateRecords && currentSoldier.privateRecords.length">
        <li v-for="record in currentSoldier.privateRecords" :key="record.id">
          Звание: {{ record.rankId || '-' }}, Начало: {{ formatDate(record.startDate) }}, Конец: {{ formatDate(record.endDate) }}
        </li>
      </ul>
      <p v-else>Нет записей</p>

      <h6>Сержантские записи</h6>
      <ul v-if="currentSoldier.sergeantRecords && currentSoldier.sergeantRecords.length">
        <li v-for="record in currentSoldier.sergeantRecords" :key="record.id">
          Звание: {{ record.rankId || '-' }}, Начало: {{ formatDate(record.startDate) }}, Конец: {{ formatDate(record.endDate) }},
          Обучение: {{ formatDate(record.leadershipTrainingDate) }}
        </li>
      </ul>
      <p v-else>Нет записей</p>

      <h6>Офицерские записи</h6>
      <ul v-if="currentSoldier.officerRecords && currentSoldier.officerRecords.length">
        <li v-for="record in currentSoldier.officerRecords" :key="record.id">
          Звание: {{ record.rankId || '-' }}, Начало: {{ formatDate(record.startDate) }}, Конец: {{ formatDate(record.endDate) }},
          Обучение: {{ formatDate(record.leadershipTrainingDate) }}, Академия: {{ record.academyName || '-' }}
        </li>
      </ul>
      <p v-else>Нет записей</p>
    </div>
  </div>
</template>

<script>
import { Offcanvas } from 'bootstrap'

export default {
  name: 'SoldierHistory',
  data() {
    return {
      currentSoldier: {
        secondName: '',
        firstName: '',
        privateRecords: [],
        sergeantRecords: [],
        officerRecords: [],
      },
      offcanvas: null,
    }
  },
  mounted() {
    this.offcanvas = new Offcanvas(document.getElementById('historyOffcanvas'))
  },
  methods: {
    open(soldier) {
      this.currentSoldier = { ...soldier }
      this.offcanvas.show()
    },
    formatDate(date) {
      return date ? new Date(date).toLocaleDateString('ru-RU') : '-'
    },
  },
}
</script>

<style scoped>
.offcanvas-body h6 {
  margin-top: 1rem;
}
</style>