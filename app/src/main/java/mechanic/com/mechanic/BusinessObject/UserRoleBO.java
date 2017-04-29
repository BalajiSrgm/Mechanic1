package mechanic.com.mechanic.BusinessObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sailesh GB on 4/17/2017.
 */

public class UserRoleBO implements Serializable{
    private String idRole;
    private String roleDescription;
    private StatusBO statusBO;
    private String UpdatedTime;

    public String getIdRole() {
        return idRole;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public StatusBO getStatusBO() {
        return statusBO;
    }

    public void setStatusBO(StatusBO statusBO) {
        this.statusBO = statusBO;
    }

    public String getUpdatedTime() {
        return UpdatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        UpdatedTime = updatedTime;
    }
}
