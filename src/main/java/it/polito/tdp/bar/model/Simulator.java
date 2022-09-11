package it.polito.tdp.bar.model;

import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.bar.model.Event.EventType;

public class Simulator {
	// Modello del bar
	private List<Tavolo> tavoli;
	
	// Parametri della simulazione
	private int NUM_EVENTI = 2000;
	private int T_ARRIVO_MAX = 10;
	private int NUM_PERSONE_MAX = 10;
	private int DURATA_MIN = 60;
	private int DURATA_MAX = 120;
	private double TOLLERANZA_MAX = 0.9;
	private double OCCUPAZIONE_MAX = 0.5;
	
	// Coda degli eventi
	private PriorityQueue<Event> queue;
	
	// Statistiche (valori in uscita)
	private Statistiche statistiche;
	
	public void init() {
		this.queue = new PriorityQueue<Event>();
		this.statistiche = new Statistiche();
		this.tavoli = new LinkedList<Tavolo>();
		creaTavoli();
		creaEventi();
	}	

	private void creaTavolo(int quantita, int dimensione) {
		for (int i = 0; i < quantita; i ++) {
			this.tavoli.add(new Tavolo(dimensione, false));
		}
	}
	
	private void creaTavoli() {
		creaTavolo(2,10);
		creaTavolo(4,8);
		creaTavolo(4,6);
		creaTavolo(5,4);
		
		// può essere utile ordinare la lista di tavoli per dimensione in modo da assegnare ai clienti il tavolo
		// più piccolo che sia in grado di accoglierli in modo da massimizzare la resa dei tavoli
		Collections.sort(this.tavoli, new Comparator<Tavolo>(){

			@Override
			public int compare(Tavolo o1, Tavolo o2) {
				return o1.getPosti()-o2.getPosti();
			}
			
		});
	}
	
	private void creaEventi() {
		Duration arrivo = Duration.ofMinutes(0); // punto zero del tempo (riferimento) --> trasformiamo 0 in Duration
		for (int i = 0; i < this.NUM_EVENTI; i++) {
			int nPersone = (int)(Math.random() * this.NUM_PERSONE_MAX + 1); // tiriamo a caso il numero di persone tra 1 e 10
			
			Duration durata = Duration.ofMinutes(this.DURATA_MIN +   // tiriamo a caso la durata, cioè tiriamo a caso un numero tra 0 e 60 e 
					(int)(Math.random() * (this.DURATA_MAX - this.DURATA_MIN + 1))); // sommiamo questo numero alla durata minima che è 60
			
			double tolleranza = Math.random() * this.TOLLERANZA_MAX; // adesso dobbiamo impostare la tolleranza massima di un gruppo di persone
			
			Event e = new Event(arrivo, EventType.ARRIVO_GRUPPO_CLIENTI, nPersone, durata, tolleranza, null);
					
			this.queue.add(e); // dopo aver creato il nuovo evento lo aggiungiamo alla coda degli eventi
			
			arrivo = arrivo.plusMinutes((int)(Math.random() * this.T_ARRIVO_MAX + 1)); // dobbiamo portare avanti il tempo, quindi sommiamo al tempo zero un valore casuale tra 1 e 10 minuti
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll(); // estraggo l'evento da elaboarare in ordine di priorità
			processEvent(e); // elaboro l'evento
		}
	}

	// metodo che esegue la simulazione vera e propria
	private void processEvent(Event e) {
		switch (e.getType()) {
		case ARRIVO_GRUPPO_CLIENTI: 
			// Dobbiamo contare i clienti totali (soddisfatti o meno)
			this.statistiche.incrementaClienti(e.getnPersone());
			
			// Cerchiamo se c'è un tavolo per questi clienti
			Tavolo tavolo = null; 
			for (Tavolo t : this.tavoli) { // la lista è già ordinata dal tavolo più piccolo al tavolo più grande e quindi diamo il primo tavolo più piccolo a disposizione che contenga il gruppo di persone
				if (!t.isOccupato() && t.getPosti() >= e.getnPersone() && 
						t.getPosti() * OCCUPAZIONE_MAX <= e.getnPersone()) {  // dobbiamo controllare che il gruppo di clienti occupi almeno la metà del tavolo (50% dei posti)
					tavolo = t; // se ho trovato un tavolo che soddisfa i vincoli richiesti sovrascrivo la variabile tavolo che era a null
					break; // il fatto che la lista di tavoli sia già ordinata dal tavolo più piccolo al tavolo più grande ci assicura di dare il tavolo
				}	// più piccolo possibile che contenga il gruppo di persone (tavolo che rispetta i vincoli)
			}
			
			if (tavolo != null) { // se ho trovato un tavolo per i clienti
				System.out.format("Trovato un tavolo da %d per %d persone\n", tavolo.getPosti(), e.getnPersone());
				this.statistiche.incrementaClientiSoddisfatti(e.getnPersone());
				tavolo.setOccupato(true);
				e.setTavolo(tavolo); // assegniamo il tavolo all'evento
				
				// Dopo un pò i clienti si alzeranno dal tavolo e quindi devo aggiungere alla coda tali eventi futuri
				this.queue.add(new Event(e.getTime().plus(e.getDurata()), EventType.TAVOLO_LIBERATO, e.getnPersone(), e.getDurata(), e.getTolleranza(), tavolo));
		
			} else {
				// in questo caso c'è solo il bancone a disposizione
				// Quindi dobbiamo decidere in base alla tolleranza del gruppo se questi clienti accetteranno di stare al bancone oppure no
				double bancone = Math.random(); // tiriamo a caso un altro numero, se cade prima della tolleranza allora il gruppo si ferma al bancone
												// se invece il numero cade nella zona di rifiuto (oltre la tolleranza) allora il gruppo abbandona il locale
				if (bancone <= e.getTolleranza()) {
					// i clienti si fermano al bancone
					System.out.format("%d persone si fermano al bancone\n", e.getnPersone());
					this.statistiche.incrementaClientiSoddisfatti(e.getnPersone());
				} else {
					// i clienti lasciano il locale
					System.out.format("%d persone vanno a casa\n", e.getnPersone());
					this.statistiche.incrementaClientiInsoddisfatti(e.getnPersone());
				}
			}
			break;
		case TAVOLO_LIBERATO:
			e.getTavolo().setOccupato(false); // libero il tavolo (metto occupato a false)
			break;
		}
	}

	public Statistiche getStatistiche() {
		return this.statistiche;
	}

}
