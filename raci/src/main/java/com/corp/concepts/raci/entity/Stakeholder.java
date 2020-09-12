package com.corp.concepts.raci.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity (name = "stakeholder")
@Data
public class Stakeholder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

    @OneToMany(mappedBy = "stakeholder")
    @JsonIgnore
    private Set<Assignment> assignments;
}
