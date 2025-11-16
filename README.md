# Salisovellus – Treeniseuranta liikkeittäin

Tämä sovellus tarjoaa yksinkertaisen tavan seurata voimaharjoittelun kehitystä liikekohtaisesti. Etusivu näyttää listan liikkeistä (esim. Penkkipunnerrus, Ylätalja, Vinopenkki käsipainoilla). Liikettä klikkaamalla avautuu näkymä, jossa näkyy kaikki suoritukset päivämäärän ja käytetyn painon mukaan.


## Sovellus on käytettävissä CSC Rahti-pilvipalvelussa:

**URL:** https://salisovellus-treeniohjelmat-git-salisovellus.2.rahtiapp.fi

**Testikirjautuminen:**
- Käyttäjätunnus: `user`
- Salasana: `user`

## 🗄️ Tietokanta

Sovellus käyttää **PostgreSQL-tietokantaa** Rahti-ympäristössä. Tietokanta sisältää seuraavat taulut:

- **app_user** - Käyttäjätiedot (username, passwordHash, role)
- **workout** - Harjoitusliikkeet (name, muscleGroup, user)
- **workout_session** - Yksittäiset treenikertymät (date, reps, weight, sets)
- **muscle_group** - Lihasryhmät (name)

Paikallisessa kehityksessä sovellus käyttää H2-muistitietokantaa.
