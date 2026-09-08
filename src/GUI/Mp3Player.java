package GUI;


import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Lecteur MP3 simple utilisant la bibliothèque JLayer.
 * Permet de lire un fichier MP3 dans un thread séparé,
 * d'arrêter la lecture et de vérifier si un morceau est en cours.
 * @author ZIRMI Amira
 * @version 1.0
 */
public class Mp3Player {

    private Player player;
    private Thread playerThread;

    /**
     * Lit un fichier MP3 en lançant un thread simple.
     * @param filePath chemin du fichier MP3
     */
    public void play(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            final Player localPlayer = new Player(fis); // variable locale finale pour le thread
            player = localPlayer;

            playerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        localPlayer.play(); // on utilise la variable locale, plus sûr
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            );

            playerThread.start();

        } catch (FileNotFoundException e) {
            System.err.println("Fichier introuvable : " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Arrête la lecture en cours si un fichier est joué.
     */
    public void stop() {
        if (player != null) {
            player.close();
        }
        playerThread = null;
        player = null;
    }


    /**
     * Vérifie si un morceau est actuellement en cours de lecture.
     * @return true si le MP3 est en cours de lecture, false sinon
     */

    public boolean isPlaying() {
        return playerThread != null && playerThread.isAlive();
    }
}
