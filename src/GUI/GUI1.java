package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import java.awt.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.logging.Level;
import java.util.logging.Logger;




/**
 * Fenêtre principale de l'application de gestion MP3.
 *
 * Cette classe crée l'interface graphique complète avec une barre de navigation
 * pour accéder à différentes fonctionnalités : fichiers MP3, dossiers MP3, playlists, et aide.
 * Elle utilise un CardLayout pour afficher successivement les différents panneaux.
 *
 * Fonctionnalités principales :
 * <ul>
 *     <li>Afficher et gérer un fichier MP3 individuel (FilePanel)</li>
 *     <li>Explorer un dossier contenant des fichiers MP3 et créer une playlist (FolderPanel)</li>
 *     <li>Afficher et gérer les playlists créées (PlaylistPanel)</li>
 *     <li>Afficher un panneau d'aide décrivant toutes les fonctionnalités (HelpPanel)</li>
 * </ul>
 *
 * Le constructeur initialise la fenêtre, définit sa taille, son titre et sa visibilité.
 *
 * Les couleurs des boutons changent dynamiquement selon le panneau sélectionné pour donner un retour visuel.
 * @author ZIRMI Amira et MOKRANE Maria
 * @version 1.0
 */





public class GUI1 extends JFrame {

    // ======== CONSTANTES DE COULEUR ========
    public static final Color BLUE_NIGHT = new Color(6, 56, 102);
    public static final Color TEXT_LIGHT = new Color(255, 255, 255);
    public static final Color MAUVE = new Color(64, 9, 87);
    public static final Color HELP_BG = new Color(92, 49, 48);
    public static final Color PINK_DARK = new Color(92, 17, 90);

    // ======== ATTRIBUTS ========
    private CardLayout cardLayout;
    private JPanel contentPanel;



    FilePanel filePanel ;
    FolderPanel folderPanel ;
    PlaylistPanel playlistPanel ;
    HelpPanel helpPanel;


    private JButton btnFile;
    private JButton btnFolder;
    private JButton btnPlaylist;
    private JButton btnHelp;

    public static final String FILE = "FILE";
    public static final String FOLDER = "FOLDER";
    public static final String PLAYLIST = "PLAYLIST";
    public static final String HELP = "HELP";


    /**
     * Constructeur de la fenêtre principale GUI1.
     *
     * Il initialise la fenêtre en appelant le constructeur de la classe parente avec le titre "Gestion MP3".
     * Ensuite, il appelle la méthode initComponents() pour créer et organiser tous les composants graphiques.
     * Enfin, il définit la taille minimale de la fenêtre, configure la fermeture par défaut et rend la fenêtre visible.
     *
     * Le constructeur se contente donc de préparer la fenêtre et de déléguer la création détaillée des composants
     * à initComponents(), pour garder le code clair et organisé.
     */


