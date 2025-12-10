package org.example.Proyecto.validators;

import org.openxava.util.Messages;
import org.openxava.validators.IValidator;
import org.example.Proyecto.model.Cliente;
import java.time.LocalDate;
import java.time.Period;

public class ClienteValidator implements IValidator {

    private Cliente cliente;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public void validate(Messages errors) throws Exception {
        if (cliente == null) return;

        if (cliente.getFechaNacimiento() != null) {
            LocalDate hoy = LocalDate.now();
            Period edad = Period.between(cliente.getFechaNacimiento(), hoy);

            if (edad.getYears() < 18) {
                errors.add("cliente_menor_edad", cliente.getNombre() + " " + cliente.getApellido());
            }
        }

        if (cliente.getCorreo() != null && !cliente.getCorreo().isEmpty()) {
            String email = cliente.getCorreo();
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.add("email_invalido", email);
            }
        }

        if (cliente.getTelefonoPrincipal() != null) {
            String telefono = cliente.getTelefonoPrincipal().replaceAll("[^0-9]", "");
            if (telefono.length() < 8) {
                errors.add("telefono_invalido", cliente.getTelefonoPrincipal());
            }
        }

        if (cliente.getTipoCliente() != null &&
                cliente.getTipoCliente().toString().equals("COMERCIAL") &&
                (cliente.getIdentificacionTributaria() == null || cliente.getIdentificacionTributaria().isEmpty())) {
            errors.add("cliente_comercial_sin_ruc");
        }
    }
}