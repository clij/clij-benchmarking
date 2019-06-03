// Workflow result visualisation macro
//
//
// Author: Robert Haase, rhaase@mpi-cbg.de
// May 2019
//
/////////////////////////////////////////////////////////

// configuration
targetFolder = "C:/structure/code/clij-workflow-benchmarking/data/benchmark/";

fontsize = 18;

function main() {
	processVideo(targetFolder + "clij_out_myers-pc-21/", targetFolder + "clij_out_myers-pc-21_video/", "CLIJ laptop" );
	processVideo(targetFolder + "clij_out_myers-pc-22/", targetFolder + "clij_out_myers-pc-22_video/", "CLIJ workstation");
	processVideo(targetFolder + "imagej_out_myers-pc-21/", targetFolder + "imagej_out_myers-pc-21_video/", "ImageJ laptop");
	processVideo(targetFolder + "imagej_out_myers-pc-22/", targetFolder + "imagej_out_myers-pc-22_video/", "ImageJ workstation");
}

function processVideo(pathIn, pathOut, datasetname) {

	filelist = getFileList(pathIn);

	// open all projections from all time points and zoom in two regions
	for (t = 0; t < lengthOf(filelist); t++) {
		run("Close All");

		// load data and fix LUTs for red/green-blind people
		open(pathIn + filelist[t]);
		run("Magenta");
		run("RGB Color");
		originalRGB = getTitle();

		// first ROI
		makeRectangle(120, 40, 90, 90);

		run("Duplicate...", " ");
		run("Enhance Contrast", "saturated=0.35");
		makeRectangle(0, 0, 89, 89);
		Roi.setStrokeColor("cyan");
		run("Add Selection...");
		run("Flatten");
		run("Scale...", "x=4 y=4 width=360 height=360 interpolation=None create");
		zoomedImage = getTitle();

		selectWindow(originalRGB);
		Roi.setStrokeColor("cyan");
		run("Add Selection...");
		run("Flatten");
		imageWithFrame = getTitle();
		run("Combine...", "stack1=[" + imageWithFrame + "] stack2=[" + zoomedImage + "]");
		rename("Comb2");
		originalRGB = getTitle();

		// second ROI
		makeRectangle(240, 250, 90, 90);

		run("Duplicate...", " ");
		run("Enhance Contrast", "saturated=0.35");
		makeRectangle(0, 0, 89, 89);
		Roi.setStrokeColor("yellow");
		run("Add Selection...");
		run("Flatten");
		run("Scale...", "x=4 y=4 width=360 height=360 interpolation=None create");
		zoomedImage = getTitle();

		selectWindow(originalRGB);
		Roi.setStrokeColor("yellow");
		run("Add Selection...");
		run("Flatten");
		imageWithFrame = getTitle();
		
		run("Combine...", "stack1=[" + imageWithFrame + "] stack2=[" + zoomedImage + "]");

		
		saveAs("Tiff", pathOut + filelist[t]);
	}

    // put text in the corner and save the result as single TIF
	run("Image Sequence...", "open=" + pathOut + "000117.raw.tif sort");
	run("Time Stamper", "starting=0 interval=1 x=10 y=15 font=" + fontsize + " decimal=0 anti-aliased or=[ " + datasetname + "]");
	saveAs("Tiff", targetFolder + datasetname + ".tif");
}

main();