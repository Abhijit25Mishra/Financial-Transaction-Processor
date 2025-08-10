package org.learn.Controllers;

import io.javalin.Javalin;
import org.learn.Impl.AccountServiceImpl;

public class AccountServiceController {
    private final AccountServiceImpl accountServiceImpl;

    public AccountServiceController(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    public void routes(Javalin app){
        app.get("/", ctx -> ctx.result("Account Service is running!"));
        app.get("/accounts/{id}", accountServiceImpl::getAccountById);
        app.post("/accounts", accountServiceImpl::createAccount);
    }
}