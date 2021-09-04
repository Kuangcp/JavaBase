package situation.timoutpool.base;

import java.util.concurrent.TimeUnit;

/**
 * @author https://github.com/kuangcp on 2021-09-04 23:23
 */
public interface TaskExecutor<P,R> {

    R execute(P param, long timeout, TimeUnit timeUnit);
}
