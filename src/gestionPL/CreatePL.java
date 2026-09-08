package gestionPL;


import mp3FileException.PlatListEmpty;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe abstraite représentant une playlist.
 * Elle définit les méthodes pour créer, ajouter des mp3 et  afficher des playlists
 *
 * @author ZIRMI Amira et MOKRANE Maria
 * @version 1.0
 */
public abstract class CreatePL {

    private String nomPL; // nom de la playlist

    /**
     * Constructeur de CreatePL.
     *
     * @param nomPL le nom du fichier playlist
     */
    public CreatePL(String nomPL){
        this.nomPL = nomPL;
    }

    /**
     * Retourne le nom de la playlist.
     * @return nom de la playlist
     */
    public String getNomPL() {
        return nomPL;
    }



    /**
     * Crée une playlist à partir d'une liste de chemins MP3.
     *
     * @param paths liste des chemins vers les fichiers MP3
     * @return message indiquant le résultat de la création
     * @throws PlatListEmpty si la liste est vide
     */
    public abstract String CreePlayListe(ArrayList<String> paths) throws PlatListEmpty;

    /**
     * Lit une playlist existante et retourne les chemins des musiques.
     *
     * @param nomFichier nom du fichier playlist
     * @return liste des chemins MP3
     * @throws IOException en cas d'erreur de lecture
     */
    public abstract ArrayList<String> readPlayListe(String nomFichier) throws IOException;

    /**
     * Ajoute une ou plusieurs musiques à une playlist existante.
     *
     * @param nomFichier nom du fichier playlist
     * @param nouvellesMusiques chemins des musiques à ajouter
     * @return message indiquant le résultat de l'ajout
     */
    public abstract String ajouterMusique(String nomFichier, ArrayList<String> nouvellesMusiques);



}
