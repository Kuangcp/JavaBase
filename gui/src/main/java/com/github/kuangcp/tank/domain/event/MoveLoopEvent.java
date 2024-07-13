package com.github.kuangcp.tank.domain.event;

import com.github.kuangcp.tank.constant.DirectType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-07-13 10:45
 */

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class MoveLoopEvent extends LoopEvent {
    public int x;
    public int y;
    public int direct = DirectType.NONE;   // 初始方向
    public int speed;

    public void move() {
        switch (this.direct) {
            case DirectType.UP:
                y -= this.speed;
                break;
            case DirectType.DOWN:
                y += this.speed;
                break;
            case DirectType.LEFT:
                x -= this.speed;
                break;
            case DirectType.RIGHT:
                x += this.speed;
                break;
        }
    }

}
