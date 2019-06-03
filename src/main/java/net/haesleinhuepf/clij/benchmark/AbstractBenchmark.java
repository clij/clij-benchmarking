package net.haesleinhuepf.clij.benchmark;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.benchmark.modules.*;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class AbstractBenchmark {

    final static long relaxTime = 1000; // ms
    final static int repetitions = 10;


    static CLIJ clij = CLIJ.getInstance();


    protected static void saveTable(long[] durations, String filename) {
        ResultsTable table = new ResultsTable();
        for (int i = 0; i < durations.length; i++) {
            table.incrementCounter();
            table.setValue("Time_in_ns", i, durations[i]);
        }
        table.save(filename);
    }

    protected static long[] measureTimes(CLIJ clij, BenchmarkableModule module, ImagePlus imp2D, ImagePlus imp3D) throws InterruptedException {
        long[] result = new long[2];

        ClearCLBuffer buffer3Da = clij.convert(imp3D, ClearCLBuffer.class);
        ClearCLBuffer buffer3Db = clij.convert(imp3D, ClearCLBuffer.class);
        ClearCLBuffer buffer3Dc = clij.convert(imp3D, ClearCLBuffer.class);

        int numberOfAngles = 360;
        float angleStepSize = 1.0f;
        int effectiveNumberOfAngles = (int)((float)numberOfAngles / angleStepSize);
        int maximumRadius = (int)Math.sqrt(Math.pow(buffer3Da.getWidth() / 2, 2) + Math.pow(buffer3Da.getHeight() / 2, 2));
        ClearCLBuffer buffer3Dd =  clij.createCLBuffer(new long[]{maximumRadius, buffer3Da.getDepth(), effectiveNumberOfAngles}, buffer3Da.getNativeType());

        ClearCLBuffer buffer2Da = clij.convert(imp2D, ClearCLBuffer.class);
        ClearCLBuffer buffer2Db = clij.convert(imp2D, ClearCLBuffer.class);
        ClearCLBuffer buffer2Dc = clij.convert(imp2D, ClearCLBuffer.class);
        ClearCLBuffer buffer2Dd = clij.create(new long[] {buffer3Da.getWidth(), buffer3Da.getHeight()}, buffer3Da.getNativeType());

        module.setCLIJ(clij);

        Thread.sleep(relaxTime);

        result[0] = module.measureImageJTime(imp2D, imp3D);

        Thread.sleep(relaxTime);

        result[1] = module.measureCLIJTime(new ClearCLBuffer[]{buffer2Da, buffer2Db, buffer2Dc, buffer2Dd}, new ClearCLBuffer[]{buffer3Da, buffer3Db, buffer3Dc, buffer3Dd});

        Thread.sleep(relaxTime);

        buffer3Da.close();
        buffer3Db.close();
        buffer3Dc.close();
        buffer3Dd.close();
        buffer2Da.close();
        buffer2Db.close();
        buffer2Dc.close();
        buffer2Dd.close();


        return result;
    }

    protected static String getComputerName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "unknownPC";
    }

}
