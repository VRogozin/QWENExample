package com.platform.resourcetracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resource_requests")
public class ResourceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;
    
    @Enumerated(EnumType.STRING)
    private Environment environment;
    
    @Column(name = "memory_gb")
    private Integer memoryGb;
    
    @Column(name = "cpu_cores")
    private Integer cpuCores;
    
    @Column(name = "storage_gb")
    private Integer storageGb;
    
    @Column(name = "artifact_storage_gb")
    private Integer artifactStorageGb;
    
    @Column(name = "docker_image_size_gb")
    private Integer dockerImageSizeGb;
    
    @Column(name = "file_storage_gb")
    private Integer fileStorageGb;
    
    @Column(name = "database_storage_gb")
    private Integer databaseStorageGb;
    
    @Column(name = "log_storage_gb")
    private Integer logStorageGb;
    
    @Column(name = "kafka_topic_size_gb")
    private Integer kafkaTopicSizeGb;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    
    private String justification;
    
    @Column(name = "estimated_duration_months")
    private Integer estimatedDurationMonths;
    
    // Constructors
    public ResourceRequest() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    
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

// Enums for resource types and environments
enum ResourceType {
    MEMORY, CPU, STORAGE, ARTIFACT_STORAGE, DOCKER_IMAGE_STORAGE, FILE_STORAGE, 
    DATABASE_STORAGE, LOG_STORAGE, KAFKA_TOPIC_STORAGE, FULL_STACK
}

enum Environment {
    DEVELOPMENT, TESTING, STAGING, PRODUCTION
}

enum RequestStatus {
    PENDING, APPROVED, REJECTED, ALLOCATED, IN_USE, RELEASED
}