# Projekat za predmet Operativni sistemi

---

## Sadržaj:
* [Opis projekta](#opis-projekta)
    * [Zadatak](#zadatak)
    * [Tehnologije i  alati](#korištene-tehnologije-i-alati)
* [Screenshots](#screenshots)
* [Licenca](#licenca)

---

## Opis projekta
**Tema projekta:** Simulator operativnog sistema

**Projekat radili:** Vladimir Mijić

### Zadatak
Specifikaciju možete pronaći na ovom [linku](https://github.com/vladocodes/os-project_os-simulator/blob/main/documentation/project_specification_sr.pdf).

#### Osnovni elementi:
- [x] Procedura za podizanje operativnog sistema - booting
- [x] Realizacija raspoređivača procesa - realizovati algoritam najvećeg prioriteta
- [x] Realizacija tehnika upravljanja memorijom - dinamičko particionisanje sa odabirom najbolje odgovarajuće memorijske particije
- [ ] Realizacija fajl sistema - drvoliko 
- [ ] Realizacija interakcije sa ulazno/izlaznim uređajima (ulaz je tastatura i izlaz je monitor). **[Opcioni dio]**
- [ ] Realizovati kreiranje, čitanje i brisanje datoteka - indeksirana alokacija memorije
- [x] Osmisliti komande operativnog sistema koje treba da sadrže najosnovnije stvari koje će omogućiti pokretanje većeg broja procesa. Osnovne komande su:
  1. Komanda za listanje datoteka i poddirektorijuma u aktuelnom direktorijumu
  2. Komanda za promjenu aktuelnog direktorijuma
  3. Komanda za kreiranje, brisanje i preimenovanje direktorijuma
  4. Komanda za pokretanje izvršne datoteke i slanje u pozadinu kako bi mogao da se pokrene novi proces - podrazumijeva se momentalno slanje u pozadinu. Ulazni parametri se uvijek prosljeđuju preko tekstualne datoteke, te se rezultat izvršavanja takođe ispisuje u neku izlaznu tekstualnu datoteku. Poziv može da izgleda ovako: 
  ```bash
  <naziv komande> <naziv izvršne datoteke> <naziv datoteke sa rezultatima>
  ```
  5. Listanje aktuelnih procesa - za svaki proces prikazati standardne informacije (pid, naziv procesa, trenutno stanje, zauzeće memorije)
  6. Prekid nekog procesa
  7. Blokiranje i odblokiranje nekog procesa
  8. Gašenje simulatora
- [x] Kreirati jednostavan asembler. Ovom prilikom je potrebno kreirati najviše desetak osnovniih naredbi asemblera (prebacivanje iz i u memoriju sa registra, sabiranje, oduzimanje, množenje, dijeljenje, naredba skoka i slično). Ovom prilikom je potrebno napraviti simulaciju registara. Prilikom učitavanja asemblerskog koda (tekstualne datoteke sa ekstenzijom `.asm`), potrebno je generisati mašinski kod, te ga potom izvršiti. Omogućiti prikaz memorije i registara.

### Korištene tehnologije i alati
<p>
<img src="https://img.shields.io/badge/java-red.svg?&style=for-the-badge&logo=java&logoColor=blue"/>
</p>

---

## Screenshots

---

## Licenca
Ovaj projekat se nalazi pod [MIT licencom](https://github.com/vladocodes/oop-project/blob/main/LICENSE).
