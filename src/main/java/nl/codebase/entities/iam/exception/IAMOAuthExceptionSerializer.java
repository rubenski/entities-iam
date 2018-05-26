package nl.codebase.entities.iam.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Map;

public class IAMOAuthExceptionSerializer extends StdSerializer<IAMOAuthException> {

    public IAMOAuthExceptionSerializer() {
        super(IAMOAuthException.class);
    }

    @Override
    public void serialize(IAMOAuthException ex, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("error", ex.getMessage());
        if (ex.getAdditionalInformation()!=null) {
            for (Map.Entry<String, String> entry : ex.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                gen.writeStringField(key, add);
            }
        }
        gen.writeEndObject();
    }

}