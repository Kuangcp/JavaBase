package com.github.kuangcp.bio.onechatone;

/**
 * Created by Myth on 2017/4/2
 * 信息的特定内容前后的的特殊字符，称为协议字符
 */
public interface ChatProtocol {

  int SERVER_PORT = 30000;
  //定义协议字符串的长度
  int PROTOCOL_LEN = 2;
  //协议字符串，服务器和客户端交互信息的前后都要加这种特殊字符串
  String MSG_ROUND = "§□";
  String USER_ROUND = "∏∑";
  String LOGIN_SUCCESS = "1";
  String NAME_REP = "-1";
  String PRIVATE_ROUND = "★【";
  String SPLIT_SIGN = "※";

}
