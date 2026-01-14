package com.platform.resourcetracker.service.impl;

import com.platform.resourcetracker.dto.ProjectDto;
import com.platform.resourcetracker.dto.ResourceRequestDto;
import com.platform.resourcetracker.entity.*;
import com.platform.resourcetracker.repository.*;
import com.platform.resourcetracker.service.ResourceTrackerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ResourceTrackerServiceImpl implements ResourceTrackerService {

    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private ResourceRequestRepository resourceRequestRepository;
    
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    // Project methods
    @Override
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        return convertToDto(project);
    }
    
    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = convertToEntity(projectDto);
        
        // Set team if provided
        if (projectDto.getTeamId() != null) {
            Team team = teamRepository.findById(projectDto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with id: " + projectDto.getTeamId()));
            project.setTeam(team);
        }
        
        Project savedProject = projectRepository.save(project);
        return convertToDto(savedProject);
    }
    
    @Override
    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        
        // Update fields
        existingProject.setName(projectDto.getName());
        existingProject.setDescription(projectDto.getDescription());
        existingProject.setStatus(projectDto.getStatus());
        
        // Update team if provided
        if (projectDto.getTeamId() != null) {
            Team team = teamRepository.findById(projectDto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with id: " + projectDto.getTeamId()));
            existingProject.setTeam(team);
        }
        
        Project updatedProject = projectRepository.save(existingProject);
        return convertToDto(updatedProject);
    }
    
    @Override
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }
    
    // Resource request methods
    @Override
    public List<ResourceRequestDto> getAllResourceRequests() {
        return resourceRequestRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ResourceRequestDto> getResourceRequestsByProjectId(Long projectId) {
        return resourceRequestRepository.findByProjectId(projectId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ResourceRequestDto> getResourceRequestsByEnvironment(Environment environment) {
        return resourceRequestRepository.findByEnvironment(environment).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public ResourceRequestDto getResourceRequestById(Long id) {
        ResourceRequest request = resourceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource request not found with id: " + id));
        return convertToDto(request);
    }
    
    @Override
    public ResourceRequestDto createResourceRequest(ResourceRequestDto resourceRequestDto) {
        ResourceRequest request = convertToEntity(resourceRequestDto);
        
        // Set project if provided
        if (resourceRequestDto.getProjectId() != null) {
            Project project = projectRepository.findById(resourceRequestDto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found with id: " + resourceRequestDto.getProjectId()));
            request.setProject(project);
        }
        
        ResourceRequest savedRequest = resourceRequestRepository.save(request);
        return convertToDto(savedRequest);
    }
    
    @Override
    public ResourceRequestDto updateResourceRequest(Long id, ResourceRequestDto resourceRequestDto) {
        ResourceRequest existingRequest = resourceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource request not found with id: " + id));
        
        // Update fields
        existingRequest.setResourceType(resourceRequestDto.getResourceType());
        existingRequest.setEnvironment(resourceRequestDto.getEnvironment());
        existingRequest.setMemoryGb(resourceRequestDto.getMemoryGb());
        existingRequest.setCpuCores(resourceRequestDto.getCpuCores());
        existingRequest.setStorageGb(resourceRequestDto.getStorageGb());
        existingRequest.setArtifactStorageGb(resourceRequestDto.getArtifactStorageGb());
        existingRequest.setDockerImageSizeGb(resourceRequestDto.getDockerImageSizeGb());
        existingRequest.setFileStorageGb(resourceRequestDto.getFileStorageGb());
        existingRequest.setDatabaseStorageGb(resourceRequestDto.getDatabaseStorageGb());
        existingRequest.setLogStorageGb(resourceRequestDto.getLogStorageGb());
        existingRequest.setKafkaTopicSizeGb(resourceRequestDto.getKafkaTopicSizeGb());
        existingRequest.setStatus(resourceRequestDto.getStatus());
        existingRequest.setJustification(resourceRequestDto.getJustification());
        existingRequest.setEstimatedDurationMonths(resourceRequestDto.getEstimatedDurationMonths());
        
        // Update project if provided
        if (resourceRequestDto.getProjectId() != null) {
            Project project = projectRepository.findById(resourceRequestDto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found with id: " + resourceRequestDto.getProjectId()));
            existingRequest.setProject(project);
        }
        
        ResourceRequest updatedRequest = resourceRequestRepository.save(existingRequest);
        return convertToDto(updatedRequest);
    }
    
    @Override
    public void deleteResourceRequest(Long id) {
        if (!resourceRequestRepository.existsById(id)) {
            throw new RuntimeException("Resource request not found with id: " + id);
        }
        resourceRequestRepository.deleteById(id);
    }
    
    // Resource utilization reports
    @Override
    public Integer getTotalMemoryByEnvironment(Environment environment) {
        Integer total = resourceRequestRepository.findTotalMemoryByEnvironment(environment);
        return total != null ? total : 0;
    }
    
    @Override
    public Integer getTotalStorageByEnvironment(Environment environment) {
        Integer total = resourceRequestRepository.findTotalStorageByEnvironment(environment);
        return total != null ? total : 0;
    }
    
    @Override
    public Integer getTotalCpuByEnvironment(Environment environment) {
        Integer total = resourceRequestRepository.findTotalCpuByEnvironment(environment);
        return total != null ? total : 0;
    }
    
    // Helper methods to convert between entities and DTOs
    private ProjectDto convertToDto(Project project) {
        ProjectDto dto = modelMapper.map(project, ProjectDto.class);
        if (project.getTeam() != null) {
            dto.setTeamId(project.getTeam().getId());
            dto.setTeamName(project.getTeam().getName());
        }
        return dto;
    }
    
    private Project convertToEntity(ProjectDto dto) {
        return modelMapper.map(dto, Project.class);
    }
    
    private ResourceRequestDto convertToDto(ResourceRequest request) {
        ResourceRequestDto dto = modelMapper.map(request, ResourceRequestDto.class);
        if (request.getProject() != null) {
            dto.setProjectId(request.getProject().getId());
        }
        return dto;
    }
    
    private ResourceRequest convertToEntity(ResourceRequestDto dto) {
        return modelMapper.map(dto, ResourceRequest.class);
    }
}