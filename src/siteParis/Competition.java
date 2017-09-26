package siteParis;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Competition {

	private String competitionNom;
	private DateFrancaise dateCloture;
	private LinkedList<Competiteur> competiteurs;
	private LinkedList<Pari> paris;

	public Competition(String competitionNom, DateFrancaise dateCloture, String[] competiteurs) throws CompetitionException{
				
		if (competiteurs.length < 2) 
			throw new CompetitionException();
		Set<String> store = new HashSet<>(); // using Set to check if there are same competiteurs because Set does not allow same values
		for (String s : competiteurs){
			if (store.add(s) == false)
				throw new CompetitionException(); 
		}
		
		if (dateCloture == null || dateCloture.estDansLePasse()) 
			throw new CompetitionException();
		
		this.competiteurs = new LinkedList<Competiteur>();
		for (String s: competiteurs){
			this.competiteurs.add(new Competiteur(s));
		}
		this.competitionNom = competitionNom;
		this.dateCloture = dateCloture;
		this.paris = new LinkedList<Pari>();
	}
	
	
	public String getCompetitionNom() {
		return competitionNom;
	}

	public DateFrancaise getDateCloture(){
		return dateCloture;
	}
	
	public LinkedList<Competiteur> getCompetiteur(){
		return competiteurs;
	}

	public void setCompetitionNom(String competitionNom) {
		this.competitionNom = competitionNom;
	}
	
	public boolean equals(Object o){
		if (o instanceof Competition){
			Competition c = (Competition) o;
			return c.getCompetitionNom().equals(this.competitionNom);
		}
		else return false;
	} 

	public LinkedList<String> toList(){
		LinkedList<String> list_competition = new LinkedList<String>();
		list_competition.add(competitionNom);
		list_competition.add(dateCloture.toString());
		return list_competition;
	}
	
	// Method to handle "MiserVainqueur"
	public void miser(Joueur joueur, long miseJeton, Competiteur vainqueur) throws JoueurException{
		joueur.debiter(miseJeton);
		joueur.setMiseEncours(miseJeton);
		setPari(new Pari(joueur, miseJeton, vainqueur));
	}
	
	// Method to handle "SolderVainqueur"
	public void solder(Competiteur gagner){
		
		long totalJetons = 0;  // somme des jetons mises pour cette competition
		long totalJetonsGagner = 0; // somme des jetons mises sur ce competiteur
		boolean checkGagner = false; // verifier joueur trouve bon gagner
		
		for (Pari p : paris){
			totalJetons += p.getMiseJetons();
			p.getJoueur().setMiseEncours(0-p.getMiseJetons());
			if (p.getCompetiteur().equals(gagner)){
				checkGagner = true;
				totalJetonsGagner += p.getMiseJetons();
			}
		}
		if (checkGagner == true){  // au moins joueur a trouve le bon competiteur
			for (Pari p : paris){
				if (p.getCompetiteur().equals(gagner)){
					p.getJoueur().crediter(p.getMiseJetons()*totalJetons/totalJetonsGagner);
				}
			}
		} else { // aucun joueur n'a trouve le bon competiteur
			for (Pari p: paris){
				p.getJoueur().crediter(p.getMiseJetons());
			}
		}
	}
	public void setPari(Pari p){
		this.paris.add(p);
	}
	public LinkedList<Pari> getPari(){
		return paris;
	}
	
}
