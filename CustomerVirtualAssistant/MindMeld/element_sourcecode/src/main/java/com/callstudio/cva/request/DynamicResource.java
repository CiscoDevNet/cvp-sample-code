
package com.callstudio.cva.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DynamicResource {

    @SerializedName("gazetteers")
    @Expose
    private Gazetteers gazetteers;

    public Gazetteers getGazetteers() {
        return gazetteers;
    }

    public void setGazetteers(Gazetteers gazetteers) {
        this.gazetteers = gazetteers;
    }

}
