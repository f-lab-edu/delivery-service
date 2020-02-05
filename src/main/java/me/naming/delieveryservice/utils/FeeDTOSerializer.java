package me.naming.delieveryservice.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import me.naming.delieveryservice.dto.FeeDTO;

public class FeeDTOSerializer extends StdSerializer<FeeDTO> {

  protected FeeDTOSerializer(Class<FeeDTO> t) {
    super(t);
  }

  protected FeeDTOSerializer(JavaType type) {
    super(type);
  }

  protected FeeDTOSerializer(Class<?> t, boolean dummy) {
    super(t, dummy);
  }

  protected FeeDTOSerializer(StdSerializer<?> src) {
    super(src);
  }

  @Override
  public void serialize(FeeDTO feeDTO, JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) throws IOException {

    jsonGenerator.writeStartObject();
    jsonGenerator.writeStringField("deliveryType", feeDTO.getDeliveryType());
    jsonGenerator.writeNumberField("basicDistance", feeDTO.getBasicDistance());
    jsonGenerator.writeNumberField("basicFee", feeDTO.getBasicFee());
    jsonGenerator.writeNumberField("extraDistance", feeDTO.getExtraDistance());
    jsonGenerator.writeNumberField("extraFee", feeDTO.getExtraFee());
    jsonGenerator.writeEndObject();

  }
}
