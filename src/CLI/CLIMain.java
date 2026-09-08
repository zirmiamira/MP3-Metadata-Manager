package CLI;

import gestionDossier.Mp3Collector;
import gestionFichiermp3.Mp3File;
import gestionPL.*;

// Import des différentes exceptions personnalisées
import mp3FileException.FolderNotFoundException;
import mp3FileException.FolderNotReadableException;
import mp3FileException.NotMp3FileException;
import mp3FileException.PlatListEmpty;

// Import des exceptions liées à la lecture MP3 via jaudiotagger
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * La classe CLIMain constitue l’interface principale du mode console.
 * Elle interprète les commandes saisies par l’utilisateur,
 * orchestre la logique de l’application, affiche les informations pertinentes,
 * gère toutes les erreurs pour garantir une exécution robuste.
 * Elle sert ainsi de point d’entrée entre l’utilisateur et les fonctionnalités du programme.
 *
 * @author ZIRMI Amira et MOKRANE Maria
 * @version 1.0
 */
public class CLIMain {




    /**
     * Constructeur par défaut.
     */
    public CLIMain() { }

    /**
     * Affiche la liste des commandes disponibles dans le mode console.
     * Permet à l’utilisateur de comprendre les options d’exécution
     * et la syntaxe des paramètres attendus.
     */
    public void printHelp() {
        System.out.println();
        System.out.println("Modes d'utilisation (console) :");
        System.out.println();
        System.out.println("  -h | --help");
        System.out.println("      Affiche l'aide.");
        System.out.println();
        System.out.println("  -d <dossier>");
        System.out.println("      Parcourt récursivement le dossier spécifié et affiche");
        System.out.println("      tous les fichiers MP3 valides trouvés.");
        System.out.println();
        System.out.println("  -f <fichier.mp3>");
        System.out.println("      Affiche les métadonnées du fichier MP3 donné.");
        System.out.println();
        System.out.println("  -d <dossier> -o <fichier_cible.format>");
        System.out.println("      Génère une playlist à partir du dossier spécifié,");
        System.out.println("      en utilisant le format par défaut XSPF,");
        System.out.println("      et l'enregistre dans le fichier cible.");
        System.out.println();
        System.out.println("  -d <dossier> -format <xspf|m3u|txt> -o <fichier_cible.format>");
        System.out.println("      Génère une playlist à partir du dossier spécifié,");
        System.out.println("      en utilisant le format choisi (XSPF, JSPF ou M3U8),");
        System.out.println("      et l'enregistre dans le fichier cible.");
        System.out.println();
        System.out.println("Mode graphique :");
        System.out.println("  java -jar chemain/gui.jar");
        System.out.println("      Lance l'interface graphique.");
        System.out.println();
    }

    /**
     * Explore un répertoire donné et analyse son contenu pour détecter
     * les fichiers MP3 valides. Affiche les fichiers trouvés ainsi que
     * les éventuelles erreurs liées à la lecture du dossier.
     *
     * @param rep le chemin du répertoire à analyser
     */
    public void exploreRep(String rep){
        Mp3Collector coll = new Mp3Collector();
        try{
            coll.scanFolder(rep);
            System.out.println(coll);

        } catch (FolderNotFoundException e) {
            System.out.println(e.getMessage());

        } catch (FolderNotReadableException e){
            System.out.println( e.getMessage());

        } catch (FileNotFoundException e) {
            System.out.println("Erreur : Fichier introuvable");
        }
    }

