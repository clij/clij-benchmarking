
import csv

# expects a CSV file with a single column
# Ignores first line (headline) and second line because of warm-up effects
#
def read_measurements_from_file(filename):
    with open(filename, newline='') as csvfile:
        reader = csv.reader(csvfile, delimiter=',')

        linecount = 0;
        sum = 0

        list = []

        for row in reader:
            # ignore line 0 because it's the text headline
            # ignore line 1 because it's a sum of processing time and compilation time
            if (linecount > 1):
                list.extend([float(row[0])])

            linecount = linecount + 1

        return list
    return []

# Expects a CSV file with only a single column
# ignores first line (headline) and returns second line
#
def read_first_measurement_from_file(filename):
    with open(filename, newline='') as csvfile:
        reader = csv.reader(csvfile, delimiter=',')

        linecount = 0;
        sum = 0

        list = []

        for row in reader:
            if (linecount == 1):
                list.extend([float(row[0])])

            linecount = linecount + 1

        return list
    return []

# Determines if the headline of a single-columned CSV file contains
# msec or ns. It returns a factor which allows to get the content of the table in msec.
#
def read_correction_factor_from_file(filename):
    with open(filename, newline='') as csvfile:
        reader = csv.reader(csvfile, delimiter=',')

        for row in reader:
            if (row[0] == "Time_in_msec"):
                return 1
            elif (row[0] == "Time_in_ns"):
                return 1 / 1000 / 1000
            return 0

# expects a CSV file with a single column
# Ignores first line (headline)
#
def read_all_measurements_from_file(filename):
    with open(filename, newline='') as csvfile:
        reader = csv.reader(csvfile, delimiter=',')

        linecount = 0;
        sum = 0

        list = []

        for row in reader:
            # ignore line 0 because it's the text headline
            if (linecount > 0):
                list.extend([float(row[0])])

            linecount = linecount + 1

        return list
    return []


# adapted from https://stackoverflow.com/questions/16399279/bland-altman-plot-in-python
import matplotlib.pyplot as plt
import numpy as np

# Draws a Bland-Altman plot
#
def bland_altman_plot(data1, data2, *args, **kwargs):
    data1     = np.asarray(data1)
    data2     = np.asarray(data2)
    mean      = np.mean([data1, data2], axis=0)
    diff      = data1 - data2                   # Difference between data1 and data2
    md        = np.mean(diff)                   # Mean of the difference
    sd        = np.std(diff, axis=0)            # Standard deviation of the difference

    plt.figure(figsize=(10, 10), dpi=80)
    plt.scatter(mean, diff, *args, **kwargs)
    plt.axhline(md,           color='gray', linestyle='--')
    plt.axhline(md + 1.96*sd, color='gray', linestyle='--')
    plt.axhline(md - 1.96*sd, color='gray', linestyle='--')


