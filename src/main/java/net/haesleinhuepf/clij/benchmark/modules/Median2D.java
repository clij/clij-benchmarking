package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.utilities.CLIJUtilities;

public class Median2D extends DefaultBenchmarkableModule {

    private Integer radius;

    public Median2D(int radius) {
        this.radius = radius;
    }

    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        IJ.run(imp2D, "Median...", "radius=" + radius);
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        int kernelSize = CLIJUtilities.radiusToKernelSize(radius);
        clij.op().medianSphere(clb2D[0], clb3D[1], kernelSize, kernelSize, 1);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName() + "_" + radius;
    }
}
