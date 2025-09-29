package com.gl.project.entities;

public enum UserGroups {
    ADMIN ("admin"),
    USER ("user");

    private String groups;
    UserGroups(String groups) {
        this.groups = groups;
    }

    public String getGroups() {
        return groups;
    }
}
