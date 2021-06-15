package com.project.service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

class UtilServiceClass {

    private UtilServiceClass() {
    }

    public static String getCurrentPrincipalName(){
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String currentUserName;

       if (!(authentication instanceof AnonymousAuthenticationToken)) {
           currentUserName = authentication.getName();
           return currentUserName;
       }
       throw new RuntimeException("Не удалось получить principal username");
   }

}
