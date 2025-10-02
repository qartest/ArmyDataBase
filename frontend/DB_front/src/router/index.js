import { createRouter, createWebHistory } from 'vue-router'
import EntityPage from '../views/EntityPage.vue'
import AntiaircraftList from '../views/AntiaircraftList.vue'
import ArtilleryList from '../views/ArtilleryList.vue'
import MissileWeapon from '../views/MissileWeapon.vue'
import InfantryFightingVehicle from '../views/InfantryFightingVehicle.vue'
import Tank from '../views/Tank.vue'
import Truck from '../views/Truck.vue'
import AutomaticWeapon from '../views/AutomaticWeapon.vue' // Новый импорт
import Carbine from '../views/Carbine.vue' // Новый импорт
import AntiaircraftTypeList from '../views/AntiaircraftTypeList.vue'
import ArtilleryTypeList from '../views/ArtilleryTypeList.vue'
import MissileWeaponTypeList from '../views/MissileWeaponTypeList.vue'
import InfantryFightingVehicleTypeList from '../views/InfantryFightingVehicleTypeList.vue'
import TankTypeList from '../views/TankTypeList.vue'
import TruckTypeList from '../views/TruckTypeList.vue'
import AutomaticWeaponTypeList from '../views/AutomaticWeaponTypeList.vue'
import CarbineTypeList from '../views/CarbineTypeList.vue'
import RankList from '../views/RankList.vue'
import SoldierList from '../views/SoldierList.vue'
import SquadList from '../views/SquadList.vue'
import PlatoonList from '../views/PlatoonList.vue'
import CompanyList from '../views/CompanyList.vue'
import MilitaryFormationList from '../views/MilitaryFormationList.vue'
import HeadquarterList from '../views/HeadquarterList.vue'
import BuildingList from '../views/BuildingList.vue'
import DivisionList from '../views/DivisionList.vue'
import CorpList from '../views/CorpList.vue'
import ArmyList from '../views/ArmyList.vue'
import SpecialtyList from '../views/SpecialtyList.vue'

const routes = [
  { path: '/', redirect: '/soldiers' },
  { path: '/complex-queries', component: EntityPage, props: { entity: 'Сложные запросы' } },
  { path: '/buildings/building', component: BuildingList },
  { path: '/buildings/headquarter', component: HeadquarterList },
  { path: '/soldiers/rank', component: RankList },
  { path: '/soldiers', component: SoldierList },
  { path: '/soldiers/specialty', component: SpecialtyList },
  { path: '/equipment-items/antiaircraft', component: AntiaircraftList },
  { path: '/equipment-items/artillery', component: ArtilleryList },
  { path: '/equipment-items/missile-weapon', component: MissileWeapon },
  { path: '/equipment-items/infantry-fighting', component: InfantryFightingVehicle },
  { path: '/equipment-items/tank', component: Tank },
  { path: '/equipment-items/truck', component: Truck },
  { path: '/equipment-items/automatic-weapon', component: AutomaticWeapon }, // Обновленный маршрут
  { path: '/equipment-items/carbine', component: Carbine }, // Обновленный маршрут
  { path: '/equipment-types/antiaircraft-type', component: AntiaircraftTypeList },
  { path: '/equipment-types/artillery-type', component: ArtilleryTypeList },
  { path: '/equipment-types/missile-weapon-type', component: MissileWeaponTypeList },
  { path: '/equipment-types/infantry-fighting-vehicle-type', component: InfantryFightingVehicleTypeList },
  { path: '/equipment-types/tank-type', component: TankTypeList },
  { path: '/equipment-types/truck-type', component: TruckTypeList },
  { path: '/equipment-types/automatic-weapon-type', component: AutomaticWeaponTypeList },
  { path: '/equipment-types/carbine-type', component: CarbineTypeList },
  { path: '/military-units', component: EntityPage, props: { entity: 'Армейские соединения' } },
  { path: '/units/army', component: ArmyList },
  { path: '/units/company', component: CompanyList },
  { path: '/units/corps', component: CorpList },
  { path: '/units/division', component: DivisionList },
  { path: '/units/militaryformation', component: MilitaryFormationList },
  { path: '/units/platoon', component: PlatoonList },
  { path: '/units/squad', component: SquadList },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router