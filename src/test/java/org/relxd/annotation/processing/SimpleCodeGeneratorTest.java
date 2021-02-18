package org.relxd.annotation.processing;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.Compiler.javac;

class SimpleCodeGeneratorTest {

    @Test
    void testSuccessfulCodeGenerator() {
        SimpleCodeGenerator pr = new SimpleCodeGenerator();

        JavaFileObject src = JavaFileObjects.forResource("src/RemotesFileTest.java");
        JavaFileObject ref = JavaFileObjects.forResource("src/RemotesFileExpectedTest.java");

        Compilation compilation = javac().withProcessors(pr).compile(src);

        CompilationSubject.assertThat(compilation).succeeded();
        CompilationSubject.assertThat(compilation)
                .generatedSourceFile("org.relxd.annotation.processing.RemotesFileTestRemote")
                .hasSourceEquivalentTo(ref);
    }
}
