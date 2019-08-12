package net.haesleinhuepf.clij.benchmark;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.benchmark.modules.*;

public class BenchmarkImageSize extends AbstractBenchmark {
    public static void main(String... args) throws InterruptedException {

        double defaultSigma = 2;
        int defaultRadius = 2;

        BenchmarkableModule[] modules = {
                //new AddImagesWeighted3D(),
                //new GaussianBlur3D(defaultSigma)
                //new MaximumZProjection(),
               // new Minimum3D(defaultRadius),
                //new Reslice3D(),
                //new RadialReslice(),
                new Transfer3DFrom()
        };


        int[] sizes = {1, 2, 4, 8, 16, 32, 64}; //, 128, 256, 512}; // in MB

        for (int size : sizes) {
            for (BenchmarkableModule module : modules) {

                long[] imageJTimes = new long[repetitions];
                long[] clijTimes = new long[repetitions];
                System.out.println("Testing " + module.getName() + " with " + size + "MB");

                for (int i = 0; i < repetitions; i++) {
                    ImagePlus imp3D = NewImage.createByteImage("title", 1024, 512, size * 2, NewImage.FILL_RANDOM);
                    ImagePlus imp2D = NewImage.createByteImage("title", 4096, 256 * size, 1, NewImage.FILL_RANDOM); // unused

                    if (module.isBinary()) {
                        IJ.setThreshold(imp3D, 128, 255);
                        IJ.run(imp3D, "Convert to Mask", "method=Default background=Dark black");
                        IJ.setThreshold(imp2D, 128, 255);
                        IJ.run(imp2D, "Convert to Mask", "method=Default background=Dark black");
                    }

                    long[] times = measureTimes(clij, module, imp2D, imp3D);

                    imageJTimes[i] = times[0];
                    clijTimes[i] = times[1];
                }

                saveTable(imageJTimes, "data/benchmarking/imagesize/" + getComputerName() + "_" + module.getName() + "_" + size + "_imagej.csv");
                saveTable(clijTimes, "data/benchmarking/imagesize/" + getComputerName() + "_" + module.getName() + "_" + size + "_clij.csv");
            }
        }
        System.out.println("Bye.");
    }

}
