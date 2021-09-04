package situation.timoutpool;

import situation.timoutpool.base.Param;
import situation.timoutpool.base.Result;
import situation.timoutpool.base.TaskExecutor;

import java.util.concurrent.TimeUnit;

/**
 * @author https://github.com/kuangcp on 2021-09-05 02:45
 */
public class TimeoutFuture implements TaskExecutor<Param, Result> {

    @Override
    public Result execute(Param param, long timeout, TimeUnit timeUnit) {
        return null;
    }
}
