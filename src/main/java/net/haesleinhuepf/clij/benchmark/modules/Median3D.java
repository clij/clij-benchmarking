package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.utilities.CLIJUtilities;

public class Median3D extends DefaultBenchmarkableModule {

    private Integer radius;

    public Median3D(int radius) {
        this.radius = radius;
    }

    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        IJ.run(imp3D, "Median 3D...", "x=" + radius + " y=" + radius + " z=" + radius);
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        int kernelSize = CLIJUtilities.radiusToKernelSize(radius);
        clij.op().medianSphere(clb3D[0], clb3D[1], kernelSize, kernelSize, kernelSize);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName() + "_" + radius;
    }
}
