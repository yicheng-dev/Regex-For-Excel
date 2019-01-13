package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker {
    private final static String integerRegExp = "[+-]?[0-9]+";
    private final static String doubleRegExp = "[+-]?([0-9]*\\.[0-9]+|[0-9]+\\.[0-9]*)|[+-]?([0-9]*\\.?[0-9]+|[0-9]+\\.?[0-9]*)[Ee][+-]?[0-9]+";
    private final static String cellRegExp = "[+-]?[A-Z]+[0-9]+";
    private final static String specialFormulaRegExp = "[+-]?(MIN|MAX|AVERAGE|SUM|COUNT)\\(" + "(" + integerRegExp + "|" + doubleRegExp + "|" + cellRegExp + ")"
            + "(" + "[,:]" + "(" + integerRegExp + "|" + doubleRegExp + "|" + cellRegExp + "))*" + "\\)";

    private String str;
    private boolean isValidFormula;

    public RegexChecker(String str){
        this.str = str.replaceAll("\\s+", "");
        if (str.charAt(0) != '=' || str.length() <= 1)
            isValidFormula = false;
        else
            isValidFormula = true;
    }


    public boolean isProbText(){
        return (!isProbInt() && !isProbDouble() && !isProbFormula());
    }

    public boolean isProbInt(){
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

    public boolean isProbDouble(){
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

    public boolean isProbFormula(){
        if (!isValidFormula)
            return false;
        if (!checkBracketNum())
            return false;
        checkFormula(str, 1, str.length() - 1);
        return isValidFormula;
    }

    private void checkFormula(String str, int p, int q){
        System.out.println("checkFormula -----\t" + str.substring(p, q + 1));
        if (!isValidFormula)
            return;
        if (checkParentheses(str, p, q)){
            checkFormula(str, p + 1, q - 1);
            return;
        }
        int dominant = -1;
        if ((dominant = findDominant(str, p, q)) >= 0){
            checkFormula(str, p, dominant - 1);
            checkFormula(str, dominant + 1, q);
        }
        else{
            Pattern unitPat = Pattern.compile(integerRegExp + "|" + doubleRegExp + "|" + cellRegExp + "|" + specialFormulaRegExp);
            Matcher unitMat = unitPat.matcher(str.substring(p, q + 1));
            if (!unitMat.matches()){
                System.out.println(str.substring(p, q + 1) + " doesn't match.");
                isValidFormula = false;
            }
            else{
                System.out.println(str.substring(p, q + 1) + " matches.");
            }
        }
    }

    private boolean checkParentheses(String str, int p, int q){
        if ( q == p + 1 && str.charAt(p) == '(' && str.charAt(q) == ')'){
            isValidFormula = false;
            return false;
        }
        int left = 0, right = 0;
        boolean anotherPair = false;
        for (int t = p; t <= q; t++){
            if (left > 0 && left == right)
                anotherPair = true;
            if (left < right){
                isValidFormula = false;
                return false;
            }
            if (str.charAt(t) == '(')
                left ++;
            else if (str.charAt(t) == ')')
                right ++;
        }
        if (left != right){
            isValidFormula = false;
            return false;
        }
        return  str.charAt(p) == '(' && str.charAt(q) == ')' && !anotherPair;
    }

    private int findDominant(String str, int p, int q){
        int braNum = 0;
        for (int i = q; i >= p; i--){
            if (str.charAt(i) == '(')
                braNum--;
            else if (str.charAt(i) == ')')
                braNum++;
            if (braNum == 0 && (str.charAt(i) == '+' || str.charAt(i) == '-') &&
                    i != p && str.charAt(i - 1) != 'e' && str.charAt(i - 1) != 'E'){
                return i;
            }
        }
        for (int i = q; i >= p; i--){
            if (str.charAt(i) == '(')
                braNum--;
            else if (str.charAt(i) == ')')
                braNum++;
            if (braNum == 0 && (str.charAt(i) == '*' || str.charAt(i) == '/') || str.charAt(i) == '%' || str.charAt(i) == '^' &&
                    i != p){
                return i;
            }
        }
        return -1;
    }

    private boolean checkBracketNum(){
        int braNum = 0;
        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == '(')
                braNum ++;
            else if (str.charAt(i) == ')')
                braNum --;
        }
        return braNum == 0;
    }

}
