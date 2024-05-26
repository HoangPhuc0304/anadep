package com.hps.anadep.model.enums;

import com.hps.anadep.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GitHubAction {
    PUSH("push"), CHECK_SUITE("check_suite");

    private final String name;

    public static GitHubAction getGitHubAction(String name) {
        for (GitHubAction gitHubAction : GitHubAction.values()) {
            if (gitHubAction.getName().equalsIgnoreCase(name)) {
                return gitHubAction;
            }
        }
        throw new NotFoundException(String.format("Cannot find [%s] GitHub Action", name));
    }
}
