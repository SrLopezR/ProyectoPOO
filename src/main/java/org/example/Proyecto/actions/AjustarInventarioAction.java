package org.example.Proyecto.actions;

import org.openxava.actions.*;
import org.openxava.util.*;
import org.openxava.jpa.XPersistence;
import org.example.Proyecto.model.*;
import org.example.Proyecto.model.enums.*;
import javax.persistence.EntityManager;
import java.time.LocalDate;

public class AjustarInventarioAction extends ViewBaseAction {

    @Override
    public void execute() throws Exception {
        // Obtener producto actual de la vista
        Object productoObj = getView().getValue("producto");
        if (productoObj == null) {
            addError("No se ha seleccionado un producto");
            return;
        }

        Producto producto = (Producto) productoObj;

        // Crear diálogo de ajuste más amigable
        String[] opciones = {"ENTRADA", "SALIDA"};
        String tipoStr = (String) javax.swing.JOptionPane.showInputDialog(
                null,
                "Seleccione tipo de ajuste:",
                "Tipo de Ajuste",
                javax.swing.JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (tipoStr == null) {
            return; // Usuario canceló
        }

        // Pedir cantidad
        String cantidadStr = javax.swing.JOptionPane.showInputDialog(
                null,
                "Ingrese la cantidad:",
                "Cantidad",
                javax.swing.JOptionPane.QUESTION_MESSAGE
        );

        if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
            return; // Usuario canceló
        }

        // Pedir motivo
        String motivo = javax.swing.JOptionPane.showInputDialog(
                null,
                "Ingrese el motivo del ajuste:",
                "Motivo",
                javax.swing.JOptionPane.QUESTION_MESSAGE
        );

        if (motivo == null) {
            motivo = ""; // Establecer motivo vacío si el usuario cancela
        }

        // Validar entrada
        TipoMovimientoInventario tipo;
        try {
            tipo = TipoMovimientoInventario.valueOf(tipoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            addError("Tipo de movimiento inválido. Use ENTRADA o SALIDA");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                addError("La cantidad debe ser mayor a 0");
                return;
            }
        } catch (NumberFormatException e) {
            addError("Cantidad inválida. Debe ser un número entero.");
            return;
        }

        EntityManager em = XPersistence.getManager();

        // Asegurarse de que el producto esté actualizado
        producto = em.merge(producto);

        // Validar que no haya stock negativo para salidas
        if (tipo == TipoMovimientoInventario.SALIDA_SERVICIO ||
                tipo == TipoMovimientoInventario.SALIDA_VENTA ||
                tipo == TipoMovimientoInventario.SALIDA_DESPERDICIO) {
            if (producto.getStock() < cantidad) {
                addError("Stock insuficiente. Disponible: " + producto.getStock());
                return;
            }
        }

        // Crear movimiento de inventario
        Inventario ajuste = new Inventario();
        ajuste.setProducto(producto);
        ajuste.setCantidad(tipo == TipoMovimientoInventario.ENTRADA ? cantidad : -cantidad);
        ajuste.setFechaActualizacion(LocalDate.now());
        ajuste.setTipoMovimiento(TipoMovimientoInventario.AJUSTE); // Siempre es AJUSTE para esta acción
        ajuste.setObservaciones("Ajuste manual - " + motivo);

        em.persist(ajuste);

        // Actualizar stock del producto
        int nuevoStock = producto.getStock() + (tipo == TipoMovimientoInventario.ENTRADA ? cantidad : -cantidad);
        producto.setStock(nuevoStock);

        // Mostrar mensaje según el resultado
        String mensaje;
        if (nuevoStock <= producto.getStockMinimo()) {
            mensaje = "Ajuste realizado. ALERTA: Stock actual (" + nuevoStock +
                    ") está en o por debajo del mínimo (" + producto.getStockMinimo() + ")";
        } else if (nuevoStock < producto.getStockMinimo() * 2) {
            mensaje = "Ajuste realizado. Advertencia: Stock actual (" + nuevoStock +
                    ") está bajo el nivel recomendado. Mínimo: " + producto.getStockMinimo();
        } else {
            mensaje = "Ajuste realizado exitosamente. Stock actualizado: " + nuevoStock;
        }

        addMessage(mensaje);

        // Forzar refresco de la vista
        getView().setValue("producto.stock", nuevoStock);
        getView().refresh();
    }
}