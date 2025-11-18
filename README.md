# Salisovellus – Treeniseuranta liikkeittäin

Tämä sovellus tarjoaa yksinkertaisen tavan seurata voimaharjoittelun kehitystä liikekohtaisesti. Etusivu näyttää listan liikkeistä (esim. Penkkipunnerrus, Ylätalja, Vinopenkki käsipainoilla). Liikettä klikkaamalla avautuu näkymä, jossa näkyy kaikki suoritukset päivämäärän ja käytetyn painon mukaan.



## Sovellus on käytettävissä CSC Rahti-pilvipalvelussa:

**URL:** https://salisovellus-treeniohjelmat-git-salisovellus.2.rahtiapp.fi

**Testikirjautuminen:**
- Käyttäjätunnus: `testi`
- Salasana: `testi`

##  Ominaisuudet

### Käyttäjille
- **Valmiit harjoitusliikkeet** - Sovelluksessa on valmiina 30+ yleisintä saliharjoitusliikettä (kyykky, penkkipunnerrus, leuanveto, jne.)
- **Omat liikkeet** - Käyttäjät voivat lisätä omia harjoitusliikkeitä
- **Suoritusten kirjaus** - Jokaisen liikkeen kohdalla voi kirjata suorituksia (päivämäärä, painot, toistot, sarjat)
- **Suoritusten poisto** - Käyttäjä voi poistaa omia suorituskertojaan
- **Henkilökohtainen seuranta** - Jokainen käyttäjä näkee omat ja yhteiset liikkeet

### Admin-käyttäjälle
- **Käyttäjien hallinta** - Admin näkee kaikki rekisteröityneet käyttäjät ja voi poistaa käyttäjiä
- **Liikkeiden hallinta** - Vain admin voi poistaa harjoitusliikkeitä
- **Laaja näkyvyys** - Admin näkee kaikkien käyttäjien liikkeet ja suoritukset

## Tietokanta

Sovellus käyttää **PostgreSQL-tietokantaa** Rahti-ympäristössä. Tietokanta sisältää seuraavat taulut:

- **app_user** - Käyttäjätiedot (username, passwordHash, role)
- **workout** - Harjoitusliikkeet (name, muscleGroup, user)
- **workout_session** - Yksittäiset treenikertymät (date, reps, weight, sets)
- **muscle_group** - Lihasryhmät (name)

Paikallisessa kehityksessä sovellus käyttää H2-muistitietokantaa.
