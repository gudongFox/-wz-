package com.cmcu.mcc.ed.entity;

public class EdProjectDetail {
    private Integer id;

    private Integer projectId;

    private String edProjectDetailcol;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getEdProjectDetailcol() {
        return edProjectDetailcol;
    }

    public void setEdProjectDetailcol(String edProjectDetailcol) {
        this.edProjectDetailcol = edProjectDetailcol == null ? null : edProjectDetailcol.trim();
    }
}