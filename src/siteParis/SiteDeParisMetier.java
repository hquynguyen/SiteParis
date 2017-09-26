package siteParis;


import java.util.LinkedList;
import java.util.Random;


/**
 * 
 * @author Bernard Prou et Julien Mallet
 * <br><br>
 * La classe qui contient toutes les méthodes "Métier" de la gestion du site de paris. 
 * <br><br>
 * Dans toutes les méthodes :
 * <ul>
 * <li>un paramètre de type <code>String</code> est invalide si il n'est pas instancié.</li>
 *  <li>pour la validité d'un password de gestionnaire et d'un password de joueur :
 * <ul>
 * <li>       lettres et chiffres sont les seuls caractères autorisés </li>
 * <li>       il doit avoir une longueur d'au moins 8 caractères </li>
 * </ul></li>       
 * <li>pour la validité d'un pseudo de joueur  :
 * <ul>
 * <li>        lettres et chiffres sont les seuls caractères autorisés  </li>
 * <li>       il doit avoir une longueur d'au moins 4 caractères</li>
 * </ul></li>       
 * <li>pour la validité d'un prénom de joueur et d'un nom de joueur :
 *  <ul>
 *  <li>       lettres et tiret sont les seuls caractères autorisés  </li>
 *  <li>      il  doit avoir une longueur d'au moins 1 caractère </li>
 * </ul></li>
 * <li>pour la validité d'une compétition  :       
 *  <ul>
 *  <li>       lettres, chiffres, point, trait d'union et souligné sont les seuls caractères autorisés </li>
 *  <li>      elle  doit avoir une longueur d'au moins 4 caractères</li>
 * </ul></li>       
 * <li>pour la validité d'un compétiteur  :       
 *  <ul>
 *  <li>       lettres, chiffres, trait d'union et souligné sont les seuls caractères autorisés </li>
 *  <li>      il doit avoir une longueur d'au moins 4 caractères.</li>
 * </ul></li></ul>
 */


public class SiteDeParisMetier {
	

	private String passwordGestionnaire;
	private LinkedList<Joueur> joueurs;
	private LinkedList<Competition> competitions;
	

	/**
	 * constructeur de <code>SiteDeParisMetier</code>. 
	 * 
	 * @param passwordGestionnaire   le password du gestionnaire.   
	 *
	 * @throws MetierException  levée 
	 * si le <code>passwordGestionnaire</code>  est invalide 
	 */
	public SiteDeParisMetier(String passwordGestionnaire) throws MetierException {
		
		validitePasswordGestionnaire(passwordGestionnaire);
		this.passwordGestionnaire = passwordGestionnaire;
		this.joueurs = new LinkedList<Joueur>(); 
		this.competitions = new LinkedList<Competition>();
	}

	/**
	 * inscrire un joueur. 
	 * 
	 * @param nom   le nom du joueur 
	 * @param prenom   le prénom du joueur   
	 * @param pseudo   le pseudo du joueur  
	 * @param passwordGestionnaire  le password du gestionnaire du site  
	 * 
	 * @throws MetierException   levée  
	 * si le <code>passwordGestionnaire</code> proposé est invalide,
	 * si le <code>passwordGestionnaire</code> est incorrect.
	 * @throws JoueurExistantException   levée si un joueur existe avec les mêmes noms et prénoms ou le même pseudo.
	 * @throws JoueurException levée si <code>nom</code>, <code>prenom</code>, <code>pseudo</code> sont invalides.
	 * 
	 * @return le mot de passe (déterminé par le site) du nouveau joueur inscrit.
	 * @throws CompetitionException 
	 */
	public String inscrireJoueur(String nom, String prenom, String pseudo, String passwordGestionnaire) throws MetierException, JoueurExistantException, JoueurException {

		validitePasswordGestionnaire(passwordGestionnaire);
		if (!passwordGestionnaire.equals(this.passwordGestionnaire))
			throw new MetierException();
		 
		if (!validiteJoueurNom(nom) || !validiteJoueurNom(prenom) || !validitePseudo(pseudo))
			throw new JoueurException();
		
		for (Joueur j : joueurs){
			if(j.getNom().equals(nom) && j.getPrenom().equals(prenom))
				throw new JoueurExistantException();
			if(j.getPseudo().equals(pseudo))
				throw new JoueurExistantException();
		}
		
		Joueur newJoueur = new Joueur(nom, prenom, pseudo);
		this.joueurs.add(newJoueur);
		newJoueur.setPasswordJoueur(this.generePassword());
		
		return newJoueur.getPasswordJoueur();
	}

