# Training Store (Console) — Java / JDBC / MariaDB

Application console permettant à un visiteur de consulter un catalogue de formations avec filtres :
- par catégorie,
- par modalité (ONSITE / REMOTE),
- par mot-clé (nom/description),
et consulter le détail d’une formation.

## 1) Prérequis
- Java (JDK) installé
- MariaDB (ou MySQL compatible) en cours d’exécution
- Un IDE (Eclipse) ou exécution en ligne de commande
- Driver JDBC MariaDB disponible dans le projet (jar ajouté au build path si besoin)

## 2) Base de données
Le script SQL est fourni dans :
- `sql/trainingStore.sql`

### Création du schéma
1. Ouvrir un client SQL (phpMyAdmin, HeidiSQL..).
2. Exécuter le script dans `sql/trainingStore.sql`.

Le script :
- crée la base `trainingStore`,
- crée les tables (category, training, etc.),
- ajoute des données minimales pour tester le catalogue,
- crée un utilisateur SQL `jury_user`.

## 3) Configuration de connexion (JDBC)
Le fichier de configuration est :
- `src/main/resources/db.properties`

Exemple :
```properties
db.url=jdbc:mariadb://localhost:3300/training_store
db.user=training_app
db.password=training_app_pwd
db.driver=org.mariadb.jdbc.Driver

