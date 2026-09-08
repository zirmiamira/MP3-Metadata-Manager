package GUI;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.Font;


/**
 * Permet d'expliquer comment utiliser l'application et aide le user
 * a naviger dans l'application.
 * @author ZIRMI Amira
 * @version 1.0
 */

public class HelpPanel extends JPanel {

    public HelpPanel() {
        setLayout(new BorderLayout());
        setBackground(GUI1.HELP_BG);

        JTextArea helpText = new JTextArea();
        helpText.setEditable(false);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);

        helpText.setForeground(GUI1.TEXT_LIGHT);
        helpText.setBackground(GUI1.HELP_BG);
        helpText.setFont(new Font("SansSerif", Font.PLAIN, 16));
        helpText.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        helpText.setText("""
                Aide - Gestion MP3
                
                Bienvenue dans l’application Gestion MP3 ! Cette interface vous permet de gérer vos fichiers MP3, vos dossiers et de créer ou modifier des playlists. Voici un guide pour utiliser chaque section de l’application :
                
                1. Fichiers MP3 (FilePanel)
                
                   Cette section vous permet de travailler avec un fichier MP3 unique.
                
                     Étapes :
                       - Cliquez sur "Choisir un fichier" et sélectionnez un fichier MP3 sur votre ordinateur.
                       - Cliquez sur "Afficher métadonnées" pour voir les informations du fichier (titre, artiste, album, année, genre, etc.).
                       - Si le fichier contient une pochette, elle s’affichera à côté des métadonnées.
                       - Cliquez sur "Play/Stop" pour écouter le MP3.
                       - Cliquez sur "Enregistrer les metaDonnees" pour sauvegarder les métadonnées du fichier dans un fichier JSPF.
                
                2. Dossiers MP3 (FolderPanel)
                
                   Cette section permet de gérer tous les MP3 d’un dossier.
                
                     Étapes :
                       - Cliquez sur "Choisir un dossier" et sélectionnez un dossier contenant des fichiers MP3.
                       - Cliquez sur "Afficher les fichiers mp3 du dossier" pour voir la liste des MP3 présents.
                       - Cliquez sur "Créer une playlist" pour générer automatiquement une playlist avec tous les fichiers du dossier :
                           * Entrez le nom de la playlist.
                           * Choisissez le format : XSPF, M3U8 ou JSPF.
                           * Sélectionnez le chemin d’enregistrement du fichier playlist.
                
                3. Playlists (PlaylistPanel)
                
                   Cette section vous permet de créer et modifier des playlists à partir de plusieurs fichiers MP3.
                
                     Étapes :
                       - Cliquez sur "Choisissez des MP3" pour sélectionner plusieurs fichiers MP3 sur votre ordinateur.
                       - Cliquez sur "Créez une playlist à partir des fichiers choisis" pour créer une nouvelle playlist :
                           * Entrez le nom de la playlist.
                           * Choisissez le format : XSPF, M3U8 ou JSPF.
                           * Sélectionnez le chemin d’enregistrement du fichier playlist.
                       - Cliquez sur "Insérez les fichiers choisis dans une playlist existante" pour ajouter vos MP3 à une playlist déjà existante.
                       - Cliquez sur "Affichez une playlist" pour ouvrir une playlist et voir les titres qu’elle contient.
                
                   Dans la liste affichée :
                        - Sélectionnez un titre et cliquez sur "Supprimer une chanson" pour retirer un fichier.
                        - Cliquez sur "Clear" pour vider entièrement la playlist affichée.
                
                4. Help
                
                     Cette section affiche ce guide. Consultez-le si vous avez besoin d’aide pour naviguer dans l’application ou comprendre comment créer et gérer vos playlists.
                
                
                Bonne utilisation 🎧
                """);

        JScrollPane scrollPane = new JScrollPane(helpText);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }
}
