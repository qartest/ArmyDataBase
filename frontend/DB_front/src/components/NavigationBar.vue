<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/soldiers">Военная База</router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto">
          <li class="nav-item">
            <router-link class="nav-link" to="/complex-queries">Сложные запросы</router-link>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="buildingsDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Здания
            </a>
            <ul class="dropdown-menu" aria-labelledby="buildingsDropdown">
              <li><router-link class="dropdown-item" to="/buildings/building">Здание</router-link></li>
              <li><router-link class="dropdown-item" to="/buildings/headquarter">Штаб</router-link></li>
            </ul>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="soldiersDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Солдаты
            </a>
            <ul class="dropdown-menu" aria-labelledby="soldiersDropdown">
              <li><router-link class="dropdown-item" to="/soldiers">Солдаты</router-link></li>
              <li><router-link class="dropdown-item" to="/soldiers/rank">Ранги</router-link></li>
              <li><router-link class="dropdown-item" to="/soldiers/specialty">Специальности</router-link></li>
            </ul>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="equipmentDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Вооружение
            </a>
            <ul class="dropdown-menu" aria-labelledby="equipmentDropdown">
              <li><router-link class="dropdown-item" to="/equipment-items/antiaircraft">ПВО</router-link></li>
              <li><router-link class="dropdown-item" to="/equipment-items/artillery">Артиллерия</router-link></li>
              <li><router-link class="dropdown-item" to="/equipment-items/missile-weapon">Ракетное оружие</router-link></li>
              <li><router-link class="dropdown-item" to="/equipment-items/infantry-fighting">Боевая машина пехоты</router-link></li>
              <li><router-link class="dropdown-item" to="/equipment-items/tank">Танк</router-link></li>
              <li><router-link class="dropdown-item" to="/equipment-items/truck">Грузовик</router-link></li>
              <li><router-link class="dropdown-item" to="/equipment-items/automatic-weapon">Автоматическое оружие</router-link></li>
              <li><router-link class="dropdown-item" to="/equipment-items/carbine">Карабин</router-link></li>
            </ul>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="equipmentTypesDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Типы вооружения
            </a>
            <ul class="dropdown-menu" aria-labelledby="equipmentTypesDropdown">
              <li><router-link class="dropdown-item" to="/equipment-types/antiaircraft-type">Тип ПВО</router-link></li>
              <li><router-link class="dropdown-item" to="/equipment-types/artillery-type">Тип артиллерии</router-link></li>
              <li><router-link class="dropdown-item" to="/equipment-types/missile-weapon-type">Тип ракетного оружия</router-link></li>
              <li><router-link class="dropdown-item" to="/equipment-types/infantry-fighting-vehicle-type">Тип боевой машины пехоты</router-link></li>
              <li><router-link class="dropdown-item" to="/equipment-types/tank-type">Тип танка</router-link></li>
              <li><router-link class="dropdown-item" to="/equipment-types/truck-type">Тип грузовика</router-link></li>
              <li><router-link class="dropdown-item" to="/equipment-types/automatic-weapon-type">Тип автоматического оружия</router-link></li>
              <li><router-link class="dropdown-item" to="/equipment-types/carbine-type">Тип карабина</router-link></li>
            </ul>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="unitsDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Армейские соединения
            </a>
            <ul class="dropdown-menu" aria-labelledby="unitsDropdown">
              <li><router-link class="dropdown-item" to="/units/army">Армия</router-link></li>
              <li><router-link class="dropdown-item" to="/units/company">Рота</router-link></li>
              <li><router-link class="dropdown-item" to="/units/corps">Корпус</router-link></li>
              <li><router-link class="dropdown-item" to="/units/division">Дивизия</router-link></li>
              <li><router-link class="dropdown-item" to="/units/militaryformation">Воинское соединение</router-link></li>
              <li><router-link class="dropdown-item" to="/units/platoon">Взвод</router-link></li>
              <li><router-link class="dropdown-item" to="/units/squad">Отряд</router-link></li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'NavigationBar',
}
</script>

<style scoped>
.navbar-nav .dropdown-menu {
  background-color: #333;
  border: 1px solid #444;
}
.navbar-nav .dropdown-item {
  color: #fff;
}
.navbar-nav .dropdown-item:hover {
  background-color: #555;
  color: #fff;
}
</style>