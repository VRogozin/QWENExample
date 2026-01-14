package com.platform.resourcetracker.service;

import com.platform.resourcetracker.dto.ResourceRequestDto;
import com.platform.resourcetracker.entity.Environment;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    public byte[] generatePdfReport(List<ResourceRequestDto> resourceRequests) throws IOException {
        Document document = new Document(PageSize.A4.rotate()); // Landscape for better table display
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        
        document.open();
        
        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph("Resource Utilization Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));
        
        // Create table
        PdfPTable table = new PdfPTable(12); // Adjust number of columns as needed
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        
        // Add headers
        String[] headers = {"ID", "Project ID", "Resource Type", "Environment", "Memory (GB)", 
                           "CPU Cores", "Storage (GB)", "Status", "Justification", 
                           "Artifact Storage", "Docker Image Size", "File Storage"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
        
        // Add data rows
        for (ResourceRequestDto request : resourceRequests) {
            table.addCell(new Phrase(String.valueOf(request.getId()), FontFactory.getFont(FontFactory.HELVETICA, 9)));
            table.addCell(new Phrase(String.valueOf(request.getProjectId()), FontFactory.getFont(FontFactory.HELVETICA, 9)));
            table.addCell(new Phrase(request.getResourceType() != null ? request.getResourceType().toString() : "", FontFactory.getFont(FontFactory.HELVETICA, 9)));
            table.addCell(new Phrase(request.getEnvironment() != null ? request.getEnvironment().toString() : "", FontFactory.getFont(FontFactory.HELVETICA, 9)));
            table.addCell(new Phrase(request.getMemoryGb() != null ? request.getMemoryGb().toString() : "0", FontFactory.getFont(FontFactory.HELVETICA, 9)));
            table.addCell(new Phrase(request.getCpuCores() != null ? request.getCpuCores().toString() : "0", FontFactory.getFont(FontFactory.HELVETICA, 9)));
            table.addCell(new Phrase(request.getStorageGb() != null ? request.getStorageGb().toString() : "0", FontFactory.getFont(FontFactory.HELVETICA, 9)));
            table.addCell(new Phrase(request.getStatus() != null ? request.getStatus().toString() : "", FontFactory.getFont(FontFactory.HELVETICA, 9)));
            table.addCell(new Phrase(request.getJustification() != null ? request.getJustification() : "", FontFactory.getFont(FontFactory.HELVETICA, 9)));
            table.addCell(new Phrase(request.getArtifactStorageGb() != null ? request.getArtifactStorageGb().toString() : "0", FontFactory.getFont(FontFactory.HELVETICA, 9)));
            table.addCell(new Phrase(request.getDockerImageSizeGb() != null ? request.getDockerImageSizeGb().toString() : "0", FontFactory.getFont(FontFactory.HELVETICA, 9)));
            table.addCell(new Phrase(request.getFileStorageGb() != null ? request.getFileStorageGb().toString() : "0", FontFactory.getFont(FontFactory.HELVETICA, 9)));
        }
        
        document.add(table);
        
        // Add summary statistics
        document.add(new Paragraph(" "));
        Font statsTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Paragraph statsTitle = new Paragraph("Summary Statistics", statsTitleFont);
        document.add(statsTitle);
        
        // Calculate and display totals
        int totalMemory = resourceRequests.stream()
                .filter(req -> req.getMemoryGb() != null)
                .mapToInt(ResourceRequestDto::getMemoryGb)
                .sum();
                
        int totalCpu = resourceRequests.stream()
                .filter(req -> req.getCpuCores() != null)
                .mapToInt(ResourceRequestDto::getCpuCores)
                .sum();
                
        int totalStorage = resourceRequests.stream()
                .filter(req -> req.getStorageGb() != null)
                .mapToInt(ResourceRequestDto::getStorageGb)
                .sum();
        
        document.add(new Paragraph("Total Memory (GB): " + totalMemory));
        document.add(new Paragraph("Total CPU Cores: " + totalCpu));
        document.add(new Paragraph("Total Storage (GB): " + totalStorage));
        
        document.close();
        
        return out.toByteArray();
    }

    public byte[] generateExcelReport(List<ResourceRequestDto> resourceRequests) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Resource Requests");
        
        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Project ID", "Resource Type", "Environment", "Memory (GB)", 
                           "CPU Cores", "Storage (GB)", "Status", "Justification", 
                           "Artifact Storage", "Docker Image Size", "File Storage"};
        
        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Create data rows
        int rowNum = 1;
        for (ResourceRequestDto request : resourceRequests) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(request.getId() != null ? request.getId() : 0);
            row.createCell(1).setCellValue(request.getProjectId() != null ? request.getProjectId() : 0);
            row.createCell(2).setCellValue(request.getResourceType() != null ? request.getResourceType().toString() : "");
            row.createCell(3).setCellValue(request.getEnvironment() != null ? request.getEnvironment().toString() : "");
            row.createCell(4).setCellValue(request.getMemoryGb() != null ? request.getMemoryGb() : 0);
            row.createCell(5).setCellValue(request.getCpuCores() != null ? request.getCpuCores() : 0);
            row.createCell(6).setCellValue(request.getStorageGb() != null ? request.getStorageGb() : 0);
            row.createCell(7).setCellValue(request.getStatus() != null ? request.getStatus().toString() : "");
            row.createCell(8).setCellValue(request.getJustification() != null ? request.getJustification() : "");
            row.createCell(9).setCellValue(request.getArtifactStorageGb() != null ? request.getArtifactStorageGb() : 0);
            row.createCell(10).setCellValue(request.getDockerImageSizeGb() != null ? request.getDockerImageSizeGb() : 0);
            row.createCell(11).setCellValue(request.getFileStorageGb() != null ? request.getFileStorageGb() : 0);
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // Create summary sheet
        Sheet summarySheet = workbook.createSheet("Summary");
        Row summaryHeaderRow = summarySheet.createRow(0);
        summaryHeaderRow.createCell(0).setCellValue("Metric");
        summaryHeaderRow.createCell(1).setCellValue("Value");
        
        // Calculate and add summary data
        int totalMemory = resourceRequests.stream()
                .filter(req -> req.getMemoryGb() != null)
                .mapToInt(ResourceRequestDto::getMemoryGb)
                .sum();
                
        int totalCpu = resourceRequests.stream()
                .filter(req -> req.getCpuCores() != null)
                .mapToInt(ResourceRequestDto::getCpuCores)
                .sum();
                
        int totalStorage = resourceRequests.stream()
                .filter(req -> req.getStorageGb() != null)
                .mapToInt(ResourceRequestDto::getStorageGb)
                .sum();
        
        int rowIndex = 1;
        Row memoryRow = summarySheet.createRow(rowIndex++);
        memoryRow.createCell(0).setCellValue("Total Memory (GB)");
        memoryRow.createCell(1).setCellValue(totalMemory);
        
        Row cpuRow = summarySheet.createRow(rowIndex++);
        cpuRow.createCell(0).setCellValue("Total CPU Cores");
        cpuRow.createCell(1).setCellValue(totalCpu);
        
        Row storageRow = summarySheet.createRow(rowIndex++);
        storageRow.createCell(0).setCellValue("Total Storage (GB)");
        storageRow.createCell(1).setCellValue(totalStorage);
        
        // Auto-size summary columns
        summarySheet.autoSizeColumn(0);
        summarySheet.autoSizeColumn(1);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        
        return out.toByteArray();
    }

    public byte[] generateUtilizationReportByEnvironment(Environment environment, Map<String, Object> utilizationData) throws IOException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        
        document.open();
        
        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph("Resource Utilization Report - " + environment.toString(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));
        
        // Add utilization data
        Font itemFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        document.add(new Paragraph("Environment: " + environment.toString(), itemFont));
        document.add(new Paragraph(" "));
        
        document.add(new Paragraph("Total Memory (GB): " + utilizationData.get("totalMemoryGb"), itemFont));
        document.add(new Paragraph("Total Storage (GB): " + utilizationData.get("totalStorageGb"), itemFont));
        document.add(new Paragraph("Total CPU Cores: " + utilizationData.get("totalCpuCores"), itemFont));
        document.add(new Paragraph(" "));
        
        // Close document
        document.close();
        
        return out.toByteArray();
    }
}