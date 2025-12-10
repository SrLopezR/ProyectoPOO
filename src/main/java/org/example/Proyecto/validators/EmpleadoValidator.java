package org.example.Proyecto.validators;

import org.openxava.util.Messages;
import org.openxava.validators.IValidator;
import org.example.Proyecto.model.Empleado;
import java.time.LocalDate;
import java.time.Period;
import java.math.BigDecimal;

public class EmpleadoValidator implements IValidator {

    private Empleado empleado;

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    @Override
    public void validate(Messages errors) throws Exception {
        if (empleado == null) return;

        if (empleado.getFechaNacimiento() != null) {
            LocalDate hoy = LocalDate.now();
            Period edad = Period.between(empleado.getFechaNacimiento(), hoy);

            if (edad.getYears() < 18) {
                errors.add("empleado_menor_edad", empleado.getNombre() + " " + empleado.getApellido());
            }
        }

        if (empleado.getFechaContratacion() != null) {
            if (empleado.getFechaContratacion().isAfter(LocalDate.now())) {
                errors.add("fecha_contratacion_futura");
            }
        }

        if (empleado.getSalarioBase() != null) {
            BigDecimal salarioMinimo = new BigDecimal("10000.00");
            if (empleado.getSalarioBase().compareTo(salarioMinimo) < 0) {
                errors.add("salario_minimo", salarioMinimo.toString());
            }
        }

        if (empleado.getCorreoCorporativo() != null && !empleado.getCorreoCorporativo().isEmpty()) {
            String email = empleado.getCorreoCorporativo();
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.add("email_corporativo_invalido", email);
            }
        }

        if (empleado.getCargo() != null &&
                empleado.getCargo().toString().equals("TECNICO") &&
                empleado.getEspecialidad() == null) {
            errors.add("tecnico_sin_especialidad");
        }
    }
}