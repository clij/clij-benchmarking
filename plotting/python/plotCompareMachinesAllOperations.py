# Script for visualising speedups of measured CLIJ/ImageJ operations
#
# Author: Robert Haase
# May 2019
#
####################################################################

# configuration

data_folder = "../../data/benchmarking/all/"

operations = [
    "AddImagesWeighted2D",
    "AddImagesWeighted3D",
    "AddScalar2D_1.0",
    "AddScalar3D_1.0",
    "AutoThreshold2D",
    "AutoThreshold3D",
    "BinaryAnd2D",
    "BinaryAnd3D",
    "Erode2D",
    "Erode3D",
    "Flip2D",
    "Flip3D",
    "GaussianBlur2D_2.0",
    "GaussianBlur3D_2.0",
    "MaximumZProjection",
    "Mean2D_2",
    "Mean3D_2",
    "Median2D_2",
    "Median3D_2",
    "Minimum2D_2",
    "Minimum3D_2",
    "MultiplyScalar2D_2.0",
    "MultiplyScalar3D_2.0",
    "RadialReslice",
    "Reslice3D",
    "Rotate2D",
    "Rotate3D",
    "Threshold2D",
    "Threshold3D"
]

columns = ["imagej", "clij"]

machines = ["myers-pc-21", "myers-pc-22"]

column_captions = ["Laptop GPU", "Workstation CPU", "Workstation GPU"]

font = {'family' : 'normal',
        'size'   : 22}




import numpy as np
import matplotlib
import matplotlib.pyplot as plt
import utils


matplotlib.rc('font', **font)

reference_machine_index = 0



durations = []
speedups = []
machine_names = []

machine_count = 0

# go through tested computers
for pcname in machines:

    root_filename = data_folder + pcname + "_"

    durations.extend([[], []])

    # go through list of tested operations
    for operation in operations:
        med = [0, 0]
        i = 0

        # go through CPU/GPU
        for col in columns:
            filename = root_filename + operation + "_" + col + ".csv"

            measurements = utils.read_measurements_from_file(filename)
            correction_factor = utils.read_correction_factor_from_file(filename)


            median = np.median(measurements) * correction_factor

            med[i] = median
            durations[machine_count * 2 + i].extend([median])
            i = i + 1

    # ignore comparison of the first CPU wit itself
    if(len(speedups) > 0):
        speedups.extend([np.divide(durations[reference_machine_index], durations[machine_count * 2 + 0])])
        machine_names.extend([pcname + "_" + columns[0]])

    speedups.extend([np.divide(durations[reference_machine_index], durations[machine_count * 2 + 1])])
    machine_names.extend([pcname + "_" + columns[1]])

    machine_count = machine_count + 1


columns = column_captions

colours = np.zeros([len(speedups), len(durations[0])])

# collect colours for the resulting table
for j in range(0, len(speedups)):
    for i in range(0, len(speedups[j])):
        colours[j][i] = np.log(speedups[j][i])

fig, ax = plt.subplots(figsize=(18,12))

# show the resulting table colours as image
im = ax.imshow(np.transpose(colours), aspect=0.2, cmap='Greens') #Wistia summer

# put text on the axes
ax.set_xticks(np.arange(len(columns)))
ax.set_yticks(np.arange(len(operations)))
ax.set_xticklabels(columns)

operation_labels = []
for operation in operations:
    if (operation == "workflow"):
        operation_labels.extend(["Example workflow"])
    else:
        operation_labels.extend([operation.split("_")[0]])

ax.set_yticklabels(operation_labels)
plt.setp(ax.get_xticklabels(), rotation=45, ha="right", rotation_mode="anchor")

# write text in each pixel of the coloured table
for i in range(len(operations)):
    for j in range(len(columns)):
        # if speedup is very high, change text colour to make it better readable
        if (speedups[j][i] > 30):
            text = ax.text(j, i, np.round(speedups[j][i], decimals=1), ha="center", va="center", color="white")
        else:
            text = ax.text(j, i, np.round(speedups[j][i], decimals=1), ha="center", va="center", color="black")

ax.set_title("Relative speedup compared to Notebook CPU")
fig.tight_layout()

plt.savefig("../images/compare_machines_all_operations.pdf")
plt.savefig("../images/compare_machines_all_operations.png")
# plt.show()





