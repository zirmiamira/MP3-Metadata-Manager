package gestionFichiermp3;

import mp3FileException.NotMp3FileException;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import gestionDossier.Mp3Collector;

/**
 * La classe Mp3File représente un fichier MP3 spécifique.
 * Elle vérifie que le fichier existe et est bien un MP3,
 * puis extrait ses métadonnées via la classe Mp3Data.
 *
 * @author ZIRMI Amira
 * @version 1.0
 */
public class Mp3File {

    private String path;    // chemin absolu du fichier MP3
    private Mp3Data mp3data; // objet contenant les métadonnées du MP3
    private File file;       // objet File associé

    // -------------------------- CONSTRUCTEUR --------------------------

    /**
     * Constructeur de Mp3File. Vérifie que le fichier est un vrai MP3
     * et lit ses métadonnées.
     *
     * @param path le chemin du fichier MP3
     * @throws FileNotFoundException si le fichier n'existe pas
     * @throws NotMp3FileException si le fichier n'est pas un MP3 valide
     * @throws CannotReadException si le fichier ne peut pas être lu
     * @throws IOException en cas d'erreur d'entrée/sortie
     * @throws TagException si une erreur survient lors de la lecture des tags
     * @throws ReadOnlyFileException si le fichier est en lecture seule
     * @throws InvalidAudioFrameException si le fichier contient une frame audio invalide
     */
    public Mp3File(String path) throws FileNotFoundException, NotMp3FileException, CannotReadException, IOException, TagException,
            ReadOnlyFileException, InvalidAudioFrameException {

        path = path.replace("\\","/");

        // Vérification que le fichier est un MP3
        boolean estMp3 = Mp3Collector.checkFileMp3(path);
        if (estMp3) {
            this.path = path;
            file = new File(path);
            mp3data = new Mp3Data(getFile());
        } else {

            throw new NotMp3FileException("Le fichier : " + path + " n'est pas un vrai fichier .mp3");
        }
    }

    // --------------------------- GETTERS ---------------------------

    /**
     * Retourne le chemin absolu du fichier MP3.
     * @return chemin du fichier
     */
    public String getPath() { return path; }

    /**
     * Retourne les métadonnées extraites du fichier MP3.
     * @return objet Mp3Data contenant les informations du MP3
     */
    public Mp3Data getMp3data() { return mp3data; }

    /**
     * Retourne l'objet File associé au fichier MP3.
     * @return fichier MP3
     */
    public File getFile() { return file; }

    // --------------------------- toString ---------------------------

    /**
     * Retourne une représentation textuelle du fichier MP3,
     * incluant son chemin et ses métadonnées.
     * @return chaîne descriptive du MP3
     */
    @Override
    public String toString() {
        return "fichier     :   " + getPath() + "\n" + getMp3data().toString();
    }
}
