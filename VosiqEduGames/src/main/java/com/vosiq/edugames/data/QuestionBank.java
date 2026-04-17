package com.vosiq.edugames.data;

import com.vosiq.edugames.model.Question;
import java.util.*;

public class QuestionBank {

    private static final Map<String, List<Question>> questionsBySubject = new HashMap<>();

    static {
        // ===== MATEMATIKA =====
        List<Question> math = new ArrayList<>();
        math.add(new Question("2 + 2 = ?", "4", Arrays.asList("3", "5", "6"), "math"));
        math.add(new Question("15 × 3 = ?", "45", Arrays.asList("35", "40", "50"), "math"));
        math.add(new Question("100 ÷ 4 = ?", "25", Arrays.asList("20", "30", "24"), "math"));
        math.add(new Question("√144 = ?", "12", Arrays.asList("11", "13", "14"), "math"));
        math.add(new Question("7² = ?", "49", Arrays.asList("14", "42", "56"), "math"));
        math.add(new Question("3³ = ?", "27", Arrays.asList("9", "18", "30"), "math"));
        math.add(new Question("Uchburchak burchaklari yig'indisi?", "180°", Arrays.asList("90°", "360°", "270°"), "math"));
        math.add(new Question("Doira yuzasi formulasi?", "πr²", Arrays.asList("2πr", "πd", "r²"), "math"));
        math.add(new Question("0.5 × 0.5 = ?", "0.25", Arrays.asList("0.5", "1", "0.1"), "math"));
        math.add(new Question("Tub son qaysi?", "7", Arrays.asList("4", "6", "9"), "math"));
        math.add(new Question("(-3) × (-4) = ?", "12", Arrays.asList("-12", "-7", "7"), "math"));
        math.add(new Question("1000 ning 10% i = ?", "100", Arrays.asList("10", "50", "200"), "math"));
        math.add(new Question("5! (faktorial) = ?", "120", Arrays.asList("25", "60", "100"), "math"));
        math.add(new Question("log₁₀(100) = ?", "2", Arrays.asList("1", "10", "3"), "math"));
        math.add(new Question("sin(90°) = ?", "1", Arrays.asList("0", "-1", "0.5"), "math"));
        questionsBySubject.put("math", math);

        // ===== INGLIZ TILI =====
        List<Question> english = new ArrayList<>();
        english.add(new Question("'Apple' o'zbekcha?", "Olma", Arrays.asList("Nok", "Uzum", "Shaftoli"), "english"));
        english.add(new Question("'Beautiful' sinonimi?", "Gorgeous", Arrays.asList("Ugly", "Bad", "Small"), "english"));
        english.add(new Question("'Go' ning Past Simple shakli?", "Went", Arrays.asList("Goed", "Gone", "Goes"), "english"));
        english.add(new Question("'I ___ a student.' (to be)", "am", Arrays.asList("is", "are", "be"), "english"));
        english.add(new Question("'Book' ko'pligi?", "Books", Arrays.asList("Bookes", "Booky", "Bookies"), "english"));
        english.add(new Question("'Water' qanday so'z turi?", "Noun", Arrays.asList("Verb", "Adjective", "Adverb"), "english"));
        english.add(new Question("'She ___ every day.' (run)", "runs", Arrays.asList("run", "running", "runned"), "english"));
        english.add(new Question("'Happy' antonimi?", "Sad", Arrays.asList("Glad", "Joyful", "Excited"), "english"));
        english.add(new Question("'There ___ two books.'", "are", Arrays.asList("is", "am", "be"), "english"));
        english.add(new Question("'Fast' qanday so'z turi?", "Adjective/Adverb", Arrays.asList("Noun", "Verb", "Pronoun"), "english"));
        english.add(new Question("'United Kingdom' poytaxti?", "London", Arrays.asList("Paris", "Berlin", "Rome"), "english"));
        english.add(new Question("'I have ___ eating.' (finish)", "finished", Arrays.asList("finish", "finishing", "finishes"), "english"));
        questionsBySubject.put("english", english);

        // ===== ONA TILI (O'ZBEK) =====
        List<Question> uzbek = new ArrayList<>();
        uzbek.add(new Question("O'zbek alifbosida nechta harf bor?", "32", Arrays.asList("26", "30", "35"), "uzbek"));
        uzbek.add(new Question("'Kitob' so'zining ko'pligi?", "Kitoblar", Arrays.asList("Kitobler", "Kitoblari", "Kitobning"), "uzbek"));
        uzbek.add(new Question("Fe'l nima?", "Harakat bildiradi", Arrays.asList("Narsa bildiradi", "Belgi bildiradi", "Son bildiradi"), "uzbek"));
        uzbek.add(new Question("'Baxt' so'zining sinonimi?", "Saodat", Arrays.asList("Qayg'u", "G'am", "Dard"), "uzbek"));
        uzbek.add(new Question("Ega gapda nima vazifasini o'taydi?", "Kim? Nima?", Arrays.asList("Qanday?", "Qayer?", "Necha?"), "uzbek"));
        uzbek.add(new Question("Alisher Navoiy qaysi asrda yashagan?", "XV asr", Arrays.asList("XIV asr", "XVI asr", "XIII asr"), "uzbek"));
        uzbek.add(new Question("'Ona' so'zi qaysi so'z turkumi?", "Ot", Arrays.asList("Fe'l", "Sifat", "Ravish"), "uzbek"));
        uzbek.add(new Question("O'zbek tilida nechta unli tovush bor?", "6", Arrays.asList("5", "7", "8"), "uzbek"));
        uzbek.add(new Question("'Yaxshi' so'zi qaysi so'z turkumi?", "Sifat", Arrays.asList("Ot", "Fe'l", "Ravish"), "uzbek"));
        uzbek.add(new Question("Maqol nima?", "Xalq donishmandligi", Arrays.asList("She'r turi", "Hikoya turi", "Drama turi"), "uzbek"));
        questionsBySubject.put("uzbek", uzbek);

        // ===== RUS TILI =====
        List<Question> russian = new ArrayList<>();
        russian.add(new Question("Сколько букв в русском алфавите?", "33", Arrays.asList("32", "30", "35"), "russian"));
        russian.add(new Question("'Книга' — это?", "Существительное", Arrays.asList("Глагол", "Прилагательное", "Наречие"), "russian"));
        russian.add(new Question("Столица России?", "Москва", Arrays.asList("Санкт-Петербург", "Казань", "Новосибирск"), "russian"));
        russian.add(new Question("'Красивый' антоним?", "Некрасивый", Arrays.asList("Умный", "Быстрый", "Весёлый"), "russian"));
        russian.add(new Question("Какой падеж отвечает на 'кого? чего?'?", "Родительный", Arrays.asList("Именительный", "Дательный", "Винительный"), "russian"));
        russian.add(new Question("'Я ___ студент.' (быть)", "—", Arrays.asList("есть", "являюсь", "буду"), "russian"));
        russian.add(new Question("Мягкий знак обозначает?", "Мягкость согласного", Arrays.asList("Звук", "Гласную", "Ударение"), "russian"));
        russian.add(new Question("Пушкин — автор произведения?", "Евгений Онегин", Arrays.asList("Война и мир", "Преступление и наказание", "Мастер и Маргарита"), "russian"));
        russian.add(new Question("'Бежать' — это?", "Глагол", Arrays.asList("Существительное", "Прилагательное", "Наречие"), "russian"));
        russian.add(new Question("Сколько падежей в русском языке?", "6", Arrays.asList("5", "7", "8"), "russian"));
        questionsBySubject.put("russian", russian);

        // ===== INFORMATIKA =====
        List<Question> informatics = new ArrayList<>();
        informatics.add(new Question("CPU nima?", "Markaziy protsessor", Arrays.asList("Xotira", "Monitor", "Klaviatura"), "informatics"));
        informatics.add(new Question("1 bayt necha bit?", "8", Arrays.asList("4", "16", "32"), "informatics"));
        informatics.add(new Question("HTML nima?", "Veb-sahifa tili", Arrays.asList("Dasturlash tili", "Ma'lumotlar bazasi", "Operatsion tizim"), "informatics"));
        informatics.add(new Question("1 KB = ? bayt", "1024", Arrays.asList("1000", "512", "2048"), "informatics"));
        informatics.add(new Question("Python qanday til?", "Dasturlash tili", Arrays.asList("Belgilash tili", "Stil tili", "So'rovlar tili"), "informatics"));
        informatics.add(new Question("www - nima degan?", "World Wide Web", Arrays.asList("World War Web", "Wide World Website", "Web World Wide"), "informatics"));
        informatics.add(new Question("RAM nima?", "Operativ xotira", Arrays.asList("Doimiy xotira", "Protsessor", "Videocard"), "informatics"));
        informatics.add(new Question("Binary (2li) sanoq tizimida 1010 = ?", "10", Arrays.asList("8", "12", "6"), "informatics"));
        informatics.add(new Question("Excel qaysi dastur?", "Jadval muharriri", Arrays.asList("Matn muharriri", "Tasvir muharriri", "Video muharrir"), "informatics"));
        informatics.add(new Question("Internet brauzer misoli?", "Chrome", Arrays.asList("Windows", "Photoshop", "Excel"), "informatics"));
        questionsBySubject.put("informatics", informatics);

        // ===== GEOGRAFIYA =====
        List<Question> geography = new ArrayList<>();
        geography.add(new Question("O'zbekiston poytaxti?", "Toshkent", Arrays.asList("Samarqand", "Buxoro", "Namangan"), "geography"));
        geography.add(new Question("Dunyodagi eng baland tog'?", "Everest", Arrays.asList("Elbrus", "Mont Blanc", "K2"), "geography"));
        geography.add(new Question("Eng katta okean?", "Tinch okean", Arrays.asList("Atlantika", "Hind", "Arktik"), "geography"));
        geography.add(new Question("Braziliya poytaxti?", "Braziliya", Arrays.asList("San-Paulo", "Rio de Janeyro", "Buenos-Ayres"), "geography"));
        geography.add(new Question("Nil daryosi qaysi qit'ada?", "Afrika", Arrays.asList("Osiyo", "Evropa", "Amerika"), "geography"));
        geography.add(new Question("Avstraliya poytaxti?", "Kanberra", Arrays.asList("Sidney", "Melburn", "Brisben"), "geography"));
        geography.add(new Question("Eng katta qit'a?", "Yevrosiyo", Arrays.asList("Afrika", "Amerika", "Antarktida"), "geography"));
        geography.add(new Question("O'zbekistonda nechta viloyat bor?", "12", Arrays.asList("10", "14", "11"), "geography"));
        geography.add(new Question("Yaponiya poytaxti?", "Tokio", Arrays.asList("Osaka", "Kioto", "Xiroshima"), "geography"));
        geography.add(new Question("Volga daryosi qayerda?", "Rossiya", Arrays.asList("Ukraina", "Germaniya", "Fransiya"), "geography"));
        questionsBySubject.put("geography", geography);

        // ===== BIOLOGIYA =====
        List<Question> biology = new ArrayList<>();
        biology.add(new Question("Fotosintez qayerda sodir bo'ladi?", "Bargda (xloroplastda)", Arrays.asList("Ildizda", "Tanada", "Shonada"), "biology"));
        biology.add(new Question("DNK nima?", "Genetik material", Arrays.asList("Oqsil", "Yog'", "Uglevod"), "biology"));
        biology.add(new Question("Inson skeletida nechta suyak bor?", "206", Arrays.asList("200", "210", "180"), "biology"));
        biology.add(new Question("Qon guruhlari nechtata?", "4", Arrays.asList("3", "5", "2"), "biology"));
        biology.add(new Question("O'simlik hujayrasi hayvon hujayrasidan farqi?", "Hujayra devori va xloroplast", Arrays.asList("Yadro", "Membrana", "Mitoxondriya"), "biology"));
        biology.add(new Question("Eng katta sutemizuvchi?", "Ko'k kit", Arrays.asList("Fil", "Karkidon", "Begemot"), "biology"));
        biology.add(new Question("Odam nechi xromosomaga ega?", "46", Arrays.asList("42", "48", "44"), "biology"));
        biology.add(new Question("Qaysi vitamin C manbai?", "Apelsin", Arrays.asList("Sut", "Tuxum", "Go'sht"), "biology"));
        biology.add(new Question("Hujayraning energiya markazi?", "Mitoxondriya", Arrays.asList("Yadro", "Ribosoma", "Vakuol"), "biology"));
        biology.add(new Question("Qon qizil rangini beruvchi modda?", "Gemoglobin", Arrays.asList("Melanin", "Xlorofill", "Karotin"), "biology"));
        questionsBySubject.put("biology", biology);

        // ===== ADABIYOT =====
        List<Question> literature = new ArrayList<>();
        literature.add(new Question("'Alpomish' eposi qaysi xalqniki?", "O'zbek", Arrays.asList("Qozoq", "Qirg'iz", "Tojik"), "literature"));
        literature.add(new Question("Alisher Navoiyning to'liq ismi?", "Nizomiddin Mir Alisher", Arrays.asList("Ali Sher Navoiy", "Husayn Boyqaro", "Zahiriddin Bobur"), "literature"));
        literature.add(new Question("'Xamsa' nechanchi asrda yozilgan?", "XV asr", Arrays.asList("XIV asr", "XVI asr", "XIII asr"), "literature"));
        literature.add(new Question("Shekspir qaysi mamlakatdan?", "Angliya", Arrays.asList("Fransiya", "Germaniya", "Italiya"), "literature"));
        literature.add(new Question("'Hamlet' muallifi?", "Shekspir", Arrays.asList("Gyote", "Dante", "Servantes"), "literature"));
        literature.add(new Question("Cho'lpon — kim?", "O'zbek shoiri", Arrays.asList("Rus adibi", "Tojik shoiri", "Qozoq adibi"), "literature"));
        literature.add(new Question("'Don Kixot' muallifi?", "Servantes", Arrays.asList("Shekspir", "Gyote", "Balzak"), "literature"));
        literature.add(new Question("Abdulla Qodiriy asarining nomi?", "O'tkan kunlar", Arrays.asList("Sarob", "Kecha va Kunduz", "Shum Bola"), "literature"));
        literature.add(new Question("Haiku qaysi xalqning she'r turi?", "Yapon", Arrays.asList("Xitoy", "Koreya", "Hind"), "literature"));
        literature.add(new Question("'Anna Karenina' muallifi?", "Lev Tolstoy", Arrays.asList("Dostoyevskiy", "Pushkin", "Turgenev"), "literature"));
        questionsBySubject.put("literature", literature);

        // ===== TARIX =====
        List<Question> history = new ArrayList<>();
        history.add(new Question("O'zbekiston mustaqilligini qachon oldi?", "1991-yil 1-sentabr", Arrays.asList("1990-yil", "1992-yil", "1989-yil"), "history"));
        history.add(new Question("Amir Temur qachon yashagan?", "1336-1405", Arrays.asList("1200-1270", "1450-1520", "1100-1180"), "history"));
        history.add(new Question("Birinchi jahon urushi qachon boshlangan?", "1914", Arrays.asList("1912", "1916", "1918"), "history"));
        history.add(new Question("Ikkinchi jahon urushi qachon tugagan?", "1945", Arrays.asList("1943", "1944", "1946"), "history"));
        history.add(new Question("Buyuk Ipak yo'li qayerdan o'tgan?", "Xitoydan O'rta Osiyoga", Arrays.asList("Hindistondan Afrikaga", "Yevropadan Rossiyaga", "Arabistondan Xitoyga"), "history"));
        history.add(new Question("Aleksandr Makedonskiy qaysi davlatni bosib olmagan?", "Xitoy", Arrays.asList("Eron", "Misr", "Hindiston"), "history"));
        history.add(new Question("Ulug'bek kimning nabirasi?", "Amir Temur", Arrays.asList("Bobur", "Husayn Boyqaro", "Shahrux"), "history"));
        history.add(new Question("Fransuz inqilobi qachon bo'lgan?", "1789", Arrays.asList("1776", "1812", "1848"), "history"));
        history.add(new Question("Rim imperiyasi qachon qulab tushgan?", "476 yil", Arrays.asList("395 yil", "1054 yil", "1453 yil"), "history"));
        history.add(new Question("Samarqand qaysi davlatning poytaxti bo'lgan?", "Temuriylar", Arrays.asList("Somoniylar", "Qoraxoniylar", "G'aznaviylar"), "history"));
        questionsBySubject.put("history", history);

        // ===== KIMYO =====
        List<Question> chemistry = new ArrayList<>();
        chemistry.add(new Question("Suvning kimyoviy formulasi?", "H₂O", Arrays.asList("CO₂", "NaCl", "O₂"), "chemistry"));
        chemistry.add(new Question("Osh tuzining kimyoviy formulasi?", "NaCl", Arrays.asList("KCl", "Na₂O", "HCl"), "chemistry"));
        chemistry.add(new Question("Oltin kimyoviy belgisi?", "Au", Arrays.asList("Ag", "Al", "Fe"), "chemistry"));
        chemistry.add(new Question("Kislorod kimyoviy belgisi?", "O", Arrays.asList("K", "Os", "Or"), "chemistry"));
        chemistry.add(new Question("Temir kimyoviy belgisi?", "Fe", Arrays.asList("Ti", "Te", "F"), "chemistry"));
        chemistry.add(new Question("Mendeleyev jadvalidagi birinchi element?", "Vodorod (H)", Arrays.asList("Geliy (He)", "Litiy (Li)", "Uglerod (C)"), "chemistry"));
        chemistry.add(new Question("CO₂ nima?", "Karbonat angidrid", Arrays.asList("Kislorod", "Suv", "Azot"), "chemistry"));
        chemistry.add(new Question("Atom nima?", "Eng kichik zarracha", Arrays.asList("Molekula", "Ion", "Elektron"), "chemistry"));
        chemistry.add(new Question("Kislota va asos qo'shilsa?", "Neytrallanish", Arrays.asList("Portlash", "Yonish", "Bug'lanish"), "chemistry"));
        chemistry.add(new Question("Benzin qaysi sinfga kiradi?", "Uglevodorodlar", Arrays.asList("Spirtlar", "Kislotalar", "Tuzlar"), "chemistry"));
        questionsBySubject.put("chemistry", chemistry);
    }

    public static List<Question> getQuestions(String subject) {
        return questionsBySubject.getOrDefault(subject, new ArrayList<>());
    }

    public static List<Question> getRandomQuestions(int count) {
        List<Question> all = new ArrayList<>();
        for (List<Question> qs : questionsBySubject.values()) all.addAll(qs);
        Collections.shuffle(all);
        return all.subList(0, Math.min(count, all.size()));
    }

    public static List<String> getSubjects() {
        return new ArrayList<>(questionsBySubject.keySet());
    }
}
