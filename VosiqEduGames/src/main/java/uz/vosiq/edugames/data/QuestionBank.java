package uz.vosiq.edugames.data;

import uz.vosiq.edugames.models.Question;
import java.util.*;

public class QuestionBank {

    public static List<Question> getQuestions(String subject, String lang) {
        List<Question> list = new ArrayList<>();

        if (lang.equals("uz")) {
            switch (subject) {
                case "math":
                    list.add(new Question("2 + 2 = ?", new String[]{"3","4","5","6"}, 1));
                    list.add(new Question("5 × 6 = ?", new String[]{"28","30","32","35"}, 1));
                    list.add(new Question("100 ÷ 4 = ?", new String[]{"20","25","30","40"}, 1));
                    list.add(new Question("7² = ?", new String[]{"42","47","49","51"}, 2));
                    list.add(new Question("√144 = ?", new String[]{"11","12","13","14"}, 1));
                    list.add(new Question("3 × 3 × 3 = ?", new String[]{"18","24","27","30"}, 2));
                    list.add(new Question("200 ning 15% = ?", new String[]{"25","30","35","40"}, 1));
                    list.add(new Question("999 + 1 = ?", new String[]{"1000","999","1001","1100"}, 0));
                    list.add(new Question("8 × 9 = ?", new String[]{"63","72","81","56"}, 1));
                    list.add(new Question("144 ÷ 12 = ?", new String[]{"10","11","12","13"}, 2));
                    break;
                case "english":
                    list.add(new Question("'Apple' o'zbekcha = ?", new String[]{"Nok","Olma","Uzum","Limon"}, 1));
                    list.add(new Question("'Book' tarjimasi = ?", new String[]{"Daftar","Qalam","Kitob","Jadval"}, 2));
                    list.add(new Question("'Beautiful' = ?", new String[]{"Yomon","Go'zal","Katta","Kichik"}, 1));
                    list.add(new Question("'Good morning' = ?", new String[]{"Xayr","Kechqurun yaxshi","Xayrli tong","Tabriklayman"}, 2));
                    list.add(new Question("Plural of 'child' = ?", new String[]{"childs","childes","children","childrens"}, 2));
                    list.add(new Question("'Water' = ?", new String[]{"Olov","Havo","Suv","Tuproq"}, 2));
                    list.add(new Question("'Fast' antonimi = ?", new String[]{"Quick","Slow","Run","Jump"}, 1));
                    list.add(new Question("'School' = ?", new String[]{"Uy","Maktab","Bozor","Bog'"}, 1));
                    list.add(new Question("Past tense of 'go' = ?", new String[]{"goed","gone","went","goes"}, 2));
                    list.add(new Question("'I ___ a student'?", new String[]{"is","are","am","be"}, 2));
                    break;
                case "uzbek":
                    list.add(new Question("'Kitob' so'zining ko'pligi = ?", new String[]{"Kitoblar","Kitobli","Kitoblik","Kitobcha"}, 0));
                    list.add(new Question("O'zbek alifbosida nechta harf bor?", new String[]{"29","30","31","32"}, 2));
                    list.add(new Question("'Go'zal' so'zining sinonimi = ?", new String[]{"Xunuk","Chiroyli","Katta","Yaxshi"}, 1));
                    list.add(new Question("Otadan kichik erkak qarindosh = ?", new String[]{"Tog'a","Amaki","Jiyan","Nevara"}, 1));
                    list.add(new Question("'Yoz' faslida necha oy bor?", new String[]{"2","3","4","6"}, 1));
                    list.add(new Question("'Kitob' so'zi qaysi turkumga kiradi?", new String[]{"Fe'l","Sifat","Ot","Olmosh"}, 2));
                    list.add(new Question("O'zbekiston poytaxti = ?", new String[]{"Samarqand","Buxoro","Toshkent","Namangan"}, 2));
                    list.add(new Question("'Tez' so'zining antonimi = ?", new String[]{"Sekin","Katta","Yaxshi","Chiroyli"}, 0));
                    break;
                case "russian":
                    list.add(new Question("'Привет' = ?", new String[]{"Xayr","Salom","Rahmat","Kechirasiz"}, 1));
                    list.add(new Question("'Книга' = ?", new String[]{"Daftar","Qalam","Kitob","Stol"}, 2));
                    list.add(new Question("'Большой' = ?", new String[]{"Kichik","Chiroyli","Katta","Tez"}, 2));
                    list.add(new Question("Rus tilida nechta harf bor?", new String[]{"30","31","32","33"}, 3));
                    list.add(new Question("'Школа' = ?", new String[]{"Uy","Maktab","Bozor","Kasalxona"}, 1));
                    list.add(new Question("'Вода' = ?", new String[]{"Non","Suv","Tuz","Yog'"}, 1));
                    list.add(new Question("'Спасибо' = ?", new String[]{"Kechirasiz","Marhamat","Rahmat","Salom"}, 2));
                    list.add(new Question("'Красивый' = ?", new String[]{"Xunuk","Chiroyli","Katta","Kichik"}, 1));
                    break;
                case "biology":
                    list.add(new Question("Inson organizmidagi eng katta organ = ?", new String[]{"Yurak","Jigar","Teri","O'pka"}, 2));
                    list.add(new Question("Fotosintez qayerda ro'y beradi?", new String[]{"Ildizda","Bargda","Poyada","Gul"}, 1));
                    list.add(new Question("DNK nima?", new String[]{"Oqsil","Irsiy ma'lumot","Vitamin","Ferment"}, 1));
                    list.add(new Question("Qon guruhlari nechta?", new String[]{"2","3","4","5"}, 2));
                    list.add(new Question("Eng katta hayvon = ?", new String[]{"Fil","Ko'k kit","Begemot","Karkidon"}, 1));
                    list.add(new Question("Vitamin C qaysi mahsulotda ko'p?", new String[]{"Go'sht","Limon","Non","Sut"}, 1));
                    list.add(new Question("Inson nechta tishga ega bo'ladi (katta yoshda)?", new String[]{"28","30","32","34"}, 2));
                    break;
                case "chemistry":
                    list.add(new Question("Suvning kimyoviy formulasi = ?", new String[]{"CO2","H2O","O2","NaCl"}, 1));
                    list.add(new Question("Tuzning kimyoviy formulasi = ?", new String[]{"KCl","MgCl","NaCl","CaCl"}, 2));
                    list.add(new Question("Kislorodning atom raqami = ?", new String[]{"6","7","8","9"}, 2));
                    list.add(new Question("Eng yengil element = ?", new String[]{"Geliy","Vodorod","Litiy","Uglerod"}, 1));
                    list.add(new Question("CO2 - bu nima?", new String[]{"Suv","Kislorod","Karbonat angidrid","Azot"}, 2));
                    list.add(new Question("Oltin elementi belgisi = ?", new String[]{"Ag","Au","Fe","Cu"}, 1));
                    break;
                case "physics":
                    list.add(new Question("Yorug'lik tezligi = ?", new String[]{"200 000 km/s","300 000 km/s","400 000 km/s","100 000 km/s"}, 1));
                    list.add(new Question("Og'irlik kuchi birligi = ?", new String[]{"Vatt","Nyuton","Joule","Pascal"}, 1));
                    list.add(new Question("Elektr toki birligi = ?", new String[]{"Volt","Amper","Om","Vatt"}, 1));
                    list.add(new Question("Issiqlik birligi = ?", new String[]{"Nyuton","Vatt","Joule","Kelvin"}, 2));
                    list.add(new Question("Yerning tortishish kuchi = ?", new String[]{"9.8 m/s²","10.2 m/s²","8.9 m/s²","11 m/s²"}, 0));
                    list.add(new Question("Tovush tezligi havoda = ?", new String[]{"200 m/s","300 m/s","343 m/s","400 m/s"}, 2));
                    break;
                case "history":
                    list.add(new Question("Amir Temur qachon tug'ilgan?", new String[]{"1316","1336","1356","1376"}, 1));
                    list.add(new Question("O'zbekiston mustaqillikni qachon qo'lga kiritdi?", new String[]{"1989","1990","1991","1992"}, 2));
                    list.add(new Question("Birinchi jahon urushi qachon boshlandi?", new String[]{"1912","1913","1914","1915"}, 2));
                    list.add(new Question("Ulug'bek qaysi shaharda rasadxona qurdirgan?", new String[]{"Buxoro","Samarqand","Xiva","Toshkent"}, 1));
                    list.add(new Question("Ikkinchi jahon urushi qachon tugadi?", new String[]{"1943","1944","1945","1946"}, 2));
                    list.add(new Question("Ibn Sino qachon yashagan?", new String[]{"980-1037","900-960","1050-1120","850-920"}, 0));
                    break;
                case "geography":
                    list.add(new Question("O'zbekiston poytaxti = ?", new String[]{"Samarqand","Buxoro","Toshkent","Namangan"}, 2));
                    list.add(new Question("Dunyoning eng uzun daryosi = ?", new String[]{"Amazon","Nil","Yangtze","Mississipi"}, 1));
                    list.add(new Question("Eng baland tog' = ?", new String[]{"K2","Everest","Kangchenjunga","Lhotse"}, 1));
                    list.add(new Question("O'zbekiston nechta viloyatdan iborat?", new String[]{"10","11","12","13"}, 3));
                    list.add(new Question("Dunyo okeanlari soni = ?", new String[]{"3","4","5","6"}, 2));
                    list.add(new Question("Eng katta materik = ?", new String[]{"Afrika","Shimoliy Amerika","Osiyo","Yevropa"}, 2));
                    break;
                case "informatics":
                    list.add(new Question("CPU nima?", new String[]{"Xotira","Protsessor","Monitor","Klaviatura"}, 1));
                    list.add(new Question("1 bayt = ?", new String[]{"4 bit","6 bit","8 bit","16 bit"}, 2));
                    list.add(new Question("HTML qisqartmasi = ?", new String[]{"High Text Markup Language","HyperText Markup Language","Home Tool Markup Language","HyperText Making Language"}, 1));
                    list.add(new Question("Java tilini kim yaratgan?", new String[]{"Bill Gates","James Gosling","Linus Torvalds","Dennis Ritchie"}, 1));
                    list.add(new Question("www nima?", new String[]{"World Wide Web","World Web Wide","Wide World Web","Web World Wide"}, 0));
                    list.add(new Question("1 KB = ?", new String[]{"512 bayt","1000 bayt","1024 bayt","2048 bayt"}, 2));
                    break;
                default:
                    list.add(new Question("2 + 2 = ?", new String[]{"3","4","5","6"}, 1));
                    list.add(new Question("5 × 5 = ?", new String[]{"20","25","30","35"}, 1));
            }
        } else if (lang.equals("ru")) {
            switch (subject) {
                case "math":
                    list.add(new Question("2 + 2 = ?", new String[]{"3","4","5","6"}, 1));
                    list.add(new Question("5 × 6 = ?", new String[]{"28","30","32","35"}, 1));
                    list.add(new Question("100 ÷ 4 = ?", new String[]{"20","25","30","40"}, 1));
                    list.add(new Question("7² = ?", new String[]{"42","47","49","51"}, 2));
                    list.add(new Question("√144 = ?", new String[]{"11","12","13","14"}, 1));
                    list.add(new Question("Сколько секунд в минуте?", new String[]{"50","60","70","80"}, 1));
                    list.add(new Question("8 × 9 = ?", new String[]{"63","72","81","56"}, 1));
                    break;
                case "english":
                    list.add(new Question("'Apple' по-русски = ?", new String[]{"Груша","Яблоко","Виноград","Лимон"}, 1));
                    list.add(new Question("'Book' по-русски = ?", new String[]{"Тетрадь","Ручка","Книга","Стол"}, 2));
                    list.add(new Question("Plural of 'child' = ?", new String[]{"childs","childes","children","childrens"}, 2));
                    list.add(new Question("'Good morning' = ?", new String[]{"До свидания","Добрый вечер","Доброе утро","Спасибо"}, 2));
                    list.add(new Question("Past tense of 'go' = ?", new String[]{"goed","gone","went","goes"}, 2));
                    break;
                case "biology":
                    list.add(new Question("Самый большой орган тела = ?", new String[]{"Сердце","Печень","Кожа","Лёгкие"}, 2));
                    list.add(new Question("Где происходит фотосинтез?", new String[]{"В корнях","В листьях","В стебле","В цветке"}, 1));
                    list.add(new Question("Сколько групп крови?", new String[]{"2","3","4","5"}, 2));
                    list.add(new Question("Самое большое животное?", new String[]{"Слон","Синий кит","Бегемот","Носорог"}, 1));
                    break;
                case "geography":
                    list.add(new Question("Столица Узбекистана = ?", new String[]{"Самарканд","Бухара","Ташкент","Наманган"}, 2));
                    list.add(new Question("Самая длинная река в мире = ?", new String[]{"Амазонка","Нил","Янцзы","Миссисипи"}, 1));
                    list.add(new Question("Самая высокая гора = ?", new String[]{"К2","Эверест","Канченджанга","Лхоцзе"}, 1));
                    list.add(new Question("Сколько океанов на Земле?", new String[]{"3","4","5","6"}, 2));
                    break;
                case "history":
                    list.add(new Question("Когда родился Амир Темур?", new String[]{"1316","1336","1356","1376"}, 1));
                    list.add(new Question("Когда Узбекистан обрёл независимость?", new String[]{"1989","1990","1991","1992"}, 2));
                    list.add(new Question("Когда началась Первая мировая война?", new String[]{"1912","1913","1914","1915"}, 2));
                    list.add(new Question("Когда закончилась Вторая мировая война?", new String[]{"1943","1944","1945","1946"}, 2));
                    break;
                default:
                    list.add(new Question("2 + 2 = ?", new String[]{"3","4","5","6"}, 1));
                    list.add(new Question("5 × 5 = ?", new String[]{"20","25","30","35"}, 1));
            }
        } else {
            // English
            switch (subject) {
                case "math":
                    list.add(new Question("2 + 2 = ?", new String[]{"3","4","5","6"}, 1));
                    list.add(new Question("5 × 6 = ?", new String[]{"28","30","32","35"}, 1));
                    list.add(new Question("100 ÷ 4 = ?", new String[]{"20","25","30","40"}, 1));
                    list.add(new Question("7² = ?", new String[]{"42","47","49","51"}, 2));
                    list.add(new Question("√144 = ?", new String[]{"11","12","13","14"}, 1));
                    list.add(new Question("How many seconds in a minute?", new String[]{"50","60","70","80"}, 1));
                    list.add(new Question("8 × 9 = ?", new String[]{"63","72","81","56"}, 1));
                    break;
                case "english":
                    list.add(new Question("Plural of 'child'?", new String[]{"childs","childes","children","childrens"}, 2));
                    list.add(new Question("Opposite of 'fast'?", new String[]{"quick","slow","run","jump"}, 1));
                    list.add(new Question("'Beautiful' synonym?", new String[]{"ugly","lovely","big","small"}, 1));
                    list.add(new Question("Past tense of 'go'?", new String[]{"goed","went","gone","goes"}, 1));
                    list.add(new Question("'I ___ a student'?", new String[]{"is","are","am","be"}, 2));
                    list.add(new Question("'Water' in plural?", new String[]{"waters","water","watery","watered"}, 0));
                    break;
                case "biology":
                    list.add(new Question("Largest organ in human body?", new String[]{"Heart","Liver","Skin","Lung"}, 2));
                    list.add(new Question("Where does photosynthesis occur?", new String[]{"Roots","Leaves","Stem","Flower"}, 1));
                    list.add(new Question("How many blood types are there?", new String[]{"2","3","4","5"}, 2));
                    list.add(new Question("Largest animal on Earth?", new String[]{"Elephant","Blue Whale","Hippo","Rhino"}, 1));
                    break;
                case "geography":
                    list.add(new Question("Capital of Uzbekistan?", new String[]{"Samarkand","Bukhara","Tashkent","Namangan"}, 2));
                    list.add(new Question("Longest river in the world?", new String[]{"Amazon","Nile","Yangtze","Mississippi"}, 1));
                    list.add(new Question("Highest mountain?", new String[]{"K2","Everest","Kangchenjunga","Lhotse"}, 1));
                    list.add(new Question("How many oceans?", new String[]{"3","4","5","6"}, 2));
                    break;
                case "history":
                    list.add(new Question("When was Amir Temur born?", new String[]{"1316","1336","1356","1376"}, 1));
                    list.add(new Question("When did Uzbekistan gain independence?", new String[]{"1989","1990","1991","1992"}, 2));
                    list.add(new Question("When did WW1 start?", new String[]{"1912","1913","1914","1915"}, 2));
                    list.add(new Question("When did WW2 end?", new String[]{"1943","1944","1945","1946"}, 2));
                    break;
                default:
                    list.add(new Question("2 + 2 = ?", new String[]{"3","4","5","6"}, 1));
                    list.add(new Question("5 × 5 = ?", new String[]{"20","25","30","35"}, 1));
            }
        }

        Collections.shuffle(list);
        return list;
    }

