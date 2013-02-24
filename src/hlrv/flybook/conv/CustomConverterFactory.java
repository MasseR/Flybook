package hlrv.flybook.conv;

import java.util.Date;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;

public class CustomConverterFactory extends DefaultConverterFactory {

    @Override
    public <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> createConverter(
            Class<PRESENTATION> presentationType, Class<MODEL> modelType) {

        if (Date.class == presentationType && Integer.class == modelType) {
            return (Converter<PRESENTATION, MODEL>) new DateToIntegerConverter();
        }

        return super.createConverter(presentationType, modelType);
    }

}
