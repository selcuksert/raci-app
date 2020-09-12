package com.corp.concepts.raci.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.springframework.lang.Nullable;

import com.corp.concepts.raci.entity.converter.ListConverter;
import com.corp.concepts.raci.entity.converter.MapConverter;
import com.corp.concepts.raci.entity.key.AssignmentKey;

import lombok.Data;

@Entity(name = "assignment")
@Data
public class Assignment {

	@EmbeddedId
	private AssignmentKey id;

	@ManyToOne
	@MapsId("taskId")
	@JoinColumn(name = "task_id")
	private Task task;

	@ManyToOne
	@MapsId("stakeholderId")
	@JoinColumn(name = "stakeholder_id")
	private Stakeholder stakeholder;

	@Convert(converter = ListConverter.class)
	private List<Responsibility> responsibilities;

	@Convert(converter = MapConverter.class)
	@Nullable
	private Map<String, String> additionalInfo;
}