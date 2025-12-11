package org.example.Proyecto.actions;

import org.openxava.actions.SaveAction;
import org.openxava.jpa.XPersistence;
import javax.persistence.EntityManager;

public class ActualizarUsuarioSaveAction extends SaveAction {

    @Override
    public void execute() throws Exception {
        // 1. Guardar primero el usuario
        super.execute();

        // 2. Sincronizar con xava_users
        String username = getView().getValueString("username");
        String password = getView().getValueString("password");
        String estado = getView().getValueString("estado");

        if (username != null && password != null && "ACTIVO".equals(estado)) {
            try {
                EntityManager em = XPersistence.getManager();

                String sql =
                        "INSERT INTO xava_users (username, password, role) " +
                                "VALUES (?, ?, 'user') " +
                                "ON CONFLICT (username) DO UPDATE " +
                                "SET password = EXCLUDED.password";

                em.createNativeQuery(sql)
                        .setParameter(1, username)
                        .setParameter(2, password)
                        .executeUpdate();

                // Mensaje para el usuario
                addMessage("usuario_preparado_login");

            } catch (Exception e) {
                // Error silencioso para el usuario
                System.err.println("Error sincronizando usuario: " + e.getMessage());
            }
        }
    }
}