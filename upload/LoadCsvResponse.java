package uk.gov.gchq.gaffer.utils.upload;

public class LoadCsvResponse {

    private String message;

    public LoadCsvResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
