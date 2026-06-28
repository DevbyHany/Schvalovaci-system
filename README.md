# Schvalovací systém – Approval Workflow (Spring Boot + React)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green)
![Docker](https://img.shields.io/badge/Docker-ready-blue)

🔗 **Live demo:** https://schvalovaci-system.vercel.app/
📄 **API dokumentace (Swagger):** https://schvalovaci-system-production.up.railway.app/swagger-ui/index.html

---
 
## K čemu aplikace slouží
 
Aplikace digitalizuje proces schvalování žádostí ve firmě. Zaměstnanec podá žádost (např. o nákup vybavení, dovolenou nebo technickou podporu), nadřízený (schvalovatel) nebo administrátor ji následně schválí nebo zamítne. Žádosti lze filtrovat podle stavu a podle role uživatele se liší, co smí v aplikaci vidět a dělat.

---

## Co aplikace umí

### Uživatel (USER)
- Registrace a přihlášení
- Vytvoření nové žádosti s názvem a popisem
- Zobrazení pouze vlastních žádostí
- Filtrování žádostí podle statusu
  
### Schvalovatel (APPROVER)
- Zobrazení všech žádostí v systému
- Schválení nebo zamítnutí cizích žádostí
- Nelze schválit vlastní žádost
  
### Administrátor (ADMIN)
- Zobrazení všech žádostí
- Schválení nebo zamítnutí žádostí
- Nemůže vytvářet žádosti
  
### Backend
- REST API s vrstvenou architekturou (Controller → Service → Repository)
- Spring Security – HTTP Basic Auth, BCrypt hashování hesel
- Validace vstupních dat (@NotBlank, @Email, @Size)
- Globální zpracování výjimek
- Role-based přístup k endpointům
- MySQL databáze
- Interaktivní API dokumentace (Swagger / OpenAPI)

---
 
## Použité technologie
 
**Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA, Hibernate, MySQL, Maven, Docker, Swagger/OpenAPI, JUnit 5, Mockito
 
**Frontend:** React, JavaScript, CSS

---
 
## Jak to spustit
 
### Přes Docker (doporučeno)
 
Vyžaduje pouze Docker Desktop, žádnou lokální instalaci Java/Maven/MySQL.
 
1. Naklonuj nebo stáhni repozitář:
```
   git clone https://github.com/DevbyHany/Schvalovaci-system.git
```
2. V kořenové složce spusť:
```
   docker compose up --build
```
3. Backend poběží na `http://localhost:8080`, MySQL na portu `3306`
### Manuální spuštění (pro vývoj)
 
#### Backend
 
Vyžaduje Java 21+ a běžící MySQL databázi. Maven není potřeba mít nainstalovaný globálně – projekt obsahuje Maven Wrapper (`mvnw`).
 
1. Naklonuj nebo stáhni repozitář
2. V `backend/src/main/resources/application.properties` nastav připojení k MySQL databázi
3. Ve složce `backend` spusť:
```
   ./mvnw spring-boot:run
```
   Na Windows: `mvnw.cmd spring-boot:run`
 
   Pokud na Linuxu/macOS `mvnw` nemá oprávnění k běhu, nastav ho takto:
```
   chmod +x mvnw
```
4. Backend běží na `http://localhost:8080`
   
#### Testy
 
```
./mvnw test
```

#### Frontend
 
1. Otevři terminál ve složce `frontend`
2. `npm install`
3. `npm start`
4. Aplikace běží na `http://localhost:3000`
   
---

## Testovací účty
 
Heslo mají všichni stejné kvůli jednoduchosti testování aplikace.
 
| Jméno   | Email             | Heslo    | Role     |
|---------|--------------------|----------|----------|
| Dominik | dominik@seznam.cz | heslo123 | USER     |
| Tomáš   | tomas@seznam.cz   | heslo123 | USER     |
| Petr    | petr@seznam.cz    | heslo123 | APPROVER |
| Filip   | filip@seznam.cz   | heslo123 | ADMIN    |
 
Po kliknutí na **Authorize** ve Swaggeru lze API testovat přímo s těmito účty.

---

## Ukázky obrazovek

### Hlavní přehled žádostí (APPROVER)
![Přehled žádostí](screenshots/dashboard-approver.png)

### Vytvoření žádosti
![Vytvoření žádosti](screenshots/create-request.png)

### Detail žádosti (APPROVER – se schválením)
![Detail approver](screenshots/detail-approver.png)

### Filtrování – Čeká se
![Filtr čeká se](screenshots/filter-pending.png)

### Filtrování – Schváleno
![Filtr schváleno](screenshots/filter-approved.png)

### Filtrování – Zamítnuto
![Filtr zamítnuto](screenshots/filter-rejected.png)

---

## Testy
 
Projekt obsahuje unit testy pro servisní vrstvu.
 
Testy lze spustit přímo v IntelliJ IDEA pravým klikem na třídu `ApprovalServiceTest` → Run, nebo přes `./mvnw test` (viz výše).
 
Testované scénáře:
- Vytvoření žádosti se statusem PENDING
- Úspěšné schválení žádosti
- Zamítnutí schválení vlastní žádosti
- Zamítnutí schválení již schválené žádosti
- Úspěšné zamítnutí žádosti
- Zamítnutí již zamítnuté žádosti
- Vyhození výjimky při nenalezení žádosti
