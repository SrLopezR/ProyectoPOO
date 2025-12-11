-- Script que se ejecutará al iniciar la base de datos
CREATE TABLE IF NOT EXISTS xava_users (
                                          username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL
    );

-- Insertar usuario admin por defecto si no existe
INSERT INTO xava_users (username, password, role)
SELECT 'admin', 'admin', 'admin'
    WHERE NOT EXISTS (SELECT 1 FROM xava_users WHERE username = 'admin');