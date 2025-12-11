package org.example.Proyecto.validators;

import org.openxava.util.Messages;
import org.openxava.validators.IPropertyValidator;

public class IPValidator implements IPropertyValidator {
    @Override
    public void validate(Messages errors, Object value, String propertyName, String modelName) {
        if (value == null) return;
        String ip = value.toString().trim();
        if (ip.isEmpty()) return;

        if (!ip.matches("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$")) {
            errors.add("IP invalida");
            return;
        }

        String[] parts = ip.split("\\.");
        for (String part : parts) {
            int num = Integer.parseInt(part);
            if (num < 0 || num > 255) {
                errors.add("IP invalida");
                return;
            }
        }
    }
}