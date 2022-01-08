package com.example.programmingtechie;

import com.example.programmingtechie.SpellChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
class EmailClient {

    private SpellChecker spellChecker;

    public EmailClient(){}

    EmailClient(SpellChecker spellChecker){
        this.spellChecker = spellChecker;
    }

    public SpellChecker getSpellChecker() {
        return spellChecker;
    }

    @Autowired
    public void setSpellChecker(@Qualifier("advancedSpellChecker") SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
    }

    void sendEmail (String emailMessage){
        spellChecker.checkSpelling(emailMessage);
    }
}