    /**
     * Affiche les métadonnées d’un fichier MP3 spécifique. Lit le fichier,
     * extrait les informations comme le titre, l’artiste...,
     * et les affiche à l’utilisateur.
     *
     * @param nomf le chemin du fichier MP3 à analyser
     */
    public void displayMetaDonnee(String nomf){
        try {
            Mp3File file = new Mp3File(nomf);
            System.out.println();
            System.out.println(file);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());

        } catch (NotMp3FileException e) {
            System.out.println(e.getMessage());

        } catch (CannotReadException e) {
            System.out.println("Erreur :  Impossible de lire le fichier MP3  ");

        } catch (IOException e) {
            System.out.println("Erreur :  d'accès au fichier ");

        } catch (TagException e) {
            System.out.println("Erreur :  dans les tags ID3 " + e.getMessage());

        } catch (ReadOnlyFileException e) {
            System.out.println("Erreur :  Le fichier est en lecture seule ");

        } catch (InvalidAudioFrameException e) {
            System.out.println("Erreur : Bloc audio invalide " + e.getMessage());
        }
    }

    /**
     * Crée une playlist dans le format spécifié (XSPF, M3U8 ou JSPF)
     * à partir du dossier contenant les fichiers MP3.
     * Génère le fichier de playlist et gère les erreurs liées à l’accès disque ou aux formats.
     *
     * @param format       le format de playlist désiré (xspf, m3u8, jspf)
     * @param folderName   le dossier où chercher les fichiers MP3
     * @param playlistName le nom du fichier playlist à générer
     */
    public void creatplaylist(String format ,String folderName, String playlistName) {

        String expectedExt = null;
        System.out.println();
        if (format.equals("--xspf") || format.equals("-xspf")) {
            expectedExt = ".xspf";

        } else if (format.equals("--m3u8") || format.equals("-m3u8")) {
            expectedExt = ".m3u8";

        } else if (format.equals("--jspf") || format.equals("-jspf")) {
            expectedExt = ".jspf";

        } else {
            System.out.println("Erreur : format invalide. Formats valides : xspf, m3u8, jspf");
            return;
        }

        if (!playlistName.endsWith(expectedExt)) {
            playlistName+=expectedExt;
        }

        try {
            CreatePL pl ;
            Mp3Collector files = new Mp3Collector();
            files.scanFolder(folderName);
            ArrayList<String> paths = files.getMp3Files();
            switch (format) {
                case "--xspf":
                case "-xspf":
                    pl= new XSPF(playlistName);
                    System.out.println(pl.CreePlayListe(paths ));
                    break;

                case "--m3u8":
                case "-m3u8":
                    pl= new M3U8(playlistName);
                    System.out.println(pl.CreePlayListe(paths ));
                    break;

                case "--jspf":
                case "-jspf":
                    pl= new JSPF(playlistName);
                    System.out.println(pl.CreePlayListe(paths));
                    break;
            }

        } catch (FolderNotFoundException e) {
            System.out.println(e.getMessage());

        } catch (FolderNotReadableException e) {
            System.out.println(e.getMessage());

        } catch (FileNotFoundException e) {
            System.out.println("Erreur : Fichier introuvable");
        }catch (PlatListEmpty e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Point d'entrée de l'application en mode console.
     * Analyse les arguments passés à la ligne de commande et appelle
     * les méthodes appropriées de CLIMain pour afficher de l'aide,
     * explorer un dossier, afficher les métadonnées d'un MP3
     * ou créer/afficher une playlist.
     *
     * @param args les arguments de la ligne de commande
     */

    public static void main(String[] args) {

        Logger.getLogger("").setLevel(Level.OFF);
        CLIMain cli = new CLIMain();
        if (args.length ==0) {
            System.out.println("Erreur : paramètres manquants.");
            System.out.println("Tapez -h ou --help pour afficher l'aide.");
        }else{
            switch (args[0]) {
                case "-h":
                case "--help":
                    cli.printHelp();
                    break;
                case "-f":
                    if (args.length < 2) {
                        System.out.println("Erreur : vous devez fournir le nom d'un fichier MP3.");
                        System.out.println("Tapez -h ou --help pour afficher l'aide.");
                    } else {
                        cli.displayMetaDonnee(args[1]);
                    }
                    break;
                case "-d":

                    //  Aucun dossier fourni
                    if (args.length < 2) {
                        System.out.println("Erreur : aucun nom de dossier fourni.");
                        System.out.println("Tapez -h ou --help pour afficher l'aide.");
                        break;
                    }


                    //  Cas simple : "-d <dossier>"
                    String nomDossier =args[1];
                    if (args.length == 2) {
                        cli.exploreRep(nomDossier);
                        break;
                    }

                    if (args.length < 4 ) {
                        System.out.println("Erreur : vous devez fournir un nom pour la playlist avec l'option -o.");
                        System.out.println("Tapez -h ou --help pour afficher l'aide.");
                        break;
                    }
                    String nomf;
                    String format;

                    if(args.length == 4 && args[2].equals("-o") ) {
                        format= "--xspf";
                        nomf = args[3];
                    } else{
                        if(args.length == 4 && !args[2].equals("-o") ){
                            System.out.println("Erreur : vous devez fournir un nom pour la playlist avec l'option -o.");
                            System.out.println("Tapez -h ou --help pour afficher l'aide.");
                            return;
                        }else{

                            format= args[2];
                            nomf = args[4];
                        }
                    }


                    //Cas complet : "-d <dossier> <format> -o <nom_playlist>"
                    cli.creatplaylist(format, nomDossier,nomf);
                    break;

                default:
                    System.out.println("Argument inconnu : " + args[0]);
                    System.out.println("Tapez -h ou --help pour afficher l'aide.");
            }


        }

    }


}
