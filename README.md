# Salisovellus – Treeniseuranta liikkeittäin

Tämä sovellus tarjoaa yksinkertaisen tavan seurata voimaharjoittelun kehitystä liikekohtaisesti. Etusivu näyttää listan liikkeistä (esim. Penkkipunnerrus, Ylätalja, Vinopenkki käsipainoilla). Liikettä klikkaamalla avautuu näkymä, jossa näkyy kaikki suoritukset päivämäärän ja käytetyn painon mukaan.

## Ominaisuudet
- Liikelistaus: listaa kaikki liikkeet (`/workouts`)
- Liikkeen sivu: näyttää suoritushistorian päivämäärän mukaan (`/workout/{id}`)
- Suorituksen lisäys: päivämäärä, sarjat (val.), toistot, paino (`/addsession/{workoutId}` → tallennus `/savesession`)
- Suorituksen poisto (ADMIN)
- Liikkeen lisääminen ja muokkaus

## Pikasetup ja ajo (Windows / PowerShell)

Sovellus käyttää oletuksena in-memory H2-tietokantaa kehityksessä. Käännös ja ajo:

```powershell
./mvnw.cmd spring-boot:run
```

Sovellus käynnistyy oletuksena osoitteeseen `http://localhost:8080`.

Vaihtoehtoisesti voit ajaa IDE:stä luokan `SalisovellusApplication`.

## Kehitysympäristö ja oletusdata

- Tietokanta: H2 in-memory (kehitys) tai MySQL (tuotanto)
  - H2-konsoli: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:gymdb`
- Skeema: luodaan automaattisesti käynnistyksessä (`spring.jpa.hibernate.ddl-auto=create`)
- SQL-logitus päällä kehitystä varten

## Kirjautuminen ja oikeudet

Sovellus käyttää in-memory käyttäjiä:

- Käyttäjä: `user` / `user` (rooli `USER`)
- Admin: `admin` / `admin` (roolit `USER`, `ADMIN`)

Kaikki reitit vaativat kirjautumisen. Poisto-toiminnot edellyttävät ADMIN-roolia. Kirjautumissivu on `/login`.

## Pääreitit

- `/` → uudelleenohjaus `/workouts`
- `/workouts` → liikelistaus (vain nimet + lihasryhmä)
- `/workout/{id}` → liikkeen suoritushistoria (päivämäärä, sarjat × toistot @ paino)
- `/add` → lisää uusi liike
- `/edit/{id}` → muokkaa liikettä
- `/delete/{id}` → poista liike (ADMIN)
- `/addsession/{workoutId}` → lomake suorituksen lisäämiseen
- `/savesession` → suorituksen tallennus (ohjaa takaisin `/workout/{id}`)
- `/deletesession/{id}` → poista yksittäinen suoritus (ADMIN)

## Mallit (Entities)

- `Workout`: liike (nimi, kuvaus, kesto, lihasryhmä)
- `WorkoutSession`: yksittäinen suoritus (päivämäärä, sarjat?, toistot, paino, viittaus `Workout`iin)
- `MuscleGroup`: lihasryhmä (nimi)

## Deployment Softala-palvelimelle (MySQL)

### 1. Paketointi (Windows)

```powershell
./mvnw.cmd clean package -DskipTests
```

JAR-tiedosto luodaan kansioon `target/salisovellus-0.0.1-SNAPSHOT.jar`

### 2. Siirrä palvelimelle

```powershell
cd target
scp salisovellus-0.0.1-SNAPSHOT.jar lyytikainen@softala.haaga-helia.fi:
```

### 3. Käynnistä palvelimella (prod-profiililla)

SSH-yhteys:
```bash
ssh lyytikainen@softala.haaga-helia.fi
```

Käynnistä sovellus MySQL-profiililla:
```bash
java -jar salisovellus-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

**HUOM:** Varmista ennen käynnistystä, että:
- MySQL-tietokanta `lyytikainen` on olemassa palvelimella
- Käyttäjä `lyytikainen` / `password` on oikein `application-prod.properties`:ssa
- Portti 9095 on MySQL:n oikea portti palvelimella

### 4. Taustapalveluna (valinnainen)

Jos haluat ajaa sovelluksen taustalla:
```bash
nohup java -jar salisovellus-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod > app.log 2>&1 &
```

Sovellus pyörii portilla 8080 (tai muulla, jos määrität `server.port` tuotanto-profiilissa).

## Profiilit

- **Oletus** (`application.properties`): H2 in-memory, kehitykseen
- **prod** (`application-prod.properties`): MySQL, tuotantoon
  - `spring.jpa.hibernate.ddl-auto=update` (säilyttää datan)
  - Thymeleaf-cache päällä
  - Vähemmän SQL-logitusta

Vaihtaaksesi profiilia, käytä:
```bash
java -jar salisovellus-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Huomioita

- H2 in-memory: data nollautuu palvelun uudelleenkäynnistyksessä
- MySQL: data säilyy (`ddl-auto=update`)
- Thymeleafin varatut sanat: älä käytä muuttujanimeä `session` templateissa (käytä esim. `s`)
- Ensimmäisellä käynnistyksellä MySQL:ssä taulut luodaan automaattisesti

