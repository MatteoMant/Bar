package it.polito.tdp.bar.model;

import java.time.Duration;

public class Event implements Comparable<Event> {
	
	public enum EventType{ // EventType definisce quali sono i possibili eventi
		ARRIVO_GRUPPO_CLIENTI, // Evento 1
		TAVOLO_LIBERATO // Evento 2
	}
	
	private Duration time; // tempo che scorre da zero in avanti
	private EventType type; // attributo del singolo evento
	private int nPersone;
	private Duration durata; // durata dell'evento
	private double tolleranza; // in questo caso l'attributo tolleranza è una "probabilità"
	private Tavolo tavolo;
	
	public Event(Duration time, EventType type, int nPersone, Duration durata, double tolleranza, Tavolo tavolo) {
		super();
		this.time = time;
		this.type = type;
		this.nPersone = nPersone;
		this.durata = durata;
		this.tolleranza = tolleranza;
		this.tavolo = tavolo;
	}

	public Duration getTime() {
		return time;
	}

	public void setTime(Duration time) {
		this.time = time;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public int getnPersone() {
		return nPersone;
	}

	public void setnPersone(int nPersone) {
		this.nPersone = nPersone;
	}

	public Duration getDurata() {
		return durata;
	}

	public void setDurata(Duration durata) {
		this.durata = durata;
	}

	public double getTolleranza() {
		return tolleranza;
	}

	public void setTolleranza(double tolleranza) {
		this.tolleranza = tolleranza;
	}

	public Tavolo getTavolo() {
		return tavolo;
	}

	public void setTavolo(Tavolo tavolo) {
		this.tavolo = tavolo;
	}

	@Override
	public int compareTo(Event other) {
		return this.time.compareTo(other.getTime());
	}
	
}