    public static List<String[]> getTrueFalseQuestions(String lang) {
        List<String[]> list = new ArrayList<>();
        if (lang.equals("uz")) {
            list.add(new String[]{"Yer quyosh atrofida aylanadi", "true"});
            list.add(new String[]{"Suv formulasi H2O2", "false"});
            list.add(new String[]{"Toshkent O'zbekiston poytaxti", "true"});
            list.add(new String[]{"Inson tanasida 206 ta suyak bor", "true"});
            list.add(new String[]{"Eng katta sayyora Mars", "false"});
            list.add(new String[]{"Yorug'lik tezligi 300 000 km/s", "true"});
            list.add(new String[]{"Amir Temur 1336-yilda tug'ilgan", "true"});
            list.add(new String[]{"O'zbekiston 1990-yilda mustaqil bo'lgan", "false"});
            list.add(new String[]{"DNA ikki zanjirdan iborat", "true"});
            list.add(new String[]{"2 × 2 = 5", "false"});
            list.add(new String[]{"Nil dunyoning eng uzun daryosi", "true"});
            list.add(new String[]{"Oltin elementi Au deb belgilanadi", "true"});
        } else if (lang.equals("ru")) {
            list.add(new String[]{"Земля вращается вокруг Солнца", "true"});
            list.add(new String[]{"Формула воды H2O2", "false"});
            list.add(new String[]{"Ташкент — столица Узбекистана", "true"});
            list.add(new String[]{"В теле человека 206 костей", "true"});
            list.add(new String[]{"Самая большая планета — Марс", "false"});
            list.add(new String[]{"Скорость света — 300 000 км/с", "true"});
            list.add(new String[]{"ДНК состоит из двух цепей", "true"});
            list.add(new String[]{"2 × 2 = 5", "false"});
            list.add(new String[]{"Нил — самая длинная река в мире", "true"});
            list.add(new String[]{"Золото обозначается символом Au", "true"});
        } else {
            list.add(new String[]{"Earth revolves around the Sun", "true"});
            list.add(new String[]{"Water formula is H2O2", "false"});
            list.add(new String[]{"Tashkent is the capital of Uzbekistan", "true"});
            list.add(new String[]{"Human body has 206 bones", "true"});
            list.add(new String[]{"The largest planet is Mars", "false"});
            list.add(new String[]{"Speed of light is 300,000 km/s", "true"});
            list.add(new String[]{"DNA has two strands", "true"});
            list.add(new String[]{"2 × 2 = 5", "false"});
            list.add(new String[]{"The Nile is the world's longest river", "true"});
            list.add(new String[]{"Gold is symbolized as Au", "true"});
        }
        Collections.shuffle(list);
        return list;
    }

