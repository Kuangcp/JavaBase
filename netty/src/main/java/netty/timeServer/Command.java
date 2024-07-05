package netty.timeServer;

/**
 * @author kuangcp on 2019-04-23 10:24 AM
 */
public interface Command {

    String QUERY_TIME = "QUERY TIME ORDER";
    String STOP_SERVER = "STOP";

    String server_close = "s_close";
    String server_close_ack = "ack_s_close";

    String client_close = "c_close";
    String client_close_ack = "ack_c_close";
}
