package com.thoughtworks.InjectContainer.enums;

public enum ExecuteStatus {
    FAILED("失败"),
    SUCCESS("成功");

    private String description;

    ExecuteStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
