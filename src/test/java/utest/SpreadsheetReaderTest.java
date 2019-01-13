package utest;

import org.junit.Assert;
import org.junit.Test;
import sheet.SpreadsheetReader;

import java.io.File;

public class SpreadsheetReaderTest {

    @Test
    public void testSpreadsheetReader(){
        File inputDir = new File(System.getProperty("user.dir") + "/Inputs/");
        System.out.println(System.getProperty("user.dir") + "/Inputs/");
        File []files = inputDir.listFiles();
        for (int fileIndex = 0; fileIndex < files.length; fileIndex++){
            SpreadsheetReader sr = new SpreadsheetReader(files[fileIndex].getAbsolutePath());
            Assert.assertTrue(sr.checkCellType());
            //sr.printCellsInfo();
        }
    }
}
