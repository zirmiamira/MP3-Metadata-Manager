package mp3FileException;

/**
 * Exception personnalisée lancée lorsqu'un fichier
 * n'est pas un vrai fichier MP3.
 *
 * @version 1.0
 */
public class NotMp3FileException extends RuntimeException {

    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message décrivant l'erreur
     */
    public NotMp3FileException(String message) {
        super("Erreur :"+message);
    }
}
