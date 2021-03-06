package org.relxd.annotation.codegen;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;
import org.relxd.annotation.codegen.SimpleCodeGenerator;

import javax.tools.JavaFileObject;

import java.util.Optional;

import static com.google.testing.compile.Compiler.javac;

class SimpleCodeGeneratorTest {

    @Test
    void testSuccessfulCodeGenerator() {
        SimpleCodeGenerator pr = new SimpleCodeGenerator();

        JavaFileObject src = JavaFileObjects.forResource("src/RemotesFileTest.java");
        JavaFileObject ref = JavaFileObjects.forResource("src/RemotesFileExpectedTest.java");

        Compilation compilation = javac().withProcessors(pr).compile(src);

        CompilationSubject.assertThat(compilation).succeeded();

        Optional<JavaFileObject> gen = compilation.generatedSourceFile("org.relxd.annotation.processing.RemotesFileTest");

        CompilationSubject.assertThat(compilation)
                .generatedSourceFile("RemotesFileTest")
                .hasSourceEquivalentTo(ref);
    }
}
