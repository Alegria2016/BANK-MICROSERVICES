-- Configurar permisos para root desde cualquier host
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'rootpassword';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

-- Configurar usuario de aplicación
CREATE USER IF NOT EXISTS 'app_user'@'%' IDENTIFIED BY 'userpassword';
GRANT ALL PRIVILEGES ON client_db.* TO 'app_user'@'%';

-- Aplicar cambios
FLUSH PRIVILEGES;