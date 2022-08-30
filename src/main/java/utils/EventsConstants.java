package utils;

import java.net.URI;

public class EventsConstants {

    private EventsConstants() { }

    public static final String CONTENT_TYPE = "application/json";

    //ncb_operations
    public static final String CE_TYPE = "team.nautilus.event.transfer.ncb.topup";
    public static final URI CE_SOURCE = URI.create("/nautilus_core/transfer_ms");
    public static final String CE_SPECVERSION = "1.0";
    public static final String CE_PAYLOADVERSION = "0.0.1";



    //
}
