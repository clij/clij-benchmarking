package net.haesleinhuepf.clij.benchmark;

import net.haesleinhuepf.clij.CLIJ;

/**
 * Main
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 05 2019
 */
public class Main {
    public static void main(String... args) throws InterruptedException {
        System.out.println(CLIJ.getInstance().getGPUName());
        BenchmarkAllOperations.main(args);
        BenchmarkImageSize.main(args);
        BenchmarkKernelSize.main(args);
        System.out.println("Bye bye.");
    }

}
