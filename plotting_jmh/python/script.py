import argparse
from glob import glob
import pandas
import matplotlib.pyplot as plt
import seaborn as sns
import itertools
import numpy as np

255, 127, 14
colors_other=np.array([[31,119,180], [44,160,44], [127,0,255], [0,0,255]])/255
colors_clij=np.array([[255,127,14], [214,39,40], [204,0,102], [204,204,0]])/255

markers_other=['o', 'x', 'o', 'x']
markers_clij=['o', 'x', 'o', 'x']

def get_extended_dataframe(path_to_file="./foo.csv"):
    df = pandas.read_csv(path_to_file)
    df.insert(loc=0, column="Filename", value=path_to_file.split("/")[-1])  
    return df


def create_master_dataframe(file_names="./*"):
    """a function to create a master dataframe with filenames and there contents"""

    file_list = glob(file_names)
    frame_list=[]

    for f in file_list:
        frame_list.append(get_extended_dataframe(f))

    return pandas.concat(frame_list, axis=0, sort=False)

def cond_and(list_of_conds):
    res = [True]*len(list_of_conds[0])
    for cond in list_of_conds:
        res*=cond
    return res

def cond_or(list_of_conds):
    res = [True]*len(list_of_conds[0])
    for cond in list_of_conds:
        res+=cond
    return res

def plot_errorbar(x, y, e, benchmark, param, color, marker):
    plt.errorbar(x,y,e, marker=marker, label=benchmark, color=color, markerfacecolor='None', markersize=10)
    plt.fill_between(x, y-e, y+e, alpha=0.5, color=color)
    

def process_benchmark_versus_param(data_frame, free_param_name, legend_entry, color, marker):

    correction_factor = 1e-9 # convert to seconds

    # df_bm = data_frame[data_frame["Benchmark"]==benchmark]
    # print(df_bm)
    x = data_frame[free_param_name]
    y = data_frame["Score"]*correction_factor
    e = data_frame["Score Error (99.9%)"]*correction_factor
   

    plot_errorbar(x, y, e, legend_entry, free_param_name.replace("Param: ", ""), color, marker)


def process_one_param(data_frame, param_dict, free_param_name, name, benchmarks_to_process, save_dir, verbose=0):

    

    if verbose:
        print("------> processing param %s..."%free_param_name)

    # list of figures to return
    figures = []

    # creating the param dictionary without the free params     
    param_dict_iter = param_dict.copy()
    del param_dict_iter[free_param_name]
    

    param_names = list(param_dict_iter.keys())
    benchmarks = pandas.unique(data_frame["Benchmark"])
    machines = pandas.unique(data_frame["Filename"].str.split(".").str.get(0).str.split("_").str.get(-2))

    for params in itertools.product(*param_dict_iter.values()):
        fig = plt.figure(figsize=(9, 7), dpi=80)
        ax = fig.add_subplot(111)
        # plt.clf()

        if verbose:
            print("-----> other param values " + str(params))

        df = data_frame
        suff=''

        # build the right data frame
        for i,p in enumerate(params):
            if np.isnan(p):
                df = df[np.isnan(df[param_names[i]])]
            else:
                df = df[df[param_names[i]]==p]
            suff+="_%s_"%param_names[i].replace("Param: ", "") + str(p)

        id_clij=0
        id_other=0

        for m in machines:
            d = df[df["Filename"].str.split(".").str.get(0).str.split("_").str.get(-2)==m]

        # d = d
            for j, b in enumerate(benchmarks):

                dl = d[d["Benchmark"]==b]

                c=None
                bs = b.split(".")[-1]
                # print(b)
            
                if (("clij" in bs) or ("CLIJ" in bs)):
                    c = colors_clij[id_clij%4]
                    mark = markers_clij[id_clij%4]
                    id_clij+=1
                else:
                    c = colors_other[id_other%4]
                    mark = markers_other[id_other%4]
                    id_other+=1

                print(d)

                benchmark_prefix = "net.haesleinhuepf.clij.benchmark.jmh."
                benchmark_short = b.replace(benchmark_prefix, "")   

                process_benchmark_versus_param(dl, free_param_name, m+"_"+benchmark_short, color=c, marker=mark)

        title = name.split("_")[-1]
        plt.title(title)

        x_axis_label = free_param_name.replace("Param: ","")
        if (x_axis_label == "size"):
            x_axis_label = "Size / MB"
        if (title == 'GaussianBlur2D' or title == 'GaussianBlur3D'):
            if (x_axis_label == "radius"):
                x_axis_label = "sigma"

        plt.xlabel(x_axis_label)
        plt.ylabel("Processing time / s")

        plt.tight_layout()
        if (save_dir != None):
            path = "%s/%s.pdf"%(save_dir.strip("/"), name+suff)
            plt.savefig(path)
            path = "%s/%s.png" % (save_dir.strip("/"), name + suff)
            plt.savefig(path)

        # fig.savefig(path)


        plt.legend(bbox_to_anchor=(1.0, -0.15,), loc=1,
           ncol=len(benchmarks), fontsize=10, columnspacing=1)
        plt.tight_layout()
        if (save_dir != None):
            path = "%s/%s_legend.png"%(save_dir.strip("/"), name+suff)
            plt.savefig(path)   
            # fig.savefig(path)
        figures.append(fig)
        plt.show()
    plt.close()
        

    
    return figures


