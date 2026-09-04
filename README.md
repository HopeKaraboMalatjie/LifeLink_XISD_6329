# LifeLink — Blood Donation Management System

**Module:** XISD6329 · **Team:** Ayabonga Hadebe (ST10455760) · Gutshwa Magagula (ST10361206) · Karabo Mojapelo (ST10436116) · Hope Malatjie (ST10444867)

LifeLink is a dual-platform blood donation management system connecting registered
donors with hospitals and donation centres. This single repository holds both
halves of the project, sharing one REST API and one MySQL database:

```
this-repo/
├── app/, gradle/, build.gradle...   ← Android app (Kotlin)
│   See app/ for the Android Studio project. Talks to lifelink-web/api/.
│
└── lifelink-web/                    ← Website (PHP/MySQL)
    ├── index.php                    Landing page
    ├── pages/                       Donor portal (login, register, dashboard,
    │                                appointments, hospitals, profile)
    ├── admin/                       Admin portal (dashboard, donors, hospitals,
    │                                appointments, alerts)
    ├── api/                         REST API consumed by the Android app
    ├── database/lifelink.sql        Full schema + demo data — import this first
    ├── config/db.php                Database connection settings
    ├── assets/                      Shared CSS/JS
    └── docs/RUNNING_THE_PROJECT.md  Full step-by-step setup guide
```

## How the two halves connect

Both platforms read and write the **same** `lifelink_db` MySQL database:

```
Android App  ──HTTP/JSON──▶  lifelink-web/api/*.php  ──▶  MySQL: lifelink_db
                                                                ▲
Website (browser) ────────────────────────────────────────────┘
  lifelink-web/pages/*.php, lifelink-web/admin/*.php
```

Book an appointment in the app and it appears on the website's admin panel
immediately, and vice versa — they're two front ends on one shared backend.

## Getting it running

See **`lifelink-web/docs/RUNNING_THE_PROJECT.md`** for the full step-by-step guide
(XAMPP install, importing the database, running the website, pointing the Android
app at it, demo logins, and troubleshooting).

Quick reference — demo accounts (seeded in `lifelink-web/database/lifelink.sql`):
- **Donor:** `donor@lifelink.co.za` / `Donor@123`
- **Admin:** `admin@lifelink.co.za` / `Admin@123`

## Project status

- **Task 1:** Updated project plan, site maps, wireframes — submitted as a separate
  Word/PDF document alongside this repository.
- **Task 2 (in progress):** Both the website and Android app are working prototypes
  wired to the same database, tested end-to-end.

## Team workflow

- Task board: Azure DevOps Kanban board (see the Task 1 project plan document)
- Branching: feature branches off `main`, merged via pull request
- Commit and push regularly as work progresses

---
*LifeLink Blood Donation · XISD6329*
