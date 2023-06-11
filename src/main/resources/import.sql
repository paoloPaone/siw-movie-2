-- Movies
INSERT INTO movie (id, title, year) VALUES (nextval('hibernate_sequence'), 'Full metal jacket', 1987);
INSERT INTO movie (id, title, year) VALUES (nextval('hibernate_sequence'), 'Non è un paese per vecchi', 2007);
INSERT INTO movie (id, title, year) VALUES (nextval('hibernate_sequence'), 'The founder', 2016);
INSERT INTO movie (id, title, year) VALUES (nextval('hibernate_sequence'), 'Harry Potter e la pietra filosofale', 2001);
INSERT INTO movie (id, title, year) VALUES (nextval('hibernate_sequence'), 'Il pianeta delle scimmie', 2001);
INSERT INTO movie (id, title, year) VALUES (nextval('hibernate_sequence'), 'Lo chiamavano Jeeg Robot', 2015);
INSERT INTO movie (id, title, year) VALUES (nextval('hibernate_sequence'), 'Yesterday', 2019);

-- Artists
INSERT INTO artist (id, name, surname,  date_of_birth) VALUES (nextval('hibernate_sequence'), 'Tim', 'Burton','1958-08-25');
INSERT INTO artist (id, name, surname,  date_of_birth) VALUES (nextval('hibernate_sequence'), 'Himesh', 'Patel','1990-10-13');
INSERT INTO artist (id, name, surname,  date_of_birth) VALUES (nextval('hibernate_sequence'), 'Danny', 'Boyle','1956-10-20');
INSERT INTO artist (id, name, surname,  date_of_birth) VALUES (nextval('hibernate_sequence'), 'Tim', 'Roth','1961-05-14');
INSERT INTO artist (id, name, surname,  date_of_birth) VALUES (nextval('hibernate_sequence'), 'Michael', 'Keaton', '1951-09-05');
INSERT INTO artist (id, name, surname,  date_of_birth) VALUES (nextval('hibernate_sequence'), 'Stanley', 'Kubrick', '1928-07-26');
INSERT INTO artist (id, name, surname,  date_of_birth) VALUES (nextval('hibernate_sequence'), 'Ronald Lee', 'Ermey',  '1944-03-24');
INSERT INTO artist (id, name, surname,  date_of_birth) VALUES (nextval('hibernate_sequence'), 'Claudio', 'Santamaria', '1974-07-22');
INSERT INTO artist (id, name, surname,  date_of_birth) VALUES (nextval('hibernate_sequence'), 'Gabriele', 'Mainetti', '1976-11-07');