<!-- src/views/EntityPage.vue -->
<template>
  <div class="container mt-4">
    <h1 class="mb-4">{{ entity }}</h1>
    <button class="btn btn-primary mb-3" @click="addItem">Добавить элемент</button>
    <p>Содержимое страницы для {{ entity }} будет добавлено позже.</p>
  </div>
</template>

<script>
export default {
  name: 'EntityPage',
  props: {
    entity: {
      type: String,
      required: true,
    },
  },
  methods: {
    addItem() {
      alert(`Добавление нового элемента для ${this.entity}`)
      // Здесь будет логика открытия формы
    },
  },
}
</script>

<style scoped>
/* Дополнительные стили, если нужны */
</style>