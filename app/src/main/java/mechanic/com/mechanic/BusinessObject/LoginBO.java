package mechanic.com.mechanic.BusinessObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sailesh GB on 4/17/2017.
 */

public class LoginBO  implements Serializable{
    private String idLogin;
    private String userName;
    private String password;
    private String firstName;
    private String LastName;
    private String UpdatedTime;
    private String userType;
    private String securityQuestion;
    private String securityAnswer;
    private UserRoleBO userRoleBO;



    public String getIdLogin() {
        return idLogin;
    }

    public void setIdLogin(String idLogin) {
        this.idLogin = idLogin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public UserRoleBO getUserRoleBO() {
        return userRoleBO;
    }

    public void setUserRoleBO(UserRoleBO userRoleBO) {
        this.userRoleBO = userRoleBO;
    }

    public String getUpdatedTime() {
        return UpdatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        UpdatedTime = updatedTime;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }
}
