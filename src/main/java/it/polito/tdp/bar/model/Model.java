package it.polito.tdp.bar.model;

public class Model {
	private Simulator sim;
	
	public Model() {
		sim = new Simulator();
	}
	
	public Statistiche simula() {
		sim.init();
		sim.run();
	    return sim.getStatistiche();
	}
}
