# Script for plotting processing time over image size
#
#
# Author: Robert Haase, MPI CBG Dresden
# May 2019
###########################################################33

# configuration
machines = ["myers-pc-21", "myers-pc-22"];

operations = [
    "AddImagesWeighted3D",
    "GaussianBlur3D_2.0",
    "MaximumZProjection",
    "Minimum3D_2",
    "Reslice3D",
    "RadialReslice"
]


font = {'family' : 'normal',
        'size'   : 22}

reference_machine_index = 0

columns = ["imagej", "clij"]
column_captions = ["Notebook", "Workstation"];

sizes = [1,2,4,8,16,32,64]

import numpy as np
import matplotlib
import matplotlib.pyplot as plt

import utils


import seaborn as sns
sns.set_style('whitegrid')

matplotlib.rc('font', **font)

# go through list of tested operations, create one plot each
for operation in operations:

    plt.figure(figsize=(9, 7), dpi=80)

    plot_legend = []

    machine_index = 0
    color_index = 0

    durations = []

    # plot all test computers processing times
    for pcname in machines:
        print(pcname)
        root_filename = "../../data/benchmarking/imagesize/" + pcname + "_";

        # plot CPU and GPU processing time
        for col in columns:
            durations = []
            upper_bound = []
            lower_bound = []
            for size in sizes:
                filename = root_filename + operation + "_" + str(size) + "_" + col  + ".csv"

                measurements = utils.read_measurements_from_file(filename)
                correction_factor = utils.read_correction_factor_from_file(filename) / 1000

                durations.extend([np.median(measurements) * correction_factor])
                upper_bound.extend([np.percentile(measurements, 75) * correction_factor])
                lower_bound.extend([np.percentile(measurements, 25) * correction_factor])

                plt.plot(np.ones(len((measurements))) * size, np.multiply(measurements, correction_factor), 'o', color='C%d'%color_index )

            plt.plot(sizes, durations, color='C%d'%color_index )
            plt.fill_between(sizes, lower_bound, upper_bound, alpha=0.5, color='C%d'%color_index)

            color_index = color_index + 1

        machine_index = machine_index + 1

    plt.xlabel("Image size / MB");
    plt.ylabel("Processing time / s")
    title = operation.split("_")[0]
    plt.title(title + " time over image size")

    print(operation)
    plt.savefig("../images/compare_machines_imagesize_processing_time_" + title + ".pdf")
    plt.savefig("../images/compare_machines_imagesize_processing_time_" + title + ".png")
    #plt.show()

