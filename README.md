SIW-MOVIE-2-LA-VENDETTA

Casi d'uso:
casi d'uso di siw movie 2
UC1 inserimento dati film amministratore

		1.l'amministratore seleziona l'opzione "Inserisci i dati di un film"
		2.il sistema mostra in una form l'elenco dei campi da compilare (titolo,anno,locandina)
		3.l'amministratore compila i campi e preme il bottone "conferma"
		4.il sistema mostra un resoconto dei dati inseriti
		6.il sistema registra il film 
	estensioni
		3.a l'amministratore clicca sul pulsante "home" e il sistema annulla l'inserimento dell'operazione


UC2 inserimento dati artista amministratore

	scenario principale di successo:
		1.l'amministratore seleziona l'opzione "Inserisci i dati di un artista"
		2.il sistema mostra in una form l'elenco dei campi da compilare (nome,cognome,data di nascita,data di morte,immagine)
		3.l'amministratore compila i campi e preme il bottone "Conferma"
		4.il sistema mostra un resoconto dei dati inseriti
		5.l'amministratore preme il tasto "Torna all'elenco artisti"
		6.il sistema registra l'artista e gli altri artisti




UC3 aggiornamento dati film amministratore

	scenario principale di successo:
		1.l'amministratore seleziona l'opzione "Operazioni sui film"
		2.il sistema mostra l'elenco di film disponibili con i bottoni "Aggiorna" e "Elimina" 
		3.l'amministratore clicca sul pulsante "Aggiorna" 
		4.il sistema mostra i dati da aggiornare del film selezionato
		5.l'amministratore modifica i campi e preme il pulsante "fine"
		6.l'amministratore preme il pulstante "conferma"
		7.il sistema aggiorna i dati e mostra la home page

	

UC4 elimina artista da parte di amministratore

	scenario principale di successo:
		1.l'amministratore seleziona l'opzione "Artisti"
		2.il sistema mostra l'elenco di artisti disponibili con il bottone  "Elimina" e un link verso l'artista
		3.l'amministratore clicca su "Elimina"
		4.il sistema mostra i dati da eliminare dell' artista selezionato 
		5.l'ammnistratore preme il tasto "ConfermaEliminazione"
		6 Il sistema aggiorna i dati e mostra la home page

	

UC5 inserimento recensione dell'utente registrato


		1.l'utente registrato clicca sul pulsante "Gestisci film"
		2.il sistema mostra i  film nell'archivio con accanto il bottone "Aggiorna"
		3.l'utente preme il pulstante "aggiungi tua recensione"
		4.il sistema mostra in una form i dati da compilare(testo,titolo,valutazione a stelle)
		5.l'utente compila i dati e conferma l'inserimento
		6.il sistema aggiunge la recensione al film
	
	
UC6 modifica recensione utente registrato

	scenario principale di successo:
		1.l'utente preme il pulsante "Gestisci"
		2.il sistema mostra i  film nell'archivio con accanto il bottone "Aggiorna"
		3.l'utente preme il pulstante "Guarda la tua recensione"
		4.il sistema mostra i dati relativi a quella recensione
		5.il sistema mostra i campi da modificare
		6.l'utente modifica i dati e conferma l'operazione
		8.il sistema modifica i dati 
		
UC7 visualizzazione film utente generico

	scenario principale di successo:
		1.L'utente generico dalla home clicca sul bottone "Esplora" nel riquadro dei film
		2.l'utente seleziona un film
		3.il sistema mostra i dati del film con tutte le recensioni e clicca sul bottone "Torna alla lista dei film"
		4.Il sistem mostra l'elenco dei film


UC8 visualizzazione  degli attori utente generico

	scenario principale di successo:
		1.L'utente generico dalla home clicca sul bottone "Esplora" nel riquadro degli artisti
		2.l'utente seleziona un artista
		3.il sistema mostra i dati dell'artist selezionato "Torna alla lista degli artisti"
		4.Il sistem mostra l'elenco degli artisti
