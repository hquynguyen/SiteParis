package siteParis;

import java.util.LinkedList;

public class Joueur {

	private String nom;
	private String prenom;
	private String pseudo;
	private long jetons; // le total de jetons que joueur a
	private String passwordJoueur;
	private long miseJetons;  // le total de jetons engages dans ses mises en cours
	
	public Joueur(String nom, String prenom, String pseudo) throws JoueurException{
		
		this.nom = nom;
		this.prenom = prenom;
		this.pseudo = pseudo;
		this.jetons = 0;
		this.miseJetons = 0;
		this.passwordJoueur = null;
	}
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public long getJetons() {
		return this.jetons;
	}

	public long getMiseJetons(){
		return this.miseJetons;
	}

	public String getPasswordJoueur() {
		return passwordJoueur;
	}

	public void setPasswordJoueur(String passwordJoueur) {
		this.passwordJoueur = passwordJoueur;
	}

	public void setJetons(long jetons) {
		this.jetons = jetons;
	}

	public void debiter(long jetons) throws JoueurException{

		if (jetons > this.jetons)
			throw new JoueurException();
		this.jetons -= jetons;
	}

	public void crediter(long jetons){
		this.jetons += jetons;
	}
	
	public void setMiseEncours(long jeton){
		this.miseJetons += jeton; 
	}

	public boolean equals(Object o){
		if (o instanceof Joueur){
			Joueur j = (Joueur) o;
			return j.nom.equals(this.nom) && j.prenom.equals(this.prenom) && j.pseudo.equals(this.pseudo);
		}
		else return false;
	}
	public String toString(){
		return this.nom + " " + this.prenom + " " + this.pseudo + " " + this.jetons;
	}
	public LinkedList<String> toList(){
		LinkedList<String> list_joueur = new LinkedList<String>();
		list_joueur.add(nom);
		list_joueur.add(prenom);
		list_joueur.add(pseudo);
		list_joueur.add(Long.toString(jetons));
		list_joueur.add(Long.toString(miseJetons));
		return list_joueur;
	}

}
