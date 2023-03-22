package com.funny.combo.trace.dubbo;

import com.funny.combo.trace.util.LogTraceUtil;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 * Created by funnystack on 17/3/15.
 */
@Activate(group = {"consumer"})
public class DubboConsumerTraceFilter implements Filter {

    public DubboConsumerTraceFilter() {
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        // 调用接口方法前
        String traceId = MDC.getMDCAdapter() == null || MDC.get(LogTraceUtil.LOG_TRACE_ID) == null ?
                LogTraceUtil.getNewTraceId() : MDC.get(LogTraceUtil.LOG_TRACE_ID).toString();
        /**
         * 隐式传参，后面的远程调用都会隐式将这些参数发送到服务器端，类似cookie
         * 【注】 setAttachment设置的KV，在完成下面一次远程调用会被清空。即多次远程调用要多次设置。
         */
        RpcContext.getContext().setAttachment(LogTraceUtil.LOG_TRACE_ID, traceId);

        // 调用接口
        Result result = invoker.invoke(invocation);

        // 调用接口方法后

        return result;//返回接口执行结果
    }

}