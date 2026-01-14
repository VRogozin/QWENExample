package com.platform.resourcetracker.service;

import com.platform.resourcetracker.dto.ProjectDto;
import com.platform.resourcetracker.dto.ResourceRequestDto;
import com.platform.resourcetracker.entity.Environment;
import java.util.List;

public interface ResourceTrackerService {
    // Project methods
    List<ProjectDto> getAllProjects();
    ProjectDto getProjectById(Long id);
    ProjectDto createProject(ProjectDto projectDto);
    ProjectDto updateProject(Long id, ProjectDto projectDto);
    void deleteProject(Long id);
    
    // Resource request methods
    List<ResourceRequestDto> getAllResourceRequests();
    List<ResourceRequestDto> getResourceRequestsByProjectId(Long projectId);
    List<ResourceRequestDto> getResourceRequestsByEnvironment(Environment environment);
    ResourceRequestDto getResourceRequestById(Long id);
    ResourceRequestDto createResourceRequest(ResourceRequestDto resourceRequestDto);
    ResourceRequestDto updateResourceRequest(Long id, ResourceRequestDto resourceRequestDto);
    void deleteResourceRequest(Long id);
    
    // Resource utilization reports
    Integer getTotalMemoryByEnvironment(Environment environment);
    Integer getTotalStorageByEnvironment(Environment environment);
    Integer getTotalCpuByEnvironment(Environment environment);
}