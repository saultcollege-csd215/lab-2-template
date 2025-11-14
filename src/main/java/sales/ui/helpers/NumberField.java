package sales.ui.helpers;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberField extends TextField {

    private final TextFormatter<Number> formatter;

    public NumberField(Number value, boolean allowNegative, int decimalPlaces) {
        // NumberFormat for integers in the default locale
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        format.setGroupingUsed(true); // allow thousands separators
        format.setMaximumFractionDigits(decimalPlaces); // set max decimal places
        format.setMinimumFractionDigits(decimalPlaces); // always show decimal places
        formatter = new TextFormatter<>(new NumberStringConverter(format), 0, change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                // replace blank with "0" or "0.00" etc.
                if ( decimalPlaces == 0 ) {
                    change.setText("0");
                } else {
                    change.setText("0" + DecimalFormatSymbols.getInstance(Locale.getDefault()).getDecimalSeparator() + "0".repeat(decimalPlaces));
                }
                change.setRange(0, change.getControlText().length());
                return change;
            }
            try {
                var n = format.parse(newText);

                if (!allowNegative && n.doubleValue() < 0) {
                    return null;
                }

                return change;
            } catch (Exception e) {
                return null;
            }
        });

        setTextFormatter(formatter);
        setValue(value);
    }

    /** Get the current Integer value (null if empty) */
    public Double getValue() {
        Number n = formatter.getValue();
        return n == null ? null : n.doubleValue();
    }

    public Integer getIntValue() {
        Number n = formatter.getValue();
        return n == null ? null : n.intValue();
    }

    /** Set the value of the field */
    public void setValue(Number value) {
        formatter.setValue(value);
    }
}