def plt_setup():
    sns.set_style('whitegrid')
    font = {'family' : 'normal', 'size'   : 22}
    plt.rc('font', **font)
    # plt.figure(figsize=(15, 15), dpi=80)


def process_frame(data_frame, name=None, save_dir=None, verbose=0):
    plt_setup()
    figures=[]

    if verbose:
        print("----- processing a new frame -----")

    # identifying operations
    operations = pandas.unique(data_frame["Benchmark"].str.split(".").str.get(-2))
    if verbose:
        print("----- detected operations:")
        for o in operations:
            print(o)

    # identifying machines
    machines = pandas.unique(data_frame["Filename"].str.split(".").str.get(0).str.split("_").str.get(-2))
    if verbose:
        print("----- detected machines:")
        for m in machines:
            print(m)        

    # identifying benchmarks
    benchmarks = pandas.unique(data_frame["Benchmark"].str.split(".").str.get(-1))
    if verbose:
        print("----- detected benchmarks:")
        for b in benchmarks:
            print(b)

    # identifying params
    params = [col for col in data_frame.columns if col.startswith("Param")]
    if verbose:
        print("----- detected params:")
        for p in params:
            print(p)

    # building a param_dict
    param_dict={}
    for param in params:
         x = pandas.unique(data_frame[param])
         # x = x[~np.isnan(x)]
         param_dict[param] = x

    if verbose:
        for param in param_dict:
            print("----- parameter %s has the following values:"%param)
            print(param_dict[param])

    if (name == None):
        name = data_frame.iloc[0,0].split(".")[0]


    for o in operations:
        if verbose:
            print("----- processing operation: %s -----"%o)
        df = data_frame[data_frame["Benchmark"].str.split(".").str.get(-2) == o]
        # print(df)
        for free_param_name in list(param_dict.keys()):
            figures.extend(process_one_param(df, param_dict, free_param_name, name=name+"_"+o, 
                benchmarks_to_process=benchmarks, save_dir=save_dir, verbose=verbose))

    # plt.close()
    return figures 



def process_csv(path_to_csv="./foo.csv", path_to_save_dir="./", benchmarks_to_process="", verbose=0):
    """take a csv file and produce plots"""

    # read the .csv file
    data_frame_raw = get_extended_dataframe(path_to_csv)
    if verbose:
        print("----> processing file %s..."%path_to_csv)

    # identifying benchmarks
    benchmarks = pandas.unique(data_frame_raw["Benchmark"])
    if verbose:
        print("the file contains the following benchmarks:")
        for b in benchmarks:
            print(b)

    if (benchmarks_to_process == None):
        benchmarks_to_process = [b.split(".")[-1] for b in benchmarks]

    # print("------------------" )
    # print(benchmarks_to_process)

    cond_bm = [data_frame_raw["Benchmark"].str.split(".").str.get(-1).str.contains(b) for b in benchmarks_to_process]


    cond = cond_or(cond_bm)
    data_frame = data_frame_raw[cond]
    name = path_to_csv.split("/")[-1].split(".")[0]

    print("------------------" )
    print(data_frame)

    process_frame(data_frame, save_dir=path_to_save_dir, verbose=verbose)

def process_files(in_files, out_dir, benchmarks, verbose):

    # logging
    if verbose:
        print("--- files to process ---")
        for f in in_files:
            print(f)

        print("--- benchmarks to process ---")
        if (benchmarks != None):            
            for b in benchmarks:
                print(b)
        else:
            print("not specified")

    # initialize plot parameters
    

    # process
    for f in in_files:
        process_csv(f.replace("\\", "/"), out_dir, benchmarks, verbose)

            
if __name__ =="__main__":

    # define arguments
    parser = argparse.ArgumentParser()
    parser.add_argument("--in_files", help="input data file path", type=str)
    parser.add_argument("--out_dir", help="output directory path for plots", type=str)
    parser.add_argument("--benchmarks", nargs="+", help="benchmarks to test")
    parser.add_argument("--verbose", help="verbose mode", type=int)

    # default values
    benchmarks = None

    # parse arguments
    args = parser.parse_args()
    in_files_ = args.in_files
    in_files = glob(in_files_)
    out_dir = args.out_dir
    verbose = args.verbose
    if (args.benchmarks!=None):
        benchmarks = args.benchmarks

    # run the processing
    process_files(in_files, out_dir, benchmarks, verbose)

