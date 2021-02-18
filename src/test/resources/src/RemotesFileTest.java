package org.relxd.annotation.processing;

import org.relxd.annotation.RelxdCodeGen;
import org.relxd.annotation.RelxdCodeGenMethod;

import java.math.BigDecimal;
import java.util.Set;

@RelxdCodeGen
public class RemotesFileTest<K,V> {

    @RelxdCodeGenMethod
    public Integer doTest(String var1, Integer var2, int var3, BigDecimal var4){

    }

    @RelxdCodeGenMethod
    public int doTest2(String var1, Integer var2, int var3, BigDecimal var4){

    }

    @RelxdCodeGenMethod
    public void doTest3(String var1, Integer var2, int var3, BigDecimal var4){

    }

    @RelxdCodeGenMethod
    public V doTest4(K var1){

    }

    @RelxdCodeGenMethod
    public void handleRemoveMany(Set<K> keys) {

    }
}
