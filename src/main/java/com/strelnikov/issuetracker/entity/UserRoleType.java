package com.strelnikov.issuetracker.entity;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

public enum UserRoleType implements Role{
    ROOT;

    private final Set<Role> children = new HashSet<>();


    @Override
    public boolean includes(Role role) {
        return this.equals(role);
    }

    @Component("UserRole")
    static class springComponent {
        private final UserRoleType rootRoleType = UserRoleType.ROOT;

        public UserRoleType getRootRoleType() {
            return rootRoleType;
        }
    }
}
