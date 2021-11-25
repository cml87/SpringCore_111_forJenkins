package com.example;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class EmailApplication {
    public static void main(String[] args) {
//      EmailClient emailClient = new EmailClient(new BasicSpellChecker());
//      //or
//      //EmailClient emailClient1 = new EmailClient(new AdvancedSpellChecker());
//      emailClient.sendEmail("Hey, this is my first email message");
//      emailClient.sendEmail("Hey, this is my second email message");

        //ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        EmailClient emailClient = applicationContext.getBean("emailClient",EmailClient.class);

        emailClient.sendEmail("Hey, this is my first email message");
        emailClient.sendEmail("Hey, this is my second email message");

        AdvancedSpellChecker advancedSpellChecker1 = applicationContext.getBean("advancedSpellChecker",AdvancedSpellChecker.class);
        AdvancedSpellChecker advancedSpellChecker2 = applicationContext.getBean("advancedSpellChecker",AdvancedSpellChecker.class);

        System.out.println("... the same memory address should be printed as this beans has Singleton scope");
        System.out.println("advancedSpellChecker1 is: "+advancedSpellChecker1);
        System.out.println("advancedSpellChecker2 is: "+advancedSpellChecker2);

        ((AnnotationConfigApplicationContext)applicationContext).registerShutdownHook();

    }
}
