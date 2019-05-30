package netty.timeServer;

/**
 * @author kuangcp on 2019-04-23 10:24 AM
 */
public interface Command {

  String QUERY_TIME = "QUERY TIME ORDER";
  String STOP_SERVER = "STOP";
}
