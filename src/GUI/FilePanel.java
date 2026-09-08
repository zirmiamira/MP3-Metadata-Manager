package GUI;

import gestionFichiermp3.Mp3File;
import mp3FileException.NotMp3FileException;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Panneau graphique permettant de :
 * - sélectionner un fichier MP3
 * - afficher ses métadonnées (tags ID3)
 * - afficher la pochette associée
 * - lire ou arrêter la musique
 * - sauvegarder les métadonnées dans un fichier JSPF
 *
 * Cette classe constitue l'interface utilisateur dédiée
 * à la gestion individuelle d'un fichier MP3.
 *
 * @author ZIRMI Amira
 * @version 1.0
 */



public class FilePanel extends JPanel {
    // ===================== ATTRIBUTS FILE =====================
    private Mp3File fichierSelectionne = null;
    private Mp3Player player;
    private JFileChooser fichierChooser;
    private JTextField filePathField;
    private JButton btnPlay;

    // Composants du panel fichiers
    private JLabel coverLabel;
    private JTextArea textAreaMeta;


    /**
     * Construit le panneau de gestion des fichiers MP3.
     *
     * Ce constructeur initialise l'interface graphique du panel :
     * - configuration du layout et des couleurs
     * - création des composants graphiques (boutons, zones de texte, etc.)
     * - préparation des écouteurs d'événements (listeners)
     *
     * Aucune action n'est effectuée sur un fichier tant que
     * l'utilisateur n'en a pas sélectionné un.
     */

    public FilePanel() {
        setBackground(GUI1.BLUE_NIGHT); // BLUE_NIGHT
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(24,24,24,24));
        initComponents();
    }
    /**
     * Initialise tous les composants graphiques du panneau.
     *
     * Cette méthode :
     * - crée et organise les composants Swing (boutons, champs de texte, zones d'affichage)
     * - configure l'apparence (couleurs, tailles, polices)
     * - initialise les écouteurs d'événements (ActionListener)
     *
     * Elle est appelée une seule fois lors de la création du panneau.
     */


    private void initComponents() {

        // ----- HEADER DU PANEL -----
        JLabel lblTitle = new JLabel("Choisir un MP3 et afficher ses métadonnées", SwingConstants.CENTER);
        lblTitle.setForeground(GUI1.TEXT_LIGHT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(lblTitle);
       add(Box.createRigidArea(new Dimension(0, 18)));

        // ----- BOUTONS + TEXTEFIELD POUR CHEMIN -----
        JPanel fileChooserPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fileChooserPanel.setBackground(GUI1.BLUE_NIGHT);

        JButton btnChoisirFichier = new JButton("Choisir un fichier");
        filePathField = new JTextField("Choisir un fichier MP3");
        filePathField.setPreferredSize(new Dimension(700, 34));
        filePathField.setEditable(false);

        fileChooserPanel.add(filePathField);
        fileChooserPanel.add(btnChoisirFichier);
        add(fileChooserPanel);

        // ----- METADONNÉES ET POCHETTE SUR LA MÊME LIGNE -----
        JPanel metaCoverPanel = new JPanel();
        metaCoverPanel.setLayout(new BoxLayout(metaCoverPanel, BoxLayout.X_AXIS));
        metaCoverPanel.setBackground(GUI1.BLUE_NIGHT);

        // Zone des métadonnées à gauche
        textAreaMeta = new JTextArea();
        textAreaMeta.setEditable(false);
        textAreaMeta.setLineWrap(true);
        textAreaMeta.setWrapStyleWord(true);
        textAreaMeta.setFont(new Font("Arial", Font.PLAIN, 16));
        textAreaMeta.setForeground(GUI1.BLUE_NIGHT);
        textAreaMeta.setMargin(new Insets(16, 22, 16, 22));

        JScrollPane scrollMeta = new JScrollPane(textAreaMeta);
        scrollMeta.setPreferredSize(new Dimension(200, 140));
        metaCoverPanel.add(scrollMeta);
        metaCoverPanel.add(Box.createRigidArea(new Dimension(24, 0))); // espace entre les deux

        // Pochette à droite
        coverLabel = new JLabel("Aucune pochette", SwingConstants.CENTER);
        coverLabel.setPreferredSize(new Dimension(180, 180));
        coverLabel.setMaximumSize(new Dimension(180, 180));
        coverLabel.setMinimumSize(new Dimension(180, 180));
        coverLabel.setOpaque(true);
        coverLabel.setBackground(new Color(144, 191, 240));
        metaCoverPanel.add(coverLabel);

        add(metaCoverPanel);
        add(Box.createRigidArea(new Dimension(0, 24)));

        // ----- BOUTONS PLAY, SAVE ET AFFICHER META SUR LA MÊME LIGNE -----
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setBackground(GUI1.BLUE_NIGHT);

        JButton btnAfficherMeta = new JButton("Afficher métadonnées");
        btnPlay = new JButton("Play/Stop");
        JButton  saveMeta = new JButton("Enregistrer les metaDonnees"); // dans un fichier jspf

        buttonsPanel.add(btnAfficherMeta);
        buttonsPanel.add(btnPlay);
        buttonsPanel.add(saveMeta);

        add(buttonsPanel);


        // ----- INIT FILE CHOOSER ET PLAYER -----
        fichierChooser = new JFileChooser();
        fichierChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter mp3Filter = new FileNameExtensionFilter("Fichiers MP3 (*.mp3)", "mp3");
        fichierChooser.setFileFilter(mp3Filter);
        fichierChooser.setAcceptAllFileFilterUsed(false);

        player = new Mp3Player();

        // ----- LISTENERS -----
        btnChoisirFichier.addActionListener(new ChoisirFichierListener());
        btnAfficherMeta.addActionListener(new AfficherMetaListener());
        btnPlay.addActionListener(new PlayButtonListener());
        saveMeta.addActionListener(new SaveMetadataListener());

    }

    // ======== INNER CLASSES LISTENERS ========

    /**
     * Listener déclenché lorsque l'utilisateur clique sur
     * le bouton "Choisir un fichier".
     *
     * Il ouvre une boîte de dialogue permettant de sélectionner
     * un fichier MP3, vérifie sa validité et prépare l'affichage
     * de ses informations.
     */
    private class ChoisirFichierListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (fichierChooser.showOpenDialog(FilePanel.this) == JFileChooser.APPROVE_OPTION) {
                String path = fichierChooser.getSelectedFile().getAbsolutePath().replace("\\", "/");
                try {
                    fichierSelectionne = new Mp3File(path);
                    filePathField.setText(path);
                    setFilePanel();
                } catch (FileNotFoundException ex) {
                    GUI1.showError(ex.getMessage(),FilePanel.this);
                } catch (NotMp3FileException ex) {
                    GUI1.showError(ex.getMessage(),FilePanel.this);
                } catch (CannotReadException ex) {
                    GUI1.showError("Erreur : Impossible de lire le fichier MP3",FilePanel.this);
                } catch (IOException ex) {
                    GUI1.showError("Erreur : d'accès au fichier",FilePanel.this);
                } catch (TagException ex) {
                    GUI1.showError("Erreur : dans les tags ID3 " + ex.getMessage() ,FilePanel.this);
                } catch (ReadOnlyFileException ex) {
                    GUI1.showError("Erreur : Le fichier est en lecture seule",FilePanel.this);
                } catch (InvalidAudioFrameException ex) {
                    GUI1.showError("Erreur : Bloc audio invalide " + ex.getMessage(),FilePanel.this);
                }
            }
        }
    }


    /**
     * Listener chargé d'afficher les métadonnées du fichier MP3 sélectionné.
     *
     * Il affiche :
     * - les informations ID3 dans la zone de texte
     * - la pochette de l'album si elle existe
     *
     * Une erreur est affichée si aucun fichier n'est sélectionné.
     */

    private class AfficherMetaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (fichierSelectionne != null) {
                BufferedImage img = fichierSelectionne.getMp3data().getCoverImage();
                if (img != null) {
                    coverLabel.setIcon(new ImageIcon(img.getScaledInstance(170, 160, Image.SCALE_SMOOTH)));
                    coverLabel.setText(null);
                } else {
                    coverLabel.setIcon(null);
                    coverLabel.setText("Aucune pochette disponible");
                }
                textAreaMeta.setForeground(GUI1.BLUE_NIGHT);
                textAreaMeta.setText(fichierSelectionne.toString());
            } else {
                GUI1.showError("Erreur : Aucun fichier sélectionné !" ,FilePanel.this);
            }
        }
    }

    /**
     * Listener du bouton Play/Stop.
     *
     * Il permet de démarrer ou d'arrêter la lecture
     * du fichier MP3 actuellement sélectionné.
     */

    private class PlayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (fichierSelectionne == null) {
                GUI1.showError("Erreur : Aucun fichier sélectionné !",FilePanel.this);
                return;
            }
            String path = fichierSelectionne.getPath();
            if (!player.isPlaying()) {
                player.play(path);
            } else {
                player.stop();
            }
        }
    }
