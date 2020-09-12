package com.corp.concepts.raci.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.corp.concepts.raci.entity.key.AssignmentKey;

import lombok.Data;

@Entity (name = "assignment")
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

	@ManyToOne
	@MapsId("responsibilityId")
	@JoinColumn(name = "responsibility_id")
	private Responsibility responsibility;
}
