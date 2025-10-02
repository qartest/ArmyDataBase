package boev.app.util;

import boev.app.models.soldiers.RussianArmyRank;

public class SwitchName {
    public static String translateRussianArmyRank(RussianArmyRank rank){
        return switch (rank) {
            case PRIVATE -> "Рядовой";
            case CORPORAL -> "Ефрейтор";
            case JUNIOR_SERGEANT -> "Младший сержант";
            case SERGEANT -> "Сержант";
            case SENIOR_SERGEANT -> "Старший сержант";
            case STARSHINA -> "Старшина";
            case WARRANT_OFFICER -> "Прапорщик";
            case SENIOR_WARRANT_OFFICER -> "Старший прапорщик";
            case JUNIOR_LIEUTENANT -> "Младший лейтенант";
            case LIEUTENANT -> "Лейтенант";
            case SENIOR_LIEUTENANT -> "Старший лейтенант";
            case CAPTAIN -> "Капитан";
            case MAJOR -> "Майор";
            case LIEUTENANT_COLONEL -> "Подполковник";
            case COLONEL -> "Полковник";
            case MAJOR_GENERAL -> "Генерал-майор";
            case LIEUTENANT_GENERAL -> "Генерал-лейтенант";
            case COLONEL_GENERAL -> "Генерал-полковник";
            case GENERAL_OF_THE_ARMY -> "Генерал армии";
            default -> "Неизвестное звание";
        };
    }
}
