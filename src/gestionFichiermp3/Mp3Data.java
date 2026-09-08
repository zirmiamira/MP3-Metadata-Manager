package gestionFichiermp3;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;

/**
 * La classe Mp3Data permet de représenter les métadonnées d’un fichier MP3.
 * Elle extrait les informations telles que titre, artiste, album, année,
 * genre, compositeur et durée du fichier MP3 en utilisant la librairie jaudiotagger.
 *
 * @author ZIRMI Amira
 * @version 1.0
 */
public class Mp3Data {

    private String titre;
    private String artiste;
    private String album;
    private String annee;
    private String genre;
    private String compositeur;
    private String duree;
    private BufferedImage coverImage;

    /**
     * Constructeur de Mp3Data. Lit un fichier MP3 et extrait ses métadonnées.
     *
     * @param file le fichier MP3 à analyser
     * @throws CannotReadException si le fichier ne peut pas être lu
     * @throws IOException si une erreur d'entrée/sortie survient
     * @throws TagException si un problème survient avec les tags
     * @throws ReadOnlyFileException si le fichier est en lecture seule
     * @throws InvalidAudioFrameException si le fichier contient une frame audio invalide
     */
    public Mp3Data(File file) throws CannotReadException, IOException, TagException,
            ReadOnlyFileException, InvalidAudioFrameException {

        AudioFile audio = AudioFileIO.read(file);
        Tag tag = audio.getTag();
        if (tag != null) {
            this.titre = tag.getFirst(FieldKey.TITLE);
            this.artiste = tag.getFirst(FieldKey.ARTIST);
            this.album = tag.getFirst(FieldKey.ALBUM);
            String rawDate = tag.getFirst(FieldKey.YEAR);
            if (rawDate != null && rawDate.length() == 8) {
                String annee = rawDate.substring(0, 4);
                String mois = rawDate.substring(4, 6);
                String jour = rawDate.substring(6, 8);

                this.annee = jour + "/" + mois + "/" + annee;
            } else {
                // fallback si ce n'est qu'une année
                this.annee = rawDate;
            }

            this.genre = tag.getFirst(FieldKey.GENRE);
            this.compositeur = tag.getFirst(FieldKey.COMPOSER);

            // récupération de la pochette d'album si elle existe
            Artwork artwork = tag.getFirstArtwork();
            if (artwork != null && artwork.getBinaryData() != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(artwork.getBinaryData());
                this.coverImage = ImageIO.read(bis);
            } else {
                this.coverImage = null;
            }
        }else{
            this.titre       = "";
            this.artiste     = "";
            this.album       = "";
            this.annee       = "";
            this.genre       = "";
            this.compositeur = "";
        }
            // récupération de la durée en secondes
            AudioHeader header = audio.getAudioHeader();
            if(header !=null) {
                int dureeSecondes = header.getTrackLength();

                // conversion en minutes et secondes
                int minutes = dureeSecondes / 60;
                int secondes = dureeSecondes % 60;

                // on stocke la durée formatée en mm:ss dans l'attribut
                this.duree = minutes + ":" + (secondes < 10 ? "0" + secondes : secondes);
            }else{
                this.duree = null;
            }

    }

    /**
     * Retourne l'image de la pochette de l'album si elle existe.
     *
     * @return BufferedImage de la pochette, ou null si aucune image n'est disponible
     */
    public BufferedImage getCoverImage() {
        return coverImage;
    }



    /**
     * Sauvegarde les métadonnées dans un fichier JSON (format JSPF simplifié)
     *
     * @param cheminNom Chemin complet du fichier JSON à créer
     */
    public String sauvegarderData(String cheminNom) {
        cheminNom = cheminNom.replace("\\", "/");
        try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(cheminNom));
                writer.write(this.toString());
                writer.close();
                return " Vos meta donnees sont enregistrer dans le fichier indique  ";
            }catch (IOException e) {
                return "Erreur : problème d'entrée/sortie lors de la création du fichier.";
            }

    }



    /**
     * Retourne une représentation textuelle des métadonnées du fichier MP3.
     *
     * @return chaîne de caractères contenant toutes les informations du MP3
     */
    @Override
    public String toString() {
        return  "titre       : " + titre + "\n" +
                "artiste     : " + artiste + "\n" +
                "album       : " + album + "\n" +
                "année       : " + annee + "\n" +
                "genre       : " + genre + "\n" +
                "compositeur : " + compositeur + "\n" +
                "durée       : " + duree;
    }

}
