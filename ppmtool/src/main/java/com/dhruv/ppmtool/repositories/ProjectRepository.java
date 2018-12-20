package com.dhruv.ppmtool.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dhruv.ppmtool.Project.*;


@Repository
public interface ProjectRepository extends CrudRepository<Project,Long> {



}
