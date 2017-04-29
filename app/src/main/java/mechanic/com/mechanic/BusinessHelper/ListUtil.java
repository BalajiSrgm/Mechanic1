package mechanic.com.mechanic.BusinessHelper;

import java.util.Collection;

/**
 * Created by Sailesh GB on 4/12/2017.
 */

public class ListUtil {

    public static boolean isNotNullOrEmpty(Collection<?> collection ){
        if(collection == null || collection.isEmpty() || collection.size() == 0){
            return false;
        }else{
            return true;
        }
    }
}
