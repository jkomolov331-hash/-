# 🎮 Vosiq International School — EduGames

**Yaratuvchi:** Najmiddinov Sirojiddin  
**Sinf:** 9A  
**Maktab:** Vosiq International School  
**Versiya:** 1.0.0

---

## 📋 LOYIHANI INTELLIJ IDEA 2025.2 DA OCHISH

### 1. Talablar
- **Java 17** yoki undan yuqori (https://adoptium.net/)
- **Maven** (IntelliJ IDEA ichida o'rnatilgan)
- **IntelliJ IDEA 2025.2**

### 2. Loyihani ochish
1. IntelliJ IDEA ni oching
2. `File → Open` → `VosiqEduGames` papkasini tanlang
3. `pom.xml` faylini tanlang va "Open as Project" bosing
4. Maven avtomatik dependencylarni yuklab oladi (Internet kerak)

### 3. Ilovani ishga tushirish
```
src/main/java/uz/vosiq/edugames/MainApp.java
```
Faylni oching → **Run** tugmasini bosing (▶) yoki:
```
Shift + F10
```

### 4. .EXE yaratish

#### Usul 1: Maven orqali JAR yaratish
```bash
mvn clean package
```
`target/VosiqEduGames-1.0.0-shaded.jar` fayli hosil bo'ladi.

#### Usul 2: JAR ni EXE ga aylantirish — Launch4j
1. **Launch4j** yuklab oling: https://launch4j.sourceforge.net/
2. Launch4j ni oching
3. `Output file` → `VosiqEduGames.exe` 
4. `Jar` → yuqoridagi JAR faylini tanlang
5. `Min JRE version` → `17.0.0`
6. `Icon` → `src/main/resources/images/logo.png` (ICO formatga aylantiring)
7. `Build wrapper` tugmasini bosing

#### Usul 3: jpackage (Java 14+)
```bash
jpackage --input target/ \
  --name "VosiqEduGames" \
  --main-jar VosiqEduGames-1.0.0-shaded.jar \
  --type exe \
  --win-menu \
  --win-shortcut
```

---

## 🎯 ILOVA IMKONIYATLARI

| O'yin | Tavsif |
|-------|--------|
| 🧩 Viktorina | 4 tanlovli savollar, taymer |
| 🃏 Xotira o'yini | Kartochka juftlarini topish |
| ✅ To'g'ri/Noto'g'ri | Tez javob berish o'yini |

### Fanlar (10 ta)
- Matematika, Ingliz tili, O'zbek tili, Rus tili
- Biologiya, Kimyo, Fizika
- Tarix, Geografiya, Informatika

### Tillar
- 🇺🇿 O'zbek tili
- 🇷🇺 Rus tili  
- 🇬🇧 Ingliz tili

---

## 📁 FAYL TUZILMASI

```
VosiqEduGames/
├── pom.xml                          # Maven config
├── README.md
└── src/
    └── main/
        ├── java/uz/vosiq/edugames/
        │   ├── MainApp.java          # Asosiy class
        │   ├── controllers/          # UI controllers
        │   ├── models/               # Ma'lumot modellari
        │   ├── data/                 # Savollar bazasi
        │   └── utils/               # Yordamchi classllar
        └── resources/
            ├── fxml/                 # UI qoliplari
            ├── styles/main.css       # Dizayn
            └── images/logo.png       # Maktab logotipi
```

---

## 🔧 MUAMMO VA YECHIMLAR

**Muammo:** `JavaFX runtime components are missing`  
**Yechim:** `pom.xml` da JavaFX dependency to'g'ri yozilganini tekshiring

**Muammo:** `Logo ko'rinmayapti`  
**Yechim:** `src/main/resources/images/logo.png` mavjudligini tekshiring

---

*© 2025-2026 Vosiq International School. Barcha huquqlar himoyalangan.*
