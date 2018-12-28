package com.dhruv.ppmtool.services;

import com.dhruv.ppmtool.repositories.BacklogRepository;
import com.dhruv.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository  backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

}
