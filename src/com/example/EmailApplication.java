package com.example;

public class EmailApplication {

    public static void main(String[] args) {
      EmailClient emailClient = new EmailClient();

      emailClient.sendEmail("Hey, this is my first email message");
      emailClient.sendEmail("Hey, this is my second email message");


    }
}
