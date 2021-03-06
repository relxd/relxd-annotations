# @RelxdCodeGen

The RelxdCodegen annotation, an intentionally simple annotation used to generate source code using handlebars templates.

In order to include a class for evaluation the class/interface has to be annotated with ***@RelxdCodeGen***.
All methods that are intended for inclusion must be annotated with ***RelxdCodeGenMethod***.

```java
@RelxdCodeGen(codeGeneratorPrefix = "/codegen/", codeGeneratorTemplate = "RPCClientCodeGen", targetClassNameSuffix = "Client")
public interface RPCMethods<K,V> extends Receiver, MethodLookup {

    RPCMethodsClient<K,V> getClient();
```

### @RelxdCodeGen Configuration Options
Parameter | Default Value | Description
------------|------------|------------
*targetPackage*|Same as the annotated object| For example 1 and 2 the template engine will have the same package name as the package 
*targetClassNamePrefix*|Same as the annotated object| For example 1 and 2 the template engine will have the same class name as a prefix
*targetClassNameSuffix*|Buddy|The generated class name for example 1 RemotesFileTestBuddy 
*codeGeneratorPrefix*|/handlebars/|The folder in resources where the templates are located 
*codeGeneratorSuffix*|.hbs|The template extension
*codeGeneratorTemplate*|ExampleBuddyClass|The example template, set this to the name of your own template

### Handlebars Template Data
Parameter |Type| Description
------------|------------|------------
*{{{packageName}}}*|String|
*{{{classNamePrefix}}}*|String|
*{{{classNameSuffix}}}*|String|
*{{{fullName}}}*|String|
*{{{fullName}}}*|String|
*{{hasGenerics}}*|Boolean|
*{{hasGenerics}}*|Boolean|
*{{generics}}*|[Array[Generic]](#generic)|
*{{methods}}*|[Array[Method]](#method)|

#### Generic
Parameter |Type| Description
------------|------------|------------
*{{{name}}}*|String|
*{{hasMore}}*|Boolean|

#### Method
Parameter |Type| Description
------------|------------|------------
*{{{name}}}*|String|
*{{{id}}}*|Integer|
*{{hasMore}}*|Boolean|
*{{{resultType}}}*|String|
*{{hasResultType}}*|Boolean|
*{{generics}}*|[Array[Param]](#param)|

#### Param
Parameter |Type| Description
------------|------------|------------
*{{{name}}}*|String|
*{{{simpleType}}}*|String|
*{{{type}}}*|String|
*{{hasMore}}*|Boolean|
*{{isGenericType}}*|Boolean|

### Example template
```java
package {{packageName}};

public class {{fullName}}{{#hasGenerics}}<{{#generics}}{{name}}{{#hasMore}}, {{/hasMore}}{{/generics}}>{{/hasGenerics}} {
    {{#methods}}
    public {{resultType}} {{name}}({{#params}}{{{type}}} {{name}}{{#hasMore}}, {{/hasMore}}{{/params}}){
        {{#hasResultType}}return null;{{/hasResultType}}
    }
    {{/methods}}
}
```

### Example [pom.xml::plugins] using maven processor plugin
```xml
<plugin>
    <groupId>org.bsc.maven</groupId>
    <artifactId>maven-processor-plugin</artifactId>
    <version>4.5</version>
    <executions>
        <!-- Run annotation processors on src/main/java sources -->
        <execution>
            <id>process</id>
            <goals>
                <goal>process</goal>
            </goals>
            <phase>generate-sources</phase>
            <configuration>
                <!-- We disable failure as the generated classes will not be available yet  -->
                <failOnError>false</failOnError>
                <!-- source output directory -->
                <!--<outputDirectory>src/main/generated</outputDirectory>-->
                <processors>
                    <processor>org.relxd.annotation.codegen.SimpleCodeGenerator</processor>
                </processors>
            </configuration>
        </execution>

        <!-- Run annotation processors on src/test/java sources -->
        <execution>
            <id>process-test</id>
            <goals>
                <goal>process-test</goal>
            </goals>
            <phase>generate-test-sources</phase>
            <configuration>
                <failOnError>false</failOnError>
            </configuration>
        </execution>
    </executions>
</plugin>

<!-- Disable annotation processors during normal compilation -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.1</version>
    <configuration>
        <compilerArgument>-proc:none</compilerArgument>
        <!--<generatedSourcesDirectory>src/main/generated</generatedSourcesDirectory>-->
    </configuration>
</plugin>
```

### Example Usage 1
```java
package org.relxd.annotation;

import org.relxd.annotation.codegen.RelxdCodeGen;
import org.relxd.annotation.codegen.RelxdCodeGenMethod;
import java.math.BigDecimal;
import java.util.Set;

@RelxdCodeGen
public class RemotesFileTest<K,V> {

    @RelxdCodeGenMethod
    public Integer doTest(K var1, Integer var2, int var3, BigDecimal var4){
        return 0;
    }

    // This methos does not have the "@RelxdCodeGenMethod" annotation and will be skipped as a result
    public int doTest2(String var1, Integer var2, int var3, BigDecimal var4){
        return 0;
    }

    @RelxdCodeGenMethod
    public void doTest3(String var1, Integer var2, int var3, BigDecimal var4){

    }

    @RelxdCodeGenMethod
    public V doTest4(K var1){
        return null;
    }

    @RelxdCodeGenMethod
    public void handleRemoveMany(Set<K> keys) {

    }
}
```


### Example Usage 2
```java
package org.relxd.grid.cache;

import org.jgroups.Receiver;
import org.jgroups.blocks.MethodLookup;
import org.relxd.annotation.codegen.RelxdCodeGen;
import org.relxd.annotation.codegen.RelxdCodeGenMethod;

import java.util.Set;

@RelxdCodeGen(codeGeneratorPrefix = "/codegen/", codeGeneratorTemplate = "RPCClientCodeGen", targetClassNameSuffix = "Client")
public interface RPCMethods<K,V> extends Receiver, MethodLookup {

    RPCMethodsClient<K,V> getClient();

    @RelxdCodeGenMethod
    void put(K key, V value);

    @RelxdCodeGenMethod
    void put(K key, V value, boolean force);

    @RelxdCodeGenMethod
    V get(K key);

    @RelxdCodeGenMethod
    void  remove(K key);

    @RelxdCodeGenMethod
    void removeMany(Set<K> keys);
}
```
