package mp3FileException;

/**
 * Exception personnalisée lancée lorsqu'un dossier
 * existe mais n'est pas lisible.
 * Étend RuntimeException pour signaler une erreur d'exécution.
 *
 * @version 1.0
 */
public class FolderNotReadableException extends RuntimeException {

    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message décrivant l'erreur
     */
    public FolderNotReadableException(String message) {
        super("Erreur:  Impossible de lire le Dossier");
    }
}