	/**
	 * supprimer un joueur. 
	 * 
	 * @param nom   le nom du joueur 
	 * @param prenom   le prénom du joueur   
	 * @param pseudo   le pseudo du joueur  
	 * @param passwordGestionnaire  le password du gestionnaire du site  
	 * 
	 * @throws MetierException
	 * si le <code>passwordGestionnaire</code>  est invalide,
	 * si le <code>passwordGestionnaire</code> est incorrect.
	 * @throws JoueurInexistantException   levée si il n'y a pas de joueur  avec le même <code>nom</code>, <code>prenom</code>  et <code>pseudo</code>.
	 * @throws JoueurException levée 
	 * si le joueur a des paris en cours,
	 * si <code>nom</code>, <code>prenom</code>, <code>pseudo</code> sont invalides.
	 * 
	 * @return le nombre de jetons à rembourser  au joueur qui vient d'être désinscrit.
	 * @throws CompetitionException 
	 * 
	 */
	public long desinscrireJoueur(String nom, String prenom, String pseudo, String passwordGestionnaire) throws MetierException, JoueurInexistantException, JoueurException {
		
		validitePasswordGestionnaire(passwordGestionnaire);
		if (!passwordGestionnaire.equals(this.passwordGestionnaire))
			throw new MetierException();
		
		if (!validiteJoueurNom(nom) || !validiteJoueurNom(prenom) || !validitePseudo(pseudo))
			throw new JoueurException();
		
		if (!joueurs.contains(new Joueur(nom,prenom,pseudo))){
			throw new JoueurInexistantException();
		}
		long remainedJeton = 0;
		for (Joueur j : joueurs){
			if (j.getPseudo().equals(pseudo)){
				if (j.getMiseJetons() > 0) // check if joueur still has bets in progress (le joueur a des paris en cours)
					throw new JoueurException();
				remainedJeton = j.getJetons();
				joueurs.remove(j);
				break;
			}
		}
		return remainedJeton;
	}

	/**
	 * ajouter une compétition.  
	 * 
	 * @param competition le nom de la compétition
	 * @param dateCloture   la date à partir de laquelle il ne sera plus possible de miser  
	 * @param competiteurs   les noms des différents compétiteurs de la compétition 
	 * @param passwordGestionnaire  le password du gestionnaire du site 
	 * 
	 * @throws MetierException levée si le tableau des
	 * compétiteurs n'est pas instancié, si le
	 * <code>passwordGestionnaire</code> est invalide, si le
	 * <code>passwordGestionnaire</code> est incorrect.
	 * @throws CompetitionExistanteException levée si une compétition existe avec le même nom. 
	 * @throws CompetitionException levée si le nom de la
	 * compétition ou des compétiteurs sont invalides, si il y a
	 * moins de 2 compétiteurs, si un des competiteurs n'est pas instancié,
	 * si deux compétiteurs ont le même nom, si la date de clôture 
	 * n'est pas instanciée ou est dépassée.
	 * @throws JoueurException 
	 */
	public void ajouterCompetition(String competition, DateFrancaise dateCloture, String [] competiteurs, String passwordGestionnaire) throws MetierException, CompetitionExistanteException, CompetitionException  {
		
		validitePasswordGestionnaire(passwordGestionnaire);
		if (!passwordGestionnaire.equals(this.passwordGestionnaire))
			throw new MetierException();
		
		if (competiteurs == null)
			throw new MetierException();
		
		if (!validiteCompetitionNom(competition))
			throw new CompetitionException();
		
		for (String c : competiteurs){
			if(!validiteCompetiteurNom(c))
				throw new CompetitionException();
		}
		for (Competition c : competitions){
			if (c.getCompetitionNom().equals(competition))
				throw new CompetitionExistanteException();
		}		
		this.competitions.add(new Competition(competition, dateCloture, competiteurs));
	}



