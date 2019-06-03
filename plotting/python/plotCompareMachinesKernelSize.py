# Script for plotting processing time over parameters of algorithms
# Algorithms:
# * Gaussian Blur
# * Minimum filter
#
# Author: Robert Haase, MPI CBG
# May 2019
#############################################3

machines = ["myers-pc-21", "myers-pc-22"];

font = {'family' : 'normal',
        'size'   : 22}

reference_machine_index = 0

columns = ["imagej", "clij"]
column_captions = ["Laptop", "Workstation"];

sigmas = range(1, 51)

import numpy as np
import matplotlib
import matplotlib.pyplot as plt

import utils

import seaborn as sns
sns.set_style('whitegrid')


matplotlib.rc('font', **font)


operation = "GaussianBlur3D";

plt.figure(figsize=(9, 7), dpi=80)

plot_legend = []

machine_index = 0

durations = []

color_index = 0;

# plot all tested computers
for pcname in machines:
    print(pcname)
    root_filename = "../../data/benchmarking/kernelsize/" + pcname + "_";

    # plot CPU and GPU
    for col in columns:
        durations = []
        errors = []
        upper_bound = []
        lower_bound = []

        for size in sigmas:
            filename = root_filename + operation + "_" + str(size) + ".0_" + str(size) + "_" + col + ".csv"

            measurements = utils.read_measurements_from_file(filename)
            correction_factor = utils.read_correction_factor_from_file(filename) / 1000

            durations.extend([np.median(measurements) * correction_factor])
            upper_bound.extend([np.percentile(measurements, 75) * correction_factor])
            lower_bound.extend([np.percentile(measurements, 25) * correction_factor])

            plt.plot(np.ones(len((measurements))) * size, np.multiply(measurements, correction_factor), 'o', color='C%d' % color_index)

        plot_legend.extend([column_captions[machine_index] + " " + col])
        print(plot_legend)

        plt.plot(sigmas, durations, color='C%d' % color_index)
        plt.fill_between(sigmas, lower_bound, upper_bound, alpha=0.5, color='C%d' % color_index)

        color_index = color_index + 1

    machine_index = machine_index + 1

plt.xlabel("Sigma / pixels");
plt.ylabel("Processing time / s")
title = operation.split("_")[0]
plt.title(title + " time over sigma")
plt.savefig("../images/compare_machines_kernelsize_processing_time_" + title + ".pdf")
plt.savefig("../images/compare_machines_kernelsize_processing_time_" + title + ".png")
#plt.show()


#############################################33

operation = "Minimum3D";
radii = range(1, 11)


plt.figure(figsize=(9, 7), dpi=80)

plot_legend = []

machine_index = 0

durations = []

color_index = 0;

# plot all tested computers
for pcname in machines:
    print(pcname)
    root_filename = "../../data/benchmarking/kernelsize/" + pcname + "_";

    # plot CPU and GPU
    for col in columns:
        durations = []
        errors = []
        upper_bound = []
        lower_bound = []


        for size in radii:
            filename = root_filename + operation + "_" + str(size) + "_" + str(size) + "_" + col + ".csv"

            measurements = utils.read_measurements_from_file(filename)
            correction_factor = utils.read_correction_factor_from_file(filename) / 1000

            durations.extend([np.median(measurements) * correction_factor])
            upper_bound.extend([np.percentile(measurements, 75) * correction_factor])
            lower_bound.extend([np.percentile(measurements, 25) * correction_factor])

            plt.plot(np.ones(len((measurements))) * size, np.multiply(measurements, correction_factor), 'o',
                 color='C%d' % color_index)


        plot_legend.extend([column_captions[machine_index] + " " + col])
        print(plot_legend)

        plt.plot(radii, durations, color='C%d' % color_index)
        plt.fill_between(radii, lower_bound, upper_bound, alpha=0.5, color='C%d' % color_index)

        color_index = color_index + 1


    machine_index = machine_index + 1

plt.xlabel("Radius / pixels");
plt.ylabel("Processing time / s")
title = operation.split("_")[0]
plt.title(title + " time over radius")
plt.savefig("../images/compare_machines_kernelsize_processing_time_" + title + ".pdf")
plt.savefig("../images/compare_machines_kernelsize_processing_time_" + title + ".png")
#plt.show()

