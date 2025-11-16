# Rahti Deployment Ohjeet

## Vaihe 1: Valmistele projekti

1. **Committaa ja pushaa tiedostot GitHubiin:**
```bash
git add Dockerfile .dockerignore openshift-deployment.yaml
git commit -m "Add Rahti deployment files"
git push origin main
```

## Vaihe 2: Kirjaudu Rahtiin

1. Mene osoitteeseen: https://rahti.csc.fi
2. Kirjaudu Haka-tunnuksilla
3. Klikkaa oikeasta yläkulmasta käyttäjätunnustasi → "Copy login command"
4. Kopioi komento ja aja terminaalissa:
```bash
oc login --token=sha256~xxxxx --server=https://api.2.rahti.csc.fi:6443
```

## Vaihe 3: Luo projekti

```bash
# Luo uusi projekti (valitse oma nimi)
oc new-project salisovellus-lyytikainen

# Tai jos projekti on jo olemassa:
oc project salisovellus-lyytikainen
```

## Vaihe 4: Rakenna sovellus GitHubista

```bash
# Luo build configuration GitHubista
oc new-build --name=salisovellus --strategy=docker \
  https://github.com/lyytkon/Salisovellus-Treeniohjelmat.git

# Seuraa buildin etenemistä
oc logs -f bc/salisovellus
```

**Odota että build valmistuu (kestää 3-5 min ensimmäisellä kerralla)**

## Vaihe 5: Deploy sovellus

```bash
# Korvaa NAMESPACE omalla projektin nimellä seuraavassa komennossa
# Esim: salisovellus-lyytikainen
export NAMESPACE=$(oc project -q)

# Päivitä NAMESPACE deployment tiedostoon
sed -i "s/NAMESPACE/$NAMESPACE/g" openshift-deployment.yaml

# Windows PowerShell:
(Get-Content openshift-deployment.yaml) -replace 'NAMESPACE', $env:NAMESPACE | Set-Content openshift-deployment.yaml

# Deployta sovellus
oc apply -f openshift-deployment.yaml
```

## Vaihe 6: Tarkista sovelluksen tila

```bash
# Katso podit
oc get pods

# Katso route (julkinen URL)
oc get route salisovellus

# Tai suoraan URL:
echo "https://$(oc get route salisovellus -o jsonpath='{.spec.host}')"
```

## Vaihe 7: Avaa sovellus selaimessa

Kopioi edellisen komennon URL ja avaa selaimessa. Sovellus käynnistyy noin 1-2 minuutissa.

## Hyödyllisiä komentoja

```bash
# Katso logit
oc logs -f deployment/salisovellus

# Uudelleenkäynnistä
oc rollout restart deployment/salisovellus

# Skaalaa (lisää podeja)
oc scale deployment/salisovellus --replicas=2

# Poista kaikki
oc delete all -l app=salisovellus
```

## Päivitys GitHubista

Kun teet muutoksia koodiin:

```bash
# 1. Pushaa muutokset GitHubiin
git add .
git commit -m "Update application"
git push

# 2. Käynnistä uusi build Rahdissa
oc start-build salisovellus

# 3. Seuraa buildia
oc logs -f bc/salisovellus
```

Build valmistuttuaan uusi versio deployataan automaattisesti.

## Huomiot

- Sovellus käyttää H2-muistitietokantaa (data katoaa uudelleenkäynnistyksessä)
- Admin-tunnus: admin / admin
- Jos haluat pysyvän tietokannan, lisää MySQL tai PostgreSQL service Rahtiin
