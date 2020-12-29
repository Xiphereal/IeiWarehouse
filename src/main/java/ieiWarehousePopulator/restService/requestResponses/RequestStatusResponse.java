package ieiWarehousePopulator.restService.requestResponses;

public class RequestStatusResponse implements RequestResponse {
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