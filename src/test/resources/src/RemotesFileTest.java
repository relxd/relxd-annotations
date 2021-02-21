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

    @RelxdCodeGenMethod
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
