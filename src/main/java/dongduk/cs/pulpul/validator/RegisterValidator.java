package dongduk.cs.pulpul.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import dongduk.cs.pulpul.domain.Member;

@Component
public class RegisterValidator implements Validator {
	public boolean supports(Class<?> clazz) {
		return Member.class.isAssignableFrom(clazz);
	}

	private static final String emailRegExp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Pattern pattern;

	public RegisterValidator() {
		pattern = Pattern.compile(emailRegExp);
	}

	public void validate(Object obj, Errors errors) {
		Member member = (Member) obj;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordCheck", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birth", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "zip", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "addressDetail", "required");

		Matcher matcher = pattern.matcher(member.getEmail());
		if (!matcher.matches()) {
			errors.rejectValue("email", "badPattern");
		}
		if (!member.getPassword().equals(member.getPasswordCheck())) {
			errors.rejectValue("passwordCheck", "noMatch");
		}
		String birth = member.getBirth();
		if (birth.length() != 8 || !isStringInt(birth)) {
			errors.rejectValue("birth", "badPattern");
		}
		String phone = member.getPhone();
		if (!isStringInt(phone)) {
			errors.rejectValue("phone", "badPattern");
		}
	}

	public static boolean isStringInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
