package com.dhruv.ppmtool.services;


import com.dhruv.ppmtool.Project.Project;
import com.dhruv.ppmtool.exceptions.ProjectIdException;
import com.dhruv.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){

        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
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
