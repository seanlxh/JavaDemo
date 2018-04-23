package com.example.demo.entity;

public class dataTask {
    private Long taskId;

    private Long dsId;

    private Integer state;

    private String username;

    private Long timestamp;

    public dataTask(Long taskId, Long dsId, Integer state, String username, Long timestamp) {
        this.taskId = taskId;
        this.dsId = dsId;
        this.state = state;
        this.username = username;
        this.timestamp = timestamp;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getDsId() {
        return dsId;
    }

    public void setDsId(Long dsId) {
        this.dsId = dsId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}