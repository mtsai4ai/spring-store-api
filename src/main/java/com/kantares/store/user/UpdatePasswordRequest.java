package com.kantares.store.user;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    String oldPassword;
    String newPassword;
}

