package com.liuzhenli.common.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by sunhapper on 2018/8/16 .
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface ActivityScope {
}
