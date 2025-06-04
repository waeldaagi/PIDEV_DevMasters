# RH Management siteweb – Projet PIDEV Web

## Overview

Ce projet a été réalisé dans le cadre du module PIDEV 3A à **Esprit School of Engineering**.  
Il s'agit d'un site web web dédiée à la gestion des ressources humaines (RH), intégrant des fonctionnalités de gestion des employés, des événements, des candidatures, et plus encore.

## Features

 Gestion des utilisateurs
- Authentification et rôles (admin, RH, employé, etc.)
- Création, modification et suppression des comptes
- Interface de profil utilisateur

Gestion des événements et participation
- Création et planification d’événements internes
- Participation aux événements (avec QR Code généré)
- Envoi de mails de confirmation (SendGrid)
- Envoi de SMS en cas d’annulation (Twilio)
- Prédiction de la participation selon le type et l’historique

 Gestion des offres de recrutement
- Création et publication d’offres d’emploi
- Consultation des offres par les candidats
- Suivi des candidatures

Gestion des demandes d'emploi
- Dépôt de candidatures avec CV
- Suivi du statut des demandes

 Gestion des congés
- Soumission de demandes de congés par les employés
- Validation ou refus des congés par l’administration
- Historique des congés

 Gestion des équipes et des projets
- Création de projets internes
- Suivi d’avancement ou de statut de projet


## Tech Stack

### Frontend
- HTML / CSS / JavaScript
- Bootstrap

### Backend
- javaFX
- java
- Doctrine ORM
- API REST

### Other Tools & APIs
- SendGrid API
- Twilio API
- API Ninjas
- Composer
- Git / GitHub

## Directory Structure


## Installation
composer install
symfony server:start
