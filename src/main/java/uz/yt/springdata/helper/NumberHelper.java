package uz.yt.springdata.helper;

import java.math.BigDecimal;

public class NumberHelper {

    public static boolean isValid(Integer i){
        return i != null && i > -1;
    }

    public static boolean isValid(BigDecimal bd){
        return bd != null && bd.doubleValue() > 0;
    }

    public static Integer toInt(String s){
        try {
            return Integer.parseInt(s);
        }catch (NumberFormatException e){
            return null;
        }
    }
}