	/**
	 * créditer le compte en jetons d'un joueur du site de paris.
	 * 
	 * @param nom   le nom du joueur 
	 * @param prenom   le prénom du joueur   
	 * @param pseudo   le pseudo du joueur  
	 * @param sommeEnJetons   la somme en jetons à créditer  
	 * @param passwordGestionnaire  le password du gestionnaire du site  
	 * 
	 * @throws MetierException   levée 
	 * si le <code>passwordGestionnaire</code>  est invalide,
	 * si le <code>passwordGestionnaire</code> est incorrect,
	 * si la somme en jetons est négative.
	 * @throws JoueurException levée  
	 * si <code>nom</code>, <code>prenom</code>,  <code>pseudo</code> sont invalides.
	 * @throws JoueurInexistantException   levée si il n'y a pas de joueur  avec les mêmes nom,  prénom et pseudo.
	 * @throws CompetitionException 
	 */
	public void crediterJoueur(String nom, String prenom, String pseudo, long sommeEnJetons, String passwordGestionnaire) throws MetierException, JoueurException, JoueurInexistantException {
		
		validitePasswordGestionnaire(passwordGestionnaire);
		if (!passwordGestionnaire.equals(this.passwordGestionnaire))
			throw new MetierException();
		
		if (sommeEnJetons < 0)
			throw new MetierException();
		
		if (!validiteJoueurNom(nom) || !validiteJoueurNom(prenom) || !validitePseudo(pseudo))
			throw new JoueurException();
		
		if (!joueurs.contains(new Joueur(nom, prenom, pseudo))){
			throw new JoueurInexistantException();
		}
		
		for (Joueur j : joueurs){
			if (j.getPseudo().equals(pseudo)){
				j.crediter(sommeEnJetons);
				break;
			}
		}
	}


	/**
	 * débiter le compte en jetons d'un joueur du site de paris.
	 * 
	 * @param nom   le nom du joueur 
	 * @param prenom   le prénom du joueur   
	 * @param pseudo   le pseudo du joueur  
	 * @param sommeEnJetons   la somme en jetons à débiter  
	 * @param passwordGestionnaire  le password du gestionnaire du site  
	 * 
	 * @throws MetierException   levée 
	 * si le <code>passwordGestionnaire</code>  est invalide,
	 * si le <code>passwordGestionnaire</code> est incorrect,
	 * si la somme en jetons est négative.
	 * @throws JoueurException levée  
	 * si <code>nom</code>, <code>prenom</code>,  <code>pseudo</code> sont invalides 
	 * si le compte en jetons du joueur est insuffisant (il deviendrait négatif).
	 * @throws JoueurInexistantException   levée si il n'y a pas de joueur  avec les mêmes nom,  prénom et pseudo.
	 * @throws CompetitionException 
	 * 
	 */

	public void debiterJoueur(String nom, String prenom, String pseudo, long sommeEnJetons, String passwordGestionnaire) throws  MetierException, JoueurInexistantException, JoueurException {
		
		validitePasswordGestionnaire(passwordGestionnaire);
		if (!passwordGestionnaire.equals(this.passwordGestionnaire))
			throw new MetierException();
		
		if (sommeEnJetons < 0)
			throw new MetierException();
		
		if (!validiteJoueurNom(nom) || !validiteJoueurNom(prenom) || !validitePseudo(pseudo))
			throw new JoueurException();
		
		if (!joueurs.contains(new Joueur(nom, prenom, pseudo))){
			throw new JoueurInexistantException();
		}
		
		for (Joueur j : joueurs){
			if (j.getPseudo().equals(pseudo)){
				j.debiter(sommeEnJetons);
				break;
			}
		}
	}


