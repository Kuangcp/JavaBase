package situation.timoutpool;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author https://github.com/kuangcp on 2021-09-04 23:28
 */
@Data
@Builder
public class Result {

    private List<String> dataList;
}
