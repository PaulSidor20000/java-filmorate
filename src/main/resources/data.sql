--truncate table films;
--truncate table genre;
--truncate table genre_link;
--truncate table users;
--truncate table likes;
--truncate table friendship;
--truncate table mpa_rating;

--DELETE FROM GENRE_LINK ;
--DELETE FROM FRIENDSHIP ;
--DELETE FROM LIKES ;
--DELETE FROM FILMS ;
--ALTER TABLE FILMS ALTER COLUMN film_id RESTART WITH 1;
--DELETE FROM MPA_RATING ;
--ALTER TABLE MPA_RATING ALTER COLUMN mpa_rating_id RESTART WITH 1;
--DELETE FROM GENRE ;
--ALTER TABLE GENRE ALTER COLUMN genre_id RESTART WITH 1;
--DELETE FROM USERS ;
--ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1;

INSERT INTO mpa_rating (mpa_rating_name) VALUES('G');
INSERT INTO mpa_rating (mpa_rating_name) VALUES('PG');
INSERT INTO mpa_rating (mpa_rating_name) VALUES('PG-13');
INSERT INTO mpa_rating (mpa_rating_name) VALUES('R');
INSERT INTO mpa_rating (mpa_rating_name) VALUES('NC-17');

INSERT INTO genre (genre_name) VALUES('Комедия');
INSERT INTO genre (genre_name) VALUES('Драма');
INSERT INTO genre (genre_name) VALUES('Мультфильм');
INSERT INTO genre (genre_name) VALUES('Триллер');
INSERT INTO genre (genre_name) VALUES('Документальный');
INSERT INTO genre (genre_name) VALUES('Боевик');

--INSERT INTO films (name, description, release_date, duration, mpa_rating_id) VALUES('Back to the Future', 'Seventeen-year-old Marty McFly came home early yesterday. 30 years earlier', '1985-07-03', 116, 2);
--INSERT INTO films (name, description, release_date, duration, mpa_rating_id) VALUES('Pulp Fiction', 'Just because you are a character does not mean you have character', '1994-05-21', 154, 4);
--INSERT INTO films (name, description, release_date, duration, mpa_rating_id) VALUES('WALL·E', 'Love is a matter of technique', '2008-06-21', 98, 1);
--INSERT INTO films (name, description, release_date, duration, mpa_rating_id) VALUES('Inception', 'Your mind is a crime scene', '2010-07-08', 148, 3);
--INSERT INTO films (name, description, release_date, duration, mpa_rating_id) VALUES('Home Alone', 'When Kevins Family Left For Vacation, They Forgot One Minor Detail: Kevin', '1990-11-10', 103, 2);

--INSERT INTO genre_link (film_id, genre_id) VALUES(1, 1);
--INSERT INTO genre_link (film_id, genre_id) VALUES(1, 6);
--INSERT INTO genre_link (film_id, genre_id) VALUES(2, 2);
--INSERT INTO genre_link (film_id, genre_id) VALUES(2, 6);
--INSERT INTO genre_link (film_id, genre_id) VALUES(3, 3);
--INSERT INTO genre_link (film_id, genre_id) VALUES(3, 1);
--INSERT INTO genre_link (film_id, genre_id) VALUES(4, 6);
--INSERT INTO genre_link (film_id, genre_id) VALUES(4, 4);
--INSERT INTO genre_link (film_id, genre_id) VALUES(4, 2);
--INSERT INTO genre_link (film_id, genre_id) VALUES(5, 1);

--INSERT INTO users (email, login, name, birthday) VALUES('user1@mail.ru', 'user1', 'Jack1', '2000-10-10');
--INSERT INTO users (email, login, name, birthday) VALUES('user2@mail.ru', 'user2', 'John2', '2001-11-15');
--INSERT INTO users (email, login, name, birthday) VALUES('user3@mail.ru', 'user3', 'Joe3', '1999-01-10');
--INSERT INTO users (email, login, name, birthday) VALUES('user4@mail.ru', 'user4', 'Hank4', '2000-05-11');
--INSERT INTO users (email, login, name, birthday) VALUES('user5@mail.ru', 'user5', 'Mike5', '1995-12-10');

--INSERT INTO likes (film_id, user_id) VALUES(1, 1);
--INSERT INTO likes (film_id, user_id) VALUES(1, 2);
--INSERT INTO likes (film_id, user_id) VALUES(1, 3);
--INSERT INTO likes (film_id, user_id) VALUES(2, 1);
--INSERT INTO likes (film_id, user_id) VALUES(2, 5);
--INSERT INTO likes (film_id, user_id) VALUES(3, 1);
--INSERT INTO likes (film_id, user_id) VALUES(3, 3);
--INSERT INTO likes (film_id, user_id) VALUES(3, 4);
--INSERT INTO likes (film_id, user_id) VALUES(3, 5);
--INSERT INTO likes (film_id, user_id) VALUES(4, 5);

--INSERT INTO friendship (user1_id, user2_id) VALUES(1, 3);
--INSERT INTO friendship (user1_id, user2_id) VALUES(2, 3);
--INSERT INTO friendship (user1_id, user2_id, friendship_status) VALUES(1, 3, true);
--INSERT INTO friendship (user1_id, user2_id, friendship_status) VALUES(2, 4, true);
--INSERT INTO friendship (user1_id, user2_id, friendship_status) VALUES(2, 5, true);
--INSERT INTO friendship (user1_id, user2_id, friendship_status) VALUES(2, 3, true);
--INSERT INTO friendship (user1_id, user2_id, friendship_status) VALUES(2, 1, true);
--INSERT INTO friendship (user1_id, user2_id) VALUES(4, 5);
--MERGE INTO friendship (user1_id, user2_id, friendship_status) KEY (user1_id, user2_id) VALUES(4, 5, true);