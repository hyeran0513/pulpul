package dongduk.cs.pulpul.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import dongduk.cs.pulpul.domain.Borrow;

@Component
public class BorrowValidator implements Validator {
	public boolean supports(Class<?> clazz) {
		return Borrow.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		Borrow borrow = (Borrow) obj;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", "required");


		if (!isStringPos(borrow.getDate())) {
			errors.rejectValue("date", "badPattern");
		}
	}

	public static boolean isStringPos(String s) {
		try {
			if (Integer.parseInt(s) < 1) {
				return false;
			}
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
