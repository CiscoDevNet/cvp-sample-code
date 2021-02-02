
package com.callstudio.cva.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gazetteers {

    @SerializedName("accounttype")
    @Expose
    private String accounttype;

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

}
