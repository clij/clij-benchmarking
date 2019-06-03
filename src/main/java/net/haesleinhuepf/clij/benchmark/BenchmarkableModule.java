package net.haesleinhuepf.clij.benchmark;

import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

public interface BenchmarkableModule {
    void test(ImagePlus imp2D, ImagePlus imp3D);

    void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3);

    void setCLIJ(CLIJ clij);

    String getName();

    default long measureImageJTime(ImagePlus imp2D, ImagePlus imp3D) {
        long timestamp = System.nanoTime();
        test(imp2D, imp3D);
        return System.nanoTime() - timestamp;
    }

    default long measureCLIJTime(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        long timestamp = System.nanoTime();
        test(clb2D, clb3D);
        return System.nanoTime() - timestamp;
    }

    default boolean isBinary() {
        return false;
    }



}
