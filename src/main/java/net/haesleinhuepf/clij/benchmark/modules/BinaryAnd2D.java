package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

public class BinaryAnd2D extends DefaultBenchmarkableModule {

    public void test(ImagePlus imp2D, ImagePlus imp3D)
    {
        ImageCalculator ic = new ImageCalculator();
        ic.run("AND create", imp2D, imp2D);
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().binaryAnd(clb2D[0], clb2D[1], clb2D[2]);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean isBinary() {
        return true;
    }
}
