USE mysql;
CREATE USER IF NOT EXISTS 'app_user'@'%' IDENTIFIED BY 'userpassword';
GRANT ALL PRIVILEGES ON *.* TO 'app_user'@'%';
CREATE USER IF NOT EXISTS 'app_user'@'localhost' IDENTIFIED BY 'userpassword';
GRANT ALL PRIVILEGES ON *.* TO 'app_user'@'localhost';
FLUSH PRIVILEGES;
