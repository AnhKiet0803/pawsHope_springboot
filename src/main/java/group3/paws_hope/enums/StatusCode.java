package group3.paws_hope.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum StatusCode {
    SUCCESS("0000","Thành công",HttpStatus.OK),
    BAD_REQUEST("1000","Yêu cầu không hợp lệ",HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("1001","Không có quyền truy cập",HttpStatus.UNAUTHORIZED),
    FORBIDDEN("1003","Truy cập bị từ chối",HttpStatus.FORBIDDEN),
    NOT_FOUND("1004","Không tìm thấy dữ liệu",HttpStatus.NOT_FOUND),
    VALIDATION_ERROR("1005","Dữ liệu không hợp lệ",HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("2000","Lỗi hệ thống",HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("2001","Dịch vụ tạm thời không khả dụng",HttpStatus.SERVICE_UNAVAILABLE);

    private final String code;
    private final String messenge;
    private final HttpStatus httpStatus;

}

