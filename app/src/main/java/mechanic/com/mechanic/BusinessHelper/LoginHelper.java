package mechanic.com.mechanic.BusinessHelper;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import mechanic.com.mechanic.BusinessObject.LoginBO;
import mechanic.com.mechanic.BusinessObject.StatusBO;
import mechanic.com.mechanic.BusinessObject.UserRoleBO;

/**
 * Created by Sailesh GB on 4/17/2017.
 */

public class LoginHelper {

    public static LoginBO databaseToBO(DataSnapshot dataSnapshot1){
        LoginBO loginBO = new LoginBO();
            String idLogin = dataSnapshot1.child("idLogin").getValue(String.class);
            if(StringUtil.isNotNullOrEmpty(idLogin)) {
                loginBO.setIdLogin(dataSnapshot1.child("idLogin").getValue(String.class));
                loginBO.setUserName(dataSnapshot1.child("userName").getValue(String.class));
                loginBO.setPassword(dataSnapshot1.child("password").getValue(String.class));
                loginBO.setFirstName(dataSnapshot1.child("firstName").getValue(String.class));
                loginBO.setLastName(dataSnapshot1.child("lastName").getValue(String.class));
                loginBO.setUserRoleBO(new UserRoleBO());
                loginBO.getUserRoleBO().setRoleDescription(dataSnapshot1.child("userRoleBO").child("roleDescription").getValue(String.class));
                loginBO.getUserRoleBO().setIdRole(dataSnapshot1.child("userRoleBO").child("idRole").getValue(String.class));
//                loginBO.getUserRoleBO().setUpdatedTime(dataSnapshot1.child("userRoleBO").child("updatedTime").getValue(String.class));
                loginBO.getUserRoleBO().setStatusBO(new StatusBO());
                StatusBO statusBO = new StatusBO();
                statusBO.setIdStatus(dataSnapshot1.child("statusBO").child("idStatus").getValue(String.class));
                //       statusBO.setUpdatedTime(dataSnapshot1.child("statusBO").child("updatedTime").getValue(String.class));
                statusBO.setDescription(dataSnapshot1.child("statusBO").child("description").getValue(String.class));
                loginBO.getUserRoleBO().setStatusBO(statusBO);
            }else{
                loginBO = null;
            }

        return loginBO;
    }

    public static LoginBO getLoginBOFromLoginBOs(List<LoginBO> loginBOs, LoginBO loginBO){
        if(ListUtil.isNotNullOrEmpty(loginBOs) && loginBO != null){
            for(LoginBO loginBO1 : loginBOs){
                if(StringUtil.isEqual(loginBO.getUserName(),loginBO1.getUserName())){
                    loginBO = loginBO1;
                    break;
                }
            }
        }
        return loginBO;
    }
}
