package dongduk.cs.pulpul.interceptor;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
	
	public List<String> loginEssential
    	= Arrays.asList("/lookup/alertList", "/market/**",  "/member/**", "/cart/**", "/order/**", "/borrow/**", "/review");

	public List<String> loginInessential
    	= Arrays.asList("/member/register", "/member/login");
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
			throws Exception {
		
		// System.out.println("pre Handle method");
		HttpSession session = request.getSession();
		String memberId = (String) session.getAttribute("id");
		
		if (memberId != null) return true;
		else {            
			response.sendRedirect("/member/login");
			return false;
		}
	}

}
