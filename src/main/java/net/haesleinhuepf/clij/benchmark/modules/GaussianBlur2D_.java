package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.GaussianBlur;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

@Deprecated
public class GaussianBlur2D_ extends DefaultBenchmarkableModule {

    private Double sigma;

    public GaussianBlur2D_(double sigma) {
        this.sigma = sigma;
    }

    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        IJ.run(imp2D, "Gaussian Blur...", "sigma=" + sigma);
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().blur(clb2D[0], clb2D[1], sigma.floatValue(), sigma.floatValue());
    }

    @Override
    public String getName() {
        return getClass().getSimpleName() + "_" + sigma;
    }
}
