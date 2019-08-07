// CLIJ benchmarking macro
// -----------------------
// 
// Procedure:
// * Difference-of-Gaussian
// * Resampling / reslicing (cylinder-projection)
// * Maximum projection
// * Spot count / save number to disc
//
// Used data: Drosophila melanogaster, histone RFP, 512x1024x121 unsigned short (16 bit)
//
// Robert Haase, Myers lab, MPI CBG, rhaase@mpi-cbg.de
// May 2019
//
/////////////////////////////////////////////////////////////////////////////////////////

sourceFolder = "G:/clij/tif/"
resultFolder = "G:/clij/headless_clij_out_myers-pc-22/"
computerName = "myers-pc-22";

//cl_device = "[Intel(R) UHD Graphics 620]";
cl_device = "P6000";

timeLogFile = "C:/structure/code/clij-benchmarking/data/benchmarking/cellcount/headless_" + computerName + "_workflowB_time_clij.csv";
fulltimeLogFile = "C:/structure/code/clij-benchmarking/data/benchmarking/cellcount/headless_" + computerName + "_workflowB_fulltime_clij.csv";
countLogFile = "C:/structure/code/clij-benchmarking/data/benchmarking/cellcount/headless_" + computerName + "_workflowB_cellcount_clij.csv";



smallBlurSigmaInPixels = 2;
blurSigmaInPixels = 6;
sampleX = 0.52;
sampleY = 0.52;
sampleZ = 2.0;
noiseThreshold = 5;


run("CLIJ Macro Extensions", "cl_device=" + cl_device);
Ext.CLIJ_clear();

for (t = 117; t < 417; t+=1) {
	run("Close All");

	startTimeFull = getTime();

    strNumber = "000000" + t;
	filename = substring(strNumber, lengthOf(strNumber) - 6, lengthOf(strNumber) ) + ".raw.tif";

	IJ.log(filename);
	open(sourceFolder + filename);
	run("32-bit");
	makeRectangle(0, 0, 400, 1024);
	run("Crop");
	
	startTimeWholeLoop = getTime();
	
	time = getTime();
	rename("original");
	original = getTitle();
	
	startTimeBlurring = getTime();

	//push image data to GPU
	Ext.CLIJ_push(original);

	// first Gaussian blur
	firstFiltered = "firstFiltered";
	Ext.CLIJ_blur3D(original, firstFiltered, smallBlurSigmaInPixels, smallBlurSigmaInPixels, 0);

	// second Gaussian blur
	secondFiltered = "secondFiltered";
	Ext.CLIJ_blur3D(original, secondFiltered, blurSigmaInPixels, blurSigmaInPixels, 0);
	
	// calculate DoG image
	imageDoG = "imageDoG";
	Ext.CLIJ_addImagesWeighted(firstFiltered, secondFiltered, imageDoG, 1.0, -1.0);
	
	positiveStack = "positiveStack";
	Ext.CLIJ_maximumImageAndScalar(imageDoG, positiveStack, 1.0);

	// resampling and reslicing ()
	scaled = "scaled";
	Ext.CLIJ_downsample3D(positiveStack, scaled, sampleX, sampleY, sampleZ);
	
	reslicedFromTop = "reslicedFromTop";
	Ext.CLIJ_resliceTop(scaled, reslicedFromTop);

	radialResliced = "radialResliced";
	Ext.CLIJ_resliceRadial(reslicedFromTop, radialResliced, 360.0, 1.0);

	reslicedFromLeft = "reslicedFromLeft";
	Ext.CLIJ_resliceLeft(radialResliced, reslicedFromLeft);
	
	// maximum projection
	maxProjected = "maxProjected";
	Ext.CLIJ_maximumZProjection(reslicedFromLeft, maxProjected);

	// flip the image to be visually more similar to ImageJ macro version
	flippedMaxProjected = "flippedMaxProjected";
	Ext.CLIJ_flip2D(maxProjected, flippedMaxProjected, false, true);

	// pull result image back from GPU
	Ext.CLIJ_pull(flippedMaxProjected);


	// detect maxima
	run("Find Maxima...", "noise=" + noiseThreshold + " output=[Single Points]");
	foundMaxima = getTitle();
	// count maxima
	run("Set Measurements...", "mean redirect=None decimal=3");
	run("Measure");
	meanGreyValue = getResult("Mean", nResults() - 1);
	getDimensions(width, height, channels, slices, frames);
	sumGreyValue = meanGreyValue * width * height;
	numberOfSpots = sumGreyValue / 255;

	endTimeWholeLoop = getTime();
	
	// save resulting maximum projection with visualised detected spots
	selectWindow(flippedMaxProjected);
	run("8-bit");
	run("Merge Channels...", "c1=[" + flippedMaxProjected + "] c2=[" + foundMaxima + "] create");
	saveAs("Tiff", resultFolder + filename);

	endTimeFull = getTime();

	// save processing time to disc
	if (!File.exists(timeLogFile)) {
		File.append("Time_in_msec", timeLogFile);
	}	
	File.append((endTimeWholeLoop - startTimeWholeLoop), timeLogFile);
		
	// save number of spots to disc
	if (!File.exists(countLogFile)) {
		File.append("Number_of_spots", countLogFile);
	}	
	File.append(numberOfSpots, countLogFile);

	// save full processing time to disc
	if (!File.exists(fulltimeLogFile)) {
		File.append("Time_in_msec", fulltimeLogFile);
	}	
	File.append((endTimeFull - startTimeFull), fulltimeLogFile);
	



	
	
	//break;
}