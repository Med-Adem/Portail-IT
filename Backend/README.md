# 🖥️ OETN IT Portal — Backend Spring Boot

## 📋 Prérequis
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- IDE : IntelliJ IDEA (recommandé)

---

## 🚀 Installation & Démarrage

### 1. Créer la base de données MySQL
```sql
-- Dans MySQL Workbench ou terminal :
mysql -u root -p

-- Exécuter le script :
source /chemin/vers/init.sql
```

### 2. Configurer `application.properties`
```properties
spring.datasource.username=root
spring.datasource.password=TON_MOT_DE_PASSE
```

### 3. Lancer le projet
```bash
cd it-portal
mvn spring-boot:run
```

Le serveur démarre sur : **http://localhost:8080**

---

## 🔑 Authentification

Toutes les routes (sauf `/api/auth/**`) nécessitent un **token JWT** dans le header :
```
Authorization: Bearer <token>
```

---

## 📡 API Reference

### 🔐 AUTH

| Méthode | URL | Description | Auth |
|---------|-----|-------------|------|
| POST | `/api/auth/register` | Créer un compte | ❌ |
| POST | `/api/auth/login` | Se connecter | ❌ |

**Register — Body:**
```json
{
  "username": "Adam Jemaa",
  "email": "adam.jemaa@oetn.com",
  "password": "monmotdepasse"
}
```

**Login — Body:**
```json
{
  "email": "adam.jemaa@oetn.com",
  "password": "monmotdepasse"
}
```

**Réponse:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "adam.jemaa@oetn.com",
  "username": "Adam Jemaa",
  "role": "USER",
  "userId": 1
}
```

---

### 🎫 SUPPORT TICKETS

| Méthode | URL | Description | Auth |
|---------|-----|-------------|------|
| POST | `/api/tickets` | Créer un ticket | USER |
| GET | `/api/tickets/my` | Mes tickets | USER |
| GET | `/api/tickets/{id}` | Ticket par ID | USER |
| GET | `/api/tickets` | Tous les tickets | ADMIN |
| PATCH | `/api/tickets/{id}/status` | Changer statut | ADMIN |
| DELETE | `/api/tickets/{id}` | Supprimer | ADMIN |

**Créer un ticket — Body:**
```json
{
  "title": "Laptop ne démarre pas",
  "description": "Mon ordinateur ne s'allume plus depuis ce matin"
}
```

**Changer statut — Body:**
```json
{
  "status": "IN_PROGRESS"
}
```
> Valeurs possibles: `OPEN`, `IN_PROGRESS`, `CLOSED`

---

### 💾 SOFTWARE REQUESTS

| Méthode | URL | Description | Auth |
|---------|-----|-------------|------|
| POST | `/api/software-requests` | Créer une demande | USER |
| GET | `/api/software-requests/my` | Mes demandes | USER |
| GET | `/api/software-requests/{id}` | Par ID | USER |
| GET | `/api/software-requests` | Toutes | ADMIN |
| PATCH | `/api/software-requests/{id}/status` | Changer statut | ADMIN |
| DELETE | `/api/software-requests/{id}` | Supprimer | ADMIN |

**Créer une demande — Body:**
```json
{
  "softwareName": "AutoCAD",
  "reason": "Nécessaire pour les projets de conception"
}
```

**Statuts:** `PENDING`, `APPROVED`, `REJECTED`

---

### 🖥️ HARDWARE INVENTORY

| Méthode | URL | Description | Auth |
|---------|-----|-------------|------|
| POST | `/api/hardware` | Soumettre un appareil | USER |
| GET | `/api/hardware/my` | Mes appareils | USER |
| GET | `/api/hardware` | Tous les appareils | ADMIN |
| DELETE | `/api/hardware/{id}` | Supprimer | ADMIN |

**Soumettre un appareil — Body:**
```json
{
  "username": "adam.jemaa",
  "laptopModel": "Dell Latitude 5420",
  "serialNumber": "SN-ABC-12345"
}
```

---

### 📄 GUIDES PDF

| Méthode | URL | Description | Auth |
|---------|-----|-------------|------|
| POST | `/api/guides` | Upload PDF | ADMIN |
| GET | `/api/guides` | Lister les guides | USER |
| GET | `/api/guides/{id}/download` | Télécharger PDF | USER |
| DELETE | `/api/guides/{id}` | Supprimer | ADMIN |

**Upload PDF (form-data):**
```
title: "Guide Outlook"
file: [fichier.pdf]
```

---

## 👤 Compte Admin par défaut
```
Email:    admin@oetn.com
Password : 
```

---

## 🏗️ Structure du projet
```
src/main/java/com/oetn/itportal/
├── config/
│   ├── SecurityConfig.java       ← Spring Security + CORS + JWT
│   └── GlobalExceptionHandler.java ← Gestion globale des erreurs
├── controller/
│   ├── AuthController.java
│   ├── SupportTicketController.java
│   ├── SoftwareRequestController.java
│   ├── HardwareInventoryController.java
│   └── GuideController.java
├── dto/                          ← Objets de transfert (Request/Response)
├── model/                        ← Entités JPA (tables MySQL)
├── repository/                   ← Accès base de données
├── security/
│   ├── JwtUtil.java              ← Génération/validation JWT
│   └── JwtFilter.java            ← Filtre HTTP JWT
└── service/                      ← Logique métier
```

---

## 🔒 Sécurité
- Mots de passe hashés avec **BCrypt**
- Authentification par **JWT** (24h d'expiration)
- Rôles : **USER** et **ADMIN**
- CORS configuré pour `localhost:5173` (React)
