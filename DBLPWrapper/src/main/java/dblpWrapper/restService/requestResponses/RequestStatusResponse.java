package dblpWrapper.restService.requestResponses;

public class RequestStatusResponse implements RequestResponse {
    private final long requestId;
    private final String message;

    public RequestStatusResponse(long requestId, String message) {
        this.requestId = requestId;
        this.message = message;
    }
}
