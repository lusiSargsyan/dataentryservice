package com.study.dataentryservice.interceptors;

import com.study.dataentryservice.helper.LogHelper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class LoggingInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		LogHelper.debug("Incoming request data: at " + new Date() + " for " + request.getRequestURI());
 		return true;
	}
}
