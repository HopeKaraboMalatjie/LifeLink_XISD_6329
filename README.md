# LifeLink Blood Donation — Complete Setup Guide
## Task 2: Requirement Analysis & System Design

**Team:** Ayabonga Hadebe · Gutshwa Magagula · Karabo Mojapelo · Hope Malatjie

---

## 📁 Project Structure

```
lifelink-web/          ← PHP/XAMPP Web Application
├── index.php          ← Landing page
├── includes/          ← db.php, header.php, footer.php
├── pages/             ← register, login, dashboard, appointments,
│                         book_appointment, hospitals, history,
│                         notifications, profile, logout
├── admin/             ← login, dashboard, donors, hospitals,
│                         appointments, inventory, alerts, reports, logout
├── api/               ← REST API (auth, donor, appointments,
│                         hospitals, alerts, notifications)
├── assets/            ← css/style.css, js/app.js
└── database/
    └── lifelink.sql   ← Full schema + seed data

lifelink-kotlin/       ← Android / Kotlin Mobile App
├── app/build.gradle   ← Retrofit, Material, Coroutines
└── src/main/
    ├── AndroidManifest.xml
    ├── java/com/lifelink/app/
    │   ├── activities/   ← Splash, Login, Register, Main, BookAppointment
    │   ├── fragments/    ← Dashboard, Appointments, Hospitals, Alerts, Profile
    │   ├── adapters/     ← AppointmentAdapter, HospitalAdapter, AlertAdapter
    │   ├── models/       ← Kotlin data classes
    │   ├── network/      ← RetrofitClient, LifeLinkApiService
    │   └── utils/        ← SessionManager, Extensions
    └── res/              ← layouts, menu, values, drawables

presentations/
├── LifeLink_WebApp_PHP_XAMPP.pptx
└── LifeLink_AndroidApp_Kotlin.pptx
```

---

## 🌐 WEB APP SETUP (PHP / XAMPP)

### Step 1 — Copy Files
Copy `lifelink-web/` → `C:\xampp\htdocs\lifelink\`

### Step 2 — Create Database
1. Start XAMPP → Apache + MySQL
2. Open `http://localhost/phpmyadmin`
3. Create database: `lifelink_db`
4. Import: `lifelink-web/database/lifelink.sql`

### Step 3 — Open the App
```
http://localhost/lifelink
```

### Default Credentials
| Role           | Email                  | Password |
|----------------|------------------------|----------|
| LifeLink Admin | admin@lifelink.co.za   | password |
| Donor          | Register on the site   | —        |

---

## 📱 ANDROID APP SETUP (Kotlin)

### Step 1 — Open Project
Open `lifelink-kotlin/` in Android Studio (Hedgehog+)
Wait for Gradle sync to complete.

### Step 2 — Configure API URL
In `app/build.gradle`:
```gradle
// Emulator (default):
buildConfigField "String", "BASE_URL", '"http://10.0.2.2/lifelink/api/"'

// Real device on same WiFi:
// buildConfigField "String", "BASE_URL", '"http://192.168.1.X/lifelink/api/"'
```

### Step 3 — Run
Click ▶ in Android Studio. App launches on emulator (API 24+) or device.

---

## 🔌 API Endpoints

| Endpoint             | Action           | Description          |
|----------------------|------------------|----------------------|
| api/auth.php         | login/register   | Donor authentication |
| api/donor.php        | profile/history  | Donor data           |
| api/appointments.php | list/book/cancel | Appointments         |
| api/hospitals.php    | list             | Approved hospitals   |
| api/alerts.php       | active           | Emergency alerts     |
| api/notifications.php| list/mark_read   | Notifications        |

All responses: `{ "success": bool, "data": [...], "message": "..." }`

---

*LifeLink Blood Donation · XISD 6329  · Task 2*
