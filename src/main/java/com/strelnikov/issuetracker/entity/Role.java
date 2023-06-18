package com.strelnikov.issuetracker.entity;

import java.util.Set;

public interface Role {

    /*
    is supplied role equals to the current one or contains it in its children
     */
    boolean includes(Role role);

    /*
    return the root of the hierarchy
     */
    static Set<Role> roots() {
        return Set.of(ProjectRoleType.ADMIN);
    }
}
