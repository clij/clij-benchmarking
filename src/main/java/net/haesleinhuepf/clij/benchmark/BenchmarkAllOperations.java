package net.haesleinhuepf.clij.benchmark;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.benchmark.modules.*;

public class BenchmarkAllOperations extends AbstractBenchmark {
    public static void main(String... args) throws InterruptedException {

        double defaultSigma = 2;
        int defaultRadius = 2;

        BenchmarkableModule[] modules = {
                /*new AddImagesWeighted2D(),
                new AddImagesWeighted3D(),

                new AddScalar2D_(1),
                new AddScalar2D(1),
                new AddScalar3D_(1),
                new AddScalar3D(1)

                new AutoThreshold2D(),
                new AutoThreshold3D(),

                new BinaryAnd2D(),
                new BinaryAnd3D(),

                new Erode2D(),
                new Erode3D(),

                new Flip2D(),
                new Flip3D(),
*/
                new GaussianBlur2D(defaultSigma),
                new GaussianBlur2D_(defaultSigma),
                new GaussianBlur3D(defaultSigma),
                new GaussianBlur3D_(defaultSigma)
/*
                new MaximumZProjection(),

                new Mean2D(defaultRadius),
                new Mean3D(defaultRadius),

                new Median2D(defaultRadius),
                new Median3D(defaultRadius),

                new Minimum2D(defaultRadius),
                new Minimum3D(defaultRadius),

                new MultiplyScalar2D(2),
                new MultiplyScalar3D(2),

                new RadialReslice(),
                new Reslice3D(),

                new Rotate2D(),
                new Rotate3D(),

                new Threshold2D(),
                new Threshold3D()*/
        };


        for (BenchmarkableModule module : modules) {

            long[] imageJTimes = new long[repetitions];
            long[] clijTimes = new long[repetitions];
            System.out.println("Testing " + module.getName());



            for (int factor = 1; factor < 8; factor = factor * 2) {
                for (int i = 0; i < repetitions; i++) {
                    ImagePlus imp3D = NewImage.createByteImage("title", 512 * factor, 512 * factor, 100, NewImage.FILL_RANDOM);
                    ImagePlus imp2D = NewImage.createByteImage("title", 512 * factor, 512 * factor, 1, NewImage.FILL_RANDOM);

                    if (module.isBinary()) {
                        IJ.setThreshold(imp3D, 128, 255);
                        IJ.run(imp3D, "Convert to Mask", "method=Default background=Dark black");
                        IJ.setThreshold(imp2D, 128, 255);
                        IJ.run(imp2D, "Convert to Mask", "method=Default background=Dark black");
                    }

                    //new ImageJ();

                    long[] times = measureTimes(clij, module, imp2D, imp3D);

                    //new WaitForUserDialog("test").show();

                    imageJTimes[i] = times[0];
                    clijTimes[i] = times[1];
                }
            }
            saveTable(imageJTimes, "data/benchmarking/all/" + getComputerName() + "_" + module.getName() + "_imagej.csv");
            saveTable(clijTimes, "data/benchmarking/all/" + getComputerName() + "_" + module.getName() + "_clij.csv");
        }

        System.out.println("Bye.");
    }

}
