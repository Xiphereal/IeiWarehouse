package ieiWarehousePopulator.restService;

public class RequestStatusResponse {
    private final long requestId;
    private final String message;

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
