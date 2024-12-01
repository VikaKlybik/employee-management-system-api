package com.klybik.management.service;

import com.klybik.management.constant.enums.EvaluatorTypeEnum;
import com.klybik.management.constant.enums.MeasureUnitEnum;
import com.klybik.management.entity.*;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelReportService {

    private static final String KPI_REPORT = "reports/kpi/reportKpi.xlsx";
    private static final String SURVEY_REPORT = "reports/survey/reportSurvey.xlsx";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public byte[] generateKpiReport(Employee employee, List<KPI> kpiList) {
        Workbook workbook = readTemplate(KPI_REPORT);
        Sheet sheet = workbook.getSheetAt(0);
        setEmployeeData(sheet, employee);
        setKpiData(sheet, kpiList);
        return getByteFromWorkbook(workbook);
    }

    public byte[] generateSurveyReport(Employee employee, List<AssessmentSummary> assessmentSummaryList, List<Competency> competencies) {
        Workbook workbook = readTemplate(SURVEY_REPORT);
        Sheet sheet = workbook.getSheetAt(0);
        setEmployeeData(sheet, employee);
        setSurveyData(sheet, competencies, assessmentSummaryList);
        return getByteFromWorkbook(workbook);
    }

    private void setSurveyData(Sheet sheet, List<Competency> competencies, List<AssessmentSummary> assessmentSummaryList) {
        int startIndex = 11;
        Row firstRow = sheet.getRow(startIndex);
        for (Competency competency : competencies) {
            Row row = sheet.getRow(startIndex);
            if (row == null) {
                row = copyRow(sheet, firstRow, startIndex, 7);
            } else {
                firstRow = row;
            }
            row.getCell(1).setCellValue(competency.getName());
            Map<EvaluatorTypeEnum, List<AssessmentSummary>> assessmentSummaryMap = assessmentSummaryList.stream()
                    .filter(assessmentSummary -> assessmentSummary.getCompetency().getId().equals(competency.getId()))
                    .collect(Collectors.groupingBy(AssessmentSummary::getEvaluatorType));
            setMarkToCell(row.getCell(2), assessmentSummaryMap.get(EvaluatorTypeEnum.COLLEAGUE));
            setMarkToCell(row.getCell(3), assessmentSummaryMap.get(EvaluatorTypeEnum.LEAD));
            setMarkToCell(row.getCell(4), assessmentSummaryMap.get(EvaluatorTypeEnum.SUBORDINATE));
            Double averageEmployeeValue = assessmentSummaryMap.entrySet()
                    .stream()
                    .filter(evaluatorTypeEnumListEntry -> !evaluatorTypeEnumListEntry.getKey().equals(EvaluatorTypeEnum.SELF))
                    .map(Map.Entry::getValue)
                    .flatMap(Collection::stream)
                    .mapToDouble(assessment -> assessment.getAssessmentSummary().doubleValue())
                    .average()
                    .orElse(0);
            row.getCell(5).setCellValue(averageEmployeeValue.equals((double) 0) ? "'-": "%.2f".formatted(averageEmployeeValue));
            setMarkToCell(row.getCell(6), assessmentSummaryMap.get(EvaluatorTypeEnum.SELF));
            Double averageTotalValue = assessmentSummaryMap.values()
                    .stream()
                    .flatMap(Collection::stream)
                    .mapToDouble(assessment -> assessment.getAssessmentSummary().doubleValue())
                    .average()
                    .orElse(0);
            row.getCell(7).setCellValue(averageTotalValue.equals((double) 0) ? "'-": "%.2f".formatted(averageTotalValue));
            startIndex++;
        }
    }

    private void setMarkToCell(Cell cell, List<AssessmentSummary> assessmentSummaryList) {
        String value = assessmentSummaryList == null || assessmentSummaryList.isEmpty()
                ? "'-"
                : "%.2f".formatted(assessmentSummaryList.get(0).getAssessmentSummary().doubleValue());
        cell.setCellValue(value);
    }

    private void setKpiData(Sheet sheet, List<KPI> kpiList) {
        if (kpiList.size() > 1) {
            sheet.shiftRows(11, sheet.getLastRowNum(), kpiList.size() - 1, true, true);
        }
        int startIndex = 10;
        BigDecimal sumOfKpiIndex = BigDecimal.ZERO;
        Row kpiListFirstRow = sheet.getRow(startIndex);
        for (KPI kpi : kpiList) {
            Row row = sheet.getRow(startIndex);
            if (row == null) {
                row = copyRow(sheet, kpiListFirstRow, startIndex, 6);
            } else {
                kpiListFirstRow = row;
            }
            KPIAssessment kpiAssessment = getLatestKPIAssessment(kpi);
            row.getCell(1).setCellValue(kpi.getName());
            row.getCell(2).setCellValue(kpi.getDescription());
            row.getCell(3).setCellValue(
                    formatValueAccordingMeasureUnit(
                            kpiAssessment == null ? BigDecimal.ZERO : kpiAssessment.getActualValue(),
                            kpi.getMeasureUnit()
                    )
            );
            row.getCell(4).setCellValue(formatValueAccordingMeasureUnit(
                    kpi.getTargetValue(),
                    kpi.getMeasureUnit()
            ));
            row.getCell(5).setCellValue("%.2f".formatted(kpi.getWeight().multiply(BigDecimal.valueOf(0.01)).doubleValue()));
            BigDecimal kpiIndex = calculateKPIIndex(
                    kpi,
                    kpiAssessment == null ? BigDecimal.ZERO : kpiAssessment.getActualValue()
            );
            row.getCell(6).setCellValue(
                    "%.2f".formatted(kpiIndex.multiply(BigDecimal.valueOf(0.01)).doubleValue())
            );
            sumOfKpiIndex = sumOfKpiIndex.add(kpiIndex);
            startIndex++;
        }
        sheet.getRow(startIndex).getCell(6).setCellValue("%.2f".formatted(sumOfKpiIndex.multiply(BigDecimal.valueOf(0.01)).doubleValue()));
    }

    private Row copyRow(Sheet sheet, Row copyRow, int index, int countOfCell) {
        Row row = sheet.createRow(index);
        for (int i = 1; i <= countOfCell; i++) {
            row.createCell(i).setCellStyle(copyRow.getCell(i).getCellStyle());
        }
        row.setRowStyle(copyRow.getRowStyle());
        return row;
    }

    private BigDecimal calculateKPIIndex(KPI kpi, BigDecimal actualValue) {
        BigDecimal percentOfComplete = actualValue.divide(kpi.getTargetValue(), 2, MathContext.DECIMAL32.getRoundingMode());
        return percentOfComplete.multiply(BigDecimal.valueOf(kpi.getWeight().doubleValue()));
    }

    private String formatValueAccordingMeasureUnit(BigDecimal value, MeasureUnitEnum measureUnit) {
        String measureUnitSign = measureUnit == MeasureUnitEnum.PERCENT ? "%" : "";
        return "%.0f%s".formatted(value.doubleValue(), measureUnitSign);
    }

    private KPIAssessment getLatestKPIAssessment(KPI kpi) {
        return kpi.getKpiAssessments().stream()
                .max(Comparator.comparing(KPIAssessment::getAssessmentDate))
                .orElse(null);
    }

    private void setEmployeeData(Sheet sheet, Employee employee) {
        int rowIndex = 3;
        sheet.getRow(rowIndex).getCell(2).setCellValue("%s %s".formatted(
                employee.getUser().getFirstName(),
                employee.getUser().getLastName())
        );

        rowIndex++;
        sheet.getRow(rowIndex).getCell(2).setCellValue(employee.getUser().getEmail());

        rowIndex++;
        sheet.getRow(rowIndex).getCell(2).setCellValue("%s, %s".formatted(
                employee.getJobTitle().getDepartment().getName(),
                employee.getJobTitle().getName()
        ));

        rowIndex++;
        sheet.getRow(rowIndex).getCell(2).setCellValue("%s".formatted(
                DATE_FORMAT.format(employee.getWorkSince())
        ));
    }

    private Workbook readTemplate(String filePath) {
        try {
            InputStream templateStream = new ClassPathResource(filePath).getInputStream();
            return new XSSFWorkbook(templateStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getByteFromWorkbook(Workbook workbook) {
        final var byteOut = new ByteArrayOutputStream();
        try {
            workbook.write(byteOut);
            byteOut.close();
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteOut.toByteArray();
    }
}
