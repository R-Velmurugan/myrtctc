package com.myrctc.common.currentuser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
    //required can be set to false for public endpoints. But a user might be logged in, and we could show personalized content.
    boolean required() default true;
}
