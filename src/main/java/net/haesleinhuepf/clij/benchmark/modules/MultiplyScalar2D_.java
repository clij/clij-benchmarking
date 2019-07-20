package net.haesleinhuepf.clij.benchmark.modules;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.benchmark.DefaultBenchmarkableModule;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

@Deprecated
public class MultiplyScalar2D_ extends DefaultBenchmarkableModule {

    private Double scalar;

    public MultiplyScalar2D_(double scalar) {
        this.scalar = scalar;
    }

    public void test(ImagePlus imp2D, ImagePlus imp3D) {
        IJ.run(imp2D, "Multiply...", "value=" + scalar);
    }

    public void test(ClearCLBuffer[] clb2D, ClearCLBuffer[] clb3D) {
        clij.op().multiplyImageAndScalar(clb2D[0], clb2D[1], scalar.floatValue());
    }

    @Override
    public String getName() {
        return getClass().getSimpleName() + "_" + scalar;
    }
}
