package mechanic.com.mechanic.BusinessHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sailesh GB on 4/17/2017.
 */

public class DateUtil {
    public static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static String getDateAndTime(Date date){
        String dateString=sdf.format(date);
        return dateString;
    }
}
