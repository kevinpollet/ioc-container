package com.my.container.test.injection.services.impl;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A custom qualifier to select
 * the lower echo service.
 *
 * @author kevinpollet
 */
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
@Documented
public @interface LowerEcho {

}
