package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();

    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;

    }

    public Account accountLogin(Account account){
        return accountDAO.login(account.getUsername(), account.getPassword());

    }

    public Account getAccountbyPassword(String password){
        return accountDAO.getAccountbyPassword(password);
    }

    public Account getAccountbyUsername(String username){
        return accountDAO.getAccountbyUsername(username);
    }

    public Account registerAccount(Account account){
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return null;
        }
        return accountDAO.registerAccount(account);
    }



















}
