package mechanic.com.mechanic.BusinessHelper;

import android.os.Bundle;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sailesh GB on 4/18/2017.
 */

public class MasterHelper {

    public static void clearEditTextVariables(EditText editText){
        editText.setText("");
    }

    public static Bundle getDefaultBundleValues(String id,String userName,String password,String userFullName){
        Bundle b = new Bundle();
        b.putString(ElementConstants.Id, id);
        b.putString(ElementConstants.Name, userName);
        b.putString(ElementConstants.password, password);
        b.putString(ElementConstants.userFirstLastName, userFullName);
        return b;

    }

    public static List<String> getSampleEmailDomain(String s){
        ArrayList<String> strings = new ArrayList<>();
        if(StringUtil.isNotNullOrEmpty(s) && s.contains("@")){
            strings.add(s+"gmail.com");
            strings.add(s+"yahoo.com");
            strings.add(s+"ymail.com");
            strings.add(s+"hotmail.com");
            strings.add(s+"yahoo.co.in");
            strings.add(s+"rediffmail.com");
            strings.add(s+"live.in");
            strings.add(s+"ibibo.com");
            strings.add(s+"in.com");
            strings.add(s+"aol.com");

        }
        return strings;
    }
}
