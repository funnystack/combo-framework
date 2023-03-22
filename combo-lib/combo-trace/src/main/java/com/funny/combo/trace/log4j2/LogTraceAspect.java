package com.funny.combo.trace.log4j2;

import com.funny.combo.trace.util.LogTraceUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.MDC;

/**
 * 日志跟踪Spring切面
 * Created by funnystack on 17/3/10.
 */
public class LogTraceAspect {

    /**
     * 环绕通知
     * @param proceedingJoinPoint
     * @throws Throwable
     */
    public Object invoke(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        doBefore();
        Object obj = proceedingJoinPoint.proceed();
        doAfter();
        return obj;
    }

    /***
     * 切入前通知（Before）
     */
    private void doBefore() {
        try {
            if (MDC.getMDCAdapter() != null) {
                MDC.put(LogTraceUtil.LOG_TRACE_ID, LogTraceUtil.getNewTraceId());
            }
        }catch (Exception e){
        }
    }


    /**
     * 切入后返回通知（AfterReturning）
     */
    private void doAfter() {
        try {
            if (MDC.getMDCAdapter() != null) {
                MDC.remove(LogTraceUtil.LOG_TRACE_ID);
            }
        }catch (Exception e) {
        }
    }

}


/**
 * spring.xml配置示例
 *
 <bean class="com.funny.combo.trace.log4j2.LogTraceAspect" id="logTraceAspect"/>
 <aop:config>
 <aop:pointcut id="pointcut" expression="execution(* com.funny.combo.coupon.service.task.CommonMessageProcess.*(..))" />
 <aop:aspect ref="logTraceAspect">
 <aop:around pointcut-ref="pointcut" method="invoke"/>
 </aop:aspect>
 </aop:config>
 */
