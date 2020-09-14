package com.corp.concepts.raci.repository;

import org.springframework.data.repository.CrudRepository;

import com.corp.concepts.raci.entity.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {

	public Task findFirstByDetail(String detail);

}
