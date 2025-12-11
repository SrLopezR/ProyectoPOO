package org.example.Proyecto.actions;

import org.openxava.actions.OnChangePropertyBaseAction;
import org.openxava.jpa.XPersistence;
import javax.persistence.EntityManager;

public class ConectarUsuarioLoginAction extends OnChangePropertyBaseAction {

    @Override
    public void execute() throws Exception {
        if (getChangedProperty().equals("estado")) {
            Object nuevoValor = getNewValue();
            if (nuevoValor != null && nuevoValor.toString().equals("ACTIVO")) {
                String username = getView().getValueString("username");
                String password = getView().getValueString("password");

                if (username != null && password != null) {
                    try {
                        EntityManager em = XPersistence.getManager();

                        try {
                            em.createNativeQuery("SELECT 1 FROM xava_users").getSingleResult();
                        } catch (Exception e) {
                            em.createNativeQuery(
                                    "CREATE TABLE xava_users (" +
                                            "username VARCHAR(50) PRIMARY KEY, " +
                                            "password VARCHAR(100) NOT NULL, " +
                                            "role VARCHAR(30) NOT NULL)"
                            ).executeUpdate();
                        }

                        String sql =
                                "INSERT INTO xava_users (username, password, role) " +
                                        "VALUES (?, ?, 'user') " +
                                        "ON CONFLICT (username) DO UPDATE " +
                                        "SET password = EXCLUDED.password, role = 'user'";

                        em.createNativeQuery(sql)
                                .setParameter(1, username)
                                .setParameter(2, password)
                                .executeUpdate();

                        System.out.println("Usuario " + username + " sincronizado para login");

                    } catch (Exception e) {
                        System.err.println("Error sincronizando usuario: " + e.getMessage());
                    }
                }
            }
        }
    }
}