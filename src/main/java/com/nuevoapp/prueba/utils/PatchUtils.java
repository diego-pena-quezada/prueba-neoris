package com.nuevoapp.prueba.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.MappingException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PatchUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public PatchUtils() {
        objectMapper.findAndRegisterModules();
    }

    public static JsonNode applyOperation(JsonNode originalJson, JsonPatch jsonPatch)
            throws JsonPatchException {
        return jsonPatch.apply(originalJson);
    }

    public <T> T applyPatch(T object, JsonPatch patchOperations) {
        JsonNode rootNode = objectMapper.valueToTree(object);
        try {
            rootNode = applyOperation(rootNode, patchOperations);
            return objectMapper.treeToValue(rootNode, (Class<T>) object.getClass());
        } catch (Exception e) {
            log.error("error applying patch operations", e);
            throw new MappingException ("error applying patch operations");
        }
    }
}

