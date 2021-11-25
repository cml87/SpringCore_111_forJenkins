package com.example;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
class AdvancedSpellChecker implements SpellChecker {
    @Override
    public void checkSpelling(String emailMessage){
        if (emailMessage!=null){
            System.out.println("Advanced spelling check ...");
            System.out.println("Spell check complete!!");
        } else {
            throw new RuntimeException("An exception occurred while checking the spelling.");
        }
    }
}