//--------------  Set Le Panel  ----------------

    /**
     * Réinitialise l'affichage du panneau fichier.
     *
     * Cette méthode est appelée après la sélection d'un nouveau fichier MP3.
     * Elle efface les anciennes métadonnées et remet l'affichage de la pochette
     * dans son état initial.
     */
    public void setFilePanel(){
        // Réinitialiser métadonnées et pochette
        textAreaMeta.setText("");
        coverLabel.setIcon(null);
        coverLabel.setText("Aucune pochette");

    }

    // ---------------------- Inner Class pour sauvegarder les métadonnées ----------------------
    /**
     * Listener chargé de sauvegarder les métadonnées du fichier MP3.
     *
     * Il ouvre une boîte de dialogue pour choisir l'emplacement
     * du fichier et appelle la méthode de sauvegarde des métadonnées.
     */
    private class SaveMetadataListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            // 1) Vérification qu'un fichier MP3 est sélectionné
            if (fichierSelectionne == null) {
               GUI1.showError("Erreur : Vous devez choisir un fichier MP3 avant de sauvegarder les métadonnées !" ,FilePanel.this);
                return;
            }

            // 2) Boîte de dialogue pour choisir le chemin et le nom du fichier
            JFileChooser saveChooser = new JFileChooser();
            saveChooser.setDialogTitle("Enregistrer les métadonnées");
            saveChooser.setSelectedFile(new File("metadata.txt")); // nom par défaut
            int userSelection = saveChooser.showSaveDialog(FilePanel.this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = saveChooser.getSelectedFile();
                String chemin = fileToSave.getAbsolutePath().replace("\\", "/");
                // 2a) Vérification de l'extension
                if (!chemin.toLowerCase().endsWith(".txt")) {  // pas d'extension → ajout .jspf
                    chemin += ".txt";
                }
                // 3) Appel de la méthode de Mp3Data pour sauvegarder
                String resultat = fichierSelectionne.getMp3data().sauvegarderData(chemin);

                // 4) Affichage d'une boîte de confirmation ou d'erreur
                JOptionPane.showMessageDialog(
                        FilePanel.this,
                        resultat,
                        "Sauvegarde des métadonnées",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }




}
