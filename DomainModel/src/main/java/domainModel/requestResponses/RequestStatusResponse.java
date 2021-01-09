package domainModel.requestResponses;

public class RequestStatusResponse implements RequestResponse {
    private long requestId;
    private String message;

    public RequestStatusResponse() {
    }

    public RequestStatusResponse(long requestId, String message) {
        this.requestId = requestId;
        this.message = message;
    }

    public long getRequestId() {
        return requestId;
    }

    public String getMessage() {
        return message;
    }
}
