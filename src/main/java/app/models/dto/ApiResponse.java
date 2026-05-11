package app.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Standard API response wrapper")
public class ApiResponse<T> {

    @Schema(description = "Indicates if the request was successful", example = "true")
    private boolean success;

    @Schema(description = "A user-friendly message about the request outcome", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Payload of the response")
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(final boolean success, final String message, final T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(final String message, final T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> success(final String message) {
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> error(final String message) {
        return new ApiResponse<>(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }
}
