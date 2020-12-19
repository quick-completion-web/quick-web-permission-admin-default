package tech.guyi.web.quick.permission.admin.defaults.db.converter;

import com.google.gson.Gson;
import tech.guyi.web.quick.permission.admin.defaults.db.entry.EntityAttach;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Converter(autoApply = true)
public class EntityAttachConverter implements AttributeConverter<EntityAttach, String> {

    private final Gson gson = new Gson();

    @Override
    public String convertToDatabaseColumn(EntityAttach attach) {
        return Optional.ofNullable(attach).map(gson::toJson).orElse(null);
    }

    @Override
    public EntityAttach convertToEntityAttribute(String s) {
        return Optional.ofNullable(s).map(json -> gson.fromJson(json, EntityAttach.class)).orElse(null);
    }
}
