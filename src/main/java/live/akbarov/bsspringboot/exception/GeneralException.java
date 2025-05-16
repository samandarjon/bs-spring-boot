package live.akbarov.bsspringboot.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GeneralException extends RuntimeException {

    protected GeneralException(String message) {
        super(message);
    }

    public static GeneralException create(String message) {
        return new GeneralException(message);
    }
}
