package siteParis;


public class Competiteur {

	private String competiteurNom;

	public Competiteur(String competiteurNom) throws CompetitionException{
		
		this.competiteurNom = competiteurNom;
	}
	
	public String getCompetiteurNom() {
		return competiteurNom;
	}

	public void setCompetiteurNom(String competiteurNom) {
		this.competiteurNom = competiteurNom;
	}

	public boolean equals(Object o){
		
		if (o instanceof Competiteur){
			Competiteur c = (Competiteur) o;
			return c.getCompetiteurNom().equals(this.competiteurNom);
		}
		return false;
	}
	
}
