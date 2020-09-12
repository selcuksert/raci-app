package com.corp.concepts.raci.entity.converter;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapConverter implements AttributeConverter<Map<String, String>, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Map<String, String> attribute) {
		String attributeJson = null;
		try {
			attributeJson = objectMapper.writeValueAsString(attribute);
		} catch (final JsonProcessingException e) {
			log.error("JSON writing error", e);
		}

		return attributeJson;
	}

	@Override
	public Map<String, String> convertToEntityAttribute(String dbData) {
		Map<String, String> attribute = null;
		try {
			attribute = objectMapper.readValue(dbData, new TypeReference<Map<String, String>>() {
			});
		} catch (final IOException e) {
			log.error("JSON reading error", e);
		}

		return attribute;
	}

}
