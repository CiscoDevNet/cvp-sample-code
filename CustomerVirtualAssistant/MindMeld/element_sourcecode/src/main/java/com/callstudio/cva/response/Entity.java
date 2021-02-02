
package com.callstudio.cva.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entity {

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("type")
    @Expose
    private String type;


    @SerializedName("value")
    @Expose
    private Object value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
