package siteParis;

/**
 * 
 * @author prou
 *
 */
public class JoueurInexistantException extends Exception {
    public JoueurInexistantException() {
        super();
     }
	public JoueurInexistantException(String motif) {
        super(motif);
     } 
}


