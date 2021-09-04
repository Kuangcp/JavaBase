package situation.timoutpool.base;

import lombok.Builder;
import lombok.Data;

/**
 * @author https://github.com/kuangcp on 2021-09-04 23:27
 */
@Data
@Builder
public class Param {
    private int start;
    private int total;
}
