package endpoints;

public class Transfers {

    public static String host = "";

    /*----- Transfer Endpoints -----*/
    private static String transferApi = "/transfers/";
    /*----- Transfer GET  -----*/
    public static String getTransferById = "";
    public static String getTransferStatusById = "";
    /*----- Transfer POST -----*/
    public static String postCreateTransferDev = "";


    public static void buildEndPoints(){
        getTransferById = host.concat(transferApi).concat("%d");
        getTransferStatusById = host.concat(transferApi).concat("transferstatus/%d");
        postCreateTransferDev = host.concat(transferApi);
    }

}
