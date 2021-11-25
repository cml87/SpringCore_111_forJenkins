package com.example;

import org.springframework.beans.factory.annotation.Autowired;
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
    public void setSpellChecker(BasicSpellChecker spellChecker) {
        this.spellChecker = spellChecker;
    }

    void sendEmail (String emailMessage){
        spellChecker.checkSpelling(emailMessage);
    }
}
