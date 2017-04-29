package mechanic.com.mechanic.BusinessValidation;

import java.util.ArrayList;
import java.util.List;

import mechanic.com.mechanic.BusinessHelper.ListUtil;
import mechanic.com.mechanic.BusinessHelper.StringUtil;
import mechanic.com.mechanic.BusinessObject.ErrorListBO;
import mechanic.com.mechanic.BusinessObject.LoginBO;

/**
 * Created by Sailesh GB on 4/17/2017.
 */

public class Validation {
    public static List<ErrorListBO> validateUserName(List<LoginBO> loginBOs, LoginBO loginBO){
        List<ErrorListBO> errorListBOs = new ArrayList<>();
        ErrorListBO errorListBO = new ErrorListBO();
        if(ListUtil.isNotNullOrEmpty(loginBOs) && loginBO != null && StringUtil.isNotNullOrEmpty(loginBO.getUserName())){
            for(LoginBO loginBO1 : loginBOs){
                if(StringUtil.isEqual(loginBO1.getUserName(),loginBO.getUserName())){
                    errorListBO.setErrorMessage("Username is already registered");
                    errorListBOs.add(errorListBO);
                    break;
                }
            }
        }
        // = new ArrayList<>();
        return errorListBOs;
    }

}
