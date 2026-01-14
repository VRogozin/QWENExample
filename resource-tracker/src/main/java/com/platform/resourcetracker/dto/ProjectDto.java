package com.platform.resourcetracker.dto;

import com.platform.resourcetracker.entity.ProjectStatus;

public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
    private Long teamId;
    private String teamName;
    
    // Constructors
    public ProjectDto() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }
    
    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }
    
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
}