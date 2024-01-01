package com.enigma.loan_app.service;

import com.enigma.loan_app.entity.Role;

public interface RoleService {
    Role getOrSave(Role role);
}
