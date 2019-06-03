package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

public class Threshold3D extends DefaultBenchmarkableModule {



    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        IJ.setThreshold(imp3D, 128, 255);
        IJ.run(imp3D, "Convert to Mask", "method=Default background=Dark black");
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().threshold(clb3D[0], clb3D[1], new Float(128));
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
