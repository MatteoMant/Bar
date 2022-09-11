package it.polito.tdp.bar.model;

public class Statistiche {
	private int clientiTot;
	private int clientiSoddisfatti;
	private int clientiInsoddisfatti;
	
	// Alla creazione dell'oggetto mettiamo a 0 tutti gli attributi per poi incrementarli con metodi opportuni
	public Statistiche() {
		super();
		this.clientiTot = 0;
		this.clientiSoddisfatti = 0;
		this.clientiInsoddisfatti = 0;
	}
	
	public void incrementaClienti(int n) {
		this.clientiTot += n;
	}
	
	public void incrementaClientiSoddisfatti(int n) {
		this.clientiSoddisfatti += n;
	}
	
	public void incrementaClientiInsoddisfatti(int n) {
		this.clientiInsoddisfatti += n;
	}

	public int getClientiTot() {
		return clientiTot;
	}

	public int getClientiSoddisfatti() {
		return clientiSoddisfatti;
	}

	public int getClientiInsoddisfatti() {
		return clientiInsoddisfatti;
	}
	
}
