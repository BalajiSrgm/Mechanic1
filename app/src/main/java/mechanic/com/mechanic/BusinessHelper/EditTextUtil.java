package mechanic.com.mechanic.BusinessHelper;

import android.widget.EditText;

/**
 * Created by Sailesh GB on 4/17/2017.
 */

public class EditTextUtil {
    public static String getString(EditText editText){
        String s = "";
        if(editText.getText().toString().trim() != null){
            s = editText.getText().toString().trim();
        }else{
            s = null;
        }
        return s;
    }
}
