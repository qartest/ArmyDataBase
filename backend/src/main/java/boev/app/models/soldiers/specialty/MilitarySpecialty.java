package boev.app.models.soldiers.specialty;

import lombok.Getter;

@Getter
public enum MilitarySpecialty {
    RIFLEMAN("Стрелок"),
    DRIVER("Водитель"),
    SAPPER("Сапёр"),
    SIGNALMAN("Связист"),
    MEDIC("Санитар"),
    GUNNER("Наводчик"),
    SNIPER("Снайпер"),
    MECHANIC("Механик"),
    INSTRUCTOR("Инструктор"),
    SQUAD_LEADER("Командир отделения"),
    STAFF_OFFICER("Штабной офицер");

    private final String displayName;

    MilitarySpecialty(String displayName) {
        this.displayName = displayName;
    }

}