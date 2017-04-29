package mechanic.com.mechanic.BusinessObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sailesh GB on 4/17/2017.
 */

public class StatusBO implements Serializable{

    private String idStatus;
    private String description;
    private String updatedTime;

    public String getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(String idStatus) {
        this.idStatus = idStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }
}
