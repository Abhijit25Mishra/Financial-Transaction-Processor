package org.learn;

import io.javalin.Javalin;

// This class defines the API routes. Its structure is correct.
public class AccountServiceController {
    private final AccountServiceImpl accountServiceImpl;

    // Constructor for dependency injection.
    public AccountServiceController(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    // This method applies the routes to the Javalin app instance.
    public void routes(Javalin app){
        app.get("/", ctx -> ctx.result("Account Service is running!"));
        // These method references now work correctly.
        app.get("/accounts/{id}", accountServiceImpl::getAccountById);
        app.post("/accounts", accountServiceImpl::createAccount);
    }
}