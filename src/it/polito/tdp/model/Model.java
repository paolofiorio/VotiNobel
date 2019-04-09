package it.polito.tdp.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.dao.EsameDAO;

public class Model {

	//esami letti dal database
	private List<Esame> esami;
	//gestione ricorsione
	private List<Esame> best;
	private double media_best;
	public Model() {
		EsameDAO dao= new EsameDAO();
		this.esami=dao.getTuttiEsami();
	}
	/**
	 * trova la combinazione di corsi aventi la somma di crediti richiesta con media max
	 * @param numeroCrediti
	 * @return elenco dei corsi richiesti oppure {@code null}
	 */
	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		best=null;
		media_best=0.0;
		Set<Esame>parziale= new HashSet<Esame>();
		
		cerca(parziale,0,numeroCrediti);
		
		return best;
	}

	private void cerca(Set<Esame> parziale, int l, int m) {
		
		//casi terminali?
		int crediti=sommaCrediti(parziale);
		if(crediti>m)
			return;
		if(crediti==m) {
			double media=calcolaMedia(parziale);
			if(media>media_best) {
				//ho trovato una sol migliore
				best=new ArrayList<Esame>(parziale);
				media_best=media;
				return;
			}else {
				return;
			}
		}
		
		//di sicuro crediti<m
		if(l==esami.size())
			return;
		
		//genero sotto-problemi
		//esami(l) è da aggiungere o no?
		//provo a non aggiungerlo
		cerca(parziale,l+1,m);
		
		
		//provo ad aggiungerlo
		parziale.add(esami.get(l));
		cerca(parziale,l+1,m);
		parziale.remove(esami.get(l));
		
		
		
	}

	private double calcolaMedia(Set<Esame> parziale) {
		double media=0.0;
		int crediti=0;
		for(Esame e:parziale) {
			media+= e.getVoto()*e.getCrediti();
			crediti+=e.getCrediti();
	}
		return media/crediti;
	}

	private int sommaCrediti(Set<Esame> parziale) {
		int somma=0;
		for(Esame e: parziale)
			somma+= e.getCrediti();
		return somma;
	}
	
	
	
	
	
	
}
