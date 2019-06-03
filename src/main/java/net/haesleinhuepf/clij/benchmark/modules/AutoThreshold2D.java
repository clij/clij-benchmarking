package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

public class AutoThreshold2D extends DefaultBenchmarkableModule {

    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        IJ.setAutoThreshold(imp2D, "Default dark");
        IJ.run(imp2D, "Convert to Mask", "method=Default background=Dark black");
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().automaticThreshold(clb2D[0], clb2D[1], "Default");
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
