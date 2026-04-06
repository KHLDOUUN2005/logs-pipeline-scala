# Data Pipeline — Analyse de Logs HTTP avec Scala et Apache Spark

## Description
Pipeline de traitement de données massives pour l'analyse de logs HTTP d'une application web.
200 000 lignes de logs traitées avec Apache Spark 3.5.1 et Scala 2.12.

## Technologies
- Scala 2.12.18
- Apache Spark 3.5.1
- sbt 1.9.9
- Java 17
- Python 3 (génération des données)

## Structure du projet
logs-pipeline/
├── build.sbt                     # Configuration du projet
├── generate_logs.py              # Génération des données de test
├── src/main/scala/main.scala     # Pipeline principal
├── data/                         # Données d'entrée (logs.csv)
└── output/                       # Résultats des analyses

## Pipeline
1. **Ingestion** — Lecture du fichier CSV (200 000 lignes)
2. **Nettoyage** — Suppression des doublons et valeurs nulles
3. **Transformation** — Ajout de colonnes enrichies (heure, catégorie, is_error)
4. **Analyses** — Top pages, codes HTTP, trafic par pays, taux d'erreur
5. **Stockage** — Export CSV dans le dossier output/

## Résultats obtenus
| Analyse | Résultat |
|---|---|
| Page la plus visitée | /products (20 376 requêtes) |
| Code HTTP dominant | 200 OK (79 881 fois) |
| Pays avec le plus de trafic | Morocco (25 155 requêtes) |
| IP la plus active | 78.33.21.9 (25 412 requêtes) |

## Lancer le projet
```bash
# Générer les données
python generate_logs.py

# Lancer la pipeline
sbt run
```
