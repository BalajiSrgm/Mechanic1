package mechanic.com.mechanic.BusinessHelper;

/**
 * Created by Sailesh GB on 4/12/2017.
 */

public class StringUtil {

    public  static boolean isEqual(String s,String s2){
        if(s != null && s2 != null){
            if(s.equals(s2)){
                return true;
            }else{
                return false;
            }
        }
        return  false;
    }

    public static boolean isNotNullOrEmpty(String s){
        if(s != null && !(s.isEmpty())){
            return true;
        }else{
            return false;
        }
    }
}
