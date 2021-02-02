
package com.callstudio.cva.request;

import java.util.List;
import java.util.Set;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetectIntentRequest {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("agent_id")
    @Expose
    private String agentId;
    @SerializedName("allowed_intents")
    @Expose
    private Set<String> allowedIntents = null;
    @SerializedName("dynamic_resource")
    @Expose
    private DynamicResource dynamicResource;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public Set<String> getAllowedIntents() {
        return allowedIntents;
    }

    public void setAllowedIntents(Set<String> allowedIntents) {
        this.allowedIntents = allowedIntents;
    }

    public DynamicResource getDynamicResource() {
        return dynamicResource;
    }

    public void setDynamicResource(DynamicResource dynamicResource) {
        this.dynamicResource = dynamicResource;
    }



    @Override
    public String toString() {
        return "DetectIntentRequest{" +
                "text='" + text + '\'' +
                ", agentId='" + agentId + '\'' +
                ", allowedIntents=" + allowedIntents +
                ", dynamicResource=" + dynamicResource +
                '}';
    }
}
