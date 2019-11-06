# List of benchmarked operations
This list shows benchmarked operations and short Java code visualising how the operations were called.
The full implementation of individual operation calls can be found in the respective Java classes:
https://github.com/clij/clij-benchmarking-jmh/tree/master/src/main/java/net/haesleinhuepf/clij/benchmark/jmh

<table border="1">

<tr>
<td>Operation</td>
<td>ImageJ</td>
<td>CLIJ</td>
</tr>

<tr>
<td>AddImagesWeighted2D</td>
<td><pre>
ImageCalculator ic = new ImageCalculator();
output = ic.run("Add create", input1, input2);
</pre></td>
<td><pre>
clij.op().addImagesWeighted(input1, input2, 
    output, 1f, 1f);
</pre></td>
</tr>

<tr>
<td>AddImagesWeighted3D</td>
<td><pre>
ImageCalculator ic = new ImageCalculator();
output = ic.run("Add create stack", input1, 
    input2);
</pre></td>
<td><pre>
clij.op().addImagesWeighted(input1, input2, 
    output, 1f, 1f);
</pre></td>
</tr>


<tr>
<td>AddScalar2D</td>
<td><pre>
IJ.run(input, "Add...", "value=1");
</pre></td>
<td><pre>
clij.op().addImageAndScalar(input, output, 
    1f);
</pre></td>
</tr>


<tr>
<td>AddScalar3D</td>
<td><pre>
IJ.run(input, "Add...", "value=1 stack");
</pre></td>
<td><pre>
clij.op().addImageAndScalar(input, output, 
    1f);
</pre></td>
</tr>

<tr>
<td>AutoThreshold2D</td>
<td><pre>
IJ.setAutoThreshold(input, "Default dark");
IJ.run(input, "Convert to Mask", 
    "method=Default background=Dark black");    
</pre></td>
<td><pre>
clij.op().automaticThreshold(input, output, 
    "Default");
</pre></td>
</tr>


<tr>
<td>AutoThreshold3D</td>
<td><pre>
IJ.setAutoThreshold(input, "Default dark");
IJ.run(input, "Convert to Mask", 
    "method=Default background=Dark black");    
</pre></td>
<td><pre>
clij.op().automaticThreshold(input, output, 
    "Default");
</pre></td>
</tr>


<tr>
<td>BinaryAnd2D</td>
<td><pre>
ImageCalculator ic = new ImageCalculator();
output = ic.run("AND create", input1, input2);
</pre></td>
<td><pre>
clij.op().binaryAnd(input1, input2, output);
</pre></td>
</tr>

<tr>
<td>BinaryAnd3D</td>
<td><pre>
ImageCalculator ic = new ImageCalculator();
output = ic.run("AND create stack", input1, 
    input2);
</pre></td>
<td><pre>
clij.op().binaryAnd(input1, input2, output);
</pre></td>
</tr>

<tr>
<td>Erode2D</td>
<td><pre>
IJ.run(input, "Erode", "");
</pre></td>
<td><pre>
clij.op().erodeSphere(input, output);
</pre></td>
</tr>

<tr>
<td>Erode3D</td>
<td><pre>
import process3d.Erode_;
output = new Erode_().erode(input, 1, true);
</pre></td>
<td><pre>
clij.op().erodeSphere(input, output);
</pre></td>
</tr>


<tr>
<td>FixedThreshold2D</td>
<td><pre>
IJ.setThreshold(input, 128, 255);
IJ.run(input, "Convert to Mask", 
    "method=Default background=Dark black");
</pre></td>
<td><pre>
clij.op().threshold(input, output, 128f);    
</pre></td>
</tr>

<tr>
<td>FixedThreshold3D</td>
<td><pre>
IJ.setThreshold(input, 128, 255);
IJ.run(input, "Convert to Mask", 
    "method=Default background=Dark black");
</pre></td>
<td><pre>
clij.op().threshold(input, output, 128f);    
</pre></td>
</tr>


<tr>
<td>Flip2D</td>
<td><pre>
IJ.run(input, "Flip Horizontally", "");
</pre></td>
<td><pre>
clij.op().flip(input, output, true, false);
</pre></td>
</tr>

<tr>
<td>Flip3D</td>
<td><pre>
IJ.run(input, "Flip Horizontally", "stack"); 
</pre></td>
<td><pre>
clij.op().flip(input, output, true, false, 
    false);
</pre></td>
</tr>

<tr>
<td>GaussianBlur2D</td>
<td><pre>
IJ.run(input, "Gaussian Blur...", "sigma=" + 
    sigma);
</pre></td>
<td><pre>
clij.op().blur(input, output, sigma, sigma);
</pre></td>
</tr>

