package situation.timoutpool.base;

import lombok.Builder;
import lombok.Data;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-04 23:27
 */
@Data
@Builder
public class Param {
    private int start;
    private int total;
}
