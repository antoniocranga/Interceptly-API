package com.interceptly.api.util.exports;

import com.interceptly.api.dao.IssueDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
public class ExcelDataExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List rows;
    private final String title;

    public ExcelDataExporter(List rows, String title) {
        this.rows = rows;
        this.title = title;
        this.workbook = new XSSFWorkbook();
        writeHeader();
        writeRows();
    }

    private void writeHeader() {
        sheet = workbook.createSheet(title);
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        List<Field> fields = new ArrayList<>(Arrays.stream(rows.get(0).getClass().getDeclaredFields()).toList());
        for (int i = 0; i < fields.size(); i++) {
            createCell(row, i, fields.get(i).getName(), style);
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeRows() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Object obj : rows) {
            Row row = sheet.createRow(rowCount++);
            List<Field> fields = new ArrayList<>(Arrays.stream(obj.getClass().getDeclaredFields()).toList());
            for (int i = 0; i < fields.size(); i++) {
                try {

                    fields.get(i).setAccessible(true);
                    if (fields.get(i).get(obj) != null && !fields.get(i).getName().equals("issue")) {
                        log.info(
                                fields.get(i).get(obj).toString()
                        );
                        createCell(row, i, fields.get(i).get(obj).toString(), style);
                    } else {

                        log.info(
                                fields.get(i).getName()
                        );
                        createCell(row, i, " ", style);
                    }
                } catch (IllegalAccessException e) {
//                    throw new RuntimeException(e);
                    log.error(e.toString());
                }
            }
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
