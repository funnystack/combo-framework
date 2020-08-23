package com.funny.combo.core.annotion;

import java.lang.annotation.*;

/**
 * 领域能力，用来描述领域实体的行为
 *
 * @author funnystack
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DomainAbility {

}
