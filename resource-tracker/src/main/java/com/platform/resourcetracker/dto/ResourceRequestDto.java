package com.platform.resourcetracker.dto;

import com.platform.resourcetracker.entity.ResourceType;
import com.platform.resourcetracker.entity.Environment;
import com.platform.resourcetracker.entity.RequestStatus;

public class ResourceRequestDto {
    private Long id;
    private Long projectId;
    private ResourceType resourceType;
    private Environment environment;
    private Integer memoryGb;
    private Integer cpuCores;
    private Integer storageGb;
    private Integer artifactStorageGb;
    private Integer dockerImageSizeGb;
    private Integer fileStorageGb;
    private Integer databaseStorageGb;
    private Integer logStorageGb;
    private Integer kafkaTopicSizeGb;
    private RequestStatus status;
    private String justification;
    private Integer estimatedDurationMonths;
    
    // Constructors
    public ResourceRequestDto() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    
    public ResourceType getResourceType() { return resourceType; }
    public void setResourceType(ResourceType resourceType) { this.resourceType = resourceType; }
    
    public Environment getEnvironment() { return environment; }
    public void setEnvironment(Environment environment) { this.environment = environment; }
    
    public Integer getMemoryGb() { return memoryGb; }
    public void setMemoryGb(Integer memoryGb) { this.memoryGb = memoryGb; }
    
    public Integer getCpuCores() { return cpuCores; }
    public void setCpuCores(Integer cpuCores) { this.cpuCores = cpuCores; }
    
    public Integer getStorageGb() { return storageGb; }
    public void setStorageGb(Integer storageGb) { this.storageGb = storageGb; }
    
    public Integer getArtifactStorageGb() { return artifactStorageGb; }
    public void setArtifactStorageGb(Integer artifactStorageGb) { this.artifactStorageGb = artifactStorageGb; }
    
    public Integer getDockerImageSizeGb() { return dockerImageSizeGb; }
    public void setDockerImageSizeGb(Integer dockerImageSizeGb) { this.dockerImageSizeGb = dockerImageSizeGb; }
    
    public Integer getFileStorageGb() { return fileStorageGb; }
    public void setFileStorageGb(Integer fileStorageGb) { this.fileStorageGb = fileStorageGb; }
    
    public Integer getDatabaseStorageGb() { return databaseStorageGb; }
    public void setDatabaseStorageGb(Integer databaseStorageGb) { this.databaseStorageGb = databaseStorageGb; }
    
    public Integer getLogStorageGb() { return logStorageGb; }
    public void setLogStorageGb(Integer logStorageGb) { this.logStorageGb = logStorageGb; }
    
    public Integer getKafkaTopicSizeGb() { return kafkaTopicSizeGb; }
    public void setKafkaTopicSizeGb(Integer kafkaTopicSizeGb) { this.kafkaTopicSizeGb = kafkaTopicSizeGb; }
    
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    
    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }
    
    public Integer getEstimatedDurationMonths() { return estimatedDurationMonths; }
    public void setEstimatedDurationMonths(Integer estimatedDurationMonths) { this.estimatedDurationMonths = estimatedDurationMonths; }
}