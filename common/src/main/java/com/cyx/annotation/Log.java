package com.cyx.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解 — 标记在 Controller 方法上，通过 AOP 自动记录操作日志到 sys_operation_log。
 * <p>
 * 使用示例：
 * <pre>{@code
 * @Log(module = "用户管理", operation = "创建用户")
 * @PostMapping
 * public Result<Long> create(...) { ... }
 * }</pre>
 * <p>
 * 注意：仅在 Spring 管理的 Bean 中被调用时生效（AOP 代理限制），
 * 同一个类内部方法调用不走代理，注解不生效。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /** 操作模块名称，如"用户管理"、"角色管理" */
    String module() default "";

    /** 操作名称，如"创建用户"、"删除角色" */
    String operation() default "";
}
