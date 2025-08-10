package org.learn;

import io.javalin.Javalin;
import org.learn.Controllers.AccountServiceController;
import org.learn.Impl.AccountServiceImpl;

public class AccountService {

    public static void main(String args[]) {
        AccountServiceImpl service = new AccountServiceImpl();
        AccountServiceController accountServiceController = new AccountServiceController(service);

        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new io.javalin.json.JavalinJackson());
        });

        accountServiceController.routes(app);


        app.start(8080);
        System.out.println("Account Service is running on port 8080");
    }
}