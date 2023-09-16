package dongduk.cs.pulpul.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dongduk.cs.pulpul.interceptor.LoginInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		LoginInterceptor loginInterceptor = new LoginInterceptor();
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns(loginInterceptor.loginEssential)
                .excludePathPatterns(loginInterceptor.loginInessential);
    }
}
