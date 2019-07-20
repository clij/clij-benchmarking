package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Filters3D;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

public class Mean3D extends DefaultBenchmarkableModule {

    private Integer radius;

    public Mean3D(int radius) {
        this.radius = radius;
    }

    public void test(ImagePlus imp2D, ImagePlus imp3D)
    {
        IJ.run(imp3D, "Mean 3D...", "x=" + radius + " y=" + radius + " z=" + radius);
        Filters3D.filter(imp3D.getStack(), Filters3D.MEAN, radius, radius, radius);
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().meanBox(clb3D[0], clb3D[1], radius, radius, radius);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName() + "_" + radius;
    }
}
