package com.calebtrevino.tallystacker.models.sofascore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Category {

    @SerializedName("id") @Expose private Integer id;
    @SerializedName("name") @Expose private String name;
    @SerializedName("slug") @Expose private String slug;
    @SerializedName("priority") @Expose private Integer priority;
    @SerializedName("mcc") @Expose private List<Integer> mcc = null;
    @SerializedName("flag") @Expose private String flag;

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

    public List<Integer> getMcc() {
        return mcc;
    }

    public void setMcc(List<Integer> mcc) {
        this.mcc = mcc;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
