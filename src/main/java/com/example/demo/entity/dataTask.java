package com.example.demo.entity;

public class dataTask {
    private Long taskId;

    private Long dsId;

    private Integer stateTmp;

    private String username;

    private Long timestamp;

    private Long threadId;

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
        return stateTmp;
    }

    public void setState(Integer state) {
        this.stateTmp = state;
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

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public dataTask(Long taskId, Long dsId, Integer state, String username, Long timestamp, Long threadId) {
        this.taskId = taskId;
        this.dsId = dsId;
        this.stateTmp = state;
        this.username = username;
        this.timestamp = timestamp;
        this.threadId = threadId;
    }
}