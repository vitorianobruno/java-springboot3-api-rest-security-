CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50),
  password VARCHAR(255),
  email VARCHAR(100),
  name VARCHAR(50),
  surname VARCHAR(50),
  phone VARCHAR(20),
  city VARCHAR(50),
  country VARCHAR(50)
);