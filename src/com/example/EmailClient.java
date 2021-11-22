package com.example;

class EmailClient {
    private BasicSpellChecker basicSpellChecker;
    //or
    //private AdvancedSpellChecker advancedSpellChecker;


    /*This is tight coupling. Whenever we create a EmailClient object, a BasicSpellChecker object will be
    * created as well, which is not needed as we only need the functionality (method checkSpelling) this class
    * offers. Moreover, if in the future we want to change the spell checker used, we'll need to change the code */

    EmailClient(){
        this.basicSpellChecker = new BasicSpellChecker();
        //or
        //this.advancedSpellChecker = new AdvancedSpellChecker()
    }

    void sendEmail (String emailMessage){
        basicSpellChecker.checkSpelling(emailMessage);
    }

}
