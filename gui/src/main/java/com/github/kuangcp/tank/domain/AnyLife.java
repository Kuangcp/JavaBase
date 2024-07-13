package com.github.kuangcp.tank.domain;

import lombok.Data;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-07-11 00:39
 */
@Data
public abstract class AnyLife {
    public boolean alive;

    public boolean isDead() {
        return !alive;
    }
}
