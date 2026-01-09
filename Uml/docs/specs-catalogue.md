# Spécifications fonctionnelles — Module Catalogue (Visiteur)

## 1. Objectif
Permettre à un visiteur non authentifié de consulter le catalogue des formations et d’appliquer des filtres :
- par catégorie,
- par modalité (ONSITE / REMOTE),
- par mot-clé (recherche sur nom et description),
ainsi que consulter le détail d’une formation.

---

## 2. Acteur
- **Visiteur (non connecté)** : utilisateur non authentifié qui consulte le catalogue.

---

## 3. Données manipulées

### 3.1 Formation (Training)
Champs minimum affichables :
- id
- name
- description
- durationDays
- modality (ONSITE / REMOTE)
- price
- category

### 3.2 Catégorie (Category)
- id
- label

---

## 4. Exigences fonctionnelles (testables)

### FR-CAT-01 — Lister toutes les formations
Le système doit permettre au visiteur d’afficher toutes les formations disponibles.

**Critères d’acceptation**
- La liste affichée contient toutes les formations présentes en base.
- Pour chaque formation, au minimum : id, name, durationDays, modality, price, category.

---

### FR-CAT-02 — Filtrer par catégorie
Le visiteur peut sélectionner une catégorie et afficher uniquement les formations associées.

**Critères d’acceptation**
- Si la catégorie existe, seules les formations avec `fk_id_category = id_category` sont affichées.
- Si aucune formation ne correspond : afficher “Aucun résultat”.
- Si l’id de catégorie est invalide (non numérique) : afficher “Veuillez saisir un nombre”.
- Si l’id est numérique mais négatife : afficher "Veuillez saisir un choix positif.".
- Si l’id est numérique mais inexistant : afficher “Aucun résultat.”.

---

### FR-CAT-03 — Filtrer par modalité
Le visiteur peut filtrer les formations par modalité :
- ONSITE
- REMOTE

**Critères d’acceptation**
- Le résultat contient uniquement les formations dont `modality` correspond au choix.
- Si aucune formation ne correspond : afficher “Aucun résultat”.
- La modalité acceptée est limitée aux valeurs ONSITE et REMOTE.

---

### FR-CAT-04 — Rechercher par mot-clé (nom/description)
Le visiteur peut saisir un mot-clé et afficher les formations dont :
- le `name` contient le mot-clé, ou
- la `description` contient le mot-clé.

La recherche est insensible à la casse.

**Critères d’acceptation**
- Le mot-clé “java” retourne les formations dont le nom ou la description contient “java” (peu importe la casse).
- Si le mot-clé ne correspond à rien : afficher “Aucun résultat”.
- Si le mot-clé est vide ou composé uniquement d’espaces :
- comportement retenu : afficher toutes les formations.

---

### FR-CAT-05 — Consulter le détail d’une formation
Le visiteur peut saisir un identifiant de formation et afficher son détail complet.

**Critères d’acceptation**
- Si l’id existe : affichage des champs complets (name, description, durationDays, modality, price, category).
- Si l’id est invalide (non numérique) : afficher “Veuillez saisir un nombre”.
- Si l’id est numérique mais inexistant : afficher “Formation introuvable”.

---

## 5. Règles de gestion

### BR-CAT-01 — Catégorie optionnelle
Une formation peut ne pas appartenir à une catégorie (`fk_id_category` peut être NULL).

### BR-CAT-02 — Valeurs de modalité
La modalité d’une formation est strictement : `ONSITE` ou `REMOTE`.

### BR-CAT-03 — Recherche insensible à la casse
La recherche par mot-clé doit fonctionner sans distinction de majuscules/minuscules.

---

## 6. Gestion des erreurs (console)

- ERR-CAT-01 : saisie non numérique pour un id → “Veuillez saisir un nombre”.
- ERR-CAT-02 : résultat vide (filtre/recherche) → “Aucun résultat”.
- ERR-CAT-03 : id formation inexistant → “Formation introuvable”.
- ERR-CAT-04 : id catégorie inexistant → “Catégorie introuvable”.
- ERR-CAT-05 : id négatife → "Veuillez saisir un choix positif.".

---

## 7. Menu console minimal (proposition)
- 1 : Afficher toutes les formations
- 2 : Filtrer par catégorie
- 3 : Filtrer par modalité (ONSITE/REMOTE)
- 4 : Rechercher par mot-clé
- 5 : Détail d’une formation
- 0 : Quitter

---

## 8. Checklist de tests (validation rapide)

1. Lister toutes les formations : résultat non vide si la base est seedée.
2. Filtrer par catégorie existante : ne renvoie que les formations de cette catégorie.
3. Filtrer par catégorie sans résultat : affiche “Aucun résultat”.
4. Filtrer par modalité ONSITE : ne renvoie que ONSITE.
5. Filtrer par modalité REMOTE : ne renvoie que REMOTE.
6. Recherche mot-clé “java” : retourne au moins “Java Fundamentals”.
7. Recherche mot-clé absent : affiche “Aucun résultat”.
8. Détail formation avec id existant : affiche toutes les infos.
9. Détail formation avec id inexistant : “Formation introuvable”.
10. Entrée non numérique sur un id : “Veuillez saisir un nombre”.

---
