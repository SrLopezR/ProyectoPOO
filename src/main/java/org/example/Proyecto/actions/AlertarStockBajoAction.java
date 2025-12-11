package org.example.Proyecto.actions;

import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class AlertarStockBajoAction extends ViewBaseAction {
    @Override
    public void execute() throws Exception {
        EntityManager em = XPersistence.getManager();
        String jpql = "SELECT COUNT(p) FROM Producto p WHERE p.stock <= p.stockMinimo";
        Query query = em.createQuery(jpql);
        Long count = (Long) query.getSingleResult();
        if (count == 0) {
            addMessage("Todos los productos tienen stock suficiente");
        } else {
            addMessage("ALERTA: Hay " + count + " productos con stock bajo. Revisar inventario.");
        }
    }
}