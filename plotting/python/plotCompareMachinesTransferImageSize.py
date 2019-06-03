
data_folder = "../../data/benchmarking/imagesize/"

operations = [
    "Transfer3DFrom"
]

machines = ["myers-pc-21", "myers-pc-22"];
column_captions = ["Laptop", "Workstation"];
sizes = [1, 2, 4, 8, 16, 32, 64]
columns = ["imagej", "clij"]
titles = ["Data transfer to GPU", "Data transfer from GPU"]


font = {'family' : 'normal',
        'size'   : 22}





import numpy as np
import matplotlib
import matplotlib.pyplot as plt

import utils



import seaborn as sns
sns.set_style('whitegrid')


matplotlib.rc('font', **font)


plot_legend = []



#plt.figure(figsize=(6, 15), dpi=80)

col_index = 0

for col in columns:

    data = []

    for operation in operations:

        fig1, ax1 = plt.subplots(figsize=(8, 6), dpi=80)

        for pcname in machines:

            root_filename = data_folder + pcname + "_";

            data_megabyte_per_millisecond = []

            for size in sizes:
                filename = root_filename + operation + "_" + str(size) + "_" + col  + ".csv"

                measurements = utils.read_measurements_from_file(filename)
                correction_factor = utils.read_correction_factor_from_file(filename) / 1000 * 1024

                data_megabyte_per_millisecond.extend(np.multiply(measurements, correction_factor / size))

            data.extend([data_megabyte_per_millisecond])

    # print(spread)

    ax1.boxplot(data)
    ax1.set_xticklabels(column_captions)

    plt.ylabel("Transfer rate GB / s")
    plt.yticks(np.arange(0, 4, 0.5))
    plt.title(titles[col_index])
    plt.savefig("../images/compare_machines_imagesize_transfertime_" + col +  ".png")
    #plt.show()

    col_index = col_index + 1

