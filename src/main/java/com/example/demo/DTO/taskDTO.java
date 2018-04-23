package com.example.demo.DTO;

public class taskDTO {
    private Long taskID;

    private Long dsID;

    private int state;

    private String userName;

    private String timestamp;

    public taskDTO(Long taskID, Long dsID, int state, String userName, String timestamp) {
        this.taskID = taskID;
        this.dsID = dsID;
        this.state = state;
        this.userName = userName;
        this.timestamp = timestamp;
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
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
}
