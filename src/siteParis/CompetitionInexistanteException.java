package siteParis;


/**
 * 
 * @author prou
 *
 */
public class CompetitionInexistanteException extends Exception {

    public CompetitionInexistanteException() {
        super();
     }
	public CompetitionInexistanteException(String motif) {
        super(motif);
     } 
}



