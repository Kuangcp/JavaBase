package situation.timoutpool.base;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-09-04 23:23
 */
public interface TaskExecutor<P,R> {

    R execute(P param, long timeout, TimeUnit timeUnit);
}
