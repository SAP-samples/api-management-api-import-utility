package com.apimgmt.gateway.util;


import com.apimgmt.gateway.model.EntityGenericClass;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LongDeserializer implements JsonDeserializer<Object> {
  private EntityGenericClass entityGenericClass;
  private String changedAt;
  private long val;

  public LongDeserializer(final EntityGenericClass obj) {
    entityGenericClass = obj;
  }

  @Override
  public EntityGenericClass deserialize(final JsonElement json, final Type typeOfT,
                                        final JsonDeserializationContext context) {
    try {
      JsonObject jsonObj = json.getAsJsonObject();
      if (jsonObj.has("life_cycle")) {
        changedAt = !jsonObj.get("life_cycle").isJsonNull() ? jsonObj.get("life_cycle").getAsJsonObject().get(
          "changed_at").getAsString() : null;
        if (changedAt != null) {
          val = Long.parseLong(changedAt.replaceAll(".*?(\\d+).*", "$1"));
        }
      } else {
        changedAt = !jsonObj.get("modified_at").isJsonNull() ? jsonObj.get("modified_at").getAsString() : null;
        if (changedAt != null) {
          val = Long.parseLong(changedAt.replaceAll(".*?(\\d+).*", "$1"));
        }
      }
      Gson gson = new Gson();
      entityGenericClass = gson.fromJson(json, entityGenericClass.getClass());
      entityGenericClass.setChangedAt(val);

    } catch (IllegalArgumentException e) {
      log.error("Unable to parse life cycle", e);
    }
    return entityGenericClass;
  }

}
