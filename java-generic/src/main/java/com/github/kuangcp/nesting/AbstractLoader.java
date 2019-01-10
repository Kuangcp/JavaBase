package com.github.kuangcp.nesting;

import java.util.Map;

/**
 * TODO: B能不能自动根据T获取到
 *
 * @author kuangcp on 19-1-10-下午2:31
 */
public abstract class AbstractLoader<T extends JsonVO, B> {

  abstract Map<B, T> getMap();
}
