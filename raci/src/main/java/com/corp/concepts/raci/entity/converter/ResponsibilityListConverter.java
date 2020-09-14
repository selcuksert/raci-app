package com.corp.concepts.raci.entity.converter;

import java.io.IOException;
import java.util.List;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.corp.concepts.raci.entity.Responsibility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponsibilityListConverter implements AttributeConverter<List<Responsibility>, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<Responsibility> attribute) {
		String attributeJson = null;
		try {
			attributeJson = objectMapper.writeValueAsString(attribute);
		} catch (final JsonProcessingException e) {
			log.error("JSON writing error", e);
		}

		return attributeJson;
	}

	@Override
	public List<Responsibility> convertToEntityAttribute(String dbData) {
		List<Responsibility> attribute = null;
		try {
			attribute = objectMapper.readValue(dbData, new TypeReference<List<Responsibility>>() {
			});
		} catch (final IOException e) {
			log.error("JSON reading error", e);
		}

		return attribute;
	}

}
