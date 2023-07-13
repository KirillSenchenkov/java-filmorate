drop table if exists users, films, mpa, genre, film_genre, user_friend, film_like;

CREATE TABLE mpa (
id integer NOT NULL PRIMARY KEY,
name varchar(50)
);

CREATE TABLE genre (
id integer NOT NULL PRIMARY KEY,
name varchar(50)
);

CREATE TABLE users  (
  id serial NOT NULL PRIMARY KEY,
  email varchar(50) NOT null,
  login varchar(50) NOT null,
  name varchar(50),
  birthday date
);

CREATE TABLE films (
  id serial NOT NULL PRIMARY KEY,
  name varchar(50) NOT NULL,
  description varchar(200),
  releaseDate date NOT NULL,
  duration integer NOT NULL,
  mpa_id integer REFERENCES mpa(id)
);

CREATE TABLE film_genre (
film_id integer references films(id) on delete cascade ,
genre_id integer references genre(id) on delete cascade,
PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE film_like (
film_id integer references films(id) on delete cascade,
user_id integer references users(id) on delete cascade,
PRIMARY KEY (film_id, user_id)
);

CREATE TABLE user_friend (
user_id integer references users(id) on delete cascade,
friend_id integer,
PRIMARY KEY (user_id, friend_id)
);

