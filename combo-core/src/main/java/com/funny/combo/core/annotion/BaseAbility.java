package com.funny.combo.core.annotion;

import java.lang.annotation.*;

/**
 * 领域能力，用来描述领域实体的行为
 *
 * @author funnystack
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface BaseAbility {
    /**
     * 当前域的唯一编码
     * @return
     */
    String code();

    /**
     * 域服务的名称用于显示
     * @return
     */
    String name();

    /**
     * 当前域的描述
     * @return
     */
    String desc() default "";

    /**
     * 当前域服务的帮助url
     * @return
     */
    String helpUrl() default "";

    /**
     *  the domain ability codes
     * @return
     */
    String[] abilityCodes() default "";
}
