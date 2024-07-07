package com.github.kuangcp.tank.ws.msg;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-07-07 11:45
 */
@Data
public class MsgPack {

    /**
     * @see com.github.kuangcp.tank.constant.DirectType
     */
    @JsonAlias("d")
    private Integer direct;

    @JsonAlias("s")
    private Boolean shot;
}
