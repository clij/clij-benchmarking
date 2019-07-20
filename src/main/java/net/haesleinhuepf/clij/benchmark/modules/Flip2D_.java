package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.Transformer;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

@Deprecated
public class Flip2D_ extends DefaultBenchmarkableModule {

    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        IJ.run(imp2D, "Flip Horizontally", "");
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().flip(clb2D[0], clb2D[1], true, false);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