	/**
	 * miserVainqueur  (parier sur une compétition, en désignant un vainqueur).
	 * Le compte du joueur est débité du nombre de jetons misés.
	 * 
	 * @param pseudo   le pseudo du joueur  
	 * @param passwordJoueur  le password du joueur  
	 * @param miseEnJetons   la somme en jetons à miser  
	 * @param competition   le nom de la compétition  relative au pari effectué
	 * @param vainqueurEnvisage   le nom du compétiteur  sur lequel le joueur mise comme étant le  vainqueur de la compétition  
	 * 
	 * @throws MetierException levée si la somme en jetons est négative.
	 * @throws JoueurInexistantException levée si il n'y a pas de
	 * joueur avec les mêmes pseudos et password.
	 * @throws CompetitionInexistanteException   levée si il n'existe pas de compétition de même nom. 
	 * @throws CompetitionException levée 
	 * si <code>competition</code> ou <code>vainqueurEnvisage</code> sont invalides,
	 * si il n'existe pas un compétiteur de  nom <code>vainqueurEnvisage</code> dans la compétition,
	 * si la compétition n'est plus ouverte (la date de clôture est dans le passé).
	 * @throws JoueurException   levée 
	 * si <code>pseudo</code> ou <code>password</code> sont invalides, 
	 * si le <code>compteEnJetons</code> du joueur est insuffisant (il deviendrait négatif).
	 */
    public void miserVainqueur(String pseudo, String passwordJoueur, long miseEnJetons, String competition, String vainqueurEnvisage) throws MetierException, JoueurInexistantException, CompetitionInexistanteException, CompetitionException, JoueurException  {
    	
		if (miseEnJetons < 0)
			throw new MetierException();
		
		Joueur joueur = verifierJoueurExistant(pseudo,passwordJoueur); // return joueur if Existence
		Competition c = verifierCompetitionExistant(competition); // return competition if Existence
		if (c.getDateCloture().estDansLePasse())				  // check if competition date is already closed
			throw new CompetitionException();
		
		Competiteur vainqueur = verifierCompetiteurExistant(c, vainqueurEnvisage);
		
		c.miser(joueur, miseEnJetons, vainqueur); // competition object will use its miser method to manage all belonged paris
			
	}


