package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.ImageMath;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

public class AddScalar2D extends DefaultBenchmarkableModule {

    private Double scalar;

    public AddScalar2D(double scalar) {
        this.scalar = scalar;
    }

    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        // IJ.run(imp2D, "Add...", "value=" + scalar);
        imp2D.getProcessor().add(scalar);
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().addImageAndScalar(clb2D[0], clb2D[1], scalar.floatValue());
    }

    @Override
    public String getName() {
        return getClass().getSimpleName() + "_" + scalar;
    }
}
