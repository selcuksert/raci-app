package com.corp.concepts.raci.entity.converter;

import java.io.IOException;
import java.util.List;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringListConverter implements AttributeConverter<List<String>, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		String attributeJson = null;
		try {
			attributeJson = objectMapper.writeValueAsString(attribute);
		} catch (final JsonProcessingException e) {
			log.error("JSON writing error", e);
		}

		return attributeJson;
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		List<String> attribute = null;
		try {
			attribute = objectMapper.readValue(dbData, new TypeReference<List<String>>() {
			});
		} catch (final IOException e) {
			log.error("JSON reading error", e);
		}

		return attribute;
	}

}
