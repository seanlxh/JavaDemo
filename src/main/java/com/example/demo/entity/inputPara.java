package com.example.demo.entity;

public class inputPara {
    private Integer typeId;

    private String name;

    private String type;

    private Integer sequence;

    private String des;

    public inputPara(Integer typeId, String name, String type, Integer sequence, String des) {
        this.typeId = typeId;
        this.name = name;
        this.type = type;
        this.sequence = sequence;
        this.des = des;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des == null ? null : des.trim();
    }
}