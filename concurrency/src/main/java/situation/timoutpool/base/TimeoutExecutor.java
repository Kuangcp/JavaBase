package situation.timoutpool.base;

import java.util.List;
import java.util.function.Function;

/**
 * @author kuangcp
 */
public interface TimeoutExecutor<P, R> {

    List<R> execute(List<P> param, Function<P, R> handler);
}
