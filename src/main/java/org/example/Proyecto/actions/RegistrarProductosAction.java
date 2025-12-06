package org.example.Proyecto.actions;

import org.example.Proyecto.model.enums.TipoMovimientoInventario;
import org.openxava.actions.*;
import org.openxava.util.*;
import org.openxava.jpa.XPersistence;
import org.example.Proyecto.model.*;
import javax.persistence.EntityManager;

public class RegistrarProductosAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        // Obtener el servicio actual
        Long servicioId = Long.parseLong(getView().getValueString("id"));
        EntityManager em = XPersistence.getManager();
        ProgramacionServicios servicio = em.find(ProgramacionServicios.class, servicioId);

        if (servicio == null) {
            addError("Servicio no encontrado");
            return;
        }

        // Pedir datos del producto a registrar
        String[] labels = {
                "Producto (ID o Nombre):",
                "Cantidad usada:",
                "Observaciones:"
        };
        String[] values = new String[3];

        // Buscar producto
        Producto producto = buscarProducto(values[0]);
        if (producto == null) {
            addError("Producto no encontrado: " + values[0]);
            return;
        }

        // Validar cantidad
        int cantidad;
        try {
            cantidad = Integer.parseInt(values[1]);
            if (cantidad <= 0) {
                addError("La cantidad debe ser mayor a 0");
                return;
            }

            // Verificar stock disponible
            if (producto.getStock() < cantidad) {
                addError("Stock insuficiente. Disponible: " + producto.getStock());
                return;
            }

        } catch (NumberFormatException e) {
            addError("Cantidad inválida. Debe ser un número entero.");
            return;
        }

        // Crear registro de uso
        UsoProductos uso = new UsoProductos();
        uso.setServicio(servicio);
        uso.setProducto(producto);
        uso.setCantidad(cantidad);
        uso.setFechaUso(java.time.LocalDate.now());
        uso.setObservaciones(values[2]);

        // Guardar en base de datos
        em.persist(uso);

        // Actualizar stock del producto
        producto.setStock(producto.getStock() - cantidad);

        // Crear movimiento de inventario
        Inventario movimiento = new Inventario();
        movimiento.setProducto(producto);
        movimiento.setCantidad(-cantidad); // Salida negativa
        movimiento.setFechaActualizacion(java.time.LocalDate.now());
        movimiento.setTipoMovimiento(TipoMovimientoInventario.SALIDA_SERVICIO);
        movimiento.setObservaciones("Uso en servicio ID: " + servicio.getId());

        em.persist(movimiento);

        addMessage("Producto registrado exitosamente. Stock actualizado: " + producto.getStock());
        getView().refresh(); // Refrescar la vista
    }

    private Producto buscarProducto(String criterio) {
        try {
            EntityManager em = XPersistence.getManager();

            // Intentar buscar por ID primero
            try {
                Long id = Long.parseLong(criterio);
                Producto producto = em.find(Producto.class, id);
                if (producto != null) {
                    return producto;
                }
            } catch (NumberFormatException e) {
                // No es un ID numérico, buscar por nombre
            }

            // Buscar por nombre
            String jpql = "SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(:nombre)";
            return em.createQuery(jpql, Producto.class)
                    .setParameter("nombre", "%" + criterio + "%")
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {
            return null;
        }
    }
}