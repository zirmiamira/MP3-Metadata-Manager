package GUI;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import gestionDossier.Mp3Collector;
import gestionPL.CreatePL;
import gestionPL.JSPF;
import gestionPL.M3U8;
import gestionPL.XSPF;
/**
 * Panneau graphique pour gérer les playlists.
 * Permet de :
 * - choisir des fichiers MP3,
 * - créer une playlist à partir des fichiers sélectionnés,
 * - insérer des fichiers dans une playlist existante,
 * - afficher le contenu d’une playlist,
 * - supprimer des musiques ou vider la playlist affichée.
 *
 * Utilise une JList pour afficher les morceaux et un JFileChooser pour sélectionner les fichiers.
 * @author ZIRMI Amira
 * @version 1.0
 */

public class PlaylistPanel extends JPanel {
    // ===================== ATTRIBUTS PLAYLIST =====================
    private ArrayList<String> playlistMp3Paths;

    private DefaultListModel<String> playlistModel;
    private JList<String> playlistList;

    private JFileChooser playlistFileChooser;

    /**
     * Constructeur de PlaylistPanel.
     * Initialise le panneau et appelle initComponents pour construire l’UI.
     */

    public PlaylistPanel() {
        setBackground(GUI1.PINK_DARK); // PINK_DARK
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(24,24,24,24));
        initComponents();
    }

    /**
     * Initialise tous les composants graphiques du panneau.
     * Configure le header, les boutons, la liste des morceaux et les listeners.
     */
    public void initComponents(){

        // ================= HEADER =================
        JLabel headerLabel = new JLabel("Gestion de la Playlist", SwingConstants.CENTER);
        headerLabel.setForeground(GUI1.TEXT_LIGHT);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(headerLabel);
        add(Box.createRigidArea(new Dimension(0, 20))); // espace sous le header

        // ================= PANEL PRINCIPAL =================
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 24, 0));
        mainPanel.setBackground(GUI1.PINK_DARK);

        // =============== COLONNE GAUCHE  ============
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(GUI1.PINK_DARK);

        JButton btnChooseMp3 = new JButton("Choisissez des MP3");
        JButton btnCreatePlaylist = new JButton("Créez une playlist à partir des fichiers choisis");
        JButton btnInsertIntoPlaylist = new JButton("Insérez les fichiers choisis dans une playlist existante");
        JButton btnShowPlaylist = new JButton("Affichez une playlist");

        Dimension btnSize = new Dimension(340, 45);
        JButton[] buttons = {
                btnChooseMp3,
                btnCreatePlaylist,
                btnInsertIntoPlaylist,
                btnShowPlaylist,
        };

        for (JButton b : buttons) {
            b.setMaximumSize(btnSize);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(b);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        }
        mainPanel.add(leftPanel);

        // =============== COLONNE DROITE  ===========

        JPanel rightPanel = new JPanel(new BorderLayout(0, 12));
        rightPanel.setBackground(GUI1.PINK_DARK);

        // -------- LISTE DES MORCEAUX --------

        playlistModel = new DefaultListModel<String>();
        playlistList = new JList<>(playlistModel);
        playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playlistList.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(playlistList);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // -------- BOUTONS BAS --------
        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
        bottomButtons.setBackground(GUI1.PINK_DARK);

        JButton btnRemove = new JButton("Supprimer une chanson");
        JButton btnClear = new JButton("Clear");

        bottomButtons.add(btnRemove);
        bottomButtons.add(btnClear);

        rightPanel.add(bottomButtons, BorderLayout.SOUTH);

        mainPanel.add(rightPanel);
        add(mainPanel);
        playlistMp3Paths = new ArrayList<String>();
        playlistFileChooser = new JFileChooser();
        playlistFileChooser.setMultiSelectionEnabled(true); // plusieurs fichiers
        playlistFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        playlistFileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Fichiers MP3", "mp3"));

        // =============== LISTENERS (à brancher) ==============

        btnChooseMp3.addActionListener(new ChooseMp3Listener());
        btnCreatePlaylist.addActionListener(new CreatePlaylistListener());
        btnInsertIntoPlaylist.addActionListener(new InsertIntoPlaylistListener());
        btnShowPlaylist.addActionListener(new ShowPlaylistListener());
        btnRemove.addActionListener(new RemoveSongListener());
        btnClear.addActionListener(new ClearPlaylistListener());



    }

    // ================== INNER CLASS ==================

    /**
     * Listener pour choisir des fichiers MP3 à ajouter à la liste.
     */
    private class ChooseMp3Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = playlistFileChooser.showOpenDialog(PlaylistPanel.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = playlistFileChooser.getSelectedFiles();
                for (File f : selectedFiles) {
                    try {
                        // Vérification via Mp3Collector
                        if (Mp3Collector.checkFileMp3(f.getAbsolutePath())) {
                            playlistMp3Paths.add(f.getAbsolutePath());
                            playlistModel.addElement(f.getName()); // afficher dans la JList
                        }
                    } catch (FileNotFoundException ex) {
                        GUI1.showError(ex.getMessage(), PlaylistPanel.this);
                    }
                }
            }
        }
    }
    /**
     * Listener pour créer une nouvelle playlist à partir des fichiers choisis.
     */
    private class CreatePlaylistListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1) Vérification qu'au moins un MP3 a été choisi
            if (playlistMp3Paths.isEmpty()) {
                GUI1.showError("Vous devez choisir au moins un fichier MP3 avant de créer une playlist.", PlaylistPanel.this);
                return;
            }

            // 2) Boîte de dialogue pour nom et type de playlist
            JTextField nameField = new JTextField("ma_playlist");
            String[] formats = {"XSPF", "M3U8", "JSPF"};
            JComboBox<String> formatBox = new JComboBox<>(formats);
            formatBox.setSelectedIndex(0); // XSPF par défaut

            JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
            panel.add(new JLabel("Nom de la playlist :"));
            panel.add(nameField);
            panel.add(new JLabel("Format :"));
            panel.add(formatBox);

            int result = JOptionPane.showConfirmDialog(
                    PlaylistPanel.this,
                    panel,
                    "Créer une playlist",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (result != JOptionPane.OK_OPTION) return; // annulation

            String nomPlaylist = nameField.getText().trim();
            if (nomPlaylist.isEmpty()) {
                GUI1.showError("Le nom de la playlist est obligatoire.", PlaylistPanel.this);
                return;
            }

            String format = (String) formatBox.getSelectedItem();

            // 3) Choix du fichier d’enregistrement
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File(nomPlaylist));
            int saveResult = chooser.showSaveDialog(PlaylistPanel.this);
            if (saveResult != JFileChooser.APPROVE_OPTION) return;

            String chemin = chooser.getSelectedFile().getAbsolutePath().replace("\\", "/");

            // 4) Création de la playlist selon le format
            CreatePL playlist;

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
            try {
                // 5) Création et message de confirmation
                String msg = playlist.CreePlayListe(playlistMp3Paths);
                JOptionPane.showMessageDialog(
                        PlaylistPanel.this,
                        msg,
                        "Playlist créée",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception ex) {
                GUI1.showError("Erreur lors de la création de la playlist.", PlaylistPanel.this);
            }
        }
    }

    /**
     * Listener pour insérer les fichiers choisis dans une playlist existante.
     */
    private class InsertIntoPlaylistListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 1) Vérification qu'au moins un MP3 a été choisi
            if (playlistMp3Paths.isEmpty()) {
                GUI1.showError("Vous devez choisir au moins un fichier MP3 avant d'ajouter à une playlist.", PlaylistPanel.this);
                return;
            }

            // 2) Sélection du fichier de playlist existante
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Playlists (*.xspf, *.jspf, *.m3u8)", "xspf", "jspf", "m3u8"
            ));

            int result = chooser.showOpenDialog(PlaylistPanel.this);
            if (result != JFileChooser.APPROVE_OPTION) return;

            File selectedFile = chooser.getSelectedFile();
            if (!selectedFile.exists()) {
                GUI1.showError("Le fichier choisi n'existe pas !", PlaylistPanel.this);
                return;
            }

            String path = selectedFile.getAbsolutePath().replace("\\", "/");
            String ext = path.substring(path.lastIndexOf('.') + 1).toLowerCase();

            CreatePL playlist;
            try {
                switch (ext) {
                    case "m3u8" -> playlist = new M3U8(path);
                    case "jspf" -> playlist = new JSPF(path);
                    case "xspf" -> playlist = new XSPF(path);
                    default -> {
                        GUI1.showError("Format de playlist non reconnu !", PlaylistPanel.this);
                        return;
                    }
                }

                String msg = playlist.ajouterMusique(path, playlistMp3Paths);
                JOptionPane.showMessageDialog(
                        PlaylistPanel.this,
                        msg,
                        "MP3 ajoutés à la playlist",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception ex) {
                GUI1.showError("Erreur lors de l'ajout à la playlist : " + ex.getMessage(), PlaylistPanel.this);
            }
        }
    }
    /**
     * Listener pour afficher le contenu d'une playlist existante dans un JDialog.
     */
    private class ShowPlaylistListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // 1) Choix de la playlist
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Playlists (*.xspf, *.jspf, *.m3u8)", "xspf", "jspf", "m3u8"
            ));

            if (chooser.showOpenDialog(PlaylistPanel.this) != JFileChooser.APPROVE_OPTION)
                return;

            File file = chooser.getSelectedFile();
            String path = file.getAbsolutePath().replace("\\", "/");

            // 2) Déterminer le format
            CreatePL playlist;
            String ext = path.substring(path.lastIndexOf('.') + 1).toLowerCase();

            try {
                switch (ext) {
                    case "xspf" -> playlist = new XSPF(path);
                    case "jspf" -> playlist = new JSPF(path);
                    case "m3u8" -> playlist = new M3U8(path);
                    default -> {
                        GUI1.showError("Format de playlist non supporté.", PlaylistPanel.this);
                        return;
                    }
                }

                // 3) Lire les musiques
                ArrayList<String> paths = playlist.readPlayListe(path);

                if (paths.isEmpty()) {
                    GUI1.showError("La playlist est vide.", PlaylistPanel.this);
                    return;
                }

                // 4) Créer le modèle pour l'affichage
                DefaultListModel<String> model = new DefaultListModel<>();
                for (String p : paths) {
                    model.addElement(new File(p).getName());
                }

                JList<String> list = new JList<>(model);
                JScrollPane scroll = new JScrollPane(list);

                // 5) Créer le dialog
                JDialog dialog = new JDialog(
                        SwingUtilities.getWindowAncestor(PlaylistPanel.this),
                        "Musiques de la playlist",
                        Dialog.ModalityType.APPLICATION_MODAL
                );

                dialog.setLayout(new BorderLayout(10, 10));
                dialog.add(scroll, BorderLayout.CENTER);

                JButton btnClose = new JButton("Fermer");
                btnClose.addActionListener(ev -> dialog.dispose());

                JPanel bottom = new JPanel();
                bottom.add(btnClose);

                dialog.add(bottom, BorderLayout.SOUTH);

                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(PlaylistPanel.this);
                dialog.setVisible(true);

            } catch (Exception ex) {
                GUI1.showError("Erreur lors de la lecture de la playlist.", PlaylistPanel.this);
            }
        }
    }
    /**
     * Listener pour supprimer une chanson sélectionnée dans la liste.
     */
    private class RemoveSongListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            int index = playlistList.getSelectedIndex();

            if (index == -1) {
                GUI1.showError("Veuillez sélectionner une musique à supprimer.", PlaylistPanel.this);
                return;
            }

            playlistMp3Paths.remove(index);
            playlistModel.remove(index);
        }
    }

    /**
     * Listener pour vider complètement la liste de la playlist.
     */

    private class ClearPlaylistListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            playlistMp3Paths.clear();
            playlistModel.clear();
        }
    }


}
