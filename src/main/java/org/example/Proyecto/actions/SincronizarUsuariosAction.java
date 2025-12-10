package org.example.Proyecto.actions;

import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import javax.persistence.EntityManager;

public class SincronizarUsuariosAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        EntityManager em = XPersistence.getManager();

        try {
            // Verificar si la tabla xava_users existe
            boolean tablaExiste = false;
            try {
                em.createNativeQuery("SELECT 1 FROM xava_users").getResultList();
                tablaExiste = true;
            } catch (Exception e) {
                // La tabla no existe
            }

            // Crear tabla si no existe
            if (!tablaExiste) {
                em.createNativeQuery(
                        "CREATE TABLE xava_users (" +
                                "username VARCHAR(50) PRIMARY KEY, " +
                                "password VARCHAR(100), " +
                                "role VARCHAR(30))"
                ).executeUpdate();
                addMessage("Tabla xava_users creada exitosamente");
            }

            // Verificar si existe usuario admin
            Long countAdmin = 0L;
            try {
                countAdmin = ((Number) em.createNativeQuery(
                        "SELECT COUNT(*) FROM xava_users WHERE username = 'admin'"
                ).getSingleResult()).longValue();
            } catch (Exception e) {
                // Ignorar error
            }

            // Crear usuario admin si no existe
            if (countAdmin == 0) {
                em.createNativeQuery(
                        "INSERT INTO xava_users (username, password, role) VALUES ('admin', 'admin', 'admin')"
                ).executeUpdate();
                addMessage("Usuario admin creado (password: admin)");
            }

            // Sincronizar usuarios activos
            String sql = "INSERT INTO xava_users (username, password, role) " +
                    "SELECT u.username, u.password, 'user' " +
                    "FROM usuarios u " +
                    "WHERE u.estado = 'ACTIVO' " +
                    "AND u.username IS NOT NULL " +
                    "AND u.password IS NOT NULL " +
                    "AND NOT EXISTS (SELECT 1 FROM xava_users x WHERE x.username = u.username)";

            int registros = 0;
            try {
                registros = em.createNativeQuery(sql).executeUpdate();
                addMessage(registros + " usuarios sincronizados con el sistema de login");
            } catch (Exception e) {
                addError("Error al sincronizar usuarios: " + e.getMessage());
            }

        } catch (Exception e) {
            addError("Error general: " + e.getMessage());
        }
    }
}