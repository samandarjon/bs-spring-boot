package live.akbarov.bsspringboot.exception;

import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends HttpStatusCodeException {

    protected NotFoundException(HttpStatusCode code, String message) {
        super(code, message);
    }

    public static NotFoundException create(String message) {
        return new NotFoundException(HttpStatus.NOT_FOUND, message);
    }


}
