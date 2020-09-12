package com.corp.concepts.raci.entity.key;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class AssignmentKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1221230824025682789L;

	@Column(name = "task_id")
	private Long taskId;

	@Column(name = "stakeholder_id")
	private Long stakeholderId;
	
	@Column(name = "responsibility_id")
	private Long responsibilityId;

}
