package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ShortProcessor;
import mpicbg.ij.integral.Mean;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

@Deprecated
public class Mean2D_ extends DefaultBenchmarkableModule {

    private Integer radius;

    public Mean2D_(int radius) {
        this.radius = radius;
    }

    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        IJ.run(imp2D, "Mean...", "radius=" + radius);
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().meanBox(clb2D[0], clb2D[1], radius, radius, 0);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName() + "_" + radius;
    }
}
