package com.example.demo.entity;

public class resultColumn {
    private Long resultId;

    private Integer columnnum;

    private String columnname;

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Integer getColumnnum() {
        return columnnum;
    }

    public void setColumnnum(Integer columnnum) {
        this.columnnum = columnnum;
    }

    public String getColumnname() {
        return columnname;
    }

    public void setColumnname(String columnname) {
        this.columnname = columnname == null ? null : columnname.trim();
    }
}