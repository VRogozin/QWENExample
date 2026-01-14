package com.platform.resourcetracker.repository;

import com.platform.resourcetracker.entity.ResourceRequest;
import com.platform.resourcetracker.entity.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRequestRepository extends JpaRepository<ResourceRequest, Long> {
    List<ResourceRequest> findByProjectId(Long projectId);
    
    List<ResourceRequest> findByEnvironment(Environment environment);
    
    @Query("SELECT SUM(r.memoryGb) FROM ResourceRequest r WHERE r.environment = :environment AND r.status IN ('APPROVED', 'ALLOCATED', 'IN_USE')")
    Integer findTotalMemoryByEnvironment(@Param("environment") Environment environment);
    
    @Query("SELECT SUM(r.storageGb) FROM ResourceRequest r WHERE r.environment = :environment AND r.status IN ('APPROVED', 'ALLOCATED', 'IN_USE')")
    Integer findTotalStorageByEnvironment(@Param("environment") Environment environment);
    
    @Query("SELECT SUM(r.cpuCores) FROM ResourceRequest r WHERE r.environment = :environment AND r.status IN ('APPROVED', 'ALLOCATED', 'IN_USE')")
    Integer findTotalCpuByEnvironment(@Param("environment") Environment environment);
}