    public static List<String[]> getMemoryPairs(String subject, String lang) {
        List<String[]> pairs = new ArrayList<>();
        if (lang.equals("uz")) {
            pairs.add(new String[]{"H2O", "Suv"});
            pairs.add(new String[]{"CO2", "Karbonat angidrid"});
            pairs.add(new String[]{"NaCl", "Tuz"});
            pairs.add(new String[]{"Au", "Oltin"});
            pairs.add(new String[]{"Fe", "Temir"});
            pairs.add(new String[]{"O2", "Kislorod"});
            pairs.add(new String[]{"Toshkent", "O'zbekiston"});
            pairs.add(new String[]{"Paris", "Fransiya"});
        } else if (lang.equals("ru")) {
            pairs.add(new String[]{"H2O", "Вода"});
            pairs.add(new String[]{"CO2", "Углекислый газ"});
            pairs.add(new String[]{"NaCl", "Соль"});
            pairs.add(new String[]{"Au", "Золото"});
            pairs.add(new String[]{"Fe", "Железо"});
            pairs.add(new String[]{"O2", "Кислород"});
            pairs.add(new String[]{"Ташкент", "Узбекистан"});
            pairs.add(new String[]{"Paris", "France"});
        } else {
            pairs.add(new String[]{"H2O", "Water"});
            pairs.add(new String[]{"CO2", "Carbon Dioxide"});
            pairs.add(new String[]{"NaCl", "Salt"});
            pairs.add(new String[]{"Au", "Gold"});
            pairs.add(new String[]{"Fe", "Iron"});
            pairs.add(new String[]{"O2", "Oxygen"});
            pairs.add(new String[]{"Tashkent", "Uzbekistan"});
            pairs.add(new String[]{"Paris", "France"});
        }
        return pairs;
    }
}
