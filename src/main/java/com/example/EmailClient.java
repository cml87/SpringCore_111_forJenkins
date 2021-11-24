package com.example;

class EmailClient {
    private SpellChecker spellChecker;

    public EmailClient(){}

    /*EmailClient(SpellChecker spellChecker){
        this.spellChecker = spellChecker;
    }*/

    public SpellChecker getSpellChecker() {
        return spellChecker;
    }

    public void setSpellChecker(SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
    }

    void sendEmail (String emailMessage){
        spellChecker.checkSpelling(emailMessage);
    }
}
