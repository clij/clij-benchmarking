package net.haesleinhuepf.clij.benchmark.clearclclijcomparison;

import ij.ImagePlus;
import ij.gui.NewImage;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLContext;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.ClearCLProgram;
import net.haesleinhuepf.clij.clearcl.util.ElapsedTime;

import java.io.IOException;
import java.util.HashMap;

/**
 * Compare ClearCLs OpenCL kernel execution with CLIJ. Subject of investigation:
 *   * Compilation times,
 *   * handover of parameters
 *   * compiled program caching.
 *
 * Author: Robert Haase, rhaase@mpi-cbg.de
 *         July 2019 in Woods Hole
 */
public class ClearCLVersusCLIJComparison {

    public static void main(String[] args) {
        ImagePlus imp = NewImage.createByteImage("byte", 1, 1, 1, NewImage.FILL_RANDOM);

        // exclude CLIJ initialisation and memory management from benchmarking
        CLIJ clij = CLIJ.getInstance();
        ClearCLBuffer input = clij.push(imp);
        ClearCLBuffer output = clij.create(input);

        ClearCLVersusCLIJComparison cvcc = new ClearCLVersusCLIJComparison();

        // first execution of a kernel via CLIJ includes OpenCL program compilation
        ElapsedTime.measureForceOutput("CLIJ initially", () -> {
            cvcc.testCLIJ(clij, input, output);
        });

        // following executions reuse the compiled program code
        for (int i = 0; i < 10; i++) {
            ElapsedTime.measureForceOutput("CLIJ subsequently", () -> {
                cvcc.testCLIJ(clij, input, output);
            });
        }

        // ClearCL does not have the mentioned caching mechanism so we keep the compuled program after
        // execution of the first kernel. Again, it is compiled while first execution
        final ClearCLProgram[] programs = new ClearCLProgram[1];
        ElapsedTime.measureForceOutput("ClearCL initially", () -> {
            programs[0] = cvcc.testClearCL(clij.getClearCLContext(), input, output);
        });

        // following executions should be faster because of reusing compilations
        for (int i = 0; i < 10; i++) {
            ElapsedTime.measureForceOutput("ClearCL subsequently", () -> {
                cvcc.runKernel(programs[0], input, output);
            });
        }
        System.exit(0);
    }

    private void testCLIJ(CLIJ clij, ClearCLBuffer input, ClearCLBuffer output) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("src", input);
        parameters.put("dst", output);
        clij.execute(ClearCLVersusCLIJComparison.class, "simplekernel.cl", "copybuffer", parameters);
    }

    private ClearCLProgram testClearCL(ClearCLContext context, ClearCLBuffer input, ClearCLBuffer output) {
        ClearCLProgram program = null;
        try {
            program = context.createProgram(ClearCLVersusCLIJComparison.class, "simplekernel.cl");
        } catch (IOException e) {
            e.printStackTrace();
        }
        program.addBuildOptionAllMathOpt();
        try {
            program.buildAndLog();
        } catch (IOException e) {
            e.printStackTrace();
        }
        runKernel(program, input, output);
        return program;
    }

    private void runKernel(ClearCLProgram program, ClearCLBuffer input, ClearCLBuffer output) {
        ClearCLKernel kernel = program.createKernel("copybuffer");
        kernel.setGlobalSizes(output.getDimensions());


        kernel.setArgument("src", input);
        kernel.setArgument("dst", output);


        kernel.run(true);

        kernel.close();

    }

}
