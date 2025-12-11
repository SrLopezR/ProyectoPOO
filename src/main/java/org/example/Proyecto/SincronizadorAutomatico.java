package org.example.Proyecto;

import org.openxava.jpa.XPersistence;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

public class SincronizadorAutomatico {

    @PostConstruct
    public void iniciar() {
        System.out.println("=== INICIANDO SINCRONIZACIÓN AUTOMÁTICA ===");

        try {
            EntityManager em = XPersistence.getManager();

            // 1. Crear tabla xava_users si no existe
            String crearTabla =
                    "CREATE TABLE IF NOT EXISTS xava_users (" +
                            "  username VARCHAR(50) PRIMARY KEY, " +
                            "  password VARCHAR(100) NOT NULL, " +
                            "  role VARCHAR(30) NOT NULL" +
                            ")";
            em.createNativeQuery(crearTabla).executeUpdate();
            System.out.println("✅ Tabla xava_users lista");

            // 2. Insertar admin si no existe
            String verificarAdmin = "SELECT COUNT(*) FROM xava_users WHERE username = 'admin'";
            Long countAdmin = ((Number) em.createNativeQuery(verificarAdmin).getSingleResult()).longValue();

            if (countAdmin == 0) {
                em.createNativeQuery("INSERT INTO xava_users (username, password, role) VALUES ('admin', 'admin', 'admin')")
                        .executeUpdate();
                System.out.println("✅ Usuario admin creado");
            }

            // 3. Sincronizar todos los usuarios activos
            String sincronizar =
                    "INSERT INTO xava_users (username, password, role) " +
                            "SELECT u.username, u.password, 'user' " +
                            "FROM usuarios u " +
                            "WHERE u.estado = 'ACTIVO' " +
                            "  AND u.username IS NOT NULL " +
                            "  AND u.password IS NOT NULL " +
                            "ON CONFLICT (username) DO UPDATE " +
                            "SET password = EXCLUDED.password";

            int sincronizados = em.createNativeQuery(sincronizar).executeUpdate();
            System.out.println("✅ " + sincronizados + " usuarios sincronizados");

            // 4. Mostrar estado
            String contar = "SELECT COUNT(*) FROM xava_users";
            Long total = ((Number) em.createNativeQuery(contar).getSingleResult()).longValue();
            System.out.println("✅ Total en sistema: " + total + " usuarios");

        } catch (Exception e) {
            System.err.println("❌ Error en sincronización: " + e.getMessage());
        }
    }
}