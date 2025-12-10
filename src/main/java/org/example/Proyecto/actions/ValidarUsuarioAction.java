package org.example.Proyecto.actions;

import org.openxava.actions.SaveAction;
import org.openxava.jpa.XPersistence;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ValidarUsuarioAction extends SaveAction {

    @Override
    public void execute() throws Exception {
        // Primero validar
        String username = getView().getValueString("username");
        String email = getView().getValueString("email");
        Long id = (Long) getView().getValue("id");

        EntityManager em = XPersistence.getManager();

        // Validar username único
        String jpql = "SELECT COUNT(u) FROM Usuario u WHERE u.username = :username";
        if (id != null) {
            jpql += " AND u.id != :id";
        }

        Query query = em.createQuery(jpql);
        query.setParameter("username", username);
        if (id != null) {
            query.setParameter("id", id);
        }

        Long count = (Long) query.getSingleResult();
        if (count > 0) {
            addError("El nombre de usuario ya existe: " + username);
            return;
        }

        // Si pasa validaciones, ejecutar el save normal
        super.execute();
    }
}