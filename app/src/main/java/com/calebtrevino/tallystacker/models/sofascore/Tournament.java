
package com.calebtrevino.tallystacker.models.sofascore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tournament {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("priority")
    @Expose
    private Integer priority;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("uniqueId")
    @Expose
    private Integer uniqueId;
    @SerializedName("uniqueName")
    @Expose
    private String uniqueName;
    @SerializedName("hasEventPlayerStatistics")
    @Expose
    private Boolean hasEventPlayerStatistics;
    @SerializedName("hasEventPlayerHeatMap")
    @Expose
    private Boolean hasEventPlayerHeatMap;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Integer uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public Boolean getHasEventPlayerStatistics() {
        return hasEventPlayerStatistics;
    }

    public void setHasEventPlayerStatistics(Boolean hasEventPlayerStatistics) {
        this.hasEventPlayerStatistics = hasEventPlayerStatistics;
    }

    public Boolean getHasEventPlayerHeatMap() {
        return hasEventPlayerHeatMap;
    }

    public void setHasEventPlayerHeatMap(Boolean hasEventPlayerHeatMap) {
        this.hasEventPlayerHeatMap = hasEventPlayerHeatMap;
    }

}
