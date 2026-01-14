package com.platform.resourcetracker.controller;

import com.platform.resourcetracker.dto.ProjectDto;
import com.platform.resourcetracker.dto.ResourceRequestDto;
import com.platform.resourcetracker.entity.Environment;
import com.platform.resourcetracker.service.ResourceTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resources")
@CrossOrigin(origins = "*")
public class ResourceTrackerController {

    @Autowired
    private ResourceTrackerService resourceTrackerService;
    
    // Project endpoints
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<ProjectDto> projects = resourceTrackerService.getAllProjects();
        return ResponseEntity.ok(projects);
    }
    
    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        ProjectDto project = resourceTrackerService.getProjectById(id);
        return ResponseEntity.ok(project);
    }
    
    @PostMapping("/projects")
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectDto projectDto) {
        ProjectDto createdProject = resourceTrackerService.createProject(projectDto);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }
    
    @PutMapping("/projects/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
        ProjectDto updatedProject = resourceTrackerService.updateProject(id, projectDto);
        return ResponseEntity.ok(updatedProject);
    }
    
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        resourceTrackerService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
    
    // Resource request endpoints
    @GetMapping("/requests")
    public ResponseEntity<List<ResourceRequestDto>> getAllResourceRequests() {
        List<ResourceRequestDto> requests = resourceTrackerService.getAllResourceRequests();
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/requests/project/{projectId}")
    public ResponseEntity<List<ResourceRequestDto>> getResourceRequestsByProjectId(@PathVariable Long projectId) {
        List<ResourceRequestDto> requests = resourceTrackerService.getResourceRequestsByProjectId(projectId);
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/requests/environment/{environment}")
    public ResponseEntity<List<ResourceRequestDto>> getResourceRequestsByEnvironment(@PathVariable Environment environment) {
        List<ResourceRequestDto> requests = resourceTrackerService.getResourceRequestsByEnvironment(environment);
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/requests/{id}")
    public ResponseEntity<ResourceRequestDto> getResourceRequestById(@PathVariable Long id) {
        ResourceRequestDto request = resourceTrackerService.getResourceRequestById(id);
        return ResponseEntity.ok(request);
    }
    
    @PostMapping("/requests")
    public ResponseEntity<ResourceRequestDto> createResourceRequest(@RequestBody ResourceRequestDto resourceRequestDto) {
        ResourceRequestDto createdRequest = resourceTrackerService.createResourceRequest(resourceRequestDto);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }
    
    @PutMapping("/requests/{id}")
    public ResponseEntity<ResourceRequestDto> updateResourceRequest(@PathVariable Long id, @RequestBody ResourceRequestDto resourceRequestDto) {
        ResourceRequestDto updatedRequest = resourceTrackerService.updateResourceRequest(id, resourceRequestDto);
        return ResponseEntity.ok(updatedRequest);
    }
    
    @DeleteMapping("/requests/{id}")
    public ResponseEntity<Void> deleteResourceRequest(@PathVariable Long id) {
        resourceTrackerService.deleteResourceRequest(id);
        return ResponseEntity.noContent().build();
    }
    
    // Resource utilization reports
    @GetMapping("/utilization/memory/{environment}")
    public ResponseEntity<Integer> getTotalMemoryByEnvironment(@PathVariable Environment environment) {
        Integer totalMemory = resourceTrackerService.getTotalMemoryByEnvironment(environment);
        return ResponseEntity.ok(totalMemory);
    }
    
    @GetMapping("/utilization/storage/{environment}")
    public ResponseEntity<Integer> getTotalStorageByEnvironment(@PathVariable Environment environment) {
        Integer totalStorage = resourceTrackerService.getTotalStorageByEnvironment(environment);
        return ResponseEntity.ok(totalStorage);
    }
    
    @GetMapping("/utilization/cpu/{environment}")
    public ResponseEntity<Integer> getTotalCpuByEnvironment(@PathVariable Environment environment) {
        Integer totalCpu = resourceTrackerService.getTotalCpuByEnvironment(environment);
        return ResponseEntity.ok(totalCpu);
    }
}