package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

@Deprecated
public class GaussianBlur3D_ extends DefaultBenchmarkableModule {

    private Double sigma;

    public GaussianBlur3D_(double sigma) {
        this.sigma = sigma;
    }

    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        IJ.run(imp3D, "Gaussian Blur 3D...", "x=" + sigma + " y=" + sigma + " z=" + sigma);
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().blur(clb3D[0], clb3D[1], sigma.floatValue(), sigma.floatValue(), sigma.floatValue());
    }

    @Override
    public String getName() {
        return getClass().getSimpleName() + "_" + sigma;
    }
}
