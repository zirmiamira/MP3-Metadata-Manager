# Projet POO & Java 2025-2026
## Thème : Gestionnaire de métadonnées de fichiers MP3 et Playlists

### Membres du binôme
* **NOM Prénom 1** : ZIRMI Amira, Groupe de TD A
* **NOM Prénom 2** : MOKRANE Maria, Groupe de TD A

### Description du projet
Ce logiciel permet d'extraire les métadonnées ID3 de fichiers musicaux MP3 et de générer des playlists aux formats XSPF, JSPF et M3U8. Il propose deux modes d'utilisation :
1. Une interface en ligne de commande (CLI).
2. Une interface graphique (GUI).

### Instructions de lancement
#### Mode Console (CLI)
Pour lancer l'analyse d'un fichier ou d'un répertoire :
- Aide : `java -jar cli.jar -h`
- Analyser un fichier : `java -jar cli.jar -f chemin/vers/musique.mp3`
- Explorer un dossier : `java -jar cli.jar -d ./musique/`
- Sauvegarder une playlist : `java -jar cli.jar -d ./musique/ -o ma_playlist.xspf`
  (il est conseillé de mettre le chemin entre guillemets " afin d’éviter les caractères spéciaux)

#### Mode Graphique (GUI)
- Lancer la commande : `java -jar gui.jar`