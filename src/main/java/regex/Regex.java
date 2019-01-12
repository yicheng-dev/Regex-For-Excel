package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    private final static String integerRegExp = "[+-]?[0-9]+";
    private final static String doubleRegExp = "[+-]?([0-9]*\\.[0-9]+|[0-9]+\\.[0-9]*)|[+-]?([0-9]*\\.?[0-9]+|[0-9]+\\.?[0-9]*)[Ee][+-]?[0-9]+";
    private final static String cellRegExp = "[A-Z]+[0-9]+";
    private final static String specialFormulaRegExp = "(MIN|MAX|AVERAGE|SUM|COUNT)\\(" + "(" + integerRegExp + "|" + doubleRegExp + "|" + cellRegExp + ")"
            + "(" + "[,:]" + "(" + integerRegExp + "|" + doubleRegExp + "|" + cellRegExp + "))*" + "\\)";

    public static boolean isProbText(String str){
        return (!isProbInt(str) && !isProbDouble(str) && !isProbFormula(str));
    }

    public static boolean isProbInt(String str){
        Pattern integerPat = Pattern.compile(integerRegExp);
        Matcher integerMat = integerPat.matcher(str);
        if (!integerMat.matches())
            return false;
        try{
            Integer.parseInt(str);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    public static boolean isProbDouble(String str){
        Pattern doublePat = Pattern.compile(doubleRegExp);
        Matcher doubleMat = doublePat.matcher(str);
        if (!doubleMat.matches())
            return false;
        try{
            Double.parseDouble(str);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    public static boolean isProbFormula(String str){
        Pattern formulaPat = Pattern.compile(specialFormulaRegExp);
        Matcher formulaMat = formulaPat.matcher(str);
        return formulaMat.matches();
    }

}
