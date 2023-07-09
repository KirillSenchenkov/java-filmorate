drop table if exists users, films, mpa, genre, film_genre, user_friend, film_like;

CREATE TABLE film_genre (
film_id integer,
genre_id integer,
PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE mpa (
id integer NOT NULL PRIMARY KEY,
name varchar(50)
);

CREATE TABLE film_like (
film_id integer,
user_id integer,
PRIMARY KEY (film_id, user_id)
);

CREATE TABLE user_friend (
user_id integer,
friend_id integer,
PRIMARY KEY (user_id, friend_id)
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


