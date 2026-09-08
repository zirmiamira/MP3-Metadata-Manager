package gestionPL;

import mp3FileException.PlatListEmpty;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe M3U8 représentant une playlist au format M3U8.
 *
 * Cette classe permet :
 *    de créer une playlist M3U8 à partir d'une liste de chemins MP3
 *    de lire une playlist M3U8 existante
 *    d'ajouter une ou plusieurs musiques à une playlist M3U8 existante
 * Le format M3U8 est un format texte simple où chaque ligne
 * représente le chemin absolu d’un fichier audio, précédé de "file:///".
 *
 * @author MOKRANE Maria
 * @version 1.0
 */
public class M3U8 extends CreatePL{

    public M3U8(String nom){
        super(nom);
    }

    @Override
    public String CreePlayListe(ArrayList<String> musics) throws PlatListEmpty{
        if (musics.isEmpty()) {
            throw new PlatListEmpty("Erreur : Playlist non créée. Aucun fichier MP3 trouvé dans votre dossier " );
        } else {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(getNomPL()));

                // Début du fichier M3U8
                writer.write("#EXTM3U\n");

                // Ajout de chaque fichier MP3 dans la playlist
                for (String m : musics) {
                       m = m.replace("\\", "/");
                        m = m.replace("/./", "/");
                        writer.write("file:///"+m + "\n"); // location
                }
                writer.close();
                return "Votre playlist M3U8 est créée avec succès";
            } catch (IOException e) {
                System.err.print(e.getMessage());
                return "Votre playlist M3U8 n'est pas créée";
            }
        }
    }

    public ArrayList<String> readPlayListe(String nomFichier) throws IOException{
        ArrayList<String> paths = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(nomFichier));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
                // Supprimer le préfixe file:/// si présent
                if (line.startsWith("file:///")) {
                    line = line.substring(8);
                }
                paths.add(line);
            }
        }
        br.close();
        return paths;
    }

    public String ajouterMusique(String nomFichier, ArrayList<String> paths) {
        if (paths.isEmpty()) {
            return "Aucune musique à ajouter.";
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomFichier, true))) { // append = true
            for (String path : paths) {
                path = path.replace("\\", "/"); // convertir les chemins Windows en '/'
                bw.write("file:///" + path.trim());
                bw.newLine();
            }
            return "Ajout des musiques effectué avec succès";
        } catch (IOException e) {
            return "L'ajout a échoué : " + e.getMessage();
        }
    }



}
