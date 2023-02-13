package com.spigen.asos.model;

import javax.management.relation.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleVO {
    private String roleId;
    private String roleName;
    private Role parentRole;
}
