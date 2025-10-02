package boev.app.models.soldiers;

import lombok.Getter;

@Getter
public enum RussianArmyRank {
    // Рядовые
    PRIVATE(RankCategory.PRIVATE, 1),          // Рядовой
    CORPORAL(RankCategory.PRIVATE, 2),         // Ефрейтор

    // Сержанты и старшины
    JUNIOR_SERGEANT(RankCategory.SERGEANT, 3), // Младший сержант
    SERGEANT(RankCategory.SERGEANT, 4),        // Сержант
    SENIOR_SERGEANT(RankCategory.SERGEANT, 5), // Старший сержант
    STARSHINA(RankCategory.SERGEANT, 6),       // Старшина

    // Прапорщики
    WARRANT_OFFICER(RankCategory.SERGEANT, 7),      // Прапорщик
    SENIOR_WARRANT_OFFICER(RankCategory.SERGEANT, 8), // Старший прапорщик

    // Младшие офицеры
    JUNIOR_LIEUTENANT(RankCategory.OFFICER, 9),  // Младший лейтенант
    LIEUTENANT(RankCategory.OFFICER, 10),        // Лейтенант
    SENIOR_LIEUTENANT(RankCategory.OFFICER, 11), // Старший лейтенант
    CAPTAIN(RankCategory.OFFICER, 12),           // Капитан

    // Старшие офицеры
    MAJOR(RankCategory.OFFICER, 13),             // Майор
    LIEUTENANT_COLONEL(RankCategory.OFFICER, 14), // Подполковник
    COLONEL(RankCategory.OFFICER, 15),           // Полковник

    // Высшие офицеры
    MAJOR_GENERAL(RankCategory.OFFICER, 16),     // Генерал-майор
    LIEUTENANT_GENERAL(RankCategory.OFFICER, 17), // Генерал-лейтенант
    COLONEL_GENERAL(RankCategory.OFFICER, 18),   // Генерал-полковник
    GENERAL_OF_THE_ARMY(RankCategory.OFFICER, 19); // Генерал армии

    private final RankCategory category;
    private final int hierarchyLevel;

    RussianArmyRank(RankCategory category, int hierarchyLevel) {
        this.category = category;
        this.hierarchyLevel = hierarchyLevel;
    }

}
