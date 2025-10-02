package boev.app.services.impl.units;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.armaments.EquipmentCategory;
import boev.app.models.armaments.instances.Equipment;
import boev.app.models.armaments.instances.fireHelp.Antiaircraft;
import boev.app.models.armaments.instances.fireHelp.Artillery;
import boev.app.models.armaments.instances.fireHelp.MissileWeapon;
import boev.app.models.armaments.instances.technique.InfantryFightingVehicle;
import boev.app.models.armaments.instances.technique.Tank;
import boev.app.models.armaments.instances.technique.Truck;
import boev.app.models.armaments.instances.weapon.AutomaticWeapon;
import boev.app.models.armaments.instances.weapon.Carbine;
import boev.app.models.builds.Headquarter;
import boev.app.models.soldiers.Soldier;
import boev.app.models.units.Company;
import boev.app.models.units.Division;
import boev.app.models.units.MilitaryFormation;
import boev.app.models.units.MilitaryUnit;
import boev.app.payload.units.militaryFormation.*;
import boev.app.payload.units.militaryunit.MilitaryUnitResponse;
import boev.app.repository.armaments.instatnces.*;
import boev.app.repository.armaments.type.*;
import boev.app.repository.builds.HeadquarterRepository;
import boev.app.repository.soliders.SolderRepository;
import boev.app.repository.units.CompanyRepository;
import boev.app.repository.units.DivisionRepository;
import boev.app.repository.units.MilitaryFormationRepository;
import boev.app.repository.units.MilitaryUnitRepository;
import boev.app.services.interfaces.units.MilitaryFormationService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MilitaryFormationServiceImpl implements MilitaryFormationService {
    private final MilitaryFormationRepository militaryFormationRepository;
    private final ModelMapper modelMapper;
    private final DivisionRepository divisionRepository;
    private final CompanyRepository companyRepository;
    private final HeadquarterRepository headquarterRepository;
    private final SolderRepository solderRepository;
    private final EquipmentRepository equipmentRepository;
    private final MilitaryUnitRepository militaryUnitRepository;

    private final ArtilleryTypeRepository artilleryTypeRepository;
    private final AntiaircraftTypeRepository antiaircraftTypeRepository;
    private final MissileWeaponTypeRepository missileWeaponTypeRepository;
    private final InfantryFightingVehicleTypeRepository infantryFightingVehicleTypeRepository;
    private final TankTypeRepository tankTypeRepository;
    private final TruckTypeRepository truckTypeRepository;
    private final AutomaticWeaponTypeRepository automaticWeaponTypeRepository;
    private final CarbineTypeRepository carbineTypeRepository;

    private final ArtilleryRepository artilleryRepository;
    private final AntiaircraftRepository antiaircraftRepository;
    private final MissileWeaponRepository missileWeaponRepository;
    private final InfantryFightingVehicleRepository infantryFightingVehicleRepository;
    private final TankRepository tankRepository;
    private final TruckRepository truckRepository;
    private final AutomaticWeaponRepository automaticWeaponRepository;
    private final CarbineRepository carbineRepository;

    @Transactional
    public List<MilitaryUnitResponse> getAllSimple() {
        return militaryFormationRepository.findAll().stream().map(modelMapper::toMilitaryUnitResponse).collect(Collectors.toList());
    }

    @Transactional
    public List<MilitaryFormationResponseLight> getAll() {
        return militaryFormationRepository.findAll().stream().map(this::tr).collect(Collectors.toUnmodifiableList());
    }


    @Transactional
    public MilitaryUnitResponse delete(long id) {
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(id).orElseThrow(() -> new Error404("Formation not found"));


        if(militaryFormation.getCompanies() != null){
            for(Company company : militaryFormation.getCompanies()){
                company.setFormation(null);
                try{
                    companyRepository.save(company);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }
        if(militaryFormation.getCommander()!= null){
            Soldier soldier = militaryFormation.getCommander();
            soldier.setCommandedUnit(null);
            try{
                solderRepository.save(soldier);
            } catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        if(militaryFormation.getDivision() != null){
            Division division = militaryFormation.getDivision();
            division.getFormations().removeIf(s -> s.getId().equals(id));
            try{
                divisionRepository.save(division);
            }catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        if(militaryFormation.getHeadquarter() != null){
            Headquarter headquarter = militaryFormation.getHeadquarter();
            headquarter.getFormations().removeIf(s -> s.getId().equals(id));

            try{
                headquarterRepository.save(headquarter);
            }catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        if(militaryFormation.getEquipment() != null){
            for(Equipment equipment : militaryFormation.getEquipment()){
                equipment.setFormation(null);

                try{
                    equipmentRepository.save(equipment);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }
        MilitaryUnitResponse militaryFormationResponse = modelMapper.toMilitaryUnitResponse(militaryFormation);
        militaryFormationRepository.deleteById(id);
        return militaryFormationResponse;
    }

    @Transactional
    public MilitaryUnitResponse create(MilitaryFormationDto militaryFormationDto) {
        MilitaryFormation militaryFormation = modelMapper.toMilitaryFormation(militaryFormationDto);

        militaryFormation.setEquipment(new ArrayList<>());
        militaryFormation.setCompanies(new ArrayList<>());

        Soldier soldier = null;
        if(militaryFormationDto.getCommanderId() != null){
            soldier = solderRepository.findById(militaryFormationDto.getCommanderId()).orElseThrow(() -> new Error404("Soldier not found"));
            if(soldier.getCommandedUnit() != null){
                MilitaryUnit militaryUnit = soldier.getCommandedUnit();
                militaryUnit.setCommander(null);
                soldier.setCommandedUnit(null);
                try{
                    militaryUnitRepository.saveAndFlush(militaryUnit);
                    solderRepository.saveAndFlush(soldier);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }
        militaryFormation.setCommander(soldier);

        Headquarter headquarter = headquarterRepository.findById(militaryFormationDto.getHeadquarterId()).orElseThrow(() -> new Error404("Headquarter not found"));
        if(headquarter != null){
            headquarter.getFormations().add(militaryFormation);
            try{
                headquarterRepository.saveAndFlush(headquarter);
            } catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }
        militaryFormation.setHeadquarter(headquarter);

        Division division = null;
        if(militaryFormationDto.getDivisionId() != null){
            division = divisionRepository.findById(militaryFormationDto.getDivisionId()).orElseThrow(() -> new Error404("Division not found"));
        }
        militaryFormation.setDivision(division);

        if(militaryFormationDto.getCompaniesId() != null){
            for(Long ids : militaryFormationDto.getCompaniesId()){
                Company company = companyRepository.findById(ids).orElseThrow(() -> new Error404("Company not found"));
                company.setFormation(militaryFormation);
                militaryFormation.getCompanies().add(company);

                try{
                    companyRepository.save(company);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        if(militaryFormationDto.getEquipmentsId() != null){
            for(Long ids : militaryFormationDto.getEquipmentsId()){
                Equipment equipment = equipmentRepository.findById(ids).orElseThrow(() -> new Error404("Equipment not found"));
                equipment.setFormation(militaryFormation);
                militaryFormation.getEquipment().add(equipment);
                try{
                    equipmentRepository.save(equipment);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        try{
            militaryFormation = militaryFormationRepository.saveAndFlush(militaryFormation);
            if(soldier != null){
                soldier.setCommandedUnit(militaryFormation);
                solderRepository.saveAndFlush(soldier);
            }
            return modelMapper.toMilitaryUnitResponse(militaryFormation);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }

    @Transactional
    public MilitaryUnitResponse update(MilitaryFormationDto militaryFormationDto, long id) {
        MilitaryFormation militaryFormation = militaryFormationRepository.findById(id).orElseThrow(() -> new Error404("Formation not found"));

        for(Equipment equipment : militaryFormation.getEquipment()){
            equipment.setFormation(null);
            try{
                equipmentRepository.saveAndFlush(equipment);
            }catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        for(Company company : militaryFormation.getCompanies()){
            company.setFormation(null);

            try{
                companyRepository.saveAndFlush(company);
            }catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }

        militaryFormation.setEquipment(new ArrayList<>());
        militaryFormation.setCompanies(new ArrayList<>());


        Soldier soldier = null;
        if(militaryFormationDto.getCommanderId() != null){
            soldier = solderRepository.findById(militaryFormationDto.getCommanderId()).orElseThrow(() -> new Error404("Soldier not found"));
            if(soldier.getCommandedUnit() != null && !soldier.getCommandedUnit().getId().equals(id)){
                MilitaryUnit militaryUnit = soldier.getCommandedUnit();
                militaryUnit.setCommander(null);
                soldier.setCommandedUnit(null);
                try{
                    militaryUnitRepository.saveAndFlush(militaryUnit);
                    solderRepository.saveAndFlush(soldier);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }
        militaryFormation.setCommander(soldier);
        if(soldier != null){
            soldier.setCommandedUnit(militaryFormation);
            try{
                solderRepository.saveAndFlush(soldier);
            }catch (DataIntegrityViolationException e) {
                System.out.println("DOG");
                throw new Error500();
            }
        }

        Headquarter headquarter = headquarterRepository.findById(militaryFormationDto.getHeadquarterId()).orElseThrow(() -> new Error404("Headquarter not found"));
        if(headquarter != null){
            headquarter.getFormations().add(militaryFormation);
            try{
                headquarterRepository.saveAndFlush(headquarter);
            } catch (DataIntegrityViolationException e) {
                throw new Error500();
            }
        }
        militaryFormation.setHeadquarter(headquarter);

        Division division = null;
        if(militaryFormationDto.getDivisionId() != null){
            division = divisionRepository.findById(militaryFormationDto.getDivisionId()).orElseThrow(() -> new Error404("Division not found"));
        }
        militaryFormation.setDivision(division);

        if(militaryFormationDto.getCompaniesId() != null){
            for(Long ids : militaryFormationDto.getCompaniesId()){
                Company company = companyRepository.findById(ids).orElseThrow(() -> new Error404("Company not found"));
                company.setFormation(militaryFormation);
                militaryFormation.getCompanies().add(company);

                try{
                    companyRepository.save(company);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        if(militaryFormationDto.getEquipmentsId() != null){
            for(Long ids : militaryFormationDto.getEquipmentsId()){
                Equipment equipment = equipmentRepository.findById(ids).orElseThrow(() -> new Error404("Equipment not found"));
                equipment.setFormation(militaryFormation);
                militaryFormation.getEquipment().add(equipment);
                try{
                    equipmentRepository.save(equipment);
                }catch (DataIntegrityViolationException e) {
                    throw new Error500();
                }
            }
        }

        militaryFormation.setName(militaryFormationDto.getName());

        try{
            militaryFormation = militaryFormationRepository.save(militaryFormation);
            return modelMapper.toMilitaryUnitResponse(militaryFormation);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }

    private MilitaryFormationResponseLight tr(MilitaryFormation militaryFormation){
        MilitaryFormationResponseLight militaryFormationResponseLight = modelMapper.toMilitaryFormationResponseLight(militaryFormation);
        militaryFormationResponseLight.setSubType(new ArrayList<>());

//        militaryFormationResponseLight.getSubType().add(new EquipmentType(EquipmentCategory.ARTILLERY.name(), new ArrayList<>()));
//        militaryFormationResponseLight.getSubType().add(new EquipmentType(EquipmentCategory.AUTOMATIC_WEAPON.name(), new ArrayList<>()));


        List<Equipment> equipmentList = militaryFormation.getEquipment();

        Map<EquipmentCategory, List<Equipment>> groupedByCategory = equipmentList.stream()
                .collect(Collectors.groupingBy(Equipment::getCategory));

        // Формируем equipmentTypes для всех категорий EquipmentCategory
        for (EquipmentCategory category : EquipmentCategory.values()) {
            List<Equipment> items = groupedByCategory.getOrDefault(category, Collections.emptyList());
            // Группируем по имени подтипа (model) внутри категории
            Map<String, List<Equipment>> groupedBySubType = items.stream()
                    .collect(Collectors.groupingBy(this::getNameEquipments));

            List<EquipmentSubType> subTypes = groupedBySubType.entrySet().stream()
                    .map(entry -> {
                        String subTypeName = entry.getKey();
                        List<Equipment> subTypeItems = entry.getValue();
                        // Суммируем количество и берем первый ID
                        long quantity = subTypeItems.size();
                        return new EquipmentSubType(subTypeName, quantity);
                    })
                    .collect(Collectors.toList());

            militaryFormationResponseLight.getSubType().add(new EquipmentType(category.name(), subTypes));
        }
        return militaryFormationResponseLight;
    }

    private String getNameEquipments(Equipment equipment) {
        Long equipmentId = equipment.getId();
        switch (equipment.getCategory()) {
            case TANK -> {
                Tank tank = tankRepository.findById(equipmentId)
                        .orElseThrow(() -> new Error404("Tank not found"));
                return tankTypeRepository.findById(tank.getType().getId())
                        .orElseThrow(() -> new Error404("TankType not found"))
                        .getModel();
            }
            case INFANTRY_FIGHTING_VEHICLE -> {
                InfantryFightingVehicle ifv = infantryFightingVehicleRepository.findById(equipmentId)
                        .orElseThrow(() -> new Error404("InfantryFightingVehicle not found"));
                return infantryFightingVehicleTypeRepository.findById(ifv.getType().getId())
                        .orElseThrow(() -> new Error404("InfantryFightingVehicleType not found"))
                        .getModel();
            }
            case TRUCK -> {
                Truck truck = truckRepository.findById(equipmentId)
                        .orElseThrow(() -> new Error404("Truck not found"));
                return truckTypeRepository.findById(truck.getType().getId())
                        .orElseThrow(() -> new Error404("TruckType not found"))
                        .getModel();
            }
            case ARTILLERY -> {
                Artillery artillery = artilleryRepository.findById(equipmentId)
                        .orElseThrow(() -> new Error404("Artillery not found"));
                return artilleryTypeRepository.findById(artillery.getType().getId())
                        .orElseThrow(() -> new Error404("ArtilleryType not found"))
                        .getModel();
            }
            case ANTIAIRCRAFT -> {
                Antiaircraft antiaircraft = antiaircraftRepository.findById(equipmentId)
                        .orElseThrow(() -> new Error404("Antiaircraft not found"));
                return antiaircraftTypeRepository.findById(antiaircraft.getType().getId())
                        .orElseThrow(() -> new Error404("AntiaircraftType not found"))
                        .getModel();
            }
            case MISSILE_WEAPON -> {
                MissileWeapon missileWeapon = missileWeaponRepository.findById(equipmentId)
                        .orElseThrow(() -> new Error404("MissileWeapon not found"));
                return missileWeaponTypeRepository.findById(missileWeapon.getType().getId())
                        .orElseThrow(() -> new Error404("MissileWeaponType not found"))
                        .getModel();
            }
            case AUTOMATIC_WEAPON -> {
                AutomaticWeapon automaticWeapon = automaticWeaponRepository.findById(equipmentId)
                        .orElseThrow(() -> new Error404("AutomaticWeapon not found"));
                return automaticWeaponTypeRepository.findById(automaticWeapon.getType().getId())
                        .orElseThrow(() -> new Error404("AutomaticWeaponType not found"))
                        .getModel();
            }
            case CARBINE -> {
                Carbine carbine = carbineRepository.findById(equipmentId)
                        .orElseThrow(() -> new Error404("Carbine not found"));
                return carbineTypeRepository.findById(carbine.getType().getId())
                        .orElseThrow(() -> new Error404("CarbineType not found"))
                        .getModel();
            }
            default -> throw new IllegalStateException("Unknown equipment category: " + equipment.getCategory());
        }
    }
}
