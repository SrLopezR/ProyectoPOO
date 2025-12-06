package org.example.Proyecto.actions;

import org.openxava.actions.*;
import org.openxava.jpa.XPersistence;
import org.openxava.util.*;
import org.example.Proyecto.model.Producto;
import org.example.Proyecto.model.enums.EstadoProducto;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class AlertarStockBajoAction extends BaseAction {

    @Override
    public void execute() throws Exception {
        EntityManager em = XPersistence.getManager();

        // Buscar productos con stock bajo
        String jpql = "SELECT p FROM Producto p WHERE p.stock <= p.stockMinimo AND p.estado = :estado";
        TypedQuery<Producto> query = em.createQuery(jpql, Producto.class);
        query.setParameter("estado", EstadoProducto.ACTIVO);
        List<Producto> productosBajoStock = query.getResultList();

        if (productosBajoStock.isEmpty()) {
            addMessage("Todos los productos tienen stock suficiente");
            return;
        }

        // Crear mensaje detallado
        StringBuilder alerta = new StringBuilder();
        alerta.append("<html><b>ALERTA: Productos con stock bajo</b><br><br>");
        alerta.append("<table border='1' cellpadding='5'>");
        alerta.append("<tr><th>Producto</th><th>Stock Actual</th><th>Stock Mínimo</th><th>Sugerido Pedir</th></tr>");

        for (Producto p : productosBajoStock) {
            int sugerido = Math.max(p.getStockMinimo() * 3 - p.getStock(), 0);
            alerta.append("<tr>")
                    .append("<td>").append(p.getNombre()).append("</td>")
                    .append("<td align='center'>").append(p.getStock()).append("</td>")
                    .append("<td align='center'>").append(p.getStockMinimo()).append("</td>")
                    .append("<td align='center'>").append(sugerido).append(" unidades</td>")
                    .append("</tr>");
        }

        alerta.append("</table></html>");

        // Mostrar alerta usando MessageDialog
        showAlertDialog(alerta.toString());
    }

    private void showAlertDialog(String mensaje) {
        try {
            // Usar JOptionPane para mostrar mensaje HTML
            javax.swing.JLabel label = new javax.swing.JLabel(mensaje);
            javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(label);
            scrollPane.setPreferredSize(new java.awt.Dimension(500, 300));

            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    scrollPane,
                    "Alerta de Stock Bajo",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception e) {
            // Fallback simple para consola
            System.out.println("=== ALERTA DE STOCK BAJO ===");
            System.out.println(mensaje.replaceAll("<[^>]*>", "")); // Remover HTML
        }

        // También mostrar mensaje en la consola de OpenXava
        addMessage("Alerta de stock bajo generada. Ver productos con stock insuficiente.");
    }
}