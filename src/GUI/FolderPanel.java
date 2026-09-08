package GUI;



import gestionDossier.Mp3Collector;
import gestionPL.CreatePL;
import gestionPL.JSPF;
import gestionPL.M3U8;
import gestionPL.XSPF;
import mp3FileException.FolderNotFoundException;
import mp3FileException.FolderNotReadableException;
import mp3FileException.PlatListEmpty;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing. SwingConstants;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.Box;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Panneau graphique permettant :
 *      de sélectionner un dossier contenant des fichiers MP3
 *      d'afficher la liste des fichiers MP3 présents dans le dossier
 *      de créer une playlist à partir des fichiers MP3 sélectionnés
 *
 * Cette classe constitue l'interface utilisateur dédiée à la gestion
 * de dossiers de fichiers MP3 et à la génération de playlists à partir d'un dossier.
 *
 * @author MOKRANE Maria
 * @version 1.0
 */
public class FolderPanel extends JPanel{

    // ===================== ATTRIBUTS FOLDER =====================
    private JTextField folderPathField;
    private JTextArea textAreaMP3;
    private JButton btnCreerPlaylist;
    private JFileChooser dossierChooser;
    private Mp3Collector dossierSelectionne = null;
    private String Docpath = null;

    /**
     * Constructeur du panel FolderPanel.
     *
     * Il définit la couleur de fond, le layout, les marges et
     * appelle initComponents() pour initialiser les composants du panneau.
     */
    public FolderPanel() {
        setBackground(GUI1.MAUVE); // MAUVE
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(24,24,24,24));
        initComponents();
    }


    /**
     * Initialise tous les composants graphiques du panneau.
     *
     * Cette méthode :
     * - crée les boutons et champs de texte
     * - configure la zone d'affichage des fichiers MP3
     * - configure le JFileChooser pour sélectionner un dossier
     * - ajoute les écouteurs d'événements (ActionListener) pour chaque bouton
     */
    private void initComponents() {

        // ----- HEADER DU PANEL -----
        JLabel lblTitle = new JLabel("Explorer un dossier MP3 et générer une playlist", SwingConstants.CENTER);
        lblTitle.setForeground(GUI1.TEXT_LIGHT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblTitle);
        add(Box.createRigidArea(new Dimension(0, 18)));

        // ----- BOUTONS + TEXTEFIELD POUR CHEMIN -----
        JPanel folderChooserPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        folderChooserPanel.setBackground(GUI1.MAUVE);

        JButton btnChoisirFolder = new JButton("Choisir un dossier");
        folderPathField = new JTextField("Choisir un dossier");
        folderPathField.setPreferredSize(new Dimension(700, 34));
        folderPathField.setEditable(false);

        folderChooserPanel.add(folderPathField);
        folderChooserPanel.add(btnChoisirFolder);
        add(folderChooserPanel);

        // ----- TEXTE AREA -----
        textAreaMP3 = new JTextArea();
        textAreaMP3.setEditable(false);
        textAreaMP3.setLineWrap(true);
        textAreaMP3.setWrapStyleWord(true);
        textAreaMP3.setFont(new Font("Arial", Font.PLAIN, 18));
        textAreaMP3.setForeground(GUI1.MAUVE);
        textAreaMP3.setMargin(new Insets(16, 22, 16, 22));

        JScrollPane scrollMeta = new JScrollPane(textAreaMP3);
        scrollMeta.setPreferredSize(new Dimension(200, 140));
        add(scrollMeta);
        add(Box.createRigidArea(new Dimension(0, 24)));


        // ----- BOUTONS CREE PLAYLIST ET AFFICHER LES MP3 SUR LA MÊME LIGNE -----

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setBackground(GUI1.MAUVE);

        JButton btnAfficherMp3 = new JButton("Afficher les fichiers mp3 du dossier selectione");
        btnCreerPlaylist = new JButton("Cree une Playlist avec les musiques du dossier");

        buttonsPanel.add(btnAfficherMp3);
        buttonsPanel.add(btnCreerPlaylist);
        add(buttonsPanel);

        //  ----- choix du dossier ------

        dossierChooser = new JFileChooser();
        dossierChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);


        btnChoisirFolder.addActionListener(new ChoisirDossierListener());
        btnAfficherMp3.addActionListener(new AfficherMp3Listener());
        btnCreerPlaylist.addActionListener(new CreerPlaylistDossierListener());

    }



    /** Permet de sélectionner un dossier à l'aide d'une boîte de dialogue */

    private class ChoisirDossierListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (dossierChooser.showOpenDialog(FolderPanel.this) == JFileChooser.APPROVE_OPTION) {
                Docpath = dossierChooser.getSelectedFile().getAbsolutePath().replace("\\", "/");

                dossierSelectionne= new Mp3Collector();
                try {
                    dossierSelectionne.scanFolder(Docpath);
                    folderPathField.setText(Docpath);
                }catch (FolderNotFoundException ex) {
                    GUI1.showError(ex.getMessage(),FolderPanel.this);

                } catch (FolderNotReadableException ex){
                    GUI1.showError( ex.getMessage(),FolderPanel.this);

                } catch (FileNotFoundException ex) {
                    GUI1.showError("Erreur : Dossier introuvable",FolderPanel.this);
                }
            }
        }
    }

    /**
     * Listener chargé de lister les fichiers MP3 présents
     * dans le dossier sélectionné.
     */

    // Listener pour le bouton Play/Stop
    private class AfficherMp3Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (dossierSelectionne == null) {
                GUI1.showError("Erreur : Aucun dossier sélectionné !", FolderPanel.this);
                return;
            }
            textAreaMP3.setText(dossierSelectionne.toString());
        }
    }

    /**
     * Listener chargé de créer une playlist
     * à partir des fichiers MP3 d'un dossier sélectionné.
     */
    private class CreerPlaylistDossierListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // 1) Aucun dossier sélectionné
            if (dossierSelectionne == null) {
                GUI1.showError("Erreur : Aucun dossier sélectionné !",FolderPanel.this);
                return;
            }
            ArrayList<String> musics= dossierSelectionne.getMp3Files();

            // 2) Aucun MP3
            if (musics.isEmpty()) {
                GUI1.showError("Impossible de créer la playlist : le dossier ne contient aucun fichier MP3.", FolderPanel.this);
                return;
            }

            // 3) Nom + format
            JTextField nameField = new JTextField("playlist");
            String [] choix =new String[]{"XSPF", "M3U8", "JSPF"};
            JComboBox<String> formatBox = new JComboBox<>(choix);
            formatBox.setSelectedIndex(0); // XSPF par défaut

            JPanel panel = new JPanel(new GridLayout(2, 2, 16, 16));
            panel.add(new JLabel("Nom de la playlist :"));
            panel.add(nameField);
            panel.add(new JLabel("Format :"));
            panel.add(formatBox);

            int result = JOptionPane.showConfirmDialog(
                    FolderPanel.this,
                    panel,
                    "Créer une playlist",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (result != JOptionPane.OK_OPTION) return;

            String nom = nameField.getText().trim();
            if (nom.isEmpty()) {
                GUI1.showError("Le nom de la playlist est obligatoire.",FolderPanel.this);
                return;
            }

            // 4) Choix du fichier
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File(nom));
            if (chooser.showSaveDialog(FolderPanel.this) != JFileChooser.APPROVE_OPTION) return;

            String chemin = chooser.getSelectedFile().getAbsolutePath().replace("\\", "/");

            // 5) Création dynamique selon le format
            CreatePL playlist;
            String format = (String) formatBox.getSelectedItem();

            try {
                switch (format) {
                    case "M3U8" -> {
                        if (!chemin.endsWith(".m3u8")) chemin += ".m3u8";
                        playlist = new M3U8(chemin);
                    }
                    case "JSPF" -> {
                        if (!chemin.endsWith(".jspf")) chemin += ".jspf";
                        playlist = new JSPF(chemin);
                    }
                    default -> { // XSPF par défaut
                        if (!chemin.endsWith(".xspf")) chemin += ".xspf";
                        playlist = new XSPF(chemin);
                    }
                }

                String msg = playlist.CreePlayListe(musics);

                JOptionPane.showMessageDialog(
                        FolderPanel.this,
                        msg,
                        "Playlist créée",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (PlatListEmpty ex) {
                GUI1.showError(ex.getMessage(),FolderPanel.this);
            } catch (Exception ex) {
                ex.printStackTrace();
                GUI1.showError("Erreur lors de la création de la playlist.", FolderPanel.this);
            }
        }
    }


}
