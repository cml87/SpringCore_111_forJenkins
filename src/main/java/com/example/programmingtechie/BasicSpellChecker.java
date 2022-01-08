package com.example.programmingtechie;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
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