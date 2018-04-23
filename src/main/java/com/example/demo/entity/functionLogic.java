package com.example.demo.entity;

public class functionLogic {
    private Long functionId;

    private Long dsId;

    private String classname;

    private String methodname;

    private Integer paranum;

    private Integer executesequence;

    private String resulttype;

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public Long getDsId() {
        return dsId;
    }

    public void setDsId(Long dsId) {
        this.dsId = dsId;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname == null ? null : classname.trim();
    }

    public String getMethodname() {
        return methodname;
    }

    public void setMethodname(String methodname) {
        this.methodname = methodname == null ? null : methodname.trim();
    }

    public Integer getParanum() {
        return paranum;
    }

    public void setParanum(Integer paranum) {
        this.paranum = paranum;
    }

    public Integer getExecutesequence() {
        return executesequence;
    }

    public void setExecutesequence(Integer executesequence) {
        this.executesequence = executesequence;
    }

    public String getResulttype() {
        return resulttype;
    }

    public void setResulttype(String resulttype) {
        this.resulttype = resulttype == null ? null : resulttype.trim();
    }
}