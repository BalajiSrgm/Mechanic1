package mechanic.com.mechanic.BusinessObject;

/**
 * Created by Sailesh GB on 4/12/2017.
 */

public class ErrorListBO {
    private String errorMessage;
    private String errorEventId;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorEventId() {
        return errorEventId;
    }

    public void setErrorEventId(String errorEventId) {
        this.errorEventId = errorEventId;
    }
}
