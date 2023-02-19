# java-filmorate
Filmorate project.

# ER - diagram
DataBase structure

![DataBase structure](resources/ER_diagram.jpg)

Friends relationships explanation

![](resources/CommonFriends.jpg)

# Examples
# 1. Find all Films:
SELECT *
FROM films;

# 2. Find all Users:
SELECT *
FROM users;

# 3. Get all friends of user id=1:
SELECT * 
FROM users
WHERE user_id IN (SELECT user2_id FROM friendship WHERE user1_id = 1 AND friendship_status = true)
OR  user_id IN (SELECT user1_id FROM friendship WHERE user2_id = 1 AND friendship_status = true)
ORDER BY user_id;

# 4. List of common friends of user id=1 and user id=2
SELECT *
FROM users
WHERE user_id IN (SELECT user2_id FROM friendship WHERE user1_id = 1 AND friendship_status = true)
OR  user_id IN (SELECT user1_id FROM friendship WHERE user2_id = 1 AND friendship_status = true)
INTERSECT
SELECT *
FROM users
WHERE user_id IN (SELECT user2_id FROM friendship WHERE user1_id = 2 AND friendship_status = true)
OR  user_id IN (SELECT user1_id FROM friendship WHERE user2_id = 2 AND friendship_status = true)
ORDER BY user_id;

# 5. Top 10 films by likes
SELECT f.*
FROM films f join likes l ON f.film_id = l.film_id
GROUP BY f.film_id
ORDER BY COUNT(l.user_id) DESC
LIMIT 10;

# 6. Top 10 Dramas by likes
SELECT f.*
FROM films f join likes l ON f.film_id = l.film_id
WHERE f.film_id IN (SELECT film_id FROM genre_link WHERE genre_id IN
                   (SELECT genre_id FROM genre WHERE genre_name = 'Drama'))
GROUP BY f.film_id
ORDER BY COUNT(l.user_id) DESC
LIMIT 10;