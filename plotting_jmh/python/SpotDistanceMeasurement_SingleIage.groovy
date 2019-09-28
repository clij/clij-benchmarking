// @DatasetIOService io
// @OpService ops
// @UIService ui

// SpotDistanceMeausrement_SingleImage.groovy
// ------------------------------
// 
// This script takes a folder of 2D-2channel TIF-images expecting the second channel
// contains the result of spot detection. Afterwards these channel images are evaluated 
// to measure the minimum spot distance within these images. Results are
// written to a CSV file
//
// Author: Deborah Schmidt, Robert Haase, MPI-CBG
//         September 2019
//
// ------------------------------------------------------------------------------------

// File(style = "directory", label="First dataset path (folder with .tifs)", value='/home/random/Development/imagej/project/clij/clij-benchmarking-data/data/benchmark/clij_out_myers-pc-21/') dataset1
// File(style = "directory", label="Second dataset path path (folder with .tifs)", value='/home/random/Development/imagej/project/clij/clij-benchmarking-data/data/benchmark/imagej_out_myers-pc-21/') dataset2

root_folder = "C:/structure/code/clij-benchmarking-data/data/benchmark/"; // must end with /

import org.scijava.table.GenericTable
import org.scijava.table.DefaultGenericTable
import net.imglib2.util.ValuePair
import org.apache.commons.math3.stat.descriptive.moment.Mean
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import org.apache.commons.math3.stat.descriptive.rank.Min
import net.imglib2.roi.labeling.LabelRegions
import net.imglib2.Point
import net.imglib2.algorithm.labeling.ConnectedComponents
import java.io.File
import java.io.FileWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;
import ij.IJ;
import java.lang.StringBuilder;
import java.lang.Double;


processFolder(root_folder + "imagej_out_myers-pc-21/", root_folder + "spot_distance_single_image_ij_myers21.csv");
processFolder(root_folder + "imagej_out_myers-pc-22/", root_folder + "spot_distance_single_image_ij_myers22.csv");
processFolder(root_folder + "clij_out_myers-pc-21/", root_folder + "spot_distance_single_image_clij_myers21.csv");
processFolder(root_folder + "clij_out_myers-pc-22/", root_folder + "spot_distance_single_image_clij_myers22.csv");

// compare images in two folder
def processFolder(folder1, target_csv_file) {

	// config folders and target files - in global variables 
	dataset1 = new File(folder1)
	target_file = target_csv_file; 
	
	// reserve memory for results
	resultCSV = new StringBuilder()
	resultCSV.append("mean,stdev\n");
	
	result = new DefaultGenericTable()
	result.appendColumn("mean")
	result.appendColumn("stdev")
	
	// check input	
	if(!dataset1.exists()) {
		return print("ERROR: Please provide an existing directory for dataset1")
	}
	
	if(!dataset1.isDirectory()) {
		return print("ERROR: Please provide a directory for dataset1")	
	}
	
	// go through all file pairs
	def count = 0;
	for (file in dataset1.listFiles()) {
	    time = System.currentTimeMillis();
		process(file)
		IJ.log("processing took " + (System.currentTimeMillis() - time) + "msec");
		count++;
	
		// for debugging
		//if (count > 3) {
		//	break;
		//}
	}
	
	// show results
	ui.show("Point distances", result)
	// doesn't work in current Fiji:
	//io.save(result, target_file);
	
	// save results to disc
	File csvFile = new File(target_file);
	writer = new BufferedWriter(new FileWriter(csvFile));
	writer.write(resultCSV.toString());
	writer.close();
}

// processes a pair of files in two folder; both have the same name
def process(file) {
	if (file.name.endsWith(".tif"))	{
		compare(file.getAbsolutePath())
	}
}

// compare two image files
def compare(img1Path){
	println("Measure avg min distance in " + img1Path)
	def _img1 = io.open(img1Path)
	
	// crop out second channel
	def img1 = ops.transform().hyperSliceView(_img1, 2, 1l)
	result.appendRow(_img1.getName())

	// measure distance and save it in results table/csv
	def ptDist = getPointDistances(img1)
	result.set("mean", result.getRowCount()-1, ptDist.getA())
	result.set("stdev", result.getRowCount()-1, ptDist.getB())

	resultCSV.append("" + ptDist.getA() + ", " + ptDist.getB() + "\n");
	
}

// measure average minimum spot distance in an image
def getPointDistances(imgA) {
	def ccaA = ops.labeling().cca(imgA, ConnectedComponents.StructuringElement.FOUR_CONNECTED)
    def ptsA = getPoints(ccaA)
    def distances = new double[ptsA.size()]
    for(def p = 0; p < ptsA.size(); p++) {
        distances[p] = minimalDistance(ptsA.get(p), ptsA, p)
    }
    return new ValuePair(new Mean().evaluate(distances), new StandardDeviation().evaluate(distances))
}

// determine the closest distance between pointA and any point in the list pointsB
def minimalDistance(pointA, pointsB, p) {
	def dist = new double[pointsB.size()]
    for(i = 0; i < pointsB.size(); i++) {
        if (p == i) {
        	dist[i] = Double.MAX_VALUE; // do not measure the distance of the spot with itself
        } else {
	        def pointB = pointsB.get(i)
	        double sum = 0
	        for (j = 0; j < pointA.numDimensions(); j++) {
	        	sum += Math.pow(pointA.getDoublePosition(j) - pointB.getDoublePosition(j), 2)
	        }
	        sum = Math.sqrt(sum)
	        if(sum == 0.0d) return 0.0d
	        dist[i] = sum
        }
    }
    return new Min().evaluate(dist)
}

// get points from an image
def getPoints(img) {
    def regions = new LabelRegions<>(img)
    def points = new ArrayList<>()
    for (region in regions) {
        def pt = new Point(img.numDimensions())
        region.min(pt)
        points.add(pt)
    }
    return points
}