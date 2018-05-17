package com.example.demo.DTO;

public class taskDTO {
    private Long taskID;

    private Long dsID;

    private int stateTmp;

    private String userName;

    private String timestamp;

    private Long threadId;

    public taskDTO(Long taskID, Long dsID, int stateTmp, String userName, String timestamp,Long threadId) {
        this.taskID = taskID;
        this.dsID = dsID;
        this.stateTmp = stateTmp;
        this.userName = userName;
        this.timestamp = timestamp;
        this.threadId = threadId;
    }

    public Long getTaskID() {
        return taskID;
    }

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
    }

    public Long getDsID() {
        return dsID;
    }

    public void setDsID(Long dsID) {
        this.dsID = dsID;
    }

    public int getState() {
        return stateTmp;
    }

    public void setState(int state) {
        this.stateTmp = state;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }
}
