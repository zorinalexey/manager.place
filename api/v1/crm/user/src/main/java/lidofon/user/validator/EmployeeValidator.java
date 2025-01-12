package lidofon.user.validator;

import lidofon.user.dto.EmployeeDto;
import lidofon.user.exception.DataValidationException;
import org.springframework.stereotype.Component;

@Component
public class EmployeeValidator {
    public void editEmployeeValidation(String id) {
     if(id == null) {
         throw new DataValidationException("Employee id is required");
     }
    }
}
