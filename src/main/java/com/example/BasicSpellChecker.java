package com.example;

import org.springframework.stereotype.Component;

@Component
class BasicSpellChecker implements SpellChecker {
    @Override
    public void checkSpelling(String emailMessage){
        if (emailMessage!=null){
            System.out.println("Basic spelling check ...");
            System.out.println("Spell check complete!!");
        } else {
            throw new RuntimeException("An exception occurred while checking the spelling.");
        }
    }
}