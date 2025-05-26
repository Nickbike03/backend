create table utente (
	id SERIAL primary key,
	name VARCHAR(100) not null,
	surname VARCHAR(100) not null,
	password VARCHAR(100) not null,
	email VARCHAR (200) not null unique,
	faculty VARCHAR(100) not null,
	role VARCHAR(100) not null
);


create table document (
	id SERIAL primary key,
	user_id INTEGER references utente(id),
	validated_admin INTEGER references utente(id),
	name varchar(200) not null,
	data bytea not null,
	description varchar(1000) not null,
	course varchar(200) not null,
	size int, 
	validated boolean not null
);

create table user_like_documents (
	user_id INTEGER REFERENCES utente(id),
	document_id INTEGER REFERENCES document(id),
	PRIMARY KEY (user_id, document_id)
);