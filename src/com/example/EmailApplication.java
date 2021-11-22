package com.example;

public class EmailApplication {
    public static void main(String[] args) {
      EmailClient emailClient = new EmailClient(new BasicSpellChecker());
      //or
      //EmailClient emailClient1 = new EmailClient(new AdvancedSpellChecker());
      emailClient.sendEmail("Hey, this is my first email message");
      emailClient.sendEmail("Hey, this is my second email message");
    }
}
