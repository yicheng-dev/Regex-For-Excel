package utest;

import org.junit.Assert;
import org.junit.Test;
import regex.Regex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RegexTest {

    private int getRegexResult(String str){
        if (Regex.isProbInt(str))
            return 0;
        if (Regex.isProbDouble(str))
            return 1;
        if (Regex.isProbFormula(str))
            return 2;
        return 3;
    }

    @Test
    public void testRegexResult() {
        String pathname = "sample";
        try {
            FileReader reader = new FileReader(pathname);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                String []strs = line.split("\\s+");
                String inputStr = strs[0];
                String answerStr = strs[1];
                try{
                    Assert.assertEquals(getRegexResult(inputStr), Integer.parseInt(answerStr));
                }catch (AssertionError error){
                    System.out.println(inputStr + " " + answerStr);
                    throw new AssertionError();
                }
            }
            br.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
