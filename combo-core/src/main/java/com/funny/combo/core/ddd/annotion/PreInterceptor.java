package com.funny.combo.core.ddd.annotion;

import com.funny.combo.core.dto.AbstractCommand;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Inherited
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PreInterceptor {

    Class<? extends AbstractCommand>[] commands() default {};

}
