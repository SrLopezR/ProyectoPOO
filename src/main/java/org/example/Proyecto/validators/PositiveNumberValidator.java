package org.example.Proyecto.validators;

import org.openxava.util.Messages;
import org.openxava.validators.IPropertyValidator;
import java.math.BigDecimal;

public class PositiveNumberValidator implements IPropertyValidator {

    @Override
    public void validate(Messages errors, Object value, String propertyName, String modelName) {
        if (value == null) return;

        if (value instanceof Integer) {
            Integer numero = (Integer) value;
            if (numero < 0) {
                errors.add("numero_negativo", propertyName);
            }
        } else if (value instanceof BigDecimal) {
            BigDecimal numero = (BigDecimal) value;
            if (numero.compareTo(BigDecimal.ZERO) < 0) {
                errors.add("numero_negativo", propertyName);
            }
        } else if (value instanceof Double) {
            Double numero = (Double) value;
            if (numero < 0) {
                errors.add("numero_negativo", propertyName);
            }
        }
    }
}