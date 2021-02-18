package org.relxd.annotation.processing;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import org.relxd.annotation.RelxdCodeGen;
import org.relxd.annotation.RelxdCodeGenMethod;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SimpleCodeGenerator extends AbstractProcessor {

    private final static Map<String, String> BaseTypes = new HashMap<>();

    static {
        BaseTypes.put("java.lang.String", "String");
        BaseTypes.put("java.lang.Integer", "Integer");
        BaseTypes.put("java.lang.Object", "Object");
        BaseTypes.put("java.lang.Boolean", "Boolean");
        BaseTypes.put("java.lang.Long", "Long");
    }

    @Override
    public synchronized void init(ProcessingEnvironment env){
        super.init(env);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedAnnotationTypes = new TreeSet<>();
        supportedAnnotationTypes.add("org.relxd.annotation.RelxdCodeGen");
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.size() == 0)
            return false;

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RelxdCodeGen.class);
        Set<? extends Element> methods = roundEnv.getElementsAnnotatedWith(RelxdCodeGenMethod.class);

        boolean ok = true;

        for (Element element : elements) {
            RelxdCodeGen relxdCodeGen = element.getAnnotation(RelxdCodeGen.class);
            boolean r = generate(element, methods, relxdCodeGen);

            if(ok && !r) {
                ok = r;
            }
        }

        return ok;
    }

    private boolean generate(Element element, Set<? extends Element> methods, RelxdCodeGen relxdCodeGen) {
        Messager messager = processingEnv.getMessager();
        Writer writer = null;
        PrintWriter printWriter = null;
        boolean ok = false;

        String targetPackage = (relxdCodeGen.targetPackage().equals(RelxdCodeGen.SOURCE)) ? element.getEnclosingElement().toString() : relxdCodeGen.targetPackage();
        String targetClassNamePrefix = (relxdCodeGen.targetClassNamePrefix().equals(RelxdCodeGen.SOURCE)) ? element.getSimpleName().toString() : relxdCodeGen.targetClassNamePrefix();

        try {
            String fullName = targetPackage + "." + targetClassNamePrefix + relxdCodeGen.targetClassNameSuffix();
            messager.printMessage(Diagnostic.Kind.OTHER, "Preparing to generate " + fullName);

            TypeElement te = (TypeElement)element;
            List<? extends TypeParameterElement> genericTypes = te.getTypeParameters();

            HashMap<String, Object> data = prepareData(genericTypes, targetPackage, targetClassNamePrefix, relxdCodeGen.targetClassNameSuffix(), methods);

            Template template = loadTemplate(relxdCodeGen.codeGeneratorTemplate(), relxdCodeGen.codeGeneratorPrefix(), relxdCodeGen.codeGeneratorSuffix());
            String newClass = template.apply(data);

            if(newClass.isEmpty())
                messager.printMessage(Diagnostic.Kind.ERROR, "The generated class source is empty for " + fullName);
            else {
                writer = processingEnv.getFiler().createSourceFile(fullName).openWriter();
                template.apply(data,writer);
                writer.flush();
                ok = true;
            }

        } catch (IOException e) {
            try {
                if(writer!= null)
                    writer.close();
            } catch (IOException ioException) {
                messager.printMessage(Diagnostic.Kind.ERROR, ioException.getMessage());
            }

            if(printWriter!= null)
                printWriter.close();
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }

        return ok;
    }

    private Template loadTemplate(String codeGeneratorTemplate, String codeGeneratorPrefix, String codeGeneratorSuffix) throws IOException {
        TemplateLoader loader = new ClassPathTemplateLoader(codeGeneratorPrefix, codeGeneratorSuffix);
        Handlebars hb = new Handlebars().with(loader);
        return hb.compile(codeGeneratorTemplate);
    }

    private HashMap<String, Object> prepareData(List<? extends TypeParameterElement> genericTypes, String packageName, String classNamePrefix, String classNameSuffix, Set<? extends Element> methods){
        HashMap<String, Object> out = new HashMap<>();

        out.put("packageName", packageName);
        out.put("classNamePrefix", classNamePrefix);
        out.put("classNameSuffix", classNameSuffix);
        out.put("fullName", classNamePrefix+classNameSuffix);

        out.put("hasGenerics", genericTypes.size() > 0);
        AtomicInteger gCount = new AtomicInteger(genericTypes.size());
        List<HashMap<String, Object>> xx = genericTypes.stream().map(x -> {
            gCount.decrementAndGet();
            HashMap<String, Object> h = new HashMap<>();
            h.put("name",x.getSimpleName().toString());
            h.put("hasMore",gCount.get() > 0);
            return h;
        }).collect(Collectors.toList());
        out.put("generics", xx);

        AtomicInteger count = new AtomicInteger(methods.size());
        List<HashMap<String, Object>> m = methods.stream().map(x -> {
            count.decrementAndGet();
            return prepareMethod(x, count.decrementAndGet() > 0);
        }).collect(Collectors.toList());
        out.put("methods", m);

        return out;
    }

    private HashMap<String, Object> prepareMethod(Element element, boolean hasMore){
        HashMap<String, Object> methodInfo = new HashMap<>();

        ExecutableElement method = (ExecutableElement) element;
        methodInfo.put("name", method.getSimpleName());
        methodInfo.put("hasMore", hasMore);
        String resultType = BaseTypes.getOrDefault(method.getReturnType().toString(),method.getReturnType().toString());
        methodInfo.put("resultType", resultType);
        methodInfo.put("hasResultType", !resultType.equals("void"));

        AtomicInteger count = new AtomicInteger(method.getParameters().size());
        List<Map<String, Object>> params =  method.getParameters().stream().map(x -> {
            count.decrementAndGet();
            return prepareMethodVars(x,count.get() > 0);
        }).collect(Collectors.toList());

        methodInfo.put("params", params);

        return methodInfo;
    }

    private HashMap<String, Object> prepareMethodVars(VariableElement element, boolean hasMore){

        HashMap<String, Object> methodInfo = new HashMap<>();
        methodInfo.put("name", element.getSimpleName());
        methodInfo.put("type", BaseTypes.getOrDefault(element.asType().toString(),element.asType().toString()));
        methodInfo.put("hasMore", hasMore);

        return methodInfo;
    }
}
