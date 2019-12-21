package application;

import org.springframework.boot.CommandLineRunner;

public class MakeItStandService implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        main(args);
    }

    public static void main(String[] args) {
        System.out.println("HELLO WORLD");
    }
}
