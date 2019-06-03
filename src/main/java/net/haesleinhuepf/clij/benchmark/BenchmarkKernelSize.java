package net.haesleinhuepf.clij.benchmark;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.benchmark.modules.GaussianBlur3D;
import net.haesleinhuepf.clij.benchmark.modules.Minimum3D;

public class BenchmarkKernelSize extends AbstractBenchmark {
    public static void main(String... args) throws InterruptedException {

        int[] sizes = new int[50];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = i + 1;
        }

        for (int size : sizes) {
            BenchmarkableModule module = new GaussianBlur3D(size);

            long[] imageJTimes = new long[repetitions];
            long[] clijTimes = new long[repetitions];
            System.out.println("Testing " + module.getName() + " with kernel size " + size);

            for (int i = 0; i < repetitions; i++) {
                ImagePlus imp3D = NewImage.createByteImage("title", 256, 256, 256, NewImage.FILL_RANDOM);
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

            saveTable(imageJTimes, "data/benchmarking/kernelsize/" + getComputerName() + "_" + module.getName() + "_" + size + "_imagej.csv");
            saveTable(clijTimes, "data/benchmarking/kernelsize/" + getComputerName() + "_" + module.getName() + "_" + size + "_clij.csv");

        }
        if (true) return;
        for (int size : sizes) {
            BenchmarkableModule module = new Minimum3D(size);

            long[] imageJTimes = new long[repetitions];
            long[] clijTimes = new long[repetitions];
            System.out.println("Testing " + module.getName() + " with kernel size " + size);

            for (int i = 0; i < repetitions; i++) {
                ImagePlus imp3D = NewImage.createByteImage("title", 256, 256, 256, NewImage.FILL_RANDOM);
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

            saveTable(imageJTimes, "data/benchmarking/kernelsize/" + getComputerName() + "_" + module.getName() + "_" + size + "_imagej.csv");
            saveTable(clijTimes, "data/benchmarking/kernelsize/" + getComputerName() + "_" + module.getName() + "_" + size + "_clij.csv");

        }

        System.out.println("Bye.");
    }

}