    public GUI1() {
        super("Gestion MP3");
        this.initComponents();

        this.pack();
        this.setSize(1000, 650);      // taille initiale
        this.setMinimumSize(new Dimension(1000, 650));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Initialise les composants graphiques de la fenêtre.
     *
     * Utilise un CardLayout pour superposer plusieurs panneaux (FilePanel, FolderPanel, PlaylistPanel, HelpPanel)
     * et n'afficher qu'un seul panneau à la fois selon le bouton cliqué.
     * La barre de navigation met à jour les couleurs pour indiquer le panneau actif.
     */

    private void initComponents() {
        // ======== CARDLAYOUT ========
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        filePanel = new FilePanel();
        folderPanel = new FolderPanel();
        playlistPanel = new PlaylistPanel();
        helpPanel = new HelpPanel();

        contentPanel.add(filePanel, FILE);
        contentPanel.add(folderPanel, FOLDER);
        contentPanel.add(playlistPanel, PLAYLIST);
        contentPanel.add(helpPanel, HELP);


        // ======== NAVBAR ========
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navPanel.setBackground(new Color(169, 186, 186));

        btnFile = new JButton("Fichiers MP3");
        btnFolder = new JButton("Dossiers MP3");
        btnPlaylist = new JButton("Playlists");
        btnHelp = new JButton("Aide");


        navPanel.add(btnFile);
        navPanel.add(btnFolder);
        navPanel.add(btnPlaylist);
        navPanel.add(btnHelp);

        // ======== ATTACH LISTENERS ========
        btnFile.addActionListener(new FileButtonListener());
        btnFolder.addActionListener(new FolderButtonListener());
        btnPlaylist.addActionListener(new PlaylistButtonListener());
        btnHelp.addActionListener(new HelpButtonListener());

        // ======== AJOUT AU FRAME ========
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(navPanel, BorderLayout.NORTH);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        // ======== BOUTON INITIAL ========
        setButtonFile();  // sélection initiale sur FILE
    }

    // ================= Nav Bar Inner Classes =================


    /**
     * Affiche le FilePanel et met à jour les couleurs des boutons.
     */

    private class FileButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showCard(FILE);

            btnFile.setBackground(TEXT_LIGHT);
            btnFile.setForeground(BLUE_NIGHT);

            btnFolder.setBackground(BLUE_NIGHT);
            btnFolder.setForeground(TEXT_LIGHT);

            btnPlaylist.setBackground(BLUE_NIGHT);
            btnPlaylist.setForeground(TEXT_LIGHT);

            btnHelp.setBackground(BLUE_NIGHT);
            btnHelp.setForeground(TEXT_LIGHT);
        }
    }

    /**
     * Affiche le FolderPanel et met à jour les couleurs des boutons.
     */

    private class FolderButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showCard(FOLDER);
            btnFolder.setBackground(TEXT_LIGHT);
            btnFolder.setForeground(MAUVE);

            btnFile.setBackground(MAUVE);
            btnFile.setForeground(TEXT_LIGHT);

            btnPlaylist.setBackground(MAUVE);
            btnPlaylist.setForeground(TEXT_LIGHT);

            btnHelp.setBackground(MAUVE);
            btnHelp.setForeground(TEXT_LIGHT);
        }
    }

    /**
     * Affiche PlaylistPanel et met à jour les couleurs des boutons.
     */
    private class PlaylistButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showCard(PLAYLIST);

            btnPlaylist.setBackground(TEXT_LIGHT);
            btnPlaylist.setForeground(PINK_DARK);

            btnFile.setBackground(PINK_DARK);
            btnFile.setForeground(TEXT_LIGHT);

            btnFolder.setBackground(PINK_DARK);
            btnFolder.setForeground(TEXT_LIGHT);

            btnHelp.setBackground(PINK_DARK);
            btnHelp.setForeground(TEXT_LIGHT);
        }
    }

    /**
     * Affiche HelpPanel et met à jour les couleurs des boutons.
     */
    private class HelpButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showCard(HELP);

            btnFile.setBackground(HELP_BG);
            btnFile.setForeground(TEXT_LIGHT);

            btnFolder.setBackground(HELP_BG);
            btnFolder.setForeground(TEXT_LIGHT);

            btnPlaylist.setBackground(HELP_BG);
            btnPlaylist.setForeground(TEXT_LIGHT);

            btnHelp.setBackground(TEXT_LIGHT);
            btnHelp.setForeground(HELP_BG);
        }
    }

    // ================= MÉTHODES =================
    /**
     * Configure les boutons de navigation pour la sélection initiale sur FilePanel.
     */
    private void setButtonFile() {
        showCard(FILE);

        btnFile.setBackground(TEXT_LIGHT);
        btnFile.setForeground(BLUE_NIGHT);

        btnFolder.setBackground(BLUE_NIGHT);
        btnFolder.setForeground(TEXT_LIGHT);

        btnPlaylist.setBackground(BLUE_NIGHT);
        btnPlaylist.setForeground(TEXT_LIGHT);

        btnHelp.setBackground(BLUE_NIGHT);
        btnHelp.setForeground(TEXT_LIGHT);
    }

    /**
     * Méthode utilitaire pour afficher un panneau dans le CardLayout.
     * @param name nom du panneau à afficher
     */

    public void showCard(String name) {
        cardLayout.show(contentPanel, name);
    }

    // ======== POUR LES ERREURS ========

    /**
     * Affiche un message d'erreur avec une boîte de dialogue.
     * @param msg message d'erreur
     * @param parent composant parent pour la boîte de dialogue
     */
    public static void showError(String msg, Component parent) {
        JOptionPane.showMessageDialog(parent, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Point d'entrée de l'application GUI.
     * Désactive les logs et crée la fenêtre principale.
     */
    public static void main(String[] args) {
        Logger.getLogger("").setLevel(Level.OFF);
        new GUI1();
    }


}
