# Projet POO & Java 2025-2026

## Thème : Gestionnaire de métadonnées de fichiers MP3 et de playlists

### Membres du binôme

- **ZIRMI Amira** — Groupe de TD A
- **MOKRANE Maria** — Groupe de TD A

# 1. Présentation du projet

Dans le cadre de l'UE **Programmation Orientée Objet (POO) & Java**, nous avons développé une application permettant de **gérer et d'exploiter les métadonnées de fichiers audio au format MP3**.

L'objectif principal du projet est de proposer un outil capable d'analyser des fichiers musicaux, d'extraire leurs informations **ID3** et de générer automatiquement des playlists à partir des fichiers détectés.

L'application peut fonctionner selon deux modes :

1. **Une interface en ligne de commande (CLI)** permettant d'utiliser le programme directement depuis un terminal.
2. **Une interface graphique (GUI)** permettant une utilisation plus simple et intuitive de l'application.

Le projet met ainsi en pratique plusieurs concepts fondamentaux de la programmation orientée objet en Java, notamment la conception de classes, l'encapsulation, la gestion des fichiers, les exceptions, les collections et la séparation des responsabilités.

# 2. Objectifs du projet

Le projet a plusieurs objectifs.

### Objectif principal

Créer un logiciel capable d'**analyser des fichiers MP3 et de récupérer leurs métadonnées ID3**, puis d'utiliser ces informations pour construire des playlists.

### Objectifs secondaires

L'application doit notamment permettre de :

- analyser un fichier MP3 ;
- parcourir un répertoire contenant plusieurs fichiers musicaux ;
- récupérer les métadonnées des fichiers ;
- afficher les informations musicales ;
- gérer plusieurs fichiers audio ;
- créer automatiquement une playlist ;
- exporter une playlist dans différents formats ;
- utiliser l'application depuis une console ;
- utiliser l'application à travers une interface graphique.

# 3. Organisation du projet

Le projet est organisé de manière à séparer les différentes responsabilités de l'application.

On retrouve notamment plusieurs parties correspondant aux différents besoins du programme :

- **gestion des fichiers MP3** ;
- **lecture des métadonnées ID3** ;
- **modélisation des informations musicales** ;
- **gestion des playlists** ;
- **export dans les différents formats** ;
- **interface CLI** ;
- **interface GUI**.

Cette séparation permet d'obtenir un code plus facilement compréhensible, maintenable et évolutif.

# 4. Instructions de lancement

## Prérequis

Pour utiliser le projet, il est nécessaire de disposer d'un environnement Java compatible avec la version utilisée pour compiler les fichiers `.jar`.

## Lancement en mode CLI

### Afficher l'aide

```bash
java -jar cli.jar -h
```

### Analyser un fichier MP3

```bash
java -jar cli.jar -f "chemin/vers/musique.mp3"
```

### Analyser un répertoire

```bash
java -jar cli.jar -d "./musique/"
```

### Générer une playlist

```bash
java -jar cli.jar -d "./musique/" -o "ma_playlist.xspf"
```

Il est recommandé d'utiliser des guillemets autour des chemins contenant des espaces ou des caractères spéciaux.

## Lancement en mode GUI

Pour lancer l'interface graphique :

```bash
java -jar gui.jar
```

# 5. Conclusion

Le projet **Gestionnaire de métadonnées MP3 et Playlists** est une application Java permettant d'analyser des fichiers musicaux, d'extraire leurs métadonnées ID3 et de générer des playlists dans différents formats.

La présence de deux interfaces, **CLI et GUI**, permet de répondre à différents besoins d'utilisation.

Sur le plan pédagogique, ce projet nous a permis de mettre en pratique les principes de la **programmation orientée objet**, la manipulation de fichiers, la gestion des erreurs, l'utilisation de bibliothèques et la conception d'une application Java complète.

Le projet constitue également une base évolutive qui pourrait être enrichie par de nouvelles fonctionnalités comme la modification des métadonnées, la gestion des pochettes, la recherche avancée ou encore l'intégration d'un lecteur audio.
