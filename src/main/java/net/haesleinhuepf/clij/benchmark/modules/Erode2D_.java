package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.Binary;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

@Deprecated
public class Erode2D_ extends DefaultBenchmarkableModule {

    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        IJ.run(imp2D, "Erode", "");

        //imp2D.show();
        //imp2D.setTitle("imagej");
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().erodeBox(clb2D[0], clb2D[1]);
        //clij.show(clb2D[1], "clij");
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean isBinary() {
        return true;
    }
}
