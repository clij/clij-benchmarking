package net.haesleinhuepf.clij.benchmark.modules;

import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

public class BinaryAnd3D extends DefaultBenchmarkableModule {

    public void test(ImagePlus imp2D, ImagePlus imp3D)
    {
        ImageCalculator ic = new ImageCalculator();
        ic.run("AND create stack", imp3D, imp3D);
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().binaryAnd(clb3D[0], clb3D[1], clb3D[2]);
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
