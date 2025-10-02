package boev.app.models;

import boev.app.models.armaments.instances.Equipment;
import boev.app.models.armaments.instances.fireHelp.Antiaircraft;
import boev.app.models.armaments.instances.fireHelp.Artillery;
import boev.app.models.armaments.instances.fireHelp.MissileWeapon;
import boev.app.models.armaments.instances.technique.InfantryFightingVehicle;
import boev.app.models.armaments.instances.technique.Tank;
import boev.app.models.armaments.instances.technique.Truck;
import boev.app.models.armaments.instances.weapon.AutomaticWeapon;
import boev.app.models.armaments.instances.weapon.Carbine;
import boev.app.models.armaments.type.fireHelp.AntiaircraftType;
import boev.app.models.armaments.type.fireHelp.ArtilleryType;
import boev.app.models.armaments.type.fireHelp.MissileWeaponType;
import boev.app.models.armaments.type.technique.InfantryFightingVehicleType;
import boev.app.models.armaments.type.technique.TankType;
import boev.app.models.armaments.type.technique.TruckType;
import boev.app.models.armaments.type.weapon.AutomaticWeaponType;
import boev.app.models.armaments.type.weapon.CarbineType;
import boev.app.models.builds.Building;
import boev.app.models.builds.Headquarter;
import boev.app.models.soldiers.Rank;
import boev.app.models.soldiers.RussianArmyRank;
import boev.app.models.soldiers.Soldier;
import boev.app.models.soldiers.records.OfficerRecord;
import boev.app.models.soldiers.records.PrivateRecord;
import boev.app.models.soldiers.records.SergeantRecord;
import boev.app.models.soldiers.specialty.MilitarySpecialtyEntity;
import boev.app.models.units.*;
import boev.app.payload.armaments.instances.equipment.EquipmentDto;
import boev.app.payload.armaments.instances.equipment.EquipmentResponse;
import boev.app.payload.armaments.instances.fireHelp.antiaircraft.AntiaircraftDto;
import boev.app.payload.armaments.instances.fireHelp.antiaircraft.AntiaircraftResponse;
import boev.app.payload.armaments.instances.fireHelp.artillery.ArtilleryDto;
import boev.app.payload.armaments.instances.fireHelp.artillery.ArtilleryResponse;
import boev.app.payload.armaments.instances.fireHelp.missleWeapon.MissileWeaponDto;
import boev.app.payload.armaments.instances.fireHelp.missleWeapon.MissileWeaponResponse;
import boev.app.payload.armaments.instances.technique.InfatryFightVehicle.InfantryFightingVehicleDto;
import boev.app.payload.armaments.instances.technique.InfatryFightVehicle.InfantryFightingVehicleResponse;
import boev.app.payload.armaments.instances.technique.Tank.TankDto;
import boev.app.payload.armaments.instances.technique.Tank.TankResponse;
import boev.app.payload.armaments.instances.technique.Truck.TruckDto;
import boev.app.payload.armaments.instances.technique.Truck.TruckResponse;
import boev.app.payload.armaments.instances.weapon.AutomaticWeapon.AutomaticWeaponDto;
import boev.app.payload.armaments.instances.weapon.AutomaticWeapon.AutomaticWeaponResponse;
import boev.app.payload.armaments.instances.weapon.Carbine.CarbineDto;
import boev.app.payload.armaments.instances.weapon.Carbine.CarbineResponse;
import boev.app.payload.armaments.type.fireHelp.antiaircraft.AntiaircraftTypeDto;
import boev.app.payload.armaments.type.fireHelp.antiaircraft.AntiaircraftTypeResponse;
import boev.app.payload.armaments.type.fireHelp.artillery.ArtilleryTypeDto;
import boev.app.payload.armaments.type.fireHelp.artillery.ArtilleryTypeResponse;
import boev.app.payload.armaments.type.fireHelp.missleWeapon.MissileWeaponTypeDto;
import boev.app.payload.armaments.type.fireHelp.missleWeapon.MissileWeaponTypeResponse;
import boev.app.payload.armaments.type.technique.InfatryFightVehicle.InfantryFightingVehicleTypeDto;
import boev.app.payload.armaments.type.technique.InfatryFightVehicle.InfantryFightingVehicleTypeResponse;
import boev.app.payload.armaments.type.technique.Tank.TankTypeDto;
import boev.app.payload.armaments.type.technique.Tank.TankTypeResponse;
import boev.app.payload.armaments.type.technique.Truck.TruckTypeDto;
import boev.app.payload.armaments.type.technique.Truck.TruckTypeResponse;
import boev.app.payload.armaments.type.weapon.AutomaticWeapon.AutomaticWeaponTypeDto;
import boev.app.payload.armaments.type.weapon.AutomaticWeapon.AutomaticWeaponTypeResponse;
import boev.app.payload.armaments.type.weapon.Carbine.CarbineTypeDto;
import boev.app.payload.armaments.type.weapon.Carbine.CarbineTypeResponse;
import boev.app.payload.builds.building.BuildingDto;
import boev.app.payload.builds.building.BuildingResponse;
import boev.app.payload.builds.headquater.HeadquarterDto;
import boev.app.payload.builds.headquater.HeadquarterResponse;
import boev.app.payload.builds.headquater.HeadquarterResponseSimple;
import boev.app.payload.soldiers.militarySpecialty.MilitarySpecialtyDto;
import boev.app.payload.soldiers.militarySpecialty.MilitarySpecialtyResponse;
import boev.app.payload.soldiers.rank.RankDto;
import boev.app.payload.soldiers.rank.RankResponse;
import boev.app.payload.soldiers.records.officer.OfficerRecordDto;
import boev.app.payload.soldiers.records.officer.OfficerRecordResponse;
import boev.app.payload.soldiers.records.privates.PrivateRecordDto;
import boev.app.payload.soldiers.records.privates.PrivateRecordResponse;
import boev.app.payload.soldiers.records.sergeant.SergeantRecordDto;
import boev.app.payload.soldiers.records.sergeant.SergeantRecordResponse;
import boev.app.payload.soldiers.solider.SoldierDto;
import boev.app.payload.soldiers.solider.SoldierMinimumDto;
import boev.app.payload.soldiers.solider.SoldierResponse;
import boev.app.payload.units.army.ArmyDto;
import boev.app.payload.units.army.ArmyResponse;
import boev.app.payload.units.company.CompanyDto;
import boev.app.payload.units.company.CompanyResponse;
import boev.app.payload.units.corp.CorpDto;
import boev.app.payload.units.corp.CorpResponse;
import boev.app.payload.units.division.DivisionDto;
import boev.app.payload.units.division.DivisionResponse;
import boev.app.payload.units.militaryFormation.MilitaryFormationDto;
import boev.app.payload.units.militaryFormation.MilitaryFormationResponse;
import boev.app.payload.units.militaryFormation.MilitaryFormationResponseLight;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.payload.units.platoon.PlatoonDto;
import boev.app.payload.units.platoon.PlatoonResponse;
import boev.app.payload.units.squad.SquadDto;
import boev.app.payload.units.squad.SquadResponse;
import org.mapstruct.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ModelMapper {

    Soldier toSoldier(SoldierDto soldierDto);

    @Mapping(source = "squad.id", target = "squadId")
    @Mapping(source = "commandedUnit.id", target = "militaryUnitId")
    @Mapping(source = "rank.id", target = "rankId")
    @Mapping(source = "specialties", target = "specialtiesId", qualifiedByName = "mapSpecialtiesToIds")
    SoldierResponse toSoldierResponse(Soldier soldier);

    OfficerRecord toOfficerRecord(OfficerRecordDto officerRecordDto);
    @Mapping(source = "rank.id", target = "rankId")
    OfficerRecordResponse toOfficerRecordResponse(OfficerRecord officerRecord);

    PrivateRecord toPrivateRecord(PrivateRecordDto privateRecordDto);
    @Mapping(source = "rank.id", target = "rankId")
    PrivateRecordResponse toPrivateRecordResponse(PrivateRecord privateRecord);

    SergeantRecord toSergeantRecord(SergeantRecordDto sergeantRecordDto);
    @Mapping(source = "rank.id", target = "rankId")
    SergeantRecordResponse toSergeantRecordResponse(SergeantRecord sergeantRecord);

    Rank toRank(RankDto rankDto);
    @Mapping(target = "name", expression = "java(translateRank(rank.getName()))")
    @Mapping(target = "hierarchyLevel", expression = "java(rank.getName().getHierarchyLevel())")
    @Mapping(target = "category", expression = "java(rank.getName().getCategory())")
    RankResponse toRankResponse(Rank rank);

    @Mapping(target = "rankName", expression = "java(translateRank(soldier.getRank().getName()))")
    SoldierMinimumDto toSoldierMinimumDto(Soldier soldier);

    @Mapping(source = "commander", target = "commanderMinimum")
    MilitaryUnitResponse toMilitaryUnitResponse(MilitaryUnit militaryUnit);

    MilitarySpecialtyEntity toMilitarySpecialtyEntity(MilitarySpecialtyDto militarySpecialtyDto);
    @Mapping(source = "name.displayName", target = "name")
    MilitarySpecialtyResponse toMilitarySpecialtyResponse(MilitarySpecialtyEntity militarySpecialtyEntity);

    Building toBuilding(BuildingDto buildingDto);
    @Mapping(source = "headquarter.id", target = "headquarterId")
    @Mapping(source = "headquarter.name", target = "headquarterName")
    BuildingResponse toBuildingResponse(Building building);

    Headquarter toHeadquarter(HeadquarterDto headquarterDto);

    @Mapping(source = "buildings", target = "buildings")
    @Mapping(source = "formations", target = "formations")
    HeadquarterResponse toHeadquarterResponse(Headquarter headquarter);

    HeadquarterResponseSimple toHeadquarterResponseSimple(Headquarter headquarter);

    AutomaticWeaponType toAutomaticWeaponType(AutomaticWeaponTypeDto automaticWeaponTypeDto);
    AutomaticWeaponTypeResponse toAutomaticWeaponTypeResponse(AutomaticWeaponType automaticWeaponType);

    CarbineType toCarbineType(CarbineTypeDto carbineTypeDto);
    CarbineTypeResponse toCarbineTypeResponse(CarbineType carbineType);

    InfantryFightingVehicleType toInfantryFightingVehicleType(InfantryFightingVehicleTypeDto infantryFightingVehicleTypeDto);
    InfantryFightingVehicleTypeResponse toInfantryFightingVehicleTypeResponse(InfantryFightingVehicleType infantryFightingVehicleType);

    TankType toTankType(TankTypeDto tankTypeDto);
    TankTypeResponse toTankTypeResponse(TankType tankType);

    TruckType toTruckType(TruckTypeDto truckTypeDto);
    TruckTypeResponse toTruckTypeResponse(TruckType truckType);

    AntiaircraftType toAntiaircraftType(AntiaircraftTypeDto automaticWeaponTypeDto);
    AntiaircraftTypeResponse toAntiaircraftTypeResponse(AntiaircraftType antiaircraftType);

    ArtilleryType toArtilleryType(ArtilleryTypeDto artilleryTypeDto);
    ArtilleryTypeResponse toArtilleryTypeResponse(ArtilleryType artilleryType);

    MissileWeaponType toMissileWeaponType(MissileWeaponTypeDto missileWeaponTypeDto);
    MissileWeaponTypeResponse toMissileWeaponTypeResponse(MissileWeaponType missileWeaponType);


    Carbine toCarbine(CarbineDto carbineDto);
    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "formation.id", target = "formationId")
    @Mapping(source = "formation.name", target = "formationName")
    CarbineResponse toCarbineResponse(Carbine carbine);

    Artillery toArtillery(ArtilleryDto artilleryDto);
    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "formation.id", target = "formationId")
    @Mapping(source = "formation.name", target = "formationName")
    ArtilleryResponse toArtilleryResponse(Artillery artillery);

    MissileWeapon toMissileWeapon(MissileWeaponDto missileWeaponDto);
    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "formation.id", target = "formationId")
    @Mapping(source = "formation.name", target = "formationName")
    MissileWeaponResponse toMissileWeaponResponse(MissileWeapon missileWeapon);

    InfantryFightingVehicle toInfantryFightingVehicle(InfantryFightingVehicleDto infantryFightVehicleDto);
    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "formation.id", target = "formationId")
    @Mapping(source = "formation.name", target = "formationName")
    InfantryFightingVehicleResponse toInfantryFightingVehicleResponse(InfantryFightingVehicle infantryFightingVehicle);

    Tank toTank(TankDto tankDto);
    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "formation.id", target = "formationId")
    @Mapping(source = "formation.name", target = "formationName")
    TankResponse toTankResponse(Tank tank);

    Truck toTruck(TruckDto truckDto);
    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "formation.id", target = "formationId")
    @Mapping(source = "formation.name", target = "formationName")
    TruckResponse toTruckResponse(Truck truck);

    AutomaticWeapon toAutomaticWeapon(AutomaticWeaponDto automaticWeaponDto);
    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "formation.id", target = "formationId")
    @Mapping(source = "formation.name", target = "formationName")
    AutomaticWeaponResponse toAutomaticWeaponResponse(AutomaticWeapon automaticWeapon);

    Antiaircraft toAntiaircraft(AntiaircraftDto antiaircraftDto);
    @Mapping(source = "type.id", target = "typeId")
    @Mapping(source = "formation.id", target = "formationId")
    @Mapping(source = "formation.name", target = "formationName")
    AntiaircraftResponse toAntiaircraftResponse(Antiaircraft antiaircraft);

    Squad toSquad(SquadDto squadDto);
    @Mapping(source = "platoon.name", target = "platoonName")
    @Mapping(source = "platoon.id", target = "platoonId")
    @Mapping(source = "soldiers", target = "soldiersMinimumDto")
    @Mapping(source = "commander", target = "commanderMinimum")
    SquadResponse toSquadResponse(Squad squad);

    Platoon toPlatoon(PlatoonDto platoonDto);

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    @Mapping(source = "squads", target = "squads")
    @Mapping(source = "commander", target = "commanderMinimum")
    PlatoonResponse toPlatoonResponse(Platoon platoon);

    Company toCompany(CompanyDto companyDto);

    @Mapping(source = "platoons", target = "platoons")
    @Mapping(source = "formation.id", target = "formationId")
    @Mapping(source = "formation.name", target = "formationName")
    @Mapping(source = "commander", target = "commanderMinimum")
    CompanyResponse toCompanyResponse(Company company);

    MilitaryFormation toMilitaryFormation(MilitaryFormationDto militaryFormationDto);

