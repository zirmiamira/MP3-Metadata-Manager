package gestionDossier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.tika.Tika;
import mp3FileException.FolderNotFoundException;
import mp3FileException.FolderNotReadableException;
/**
 * La classe Mp3Collector permet de parcourir récursivement un dossier
 * afin de collecter uniquement les fichiers MP3 valides.
 * Lorsqu’un fichier est reconnu comme un MP3 valide, son chemin absolu est
 * enregistré dans une liste.
 *
 * @author MOKRANE Maria
 * @version 1.0
 */
public class Mp3Collector {

    // Liste des chemins absolus des fichiers MP3 trouvés
    private ArrayList<String> paths;

    /**
     * Le constructeur initialise la liste des fichiers MP3.
     */
    public Mp3Collector() {
        paths = new ArrayList<String>();
    }

    /**
     * Parcourt récursivement un dossier et ses sous-dossiers afin de
     * collecter tous les fichiers MP3 valides.
     * Les fichiers valides sont ajoutés à la liste interne.
     *
     * @param folderPath le chemin du dossier à analyser
     * @throws FolderNotFoundException si le dossier n’existe pas
     * @throws FolderNotReadableException si le dossier n’est pas lisible
     * @throws FileNotFoundException si un fichier attendu n’existe pas
     */
    public void scanFolder(String folderPath) throws FolderNotFoundException, FolderNotReadableException, FileNotFoundException {
        folderPath = folderPath.replace("\\", "/");
        File folder = new File(folderPath);

        // 1) Le dossier n'existe pas
        if (!folder.exists()) {
            throw new FolderNotFoundException("Le dossier : " + folderPath + " n'existe pas ou bien c'est un dossier");
        }

        // 2) Le dossier existe mais n'est pas lisible
        if (!folder.isDirectory() || !folder.canRead()) {
            throw new FolderNotReadableException("Le dossier '" + folderPath + "' n'est pas un vrai repertoire.");
        }

        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            String path = file.getAbsolutePath();
            if (file.isDirectory()) {
                // Appel récursif pour les sous-dossiers
                scanFolder(path);
            } else if (checkFileMp3(path)) {
                path = path.replace("\\", "/");
                paths.add(path);
            }
        }
    }

    // ---------------------- CHECK EXTENSION + MIME --------------------

    /**
     * Vérifie si un fichier donné est un MP3 valide.
     * La vérification se fait sur l’extension et le type MIME.
     *
     * @param path le chemin du fichier à vérifier
     * @return boolean true si le fichier est un MP3 valide, false sinon
     * @throws FileNotFoundException si le fichier n’existe pas ou n’est pas un fichier
     */
    public static boolean checkFileMp3(String path) throws FileNotFoundException{
        path = path.replace("\\", "/");
        File file = new File(path);

        // Vérifier que le fichier existe
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("Le fichier " + path + " n'existe pas ou n'est pas un fichier ");
        }

        // Vérifie l'extension
        String lower = path.toLowerCase();
        if (!lower.endsWith(".mp3")) return false;
        try {
            // Essaie de vérifier le type MIME
            Tika tika = new Tika();
            String mimeType = tika.detect(file);
            return "audio/mpeg".equals(mimeType);
        }catch (IOException e) {
            return false;
        }
    }

    // --------------------------- GETTERS ---------------------------

    /**
     * Retourne la liste des chemains vers les fichiers MP3 collectés.
     *
     * @return liste des chemins des fichiers MP3
     */
    public ArrayList<String> getMp3Files() {
        return paths;
    }

    public void setPaths(ArrayList<String> paths){
        this.paths=paths;
    }

    // --------------------------- toString ---------------------------

    /**
     * Retour les noms des fichiers trouvés.
     *
     * @return chaîne de caractères listant tous les fichiers MP3
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (paths.isEmpty()) {
            sb.append("Aucun fichier MP3 trouvé.\n");
        } else {
            Iterator<String> it = paths.iterator();
            while (it.hasNext()) {
                String path = it.next();
                File f = new File(path);
                String nomFichier = f.getName(); // récupère le nom du fichier seulement
                sb.append(nomFichier).append("\n");
            }
        }
        return sb.toString();
    }
}
