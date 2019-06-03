# Script for plotting transfer time between CPU  and GPU
#
# Author: Robert Haase, MPI CBG
# May 2019
#############################################3

import utils
import numpy as np

sizes = [1,2,4,8,16,32,64]

measurements_notebook_push = []
measurements_notebook_pull = []
measurements_workstation_push = []
measurements_workstation_pull = []

for size in sizes:
    filename_transfer_time_notebook_push = "../../data/benchmarking/imagesize/myers-pc-21_Transfer3DFrom_" + str(size) + "_imagej.csv"
    filename_transfer_time_notebook_pull = "../../data/benchmarking/imagesize/myers-pc-21_Transfer3DFrom_" + str(size) + "_clij.csv"
    filename_transfer_time_workstation_push = "../../data/benchmarking/imagesize/myers-pc-22_Transfer3DFrom_" + str(size) + "_imagej.csv"
    filename_transfer_time_workstation_pull = "../../data/benchmarking/imagesize/myers-pc-22_Transfer3DFrom_" + str(size) + "_clij.csv"

    correction_factor = utils.read_correction_factor_from_file(filename_transfer_time_notebook_push) / 1000 * 1024

    measurements_notebook_push.extend(np.divide(utils.read_measurements_from_file(filename_transfer_time_notebook_push), size))
    measurements_notebook_pull.extend(np.divide(utils.read_measurements_from_file(filename_transfer_time_notebook_pull), size))
    measurements_workstation_push.extend(np.divide(utils.read_measurements_from_file(filename_transfer_time_workstation_push), size))
    measurements_workstation_pull.extend(np.divide(utils.read_measurements_from_file(filename_transfer_time_workstation_pull), size))

print("notebook push " +
      str(np.median(measurements_notebook_push) * correction_factor) + " +/- " +
      str(np.std(measurements_notebook_push) * correction_factor))

print("notebook pull " +
      str(np.median(measurements_notebook_pull) * correction_factor) + " +/- " +
      str(np.std(measurements_notebook_pull) * correction_factor))

print("workstation push " +
      str(np.median(measurements_workstation_push) * correction_factor) + " +/- " +
      str(np.std(measurements_workstation_push) * correction_factor))

print("workstation pull " +
      str(np.median(measurements_workstation_pull) * correction_factor) + " +/- " +
      str(np.std(measurements_workstation_pull) * correction_factor))







