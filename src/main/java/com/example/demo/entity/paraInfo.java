package com.example.demo.entity;

public class paraInfo {
    private Long paraId;

    private Long funcId;

    private String paraType;

    private Integer oriType;

    private String inputContent;

    private Integer funcResult;

    private Integer inputPara;

    public Long getParaId() {
        return paraId;
    }

    public void setParaId(Long paraId) {
        this.paraId = paraId;
    }

    public Long getFuncId() {
        return funcId;
    }

    public void setFuncId(Long funcId) {
        this.funcId = funcId;
    }

    public String getParaType() {
        return paraType;
    }

    public void setParaType(String paraType) {
        this.paraType = paraType == null ? null : paraType.trim();
    }

    public Integer getOriType() {
        return oriType;
    }

    public void setOriType(Integer oriType) {
        this.oriType = oriType;
    }

    public String getInputContent() {
        return inputContent;
    }

    public void setInputContent(String inputContent) {
        this.inputContent = inputContent == null ? null : inputContent.trim();
    }

    public Integer getFuncResult() {
        return funcResult;
    }

    public void setFuncResult(Integer funcResult) {
        this.funcResult = funcResult;
    }

    public Integer getInputPara() {
        return inputPara;
    }

    public void setInputPara(Integer inputPara) {
        this.inputPara = inputPara;
    }
}