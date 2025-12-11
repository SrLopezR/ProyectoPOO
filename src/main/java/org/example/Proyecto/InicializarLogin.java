package org.example.Proyecto;

import org.openxava.jpa.XPersistence;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

public class InicializarLogin {

    @PostConstruct
    public void init() {
        System.out.println("=== INICIALIZANDO SISTEMA DE LOGIN ===");

        try {
            EntityManager em = XPersistence.getManager();

            String crearTabla =
                    "CREATE TABLE IF NOT EXISTS xava_users (" +
                            "  username VARCHAR(50) PRIMARY KEY, " +
                            "  password VARCHAR(100) NOT NULL, " +
                            "  role VARCHAR(30) NOT NULL DEFAULT 'user'" +
                            ")";
            em.createNativeQuery(crearTabla).executeUpdate();
            System.out.println("Tabla xava_users verificada");

            String insertAdmin =
                    "INSERT INTO xava_users (username, password, role) " +
                            "VALUES ('admin', 'admin', 'admin') " +
                            "ON CONFLICT (username) DO NOTHING";
            em.createNativeQuery(insertAdmin).executeUpdate();
            System.out.println("Usuario admin verificado");

            String sincronizar =
                    "INSERT INTO xava_users (username, password, role) " +
                            "SELECT u.username, u.password, 'user' " +
                            "FROM usuarios u " +
                            "WHERE u.estado = 'ACTIVO' " +
                            "ON CONFLICT (username) DO UPDATE " +
                            "SET password = EXCLUDED.password";

            int count = em.createNativeQuery(sincronizar).executeUpdate();
            System.out.println(count + " usuarios sincronizados");

        } catch (Exception e) {
            System.err.println("Error en inicialización: " + e.getMessage());
        }
    }
}