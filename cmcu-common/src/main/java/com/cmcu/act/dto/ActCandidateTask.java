package com.cmcu.act.dto;

import lombok.Data;

import java.util.List;

@Data
public class ActCandidateTask {

    private String name;

    private String preTaskKey;

    private String currentTaskKey;

    private int seq;

    private List<ActCandidateUser> candidateUserList;

    private String loopCharacteristics;

    private String assignee;

    private List<String> candidateUsers;

    private List<String> candidateGroups;
}
