package com.dhruv.ppmtool.services;

import com.dhruv.ppmtool.Project.Backlog;
import com.dhruv.ppmtool.Project.Project;
import com.dhruv.ppmtool.exceptions.ProjectIdException;
import com.dhruv.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.dhruv.ppmtool.repositories.BacklogRepository;
import org.springframework.stereotype.Service;



@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project){

        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            if(project.getId()==null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId()!=null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);

        }catch(Exception e){
            throw new ProjectIdException("Project Id '"+  project.getProjectIdentifier().toUpperCase()+"'already exists" );
        }

    }

    public Project findProjectByIdentiifier(String projectId){

        Project project=projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project==null){
            throw new ProjectIdException("Project Id '"+  projectId+"'does not exist" );

        }
            return  project;

    }

    public Iterable<Project> findAllProjects(){
        return  projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId){

        Project project=projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project==null){
            throw new ProjectIdException("Cannot delete project with ID '"+projectId+"'. this project does not exist");
        }
        projectRepository.delete(project);
    }





}
