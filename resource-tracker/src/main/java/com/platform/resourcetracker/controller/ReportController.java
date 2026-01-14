package com.platform.resourcetracker.controller;

import com.platform.resourcetracker.entity.Environment;
import com.platform.resourcetracker.service.ReportService;
import com.platform.resourcetracker.service.ResourceTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ResourceTrackerService resourceTrackerService;
    
    @Autowired
    private ReportService reportService;
    
    @GetMapping(value = "/resources/summary/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdfSummaryReport() throws IOException {
        var allRequests = resourceTrackerService.getAllResourceRequests();
        byte[] pdfBytes = reportService.generatePdfReport(allRequests);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "resource_summary_report.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
    
    @GetMapping(value = "/resources/summary/xlsx", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> generateExcelSummaryReport() throws IOException {
        var allRequests = resourceTrackerService.getAllResourceRequests();
        byte[] excelBytes = reportService.generateExcelReport(allRequests);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "resource_summary_report.xlsx");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }
    
    @GetMapping("/utilization/environment/{environment}")
    public ResponseEntity<Map<String, Object>> getResourceUtilizationByEnvironment(@PathVariable Environment environment) {
        Map<String, Object> utilization = new HashMap<>();
        
        utilization.put("environment", environment);
        utilization.put("totalMemoryGb", resourceTrackerService.getTotalMemoryByEnvironment(environment));
        utilization.put("totalStorageGb", resourceTrackerService.getTotalStorageByEnvironment(environment));
        utilization.put("totalCpuCores", resourceTrackerService.getTotalCpuByEnvironment(environment));
        
        return ResponseEntity.ok(utilization);
    }
    
    @GetMapping(value = "/utilization/environment/{environment}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateUtilizationReportByEnvironment(@PathVariable Environment environment) throws IOException {
        Map<String, Object> utilization = new HashMap<>();
        utilization.put("environment", environment);
        utilization.put("totalMemoryGb", resourceTrackerService.getTotalMemoryByEnvironment(environment));
        utilization.put("totalStorageGb", resourceTrackerService.getTotalStorageByEnvironment(environment));
        utilization.put("totalCpuCores", resourceTrackerService.getTotalCpuByEnvironment(environment));
        
        byte[] pdfBytes = reportService.generateUtilizationReportByEnvironment(environment, utilization);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "utilization_report_" + environment.toString() + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}