SELECT * FROM users;

-- get all friends of user id=1
SELECT * 
FROM users
WHERE user_id IN (SELECT user2_id FROM friendship WHERE user1_id = 1 AND friendship_status = true)
OR  user_id IN (SELECT user1_id FROM friendship WHERE user2_id = 1 AND friendship_status = true)
ORDER BY user_id;

 -- common friends id = 1 and id = 2 (id = 1 and id = 2 are also friends)
 -- common friends id = 3 and id = 4 (id = 3 and id = 4 are not friends)
SELECT * 
FROM users
WHERE user_id IN (SELECT user2_id FROM friendship WHERE user1_id = 1 AND friendship_status = true)
--OR  user_id IN (SELECT user1_id FROM friendship WHERE user2_id = 1 AND friendship_status = true)
INTERSECT
SELECT * 
FROM users
WHERE user_id IN (SELECT user2_id FROM friendship WHERE user1_id = 2 AND friendship_status = true)
--OR  user_id IN (SELECT user1_id FROM friendship WHERE user2_id = 2 AND friendship_status = true)
ORDER BY user_id;

-- top 10 films
SELECT f.*
FROM films f join likes l ON f.film_id = l.film_id
WHERE like_status = TRUE
GROUP BY f.film_id
ORDER BY COUNT(l.user_id) DESC
LIMIT 10;

-- top 10 Dramas
SELECT f.*
FROM films f join likes l ON f.film_id = l.film_id
WHERE f.film_id IN (SELECT film_id FROM genre_link WHERE genre_id IN 
							   	  (SELECT genre_id FROM genre WHERE genre = 'Drama'))
AND like_status = true
GROUP BY f.film_id
ORDER BY COUNT(l.user_id) DESC
LIMIT 10;

--
SELECT f.*, mpa.mpa_rating_name
            FROM films f
            LEFT JOIN mpa_rating mpa ON mpa.mpa_rating_id = f.mpa_rating_id;

--
SELECT f.*, 
				mpa.mpa_rating_name, 
				GROUP_CONCAT (DISTINCT g.genre_id ORDER BY g.genre_id) AS genre_ids,
				GROUP_CONCAT (DISTINCT g.genre_name ORDER BY g.genre_id) AS genre_names,
				GROUP_CONCAT (DISTINCT l.user_id ORDER BY l.user_id) AS user_ids
FROM films f
LEFT JOIN mpa_rating mpa ON mpa.mpa_rating_id = f.mpa_rating_id
LEFT JOIN genre_link gl ON gl.film_id = f.film_id
LEFT JOIN genre g ON g.genre_id = gl.genre_id
LEFT JOIN likes l ON l.film_id = f.film_id
GROUP BY f.film_id
HAVING f.film_id = 1;

-- GET_FRIENDS_OF_USER
SELECT u.*,
GROUP_CONCAT (DISTINCT fr.user2_id ORDER BY fr.user2_id) AS friends_ids
FROM users u
LEFT JOIN friendship fr ON fr.user1_id = u.user_id 
WHERE user_id IN (SELECT user2_id FROM friendship WHERE user1_id = 2)
OR user_id IN (SELECT user1_id FROM friendship WHERE user2_id = 2 AND friendship_status = TRUE)
GROUP BY u.user_id;
           
-- GET_COMMON_FRIENDS
SELECT * 
FROM users
WHERE user_id IN (SELECT user2_id FROM friendship WHERE user1_id = 1)
OR user_id IN (SELECT user1_id FROM friendship WHERE user2_id = 1 AND friendship_status = TRUE)
INTERSECT
SELECT * 
FROM users
WHERE user_id IN (SELECT user2_id FROM friendship WHERE user1_id = 2)
OR user_id IN (SELECT user1_id FROM friendship WHERE user2_id = 2 AND friendship_status = TRUE);


