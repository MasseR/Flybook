package hlrv.flybook.conv;

import java.util.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

/**
 * A converter that converts from {@link Integer} to {@link Date} and back.
 */
public class DateToIntegerConverter implements Converter<Date, Integer> {

    @Override
    public Integer convertToModel(Date value, Locale locale) {
        if (value == null) {
            return null;
        }

        return (int) (value.getTime() / 1000L);
    }

    @Override
    public Date convertToPresentation(Integer value, Locale locale) {
        if (value == null) {
            return null;
        }

        return new Date((long) value * 1000L);
    }

    @Override
    public Class<Integer> getModelType() {
        return Integer.class;
    }

    @Override
    public Class<Date> getPresentationType() {
        return Date.class;
    }

}
