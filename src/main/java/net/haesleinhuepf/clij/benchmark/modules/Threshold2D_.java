package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Thresholder;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

@Deprecated
public class Threshold2D_ extends DefaultBenchmarkableModule {

    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        IJ.setThreshold(imp2D, 128, 255);
        IJ.run(imp2D, "Convert to Mask", "method=Default background=Dark black");
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().threshold(clb2D[0], clb2D[1], new Float(128));
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
