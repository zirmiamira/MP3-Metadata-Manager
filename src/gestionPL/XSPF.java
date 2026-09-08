package gestionPL;
import mp3FileException.PlatListEmpty;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe XSPF représentant une playlist au format XSPF (XML Shareable Playlist Format).
 *
 * Cette classe permet :
 *    de créer une playlist XSPF à partir d'une liste de fichiers MP3
 *    de lire une playlist XSPF existante
 *    d'ajouter une ou plusieurs musiques à une playlist XSPF existante
 *
 * Le format XSPF est un format XML structuré reposant sur les balises
 * @author ZIRMI Amira
 * @version 1.0
 */
public class XSPF extends CreatePL{

    public XSPF(String nom){
        super(nom);
    }

    @Override
    public String CreePlayListe(ArrayList<String> musics) throws PlatListEmpty{
        if (musics.isEmpty()){
            throw new PlatListEmpty("Erreur  :  PlayList non cree. Aucun fichier mp3 trouver dans votre dossier  " );
        }else {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(getNomPL()));

                // Début du fichier XSPF
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<playlist version=\"1\" xmlns=\"http://xspf.org/ns/0/\">\n");
                writer.write("  <trackList>\n");

                // Ajout de chaque fichier MP3 dans la playlist
                for (String m : musics) {
                        writer.write("    <track>\n");
                        writer.write("      <location>file:///" + m.trim() + "</location>\n");
                        writer.write("    </track>\n");

                }
                writer.write("  </trackList>\n");
                writer.write("</playlist>\n");
                writer.close();
                return "Votre playliste est cree avec succee";
            } catch (IOException e) {
                return "Erreur : Votre playliste n'est pas cree  ( "+e.getMessage()+" )";
            }
        }
    }

    public ArrayList<String> readPlayListe(String nomFichier) throws IOException {
        ArrayList<String> paths = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(nomFichier));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            String path = null;

            // Vérifie si la ligne contient <location> et </location> sur la même ligne
            if (line.contains("<location>") && line.contains("</location>")) {
                path = line.substring(line.indexOf("<location>") + 10, line.indexOf("</location>")).trim();
            }

            // Supprime le préfixe file:/// si présent
            if (path != null && path.startsWith("file:///")) {
                path = path.substring(8);
            }

            if (path != null && !path.isEmpty()) {
                paths.add(path);
            }
        }

        br.close();
        return paths;
    }

    public String ajouterMusique(String nomFichier, ArrayList<String> nouvellesMusiques) {
        if (nouvellesMusiques.isEmpty()) {
            return "Aucune musique à ajouter.";
        }

        ArrayList<String> lignes = new ArrayList<>();

        // Lire le fichier existant
        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String line;
            while ((line = br.readLine()) != null) {
                lignes.add(line);
            }
        } catch (IOException e) {
            return "L'ajout a échoué (lecture) : " + e.getMessage();
        }

        // Trouver la position de </trackList>
        int index = -1;
        for (int i = 0; i < lignes.size(); i++) {
            if (lignes.get(i).trim().equals("</trackList>")) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return "Impossible d'ajouter les musiques : balise </trackList> introuvable";
        }

        // Ajouter chaque musique avant </trackList>
        for (int i = 0; i < nouvellesMusiques.size(); i++) {
            String m = nouvellesMusiques.get(i).replace("\\", "/");
            String track = "    <track>\n      <location>file:///" + m.trim() + "</location>\n    </track>";
            lignes.add(index + i, track); // insérer successivement
        }

        // Réécrire le fichier
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomFichier))) {
            for (String l : lignes) {
                bw.write(l);
                bw.newLine();
            }
            return "Ajout des musiques effectué avec succès";
        } catch (IOException e) {
            return "L'ajout a échoué (écriture) : " + e.getMessage();
        }
    }



}
