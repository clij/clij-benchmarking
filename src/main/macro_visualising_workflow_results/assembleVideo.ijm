// This macro assembles several videos as one.
// * Left: Maximun projection
// * Right, first row: ImageJ results
// * Right, second row: CLIJ results
// * Right, third row: Plot spot count over time
//
// Author: Robert Haase, rhaase@mpi-cbg.de
// May 2019
//
/////////////////////////////////////////////////////////

run("Close All");

run("Image Sequence...", "open=C:/structure/code/clij-benchmarking-public/plotting/images/spotcount_overtime/spot_count_plot_00000.png sort");
makeRectangle(0, 30, 1250, 320);
run("Crop");
run("Invert", "stack");

open("C:/structure/code/clij-workflow-benchmarking/data/benchmark/ImageJ laptop.tif");

open("C:/structure/code/clij-workflow-benchmarking/data/benchmark/CLIJ laptop.tif");

open("C:/structure/mpicloud/Projects/201905_CLIJ_paper/Florence_bgsub_max_117-416.tif");
makeRectangle(0, 0, 400, 1024);
run("Crop");

newImage("Untitled", "RGB black", 10, 10, 300);

selectWindow("ImageJ laptop.tif");

run("Combine...", "stack1=Untitled stack2=[ImageJ laptop.tif] combine");

newImage("Untitled", "RGB black", 10, 10, 300);

run("Combine...", "stack1=[Combined Stacks] stack2=Untitled combine");

run("Combine...", "stack1=[Combined Stacks] stack2=[CLIJ laptop.tif] combine");

run("Combine...", "stack1=[Combined Stacks] stack2=spotcount_overtime combine");

selectWindow("Florence_bgsub_max_117-416.tif");
run("RGB Color");
run("Combine...", "stack1=Florence_bgsub_max_117-416.tif stack2=[Combined Stacks]");
