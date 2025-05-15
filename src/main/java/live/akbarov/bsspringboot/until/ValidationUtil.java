package live.akbarov.bsspringboot.until;

import jakarta.validation.ValidationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {

    public void onCondition(boolean isExist, String message) {
        if (isExist) {
            throw new ValidationException(message);
        }
    }
}
