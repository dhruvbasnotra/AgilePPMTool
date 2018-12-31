package com.dhruv.ppmtool.services;

import com.dhruv.ppmtool.repositories.BacklogRepository;
import com.dhruv.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dhruv.ppmtool.Project.Backlog;
import com.dhruv.ppmtool.Project.ProjectTask;
import com.dhruv.ppmtool.repositories.ProjectRepository;
import com.dhruv.ppmtool.Project.Project;
import com.dhruv.ppmtool.exceptions.ProjectNotFoundException;
import java.util.*;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository  backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

        //Exceptions: Project not found
        try {
            //PTs to be added to a specific project, project != null, BL exists
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            //set the bl to pt
            projectTask.setBacklog(backlog);
            //we want our project sequence to be like this: IDPRO-1  IDPRO-2  ...100 101
            Integer BacklogSequence = backlog.getPTSequence();
            // Update the BL SEQUENCE
            BacklogSequence++;
            backlog.setPTSequence(BacklogSequence);
            //Add Sequence to Project Task
            projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //INITIAL priority when priority null
            if (projectTask.getPriority() == null) {
                projectTask.setPriority(3);
            }
            //INITIAL status when status is null
            if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);
        }catch (Exception e){
            throw new ProjectNotFoundException("Project not Found");
        }
    }

    public Iterable<ProjectTask>findBacklogById(String id){
        Project project = projectRepository.findByProjectIdentifier(id);

        if(project==null){
            throw new ProjectNotFoundException("Project with ID: '"+id+"' does not exist");
        }

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){

        //make sure we are searching on an existing backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if(backlog==null){
            throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"' does not exist");
        }

        //make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

        if(projectTask == null){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
        }

        //make sure that the backlog/project id in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project: '"+backlog_id);
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id){

        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

        //Backlog backlog = projectTask.getBacklog();
       // List<ProjectTask> pts = backlog.getProjectTasks();
       // pts.remove(projectTask);
      //  backlogRepository.save(backlog);

        projectTaskRepository.delete(projectTask);
    }
}
