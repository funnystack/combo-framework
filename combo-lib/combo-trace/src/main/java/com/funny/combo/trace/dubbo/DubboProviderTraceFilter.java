package com.funny.combo.trace.dubbo;

import com.funny.combo.trace.util.LogTraceUtil;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 * Created by funnystack on 17/3/15.
 */
@Activate(group = {"provider"})
public class DubboProviderTraceFilter implements Filter {
    public DubboProviderTraceFilter() {
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        // 调用接口方法前
        try {
            /**
             * 获取客户端隐式传入的参数
             */
            if (MDC.getMDCAdapter() != null) {
                String traceId = RpcContext.getContext().getAttachment(LogTraceUtil.LOG_TRACE_ID);
                if (traceId == null || traceId.length() == 0) {
                    traceId = LogTraceUtil.getNewTraceId();
                }
                MDC.put(LogTraceUtil.LOG_TRACE_ID, traceId);
            }
        } catch (Exception e) {
        }

        // 调用接口
        Result result = invoker.invoke(invocation);

        // 调用接口方法后
        try {
            if (MDC.getMDCAdapter() != null) {
                MDC.remove(LogTraceUtil.LOG_TRACE_ID);
            }
        } catch (Exception e) {
        }

        return result;//返回接口执行结果
    }
}

