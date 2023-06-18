package com.strelnikov.issuetracker;


import com.strelnikov.issuetracker.entity.IssueRoleType;
import com.strelnikov.issuetracker.entity.ProjectRoleType;
import com.strelnikov.issuetracker.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class RoleTest {

    @Test
    void shouldNotThrowStackOverflowException() {
        final var roots = Role.roots();
        final var existingRoles = Stream.concat(
                stream(ProjectRoleType.values()),
                stream(IssueRoleType.values())
        ).toList();
        assertDoesNotThrow(() -> {
            for (Role root : roots) {
                for (var roleToCheck : existingRoles) {
                    root.includes(roleToCheck);
                }
            }
        });
    }

    @ParameterizedTest
    @MethodSource("provideArgs")
    void shouldIncludeOrNotTheGivenRoles(Role root, Set<Role> rolesToCheck, boolean shouldInclude) {
        for (Role role : rolesToCheck) {
            assertEquals(shouldInclude, root.includes(role));
        }
    }

    private static Stream<Arguments> provideArgs() {
        return Stream.of(
                arguments(
                        ProjectRoleType.ADMIN,
                        Stream.concat(
                                stream(IssueRoleType.values()),
                                stream(ProjectRoleType.values())
                        ).collect(Collectors.toSet()),
                        true
                ),
                arguments(
                        ProjectRoleType.MANAGER,
                        Set.of(ProjectRoleType.MANAGER, IssueRoleType.REPORTER, IssueRoleType.ASSIGNEE, IssueRoleType.VIEWER),
                        true
                ),
                arguments(
                        IssueRoleType.VIEWER,
                        Set.of(IssueRoleType.ASSIGNEE),
                        false
                ),
                arguments(
                        ProjectRoleType.MANAGER,
                        Set.of(ProjectRoleType.ADMIN),
                        false
                        )

        );
    }
}
