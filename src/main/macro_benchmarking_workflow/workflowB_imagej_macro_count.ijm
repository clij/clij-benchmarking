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

sourceFolder = "D:/benchmark/tif/"
resultFolder = "D:/benchmark/imagej_out_myers-pc-21/"
computerName = "myers-pc-21";

timeLogFile = "C:/structure/code/clij-benchmarking-public/data/benchmarking/cellcount/" + computerName + "_workflowB_time_imagej.csv";
fulltimeLogFile = "C:/structure/code/clij-benchmarking-public/data/benchmarking/cellcount/" + computerName + "_workflowB_fulltime_imagej.csv";
countLogFile = "C:/structure/code/clij-benchmarking-public/data/benchmarking/cellcount/" + computerName + "_workflowB_cellcount_imagej.csv";


smallBlurSigmaInPixels = 2;
blurSigmaInPixels = 6;
sampleX = 0.52;
sampleY = 0.52;
sampleZ = 2.0;
noiseThreshold = 5;



for (t = 117; t < 417; t+=1) {

	run("Close All");

	startTimeFull = getTime();

	// load image
    strNumber = "000000" + t;
	filename = substring(strNumber, lengthOf(strNumber) - 6, lengthOf(strNumber) ) + ".raw.tif";

	IJ.log(filename);
	open(sourceFolder + filename);
	run("32-bit");
	makeRectangle(0, 0, 400, 1024);
	run("Crop");

	startTimeWholeLoop = getTime();
	
	getDimensions(width, height, channels, slices, frames);
	run("Properties...", "channels=" + channels + " slices=" + slices + " frames=" + frames + " unit=pixel pixel_width=1.0000 pixel_height=1.0000 voxel_depth=1.0000");

	original = getTitle();

	// first Gaussian blur
	selectWindow(original);
	run("Duplicate...", "duplicate");
	run("Gaussian Blur...", "sigma=" + smallBlurSigmaInPixels + " stack");
	firstFiltered = getTitle();

	// second Gaussian blur
	selectWindow(original);
	run("Duplicate...", "duplicate");
	run("Gaussian Blur...", "sigma=" + blurSigmaInPixels + " stack");
	secondFiltered = getTitle();
	
	// calculate DoG image
	imageCalculator("Subtract create 32-bit stack", firstFiltered, secondFiltered);
	imageDoG = getTitle();

	
	getDimensions(width, height, channels, slices, frames);
	newImage("Untitled", "16-bit black", width, height, slices);
	emptyStack = getTitle();
	run("Add...", "value=1 stack");
	
	imageCalculator("Max create 32-bit stack", imageDoG, emptyStack);

	positiveStack = getTitle();

	// cylinder-projections
	getDimensions(width, height, channels, slices, frames);
	run("Scale...", "x=" + sampleX + " y=" + sampleY + " z=" + sampleZ + " width=" + (width * sampleX) + " height=" + (height * sampleY) + " depth=" + (slices * sampleZ) + " interpolation=None process create");
	scaled = getTitle();
	
	run("Reslice [/]...", "output=1.982 start=Top avoid");
	
	getDimensions(width, height, channels, slices, frames);
	makeLine(width/2, height/2, width/2, height/2 + sqrt(pow(width/2,2) + pow(height/2,2)));
	
	run("Radial Reslice", "angle=360 degrees_per_slice=1 direction=Clockwise");
	
	run("Reslice [/]...", "output=1.982 start=Left avoid");
	
	// maximum projection
	run("Z Project...", "projection=[Max Intensity]");
	maxProjected = getTitle();
	
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
	selectWindow(maxProjected);
	run("8-bit");
	run("Merge Channels...", "c1=[" + maxProjected + "] c2=[" + foundMaxima + "] create");
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
