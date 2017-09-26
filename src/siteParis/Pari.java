package siteParis;

public class Pari {
	
	private Joueur joueur;
	private Competiteur competiteur;
	private long miseJetons;
	
	public Pari(Joueur joueur, long miseJetons, Competiteur vainqueur){
		this.joueur = joueur;
		this.competiteur = vainqueur;
		this.miseJetons = miseJetons;
	}

	public Joueur getJoueur() {
		return joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}

	public Competiteur getCompetiteur() {
		return competiteur;
	}

	public void setCompetiteur(Competiteur competiteur) {
		this.competiteur = competiteur;
	}

	public long getMiseJetons() {
		return miseJetons;
	}

	public void setMiseJetons(long miseJetons) {
		this.miseJetons = miseJetons;
	}
	public String toString(){
		return " " + competiteur.getCompetiteurNom() + " " + Long.toString(miseJetons) + " " + joueur.getNom();
	}
	
}
