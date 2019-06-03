package net.haesleinhuepf.clij.benchmark;

import net.haesleinhuepf.clij.CLIJ;

public abstract class DefaultBenchmarkableModule implements BenchmarkableModule {

    protected CLIJ clij = null;

    @Override
    public void setCLIJ(CLIJ clij) {
        this.clij = clij;
    }
}
