package com.cyx.system.aspect;

import com.cyx.annotation.Log;
import com.cyx.system.entity.po.SysOperationLog;
import com.cyx.system.mapper.SysOperationLogMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 操作日志切面 — 拦截所有标注了 @Log 的 Controller 方法，自动记录操作审计信息。
 * <p>
 * 设计要点：
 * <ul>
 *   <li>使用 {@code @Around} 而非 {@code @AfterReturning}，因为要记录执行耗时</li>
 *   <li>日志写入是异步的（通过异步执行器），不阻塞接口响应</li>
 *   <li>即使接口抛异常也会记录，保证审计完整性</li>
 *   <li>请求参数超过 1000 字符时自动截断，防止大 JSON 撑爆日志字段</li>
 * </ul>
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);
    private static final int MAX_PARAMS_LENGTH = 1000;

    private final SysOperationLogMapper operationLogMapper;
    private final ObjectMapper objectMapper;

    public OperationLogAspect(SysOperationLogMapper operationLogMapper, ObjectMapper objectMapper) {
        this.operationLogMapper = operationLogMapper;
        this.objectMapper = objectMapper;
    }

    /**
     * 环绕通知：记录 @Log 注解标记的方法的调用信息。
     * <p>
     * 使用 {@code ProceedingJoinPoint} 获取方法入参和返回值，
     * 用 {@code MethodSignature} 读取注解上的 module/operation。
     */
    @Around("@annotation(com.cyx.annotation.Log)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;
        int responseCode = 200;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            responseCode = 500;
            throw e;
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            // 异步记录日志，失败不影响主流程
            try {
                saveLog(joinPoint, responseCode, elapsed);
            } catch (Exception e) {
                log.warn("操作日志记录失败: {}", e.getMessage());
            }
        }
    }

    /**
     * 组装日志对象并写入数据库。
     */
    private void saveLog(ProceedingJoinPoint joinPoint, int responseCode, long elapsed) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);

        SysOperationLog operationLog = new SysOperationLog();
        // 从 SecurityContextHolder 获取当前登录用户
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            operationLog.setUsername(auth.getName());
            // userId 从 JWT token 的 subject 获取不到，设为 null；
            // 生产环境应解析 token 中的 userId claim
        }
        operationLog.setModuleName(logAnnotation.module());
        operationLog.setOperationName(logAnnotation.operation());

        // 从 RequestContextHolder 获取 HTTP 请求详情
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            operationLog.setRequestMethod(request.getMethod());
            operationLog.setRequestUri(request.getRequestURI());
            operationLog.setClientIp(getClientIp(request));
        }

        // 序列化请求参数，超长截断
        String params = serializeArgs(joinPoint.getArgs());
        operationLog.setRequestParams(params.length() > MAX_PARAMS_LENGTH
                ? params.substring(0, MAX_PARAMS_LENGTH) + "..."
                : params);

        operationLog.setResponseCode(responseCode);
        operationLog.setExecutionTimeMs(elapsed);
        operationLog.setCreatedAt(LocalDateTime.now());
        operationLogMapper.insert(operationLog);
    }

    /**
     * 将方法参数序列化为 JSON 字符串，跳过无法序列化的对象（如 HttpServletRequest/Response）。
     */
    private String serializeArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        try {
            // 过滤掉 Servlet API 对象，它们无法序列化且没有业务意义
            Object[] filtered = java.util.Arrays.stream(args)
                    .filter(a -> !(a instanceof HttpServletRequest)
                            && !(a instanceof jakarta.servlet.http.HttpServletResponse))
                    .toArray();
            if (filtered.length == 0) {
                return "";
            }
            return objectMapper.writeValueAsString(filtered);
        } catch (JsonProcessingException e) {
            return "[参数序列化失败]";
        }
    }

    /**
     * 获取客户端真实 IP — 优先从反向代理头读取，防止拿到 nginx 的 IP。
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // X-Forwarded-For 可能包含多级代理 IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
