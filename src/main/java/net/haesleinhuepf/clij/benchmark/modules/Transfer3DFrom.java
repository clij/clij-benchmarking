package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCL;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

public class Transfer3DFrom extends DefaultBenchmarkableModule {
    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        ClearCLBuffer result = clij.convert(imp3D, ClearCLBuffer.class);
        result.close();
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        ImagePlus result = clij.convert(clb3D[0], ImagePlus.class);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
