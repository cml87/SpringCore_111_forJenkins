package com.example;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class EmailApplication {
    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

        AdvancedSpellChecker advancedSpellChecker1 = applicationContext.getBean("advancedSpellChecker",AdvancedSpellChecker.class);
       // BasicSpellChecker   basicSpellChecker1 = applicationContext.getBean("basicSpellChecker",BasicSpellChecker.class);

        System.out.println("*************************");
        System.out.println("***... beans usage ...***");
        System.out.println("*************************");


        ((AnnotationConfigApplicationContext)applicationContext).registerShutdownHook();

    }
}