	/**
	 * solder une compétition vainqueur (compétition avec vainqueur).  
	 * 
	 * Chaque joueur ayant misé sur cette compétition
	 * en choisissant ce compétiteur est crédité d'un nombre de
	 * jetons égal à :
	 * 
	 * (le montant de sa mise * la somme des
	 * jetons misés pour cette compétition) / la somme des jetons
	 * misés sur ce compétiteur.
	 *
	 * Si aucun joueur n'a trouvé le
	 * bon compétiteur, des jetons sont crédités aux joueurs ayant
	 * misé sur cette compétition (conformément au montant de
	 * leurs mises). La compétition est "supprimée" si il ne reste
	 * plus de mises suite à ce solde.
	 * 
	 * @param competition   le nom de la compétition  
	 * @param vainqueur   le nom du vainqueur de la compétition 
	 * @param passwordGestionnaire  le password du gestionnaire du site 
	 * 
	 * @throws MetierException   levée 
	 * si le <code>passwordGestionnaire</code>  est invalide,
	 * si le <code>passwordGestionnaire</code> est incorrect.
	 * @throws CompetitionInexistanteException   levée si il n'existe pas de compétition de même nom.
	 * @throws CompetitionException levée 
	 * si le nom de la compétition ou du vainqueur est invalide, 
	 * si il n'existe pas de compétiteur du nom du vainqueur dans la compétition,
	 * si la date de clôture de la compétition est dans le futur.
	 * 
	 */	
	public void solderVainqueur(String competition, String vainqueur, String passwordGestionnaire) throws MetierException,  CompetitionInexistanteException, CompetitionException  {
		
		validitePasswordGestionnaire(passwordGestionnaire);
		if (!passwordGestionnaire.equals(this.passwordGestionnaire))
			throw new MetierException();
		
		Competition c = verifierCompetitionExistant(competition); // return competition if Existence
		if (!c.getDateCloture().estDansLePasse()) 				  // check if competition date is not closed yet
			throw new CompetitionException();
		
		Competiteur gagner = verifierCompetiteurExistant(c, vainqueur); // return competiteur of competition and this one will be winner
		c.solder(gagner);												// competition object will use its solder method
		
		competitions.remove(c);
	}

	
	/** 
	 * consulter les  joueurs.
	 * 
	 * @param passwordGestionnaire  le password du gestionnaire du site de paris 

	 * @throws MetierException   levée  
	 * si le <code>passwordGestionnaire</code>  est invalide,
	 * si le <code>passwordGestionnaire</code> est incorrect.
	 * 
	 * @return une liste de liste dont les éléments (de type <code>String</code>) représentent un joueur avec dans l'ordre : 
	 *  <ul>
	 *  <li>       le nom du joueur  </li>
	 *  <li>       le prénom du joueur </li>
	 *  <li>       le pseudo du joueur  </li>
	 *  <li>       son compte en jetons restant disponibles </li>
	 *  <li>       le total de jetons engagés dans ses mises en cours. </li>
	 *  </ul>
	 */
	public LinkedList <LinkedList <String>> consulterJoueurs(String passwordGestionnaire) throws MetierException {
		
		validitePasswordGestionnaire(passwordGestionnaire);
		if (!passwordGestionnaire.equals(this.passwordGestionnaire))
			throw new MetierException();
		
		LinkedList<LinkedList<String>> list_joueur = new LinkedList<LinkedList<String>>();
		for (Joueur j : joueurs){
			list_joueur.add(j.toList());
		}
		return list_joueur;
	}

	/**
	 * connaître les compétitions en cours.
	 * 
	 * @return une liste de liste dont les éléments (de type <code>String</code>) représentent une compétition avec dans l'ordre : 
	 *  <ul>
	 *  <li>       le nom de la compétition,  </li>
	 *  <li>       la date de clôture de la compétition. </li>
	 *  </ul>
	 */
	public LinkedList <LinkedList <String>> consulterCompetitions(){
		
		LinkedList<LinkedList<String>> list_competition = new LinkedList<LinkedList<String>>();
		for (Competition c : competitions){
			list_competition.add(c.toList());
		}
		
		return list_competition;
	} 

	/**
	 * connaître  la liste des noms des compétiteurs d'une compétition.  
	 * 
	 * @param competition   le nom de la compétition  
	 * 
	 * @throws CompetitionException   levée  
	 * si le nom de la compétition est invalide.
	 * @throws CompetitionInexistanteException   levée si il n'existe pas de compétition de même nom. 
	 * 
	 * @return la liste des compétiteurs de la  compétition.
	 */
	public LinkedList <String> consulterCompetiteurs(String competition) throws CompetitionException, CompetitionInexistanteException{
		
		if (!validiteCompetitionNom(competition))
			throw new CompetitionException();
		
		Competition c = verifierCompetitionExistant(competition);
		LinkedList<String> list_competiteur = new LinkedList<String>();
		for (Competiteur com : c.getCompetiteur()){
			list_competiteur.add(com.getCompetiteurNom());
		}
		
		return list_competiteur;
	}

	/**
	 * vérifier la validité du password du gestionnaire.
	 * 
	 * @param passwordGestionnaire   le password du gestionnaire à vérifier
	 * 
	 * @throws MetierException   levée 
	 * si le <code>passwordGestionnaire</code> est invalide.  
	 */
	protected void validitePasswordGestionnaire(String passwordGestionnaire) throws MetierException {
		if (passwordGestionnaire == null) 
			throw new MetierException();
		if (passwordGestionnaire.length() <8)
			throw new MetierException();
		for (int i = 0; i < passwordGestionnaire.length(); i++){
			char c = passwordGestionnaire.charAt(i);
			if (!Character.isLetterOrDigit(c))
				throw new MetierException();
		}
	}
	
