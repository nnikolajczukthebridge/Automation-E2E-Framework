package endpoints;

public class Accounts {
    public static String host = "";
    public static String accountApi = "/accounts/";
    /*----- GET -----*/
    public static String getAccountById = "";
    /*----- POST -----*/
    public static String createNewAccount = "";

    public static void buildEndPoints(){
        getAccountById = host.concat(accountApi).concat("%d");
        createNewAccount = host.concat(accountApi);
    }
}
