package com.strelnikov.issuetracker.entity;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum ProjectRoleType implements Role {
    ADMIN, MANAGER, VIEWER;

    private final Set<Role> children = new HashSet<>();

    /*
    assign proper children to each role
     */
    static {
        ADMIN.children.add(MANAGER);
        MANAGER.children.addAll(List.of(ProjectRoleType.VIEWER,IssueRoleType.ASSIGNEE, IssueRoleType.REPORTER));
        VIEWER.children.add(IssueRoleType.VIEWER);
    }

    @Override
    public boolean includes(Role role) {
        return this.equals(role) || children.stream().anyMatch(r -> r.includes(role));
    }

    /*
    lets reference enum values by @ProjectRole.{ROLE}
     */
    @Component("ProjectRole")
    static class SpringComponent {
        private final ProjectRoleType ADMIN = ProjectRoleType.ADMIN;
        private final ProjectRoleType MANAGER = ProjectRoleType.MANAGER;
        private final ProjectRoleType VIEWER = ProjectRoleType.VIEWER;

        public ProjectRoleType getADMIN() {
            return ADMIN;
        }

        public ProjectRoleType getMANAGER() {
            return MANAGER;
        }

        public ProjectRoleType getVIEWER() {
            return VIEWER;
        }
    }
}
