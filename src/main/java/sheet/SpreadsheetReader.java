package sheet;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import regex.RegexChecker;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

public class SpreadsheetReader {

    private Workbook workbook;
    private String filename;
    private Vector<Cell> cells;

    public SpreadsheetReader(String filename){
        this.filename = filename;
        cells = new Vector<Cell>();
        try {
            workbook = WorkbookFactory.create(new FileInputStream(filename));
        }catch (IOException e){
            System.out.println("File Not Found.");
            workbook = null;
            return;
        }catch (InvalidFormatException e){
            System.out.println("Invalid format.");
            workbook = null;
            return;
        }
        System.out.println("Sheet Num: " + workbook.getNumberOfSheets());
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++){
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            System.out.println("NumberOfRows: " + sheet.getLastRowNum() + 1);
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++){
                Row row = sheet.getRow(rowIndex);
                System.out.println("NumberOfCols: " + row.getLastCellNum() + 1);
                for (int colIndex = 0; colIndex <= row.getLastCellNum(); colIndex++){
                    Cell cell = row.getCell(colIndex);
                    try{
                        if (cell != null)
                            cells.add(cell);
                    }catch (NullPointerException e){
                        System.out.println("Null cell.");
                    }
                }
            }
        }
    }

    public void printCellsInfo(){
        System.out.println("Here is the information of spreadsheet " + filename);
        for (int i = 0; i < cells.size(); i++){
            System.out.print("sheet: " + cells.get(i).getSheet().getSheetName() + "\trow: " + cells.get(i).getRowIndex() + "\tcol: " + cells.get(i).getColumnIndex() + "\ttype: ");
            //System.out.println(cells.get(i).getCellType());
            if (cells.get(i).getCellTypeEnum() == CellType.STRING)
                System.out.println("String\tdata: " + cells.get(i).getStringCellValue());
            else if (cells.get(i).getCellTypeEnum() == CellType.NUMERIC)
                System.out.println("Numeric\tdata: " + cells.get(i).getNumericCellValue());
            else if (cells.get(i).getCellTypeEnum() == CellType.BOOLEAN)
                System.out.println("Boolean\tdata: " + cells.get(i).getBooleanCellValue());
            else if (cells.get(i).getCellTypeEnum() == CellType.FORMULA)
                System.out.println("Formula\tdata: " + cells.get(i).getCellFormula());
            else
                System.out.println();
        }
    }

    public boolean checkCellType(){
        for (int i = 0; i < cells.size(); i++){
            Cell cell = cells.get(i);
            if (cell.getCellTypeEnum() == CellType.STRING){
                RegexChecker regexChecker = new RegexChecker(cell.getStringCellValue());
                if (!regexChecker.isProbText()){
                    System.out.println("Check failure --- " + cell.getStringCellValue());
                    return false;
                }
            }
            if (cell.getCellTypeEnum() == CellType.FORMULA){
                RegexChecker regexChecker = new RegexChecker("=" + cell.getCellFormula());
                if (!regexChecker.isProbFormula()){
                    System.out.println("Check failure --- " + cell.getCellFormula());
                    return false;
                }
            }
            if (cell.getCellTypeEnum() == CellType.NUMERIC){
                RegexChecker regexChecker = new RegexChecker(String.valueOf(cell.getNumericCellValue()));
                if (!regexChecker.isProbDouble() && !regexChecker.isProbInt()){
                    System.out.println("Check failure --- " + cell.getNumericCellValue());
                    return false;
                }
            }
        }
        return true;
    }
}
