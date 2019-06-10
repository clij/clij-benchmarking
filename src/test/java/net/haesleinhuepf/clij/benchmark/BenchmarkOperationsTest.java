package net.haesleinhuepf.clij.benchmark;

import net.haesleinhuepf.clij.CLIJ;
import org.junit.Test;

public class BenchmarkOperationsTest {
    @Test
    public void executeOperationsBenchmarks() throws InterruptedException {
        System.out.println(CLIJ.getInstance().getGPUName());
        BenchmarkAllOperations.main(new String[]{});
        BenchmarkImageSize.main(new String[]{});
        BenchmarkKernelSize.main(new String[]{});
        System.out.println("Bye bye.");
    }
}
