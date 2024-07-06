package situation.timoutpool.base;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-04 23:28
 */
@Data
@Builder
public class Result {

    private List<String> dataList;
}
