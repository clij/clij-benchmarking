package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Filters3D;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

@Deprecated
public class Mean3D_ extends DefaultBenchmarkableModule {

    private Integer radius;

    public Mean3D_(int radius) {
        this.radius = radius;
    }

    public void test(ImagePlus imp2D, ImagePlus imp3D)
    {
        IJ.run(imp3D, "Mean 3D...", "x=" + radius + " y=" + radius + " z=" + radius);
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().meanBox(clb3D[0], clb3D[1], radius, radius, radius);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName() + "_" + radius;
    }
}
