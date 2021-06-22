package com.project.service;

import com.project.entity.Profile;
import com.project.entity.Role;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

class UtilServiceClass {

    private UtilServiceClass() {
    }

    public static String getCurrentPrincipalName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName;

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            return currentUserName;
        }
        throw new RuntimeException("Не удалось получить principal username");
    }

    public static boolean isAdmin(Profile profile) {
        boolean isAdmin = false;

        for (Role role : profile.getUser().getRoles()) {
            if (role.getName().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }

        return isAdmin;
    }

}