<tr>
<td>GaussianBlur3D</td>
<td><pre>
IJ.run(input, "Gaussian Blur 3D...", "x=" + 
    sigma + " y=" + sigma + " z=" + sigma);
</pre></td>
<td><pre>
clij.op().blur(input, output, sigma, sigma, 
    sigma);
</pre></td>
</tr>

<tr>
<td>MaximumZProjection</td>
<td><pre>
IJ.run(input, "Z Project...", 
    "projection=[Max Intensity]");
</pre></td>
<td><pre>
clij.op().maximumZProjection(input, output);
</pre></td>
</tr>

<tr>
<td>Mean2D</td>
<td><pre>
IJ.run(input, "Mean...", "radius=" + radius);
</pre></td>
<td><pre>
kernelSize = CLIJUtilities.radiusToKernelSize(
    radius);
clij.op().meanSphere(input, output, kernelSize,
    kernelSize);
</pre></td>
</tr>

<tr>
<td>Mean3D</td>
<td><pre>
IJ.run(input, "Mean 3D...", "x=" + radius + 
    " y=" + radius + " z=" + radius);
</pre></td>
<td><pre>
kernelSize = CLIJUtilities.radiusToKernelSize(
    radius);
clij.op().meanSphere(input, output, kernelSize,
    kernelSize, kernelSize);    
</pre></td>
</tr>

<tr>
<td>Median2D</td>
<td><pre>
IJ.run(input, "Median...", "radius=" + radius);
</pre></td>
<td><pre>
kernelSize = CLIJUtilities.radiusToKernelSize(
    radius);
clij.op().medianSphere(input, output, 
    kernelSize, kernelSize);
</pre></td>
</tr>

<tr>
<td>Median3D</td>
<td><pre>
IJ.run(input, "Median 3D...", "x=" + radius + 
    " y=" + radius + " z=" + radius);
</pre></td>
<td><pre>
kernelSize = CLIJUtilities.radiusToKernelSize(
    radius);
clij.op().medianSphere(input, output, 
    kernelSize, kernelSize, kernelSize);    
</pre></td>
</tr>

<tr>
<td>Minimum2D</td>
<td><pre>
IJ.run(input, "Minimum...", "radius=" + radius);  
</pre></td>
<td><pre>
kernelSize = CLIJUtilities.radiusToKernelSize(
    radius);
clij.op().minimumSphere(input, output, 
    kernelSize, kernelSize);
</pre></td>
</tr>

<tr>
<td>Minimum3D</td>
<td><pre>
IJ.run(input, "Minimum 3D...", "x=" + radius + 
    " y=" + radius + " z=" + radius);
</pre></td>
<td><pre>
kernelSize = CLIJUtilities.radiusToKernelSize(
    radius);
clij.op().minimumSphere(input, output, 
    kernelSize, kernelSize, kernelSize);
</pre></td>
</tr>

<tr>
<td>MultiplyScalar2D</td>
<td><pre>
IJ.run(input, "Multiply...", "value=2");
</pre></td>
<td><pre>
clij.op().multiplyImageAndScalar(input, 
    output, 2f);
</pre></td>
</tr>

<tr>
<td>MultiplyScalar3D</td>
<td><pre>
IJ.run(input, "Multiply...", "value=2 stack");
</pre></td>
<td><pre>
clij.op().multiplyImageAndScalar(input, output,
    2f);
</pre></td>
</tr>

<tr>
<td>RadialReslice</td>
<td><pre>
input.setRoi(new Line(input.getWidth() / 2, 
    input.getWidth() / 2, 0, 0));
Radial_Reslice_Copy rr = 
    new Radial_Reslice_Copy();
rr.setup("", input);
rr.run(input.getProcessor());
</pre></td>
<td><pre>
numberOfAngles = 360;
angleStepSize = 1.0f;
effectiveNumberOfAngles = (int)((float)
    numberOfAngles / angleStepSize);
maximumRadius = (int)Math.sqrt(
    Math.pow(input.getWidth() / 2, 2) + 
    Math.pow(input.getHeight() / 2, 2));
ClearCLBuffer output =  clij.createCLBuffer(
    new long[]{maximumRadius, 
    input.getDepth(), 
    effectiveNumberOfAngles}, 
    input.getNativeType());
clij.op().radialProjection(input, output, 
    angleStepSize);
</pre></td>
</tr>


<tr>
<td>Rotate2D</td>
<td><pre>
IJ.run(input, "Rotate 90 Degrees Left", "");
</pre></td>
<td><pre>
clij.op().rotateLeft(input, output);
</pre></td>
</tr>

<tr>
<td>Rotate3D</td>
<td><pre>
IJ.run(input, "Rotate 90 Degrees Left", "");   
</pre></td>
<td><pre>
clij.op().rotateLeft(input, output);
</pre></td>
</tr>


</table>

[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
