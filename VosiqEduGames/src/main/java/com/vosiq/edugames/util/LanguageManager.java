package com.vosiq.edugames.util;

import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    private static String currentLang = "uz";

    private static final Map<String, Map<String, String>> translations = new HashMap<>();

    static {
        // === O'ZBEK TILI ===
        Map<String, String> uz = new HashMap<>();
        uz.put("app.title", "Vosiq International School — EduGames");
        uz.put("menu.games", "O'yinlar");
        uz.put("menu.subjects", "Fanlar");
        uz.put("menu.about", "Ilova haqida");
        uz.put("menu.settings", "Sozlamalar");
        uz.put("menu.language", "Til");
        uz.put("menu.back", "Orqaga");
        uz.put("menu.start", "Boshlash");
        uz.put("menu.exit", "Chiqish");
        uz.put("subject.math", "Matematika");
        uz.put("subject.english", "Ingliz tili");
        uz.put("subject.uzbek", "Ona tili");
        uz.put("subject.russian", "Rus tili");
        uz.put("subject.informatics", "Informatika");
        uz.put("subject.geography", "Geografiya");
        uz.put("subject.biology", "Biologiya");
        uz.put("subject.literature", "Adabiyot");
        uz.put("subject.history", "Tarix");
        uz.put("subject.chemistry", "Kimyo");
        uz.put("game.quiz", "Viktorina");
        uz.put("game.truefalse", "To'g'ri / Noto'g'ri");
        uz.put("game.memory", "Xotira o'yini");
        uz.put("game.crossword", "Krossword");
        uz.put("game.flags", "Bayroqlarni toping");
        uz.put("game.tugofwar", "Arqon tortish");
        uz.put("game.colors", "Farqli rangni toping");
        uz.put("game.random", "Tasodifiy o'yin");
        uz.put("game.custom", "O'z ma'lumotlaringizni kiriting");
        uz.put("game.score", "Ball");
        uz.put("game.time", "Vaqt");
        uz.put("game.correct", "To'g'ri!");
        uz.put("game.wrong", "Noto'g'ri!");
        uz.put("game.next", "Keyingisi");
        uz.put("game.finish", "Yakunlash");
        uz.put("game.result", "Natija");
        uz.put("game.playagain", "Qayta o'ynash");
        uz.put("game.choosemode", "Rejimni tanlang");
        uz.put("game.randommode", "Tasodifiy savollar");
        uz.put("game.custommode", "O'z savollaringiz");
        uz.put("about.title", "Ilova haqida");
        uz.put("about.appname", "Vosiq EduGames");
        uz.put("about.version", "Versiya 1.0.0");
        uz.put("about.school", "Vosiq International School");
        uz.put("about.author", "Yaratuvchi: Najmiddinov Sirojiddin");
        uz.put("about.class", "9A sinf o'quvchisi");
        uz.put("about.year", "2025-yil");
        uz.put("about.desc", "Bu ilova o'quvchilar uchun o'yin orqali o'rganish platformasidir. Matematika, til fanlari, tabiiy fanlar va boshqa yo'nalishlar bo'yicha interaktiv o'yinlar mavjud.");
        uz.put("settings.lang.uz", "O'zbek tili");
        uz.put("settings.lang.ru", "Rus tili");
        uz.put("settings.lang.en", "Ingliz tili");
        uz.put("custom.addquestion", "Savol qo'shish");
        uz.put("custom.question", "Savol:");
        uz.put("custom.answer", "Javob:");
        uz.put("custom.wronganswers", "Noto'g'ri javoblar (vergul bilan):");
        uz.put("custom.save", "Saqlash");
        uz.put("custom.play", "O'ynash");
        uz.put("custom.clear", "Tozalash");
        translations.put("uz", uz);

        // === RUS TILI ===
        Map<String, String> ru = new HashMap<>();
        ru.put("app.title", "Vosiq International School — EduGames");
        ru.put("menu.games", "Игры");
        ru.put("menu.subjects", "Предметы");
        ru.put("menu.about", "О приложении");
        ru.put("menu.settings", "Настройки");
        ru.put("menu.language", "Язык");
        ru.put("menu.back", "Назад");
        ru.put("menu.start", "Начать");
        ru.put("menu.exit", "Выход");
        ru.put("subject.math", "Математика");
        ru.put("subject.english", "Английский язык");
        ru.put("subject.uzbek", "Узбекский язык");
        ru.put("subject.russian", "Русский язык");
        ru.put("subject.informatics", "Информатика");
        ru.put("subject.geography", "География");
        ru.put("subject.biology", "Биология");
        ru.put("subject.literature", "Литература");
        ru.put("subject.history", "История");
        ru.put("subject.chemistry", "Химия");
        ru.put("game.quiz", "Викторина");
        ru.put("game.truefalse", "Правда / Ложь");
        ru.put("game.memory", "Игра памяти");
        ru.put("game.crossword", "Кроссворд");
        ru.put("game.flags", "Найди флаг");
        ru.put("game.tugofwar", "Перетягивание каната");
        ru.put("game.colors", "Найди отличный цвет");
        ru.put("game.random", "Случайная игра");
        ru.put("game.custom", "Введите свои данные");
        ru.put("game.score", "Очки");
        ru.put("game.time", "Время");
        ru.put("game.correct", "Правильно!");
        ru.put("game.wrong", "Неправильно!");
        ru.put("game.next", "Следующий");
        ru.put("game.finish", "Завершить");
        ru.put("game.result", "Результат");
        ru.put("game.playagain", "Играть снова");
        ru.put("game.choosemode", "Выберите режим");
        ru.put("game.randommode", "Случайные вопросы");
        ru.put("game.custommode", "Свои вопросы");
        ru.put("about.title", "О приложении");
        ru.put("about.appname", "Vosiq EduGames");
        ru.put("about.version", "Версия 1.0.0");
        ru.put("about.school", "Vosiq International School");
        ru.put("about.author", "Автор: Наджмиддинов Сирожиддин");
        ru.put("about.class", "Ученик 9А класса");
        ru.put("about.year", "2025 год");
        ru.put("about.desc", "Это приложение — платформа обучения через игры для учеников. Доступны интерактивные игры по математике, языкам, естественным наукам и другим предметам.");
        ru.put("settings.lang.uz", "Узбекский");
        ru.put("settings.lang.ru", "Русский");
        ru.put("settings.lang.en", "Английский");
        ru.put("custom.addquestion", "Добавить вопрос");
        ru.put("custom.question", "Вопрос:");
        ru.put("custom.answer", "Ответ:");
        ru.put("custom.wronganswers", "Неверные ответы (через запятую):");
        ru.put("custom.save", "Сохранить");
        ru.put("custom.play", "Играть");
        ru.put("custom.clear", "Очистить");
        translations.put("ru", ru);

        // === INGLIZ TILI ===
        Map<String, String> en = new HashMap<>();
        en.put("app.title", "Vosiq International School — EduGames");
        en.put("menu.games", "Games");
        en.put("menu.subjects", "Subjects");
        en.put("menu.about", "About");
        en.put("menu.settings", "Settings");
        en.put("menu.language", "Language");
        en.put("menu.back", "Back");
        en.put("menu.start", "Start");
        en.put("menu.exit", "Exit");
        en.put("subject.math", "Mathematics");
        en.put("subject.english", "English");
        en.put("subject.uzbek", "Uzbek Language");
        en.put("subject.russian", "Russian Language");
        en.put("subject.informatics", "Computer Science");
        en.put("subject.geography", "Geography");
        en.put("subject.biology", "Biology");
        en.put("subject.literature", "Literature");
        en.put("subject.history", "History");
        en.put("subject.chemistry", "Chemistry");
        en.put("game.quiz", "Quiz");
        en.put("game.truefalse", "True / False");
        en.put("game.memory", "Memory Game");
        en.put("game.crossword", "Crossword");
        en.put("game.flags", "Find the Flag");
        en.put("game.tugofwar", "Tug of War");
        en.put("game.colors", "Find Different Color");
        en.put("game.random", "Random Game");
        en.put("game.custom", "Enter Your Own Data");
        en.put("game.score", "Score");
        en.put("game.time", "Time");
        en.put("game.correct", "Correct!");
        en.put("game.wrong", "Wrong!");
        en.put("game.next", "Next");
        en.put("game.finish", "Finish");
        en.put("game.result", "Result");
        en.put("game.playagain", "Play Again");
        en.put("game.choosemode", "Choose Mode");
        en.put("game.randommode", "Random Questions");
        en.put("game.custommode", "Your Own Questions");
        en.put("about.title", "About");
        en.put("about.appname", "Vosiq EduGames");
        en.put("about.version", "Version 1.0.0");
        en.put("about.school", "Vosiq International School");
        en.put("about.author", "Created by: Najmiddinov Sirojiddin");
        en.put("about.class", "Student of 9A grade");
        en.put("about.year", "Year 2025");
        en.put("about.desc", "This application is a game-based learning platform for students. Interactive games are available in mathematics, languages, natural sciences and other subjects.");
        en.put("settings.lang.uz", "Uzbek");
        en.put("settings.lang.ru", "Russian");
        en.put("settings.lang.en", "English");
        en.put("custom.addquestion", "Add Question");
        en.put("custom.question", "Question:");
        en.put("custom.answer", "Answer:");
        en.put("custom.wronganswers", "Wrong answers (comma separated):");
        en.put("custom.save", "Save");
        en.put("custom.play", "Play");
        en.put("custom.clear", "Clear");
        translations.put("en", en);
    }

    public static void setLanguage(String lang) {
        currentLang = lang;
    }

    public static String getLanguage() {
        return currentLang;
    }

    public static String get(String key) {
        Map<String, String> lang = translations.getOrDefault(currentLang, translations.get("uz"));
        return lang.getOrDefault(key, key);
    }
}
