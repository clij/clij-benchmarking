package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.ZProjector;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

public class MaximumZProjection extends DefaultBenchmarkableModule {

    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        IJ.run(imp3D, "Z Project...", "projection=[Max Intensity]");
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().maximumZProjection(clb3D[0], clb2D[3]);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
