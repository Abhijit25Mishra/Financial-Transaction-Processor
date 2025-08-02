package org.learn;

import io.javalin.Javalin;

public class AccountService {

    public static void main(String args[]) {
        AccountServiceImpl service = new AccountServiceImpl();
        AccountServiceController controller = new AccountServiceController(service);

        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new io.javalin.json.JavalinJackson());
        });

        controller.routes(app);

        app.start(8080);
        System.out.println("Account Service is running on port 8080");
    }
}