//    @Mapping(source = "division.id", target = "divisionId")
//    @Mapping(source = "companies", target = "companiesId", qualifiedByName = "mapMilitaryUnitToIds")
//    @Mapping(source = "equipment", target = "equipmentsId", qualifiedByName = "mapEquipmentsToIds")
    @Mapping(source = "commander", target = "commanderMinimum")
    @Mapping(source = "division.id", target = "divisionId")
    @Mapping(source = "division.name", target = "divisionName")
    @Mapping(source = "headquarter.name", target = "headquarterName")
    @Mapping(source = "headquarter.id", target = "headquarterId")
    @Mapping(source = "companies", target = "companies")
    MilitaryFormationResponseLight toMilitaryFormationResponseLight(MilitaryFormation militaryFormation);

    Division toDivision(DivisionDto divisionDto);
    @Mapping(source = "corp.id", target = "corpId")
    @Mapping(source = "commander", target = "commanderMinimum")
    @Mapping(source = "corp.name", target = "corpName")
    @Mapping(source = "formations", target = "formations")
    DivisionResponse toDivisionResponse(Division division);


    Corp toCorp(CorpDto corpDto);
    @Mapping(source = "army.id", target = "armyId")
    @Mapping(source = "commander", target = "commanderMinimum")
    @Mapping(source = "army.name", target = "armyName")
    @Mapping(source = "divisions", target = "divisions")
    CorpResponse toCorpResponse(Corp corp);

    Army toArmy(ArmyDto armyDto);
    @Mapping(source = "commander", target = "commanderMinimum")
    @Mapping(source = "corps", target = "corps")
    ArmyResponse toArmyResponse(Army army);


    default String translateRank(RussianArmyRank rank) {
        switch (rank) {
            case PRIVATE:
                return "Рядовой";
            case CORPORAL:
                return "Ефрейтор";
            case JUNIOR_SERGEANT:
                return "Младший сержант";
            case SERGEANT:
                return "Сержант";
            case SENIOR_SERGEANT:
                return "Старший сержант";
            case STARSHINA:
                return "Старшина";
            case WARRANT_OFFICER:
                return "Прапорщик";
            case SENIOR_WARRANT_OFFICER:
                return "Старший прапорщик";
            case JUNIOR_LIEUTENANT:
                return "Младший лейтенант";
            case LIEUTENANT:
                return "Лейтенант";
            case SENIOR_LIEUTENANT:
                return "Старший лейтенант";
            case CAPTAIN:
                return "Капитан";
            case MAJOR:
                return "Майор";
            case LIEUTENANT_COLONEL:
                return "Подполковник";
            case COLONEL:
                return "Полковник";
            case MAJOR_GENERAL:
                return "Генерал-майор";
            case LIEUTENANT_GENERAL:
                return "Генерал-лейтенант";
            case COLONEL_GENERAL:
                return "Генерал-полковник";
            case GENERAL_OF_THE_ARMY:
                return "Генерал армии";
            default:
                return rank.name();
        }
    }

    @Named("mapEquipmentsToIds")
    default List<Long> mapEquipmentsToIds(List<Equipment> equipment){
        if(equipment == null){
            return null;
        }
        return equipment.stream().filter(Objects::nonNull).map(Equipment::getId).collect(Collectors.toList());
    }

    @Named("mapMilitaryUnitToIds")
    default List<Long> mapMilitaryUnitToIds(List<? extends MilitaryUnit> militaryUnits){
        if(militaryUnits == null){
            return null;
        }
        return militaryUnits.stream().filter(Objects::nonNull).map(MilitaryUnit::getId).collect(Collectors.toList());
    }

    @Named("mapSoldiersToIds")
    default List<Long> mapSoldiersToIds(List<Soldier> soldiers){
        if(soldiers == null){
            return null;
        }
        return soldiers.stream().map(Soldier::getId).collect(Collectors.toList());
    }
    @Named("mapFormationsToIds")
    default List<Long> mapFormationsToIds(List<MilitaryFormation> formations) {
        if (formations == null) {
            return null;
        }
        return formations.stream()
                .map(MilitaryFormation::getId)
                .collect(Collectors.toList());
    }

    @Named("mapSpecialtiesToIds")
    default List<Long> mapSpecialtiesToIds(Set<MilitarySpecialtyEntity> specialties) {
        if (specialties == null) {
            return null;
        }
        return specialties.stream()
                .map(MilitarySpecialtyEntity::getId)
                .collect(Collectors.toList());
    }

    @Named("mapBuildingsToIds")
    default List<Long> mapBuildingsToIds(List<Building> buildings) {
        if (buildings == null) {
            return null;
        }
        return buildings.stream()
                .map(Building::getId)
                .collect(Collectors.toList());
    }


}
