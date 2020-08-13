package de.kaiserpfalzedv.vaadin;

import javax.enterprise.inject.Any;
import javax.enterprise.util.AnnotationLiteral;
import java.lang.annotation.*;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-08-08
 */

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface BeanNavigatorErrorView {
}
