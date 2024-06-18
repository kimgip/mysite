package com.poscodx.mysite.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
public class MeasureExecutionTimeAspect {
	private static Log logger = LogFactory.getLog(MeasureExecutionTimeAspect.class);

	@Around("execution(* *..*.repository.*.*(..)) || execution(* *..*.service.*.*(..))")
	public Object adviceAround(ProceedingJoinPoint pjp) throws Throwable{
		// before
		StopWatch sw = new StopWatch();
		sw.start();
		
		Object result = pjp.proceed();
		
		// after
		sw.stop();
		
		long totalTime = sw.getTotalTimeMillis();
		String className = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName();
		String taskName = className + "." + methodName;
		// 로깅
		logger.info("[Execution Time]["+taskName+"]"+totalTime+"millis");
		
		return result;
	}
}
