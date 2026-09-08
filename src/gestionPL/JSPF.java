package gestionPL;

import gestionDossier.Mp3Collector;
import mp3FileException.PlatListEmpty;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe JSPF représentant une playlist au format JSPF (JSON).
 * Cette classe permet :
 *   de créer une playlist JSPF à partir d'une liste de chemins MP3
 *   de lire une playlist JSPF existante
 *   d'ajouter une ou plusieurs musiques à une playlist JSPF existante
 * Le format JSPF est basé sur le JSON et utilise la clé "location"
 * pour stocker le chemin absolu des fichiers audio.
 *
 * @author MOKRANE Maria
 * @version 1.0
 */
public class JSPF extends CreatePL{

    public JSPF(String nom){
        super(nom);
    }

    @Override
    public String CreePlayListe(ArrayList<String> musics) throws PlatListEmpty {
        if (musics.isEmpty()) {
            throw new PlatListEmpty("Erreur : Playlist non créée. Aucun fichier MP3 trouvé dans votre dossier ");
        } else {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(getNomPL()));

                // Début du fichier JSPF
                writer.write("{\n");
                writer.write("  \"playlist\": {\n");
                writer.write("    \"title\": \"" + getNomPL() + "\",\n");
                writer.write("    \"track\": [\n");

                // Ajout de chaque fichier MP3 dans la playlist
                for (int i = 0; i < musics.size(); i++) {
                    String m = musics.get(i).replace("\\", "/");

                        writer.write("      {\n");
                        writer.write("        \"location\": \"file:///" + m + "\"\n");
                        writer.write("      }");

                    // Virgule sauf pour le dernier élément
                    if (i < musics.size() - 1) {
                        writer.write(",");
                    }
                    writer.write("\n");
                }
                writer.write("    ]\n");
                writer.write("  }\n");
                writer.write("}\n");

                writer.close();
                return "Votre playlist JSPF est créée avec succès";
            } catch (IOException e) {
                System.err.print(e.getMessage());
                return "Votre playlist JSPF n'est pas créée";
            }
        }
    }
    public ArrayList<String> readPlayListe(String nomFichier) throws IOException{
        ArrayList<String> paths = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(nomFichier));
        String line;

        while ((line = br.readLine()) != null) {
            line = line.trim();

            // Vérifie si la ligne contient "location"
            if (line.startsWith("\"location\"")) {
                line=line.substring(10).trim(); //supprimer  "location"
                line=line.substring(1).trim(); //supprimer :
                line=line.substring(1).trim(); //supprimer le premier "
                if (line.startsWith("file:///")) {
                    line = line.substring(8).trim(); // supprimer file:///
                }
                if (line.endsWith("\"")) {
                    line = line.substring(0, line.length() - 1); // supprimer le dernier "
                }
                try{
                    boolean estMp3 = Mp3Collector.checkFileMp3(line);
                    if(estMp3) {
                        paths.add(line);
                    }
                }catch(FileNotFoundException e){

                }

            }
        }
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

        // Trouver la fermeture du tableau "track"
        int index = -1;
        for (int i = lignes.size() - 1; i >= 0; i--) {
            if (lignes.get(i).trim().equals("]")) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return "Impossible d'ajouter les musiques : tableau 'track' introuvable";
        }

        // Vérifier la virgule sur la dernière entrée existante
        if (index > 1) {
            String prevLine = lignes.get(index - 1).trim();
            if (!prevLine.endsWith(",")) {
                lignes.set(index - 1, lignes.get(index - 1) + ",");
            }
        }

        // Ajouter toutes les nouvelles musiques
        for (int i = 0; i < nouvellesMusiques.size(); i++) {
            String m = nouvellesMusiques.get(i).replace("\\", "/");
            String nouvelleLigne = "      {\n        \"location\": \"file:///" + m.trim() + "\"\n      }";

            // Ajouter une virgule sauf pour la dernière musique ajoutée
            if (i < nouvellesMusiques.size() - 1) {
                nouvelleLigne += ",";
            }

            lignes.add(index + i, nouvelleLigne);
        }

        // Supprimer la virgule éventuelle de la dernière entrée du tableau
        int dernierIndex = index + nouvellesMusiques.size() - 1;
        String dernier = lignes.get(dernierIndex).trim();
        if (dernier.endsWith(",")) {
            lignes.set(dernierIndex, lignes.get(dernierIndex).substring(0, lignes.get(dernierIndex).length() - 1));
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