	// Generate a random password from 8 -> 12 characters
	protected String generePassword() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder password = new StringBuilder();
        Random rnd = new Random();
        int length = rnd.nextInt(5)+8; // generate a random number from 8 -> 12
        while (password.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            password.append(SALTCHARS.charAt(index));
        }
        String passowrdStr = password.toString();
        return passowrdStr;
    }
	
	// Return a joueur if existence, if null, throw exception
	protected Joueur verifierJoueurExistant(String pseudo, String passwordJoueur) throws JoueurException, JoueurInexistantException{
		
		if(!validitePseudo(pseudo) || !validitePasswordJoueur(passwordJoueur))
			throw new JoueurException();

		Joueur searching = null;
		for(Joueur j: joueurs){
			if (j.getPseudo().equals(pseudo)){
				if (j.getPasswordJoueur().equals(passwordJoueur)){
					searching = j;
					break;
				}	
			}			
		}
		if (searching == null)
			throw new JoueurInexistantException();
		return searching;
	}
	
	// Return a competition if existence, if null, throw exception
	protected Competition verifierCompetitionExistant(String competition) throws CompetitionException, CompetitionInexistanteException{
		
		if (!validiteCompetitionNom(competition))
			throw new CompetitionException();
		
		Competition searching = null;
		for (Competition c: competitions){
			if (c.getCompetitionNom().equals(competition)) {
					searching = c;
					break;
				}
		}
		if (searching == null)
			throw new CompetitionInexistanteException();
		return searching;
	}
	
	// Return a competiteur if existence, if null, throw exception
	protected Competiteur verifierCompetiteurExistant(Competition competition, String competiteur) throws CompetitionException{
		
		if (!validiteCompetiteurNom(competiteur))
			throw new CompetitionException();
		
		Competiteur searching = null;
		if (competition.getCompetiteur().contains(new Competiteur(competiteur)))
			searching = new Competiteur(competiteur);
		else{
			throw new CompetitionException();
		}
		return searching;		
	}
	
	// These methods below to validate nom, prenom.. following "Cahier des charges"
	protected boolean validiteJoueurNom(String nom){
		if (nom == null || nom.length() < 1)
			return false;
		for (int i = 0; i < nom.length(); i++){
			char c = nom.charAt(i);
			if (!Character.isLetter(c) && c != '-')
				return false;
		}
		return true;
	}
	protected boolean validitePseudo(String pseudo){
		if (pseudo == null || pseudo.length() < 4)
			return false;
		for (int i = 0; i < pseudo.length(); i++){
			char c = pseudo.charAt(i);
			if (!Character.isLetterOrDigit(c))
				return false;
		}
		return true;
	}
	
	protected boolean validiteCompetitionNom(String nom){
		if (nom == null || nom.length() < 4)
			return false;
		for (int i = 0; i < nom.length(); i++){
			char c = nom.charAt(i);
			if (!Character.isLetterOrDigit(c) && c != '-' && c != '_' && c != '.')
				return false;
			}
		return true;
	}
	
	protected boolean validiteCompetiteurNom(String nom){
		if (nom == null || nom.length() < 4)
			return false;
		for (int i = 0; i < nom.length(); i++){
			char c = nom.charAt(i);
			if (!Character.isLetterOrDigit(c) && c != '-' && c != '_')
				return false;
		}
		return true;
	}
	
	protected boolean validitePasswordJoueur(String passwordJoueur){
		if (passwordJoueur == null || passwordJoueur.length() < 8)
			return false;
		for (int i = 0; i < passwordJoueur.length(); i++){
			char c = passwordJoueur.charAt(i);
			if (!Character.isLetterOrDigit(c))
				return false;
		}
		return true;
	}

}
