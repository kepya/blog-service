# API de Gestion de Blog

Ce projet est une API RESTful construite avec Spring Boot, permettant de gérer un blog en ligne. L'API offre des fonctionnalités pour gérer les articles de blog, les utilisateurs, et les commentaires associés aux articles.

## Fonctionnalités

- CRUD pour les articles de blog
- Gestion des utilisateurs et authentification (JWT)
- CRUD pour les commentaires
- Recherche et tri des articles

## Prérequis

- [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/)
- Base de données (PostegreSql par défaut, modifiable dans `application.properties`)

## Installation

1. **Clonez le projet depuis GitHub :**
   ```bash
   git clone git@github.com:kepya/blog-service.git
   cd blog-service
