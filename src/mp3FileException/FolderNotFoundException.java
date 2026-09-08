package mp3FileException;

/**
 * Exception personnalisée lancée lorsqu'un dossier
 * spécifié n'existe pas.
 * Étend RuntimeException pour signaler une erreur d'exécution.
 *
 * @version 1.0
 */
public class FolderNotFoundException extends RuntimeException {

    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message décrivant l'erreur
     */
    public FolderNotFoundException(String message) {
        super( "Erreur:  Dossier introuvable");
    }
}
