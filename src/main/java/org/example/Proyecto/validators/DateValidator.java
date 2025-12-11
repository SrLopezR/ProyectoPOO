package org.example.Proyecto.validators;

import org.openxava.util.Messages;
import org.openxava.validators.IPropertyValidator;
import java.time.LocalDate;

public class DateValidator implements IPropertyValidator {

    @Override
    public void validate(Messages errors, Object value, String propertyName, String modelName) {
        if (value instanceof LocalDate) {
            LocalDate fecha = (LocalDate) value;
            LocalDate hoy = LocalDate.now();

            if (!propertyName.contains("Vencimiento") && !propertyName.contains("Fin")) {
                if (fecha.isAfter(hoy)) {
                    errors.add("fecha_futura_no_permitida", propertyName);
                }
            }

            if (fecha.isBefore(hoy.minusYears(100))) {
                errors.add("fecha_muy_antigua", propertyName);
            }
        }
    }
}