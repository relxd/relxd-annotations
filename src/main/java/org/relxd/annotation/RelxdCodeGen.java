package org.relxd.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RelxdCodeGen {

    public static String SOURCE = "$SOURCE";

    String targetPackage() default RelxdCodeGen.SOURCE;
    String targetClassNamePrefix() default RelxdCodeGen.SOURCE;
    String targetClassNameSuffix() default "Buddy";

    String codeGeneratorPrefix() default "/handlebars/";
    String codeGeneratorSuffix() default ".hbs";
    String codeGeneratorTemplate() default "ExampleBuddyClass";
}
