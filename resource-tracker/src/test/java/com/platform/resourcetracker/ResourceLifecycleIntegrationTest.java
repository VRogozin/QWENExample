package com.platform.resourcetracker;

import com.platform.resourcetracker.dto.ProjectDto;
import com.platform.resourcetracker.dto.ResourceRequestDto;
import com.platform.resourcetracker.entity.*;
import com.platform.resourcetracker.repository.ProjectRepository;
import com.platform.resourcetracker.repository.ResourceRequestRepository;
import com.platform.resourcetracker.service.ResourceTrackerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class ResourceLifecycleIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ResourceTrackerService resourceTrackerService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ResourceRequestRepository resourceRequestRepository;

    private ProjectDto testProject;

    @BeforeEach
    void setUp() {
        // Clear existing data
        resourceRequestRepository.deleteAll();
        projectRepository.deleteAll();

        // Create a test project
        testProject = new ProjectDto();
        testProject.setName("Test Project");
        testProject.setDescription("A test project for resource tracking");
        testProject.setStatus(ProjectStatus.PLANNING);
        testProject = resourceTrackerService.createProject(testProject);
    }

    @Test
    void testResourceRequestLifecycle() {
        // Step 1: Create a resource request (initial state: PENDING)
        ResourceRequestDto resourceRequest = new ResourceRequestDto();
        resourceRequest.setProjectId(testProject.getId());
        resourceRequest.setResourceType(ResourceType.FULL_STACK);
        resourceRequest.setEnvironment(Environment.DEVELOPMENT);
        resourceRequest.setMemoryGb(8);
        resourceRequest.setCpuCores(4);
        resourceRequest.setStorageGb(100);
        resourceRequest.setArtifactStorageGb(10);
        resourceRequest.setDockerImageSizeGb(5);
        resourceRequest.setFileStorageGb(20);
        resourceRequest.setDatabaseStorageGb(15);
        resourceRequest.setLogStorageGb(5);
        resourceRequest.setKafkaTopicSizeGb(2);
        resourceRequest.setJustification("Development environment resources");
        resourceRequest.setEstimatedDurationMonths(6);
        resourceRequest.setStatus(RequestStatus.PENDING);

        ResourceRequestDto createdRequest = resourceTrackerService.createResourceRequest(resourceRequest);
        assertNotNull(createdRequest.getId());
        assertEquals(RequestStatus.PENDING, createdRequest.getStatus());

        // Step 2: Update request status to APPROVED
        createdRequest.setStatus(RequestStatus.APPROVED);
        ResourceRequestDto approvedRequest = resourceTrackerService.updateResourceRequest(createdRequest.getId(), createdRequest);
        assertEquals(RequestStatus.APPROVED, approvedRequest.getStatus());

        // Step 3: Update request status to ALLOCATED
        approvedRequest.setStatus(RequestStatus.ALLOCATED);
        ResourceRequestDto allocatedRequest = resourceTrackerService.updateResourceRequest(approvedRequest.getId(), approvedRequest);
        assertEquals(RequestStatus.ALLOCATED, allocatedRequest.getStatus());

        // Step 4: Update request status to IN_USE
        allocatedRequest.setStatus(RequestStatus.IN_USE);
        ResourceRequestDto inUseRequest = resourceTrackerService.updateResourceRequest(allocatedRequest.getId(), allocatedRequest);
        assertEquals(RequestStatus.IN_USE, inUseRequest.getStatus());

        // Step 5: Verify resource utilization calculations
        Integer totalMemory = resourceTrackerService.getTotalMemoryByEnvironment(Environment.DEVELOPMENT);
        assertEquals(8, totalMemory);

        Integer totalStorage = resourceTrackerService.getTotalStorageByEnvironment(Environment.DEVELOPMENT);
        assertEquals(100, totalStorage);

        Integer totalCpu = resourceTrackerService.getTotalCpuByEnvironment(Environment.DEVELOPMENT);
        assertEquals(4, totalCpu);

        // Step 6: Test filtering by project
        List<ResourceRequestDto> requestsForProject = resourceTrackerService.getResourceRequestsByProjectId(testProject.getId());
        assertEquals(1, requestsForProject.size());
        assertEquals(testProject.getId(), requestsForProject.get(0).getProjectId());

        // Step 7: Test filtering by environment
        List<ResourceRequestDto> requestsForEnvironment = resourceTrackerService.getResourceRequestsByEnvironment(Environment.DEVELOPMENT);
        assertTrue(requestsForEnvironment.size() >= 1); // At least our test request
        
        // Verify that only 'ACTIVE' statuses are counted in utilization
        ResourceRequestDto newRequest = new ResourceRequestDto();
        newRequest.setProjectId(testProject.getId());
        newRequest.setResourceType(ResourceType.MEMORY);
        newRequest.setEnvironment(Environment.DEVELOPMENT);
        newRequest.setMemoryGb(16);
        newRequest.setStatus(RequestStatus.REJECTED); // This should not be counted in utilization
        
        ResourceRequestDto rejectedRequest = resourceTrackerService.createResourceRequest(newRequest);
        
        // Total memory should still be 8, not 24 (8+16), because rejected request is not counted
        Integer totalMemoryAfterRejected = resourceTrackerService.getTotalMemoryByEnvironment(Environment.DEVELOPMENT);
        assertEquals(8, totalMemoryAfterRejected);
    }

    @Test
    void testReportingCapabilities() {
        // Create multiple resource requests across different environments
        createSampleData();

        // Test utilization reports by environment
        Integer devMemory = resourceTrackerService.getTotalMemoryByEnvironment(Environment.DEVELOPMENT);
        Integer testMemory = resourceTrackerService.getTotalMemoryByEnvironment(Environment.TESTING);
        Integer stagingMemory = resourceTrackerService.getTotalMemoryByEnvironment(Environment.STAGING);
        Integer prodMemory = resourceTrackerService.getTotalMemoryByEnvironment(Environment.PRODUCTION);

        // Each environment should have some allocated memory
        assertTrue(devMemory > 0);
        assertTrue(testMemory > 0);
        assertTrue(stagingMemory > 0);
        assertTrue(prodMemory > 0);

        // Test project-based filtering
        List<ResourceRequestDto> allRequests = resourceTrackerService.getAllResourceRequests();
        assertTrue(allRequests.size() >= 4); // At least 4 requests from sample data

        List<ResourceRequestDto> projectRequests = resourceTrackerService.getResourceRequestsByProjectId(testProject.getId());
        assertTrue(projectRequests.size() >= 1);
    }

    private void createSampleData() {
        // Create requests for different environments
        for (Environment env : Environment.values()) {
            ResourceRequestDto req = new ResourceRequestDto();
            req.setProjectId(testProject.getId());
            req.setResourceType(ResourceType.FULL_STACK);
            req.setEnvironment(env);
            req.setMemoryGb(4 * (env.ordinal() + 1)); // Different amounts per environment
            req.setCpuCores(2 * (env.ordinal() + 1));
            req.setStorageGb(50 * (env.ordinal() + 1));
            req.setStatus(RequestStatus.ALLOCATED);
            req.setJustification("Resources for " + env.toString() + " environment");

            resourceTrackerService.createResourceRequest(req);
        }
    }
}