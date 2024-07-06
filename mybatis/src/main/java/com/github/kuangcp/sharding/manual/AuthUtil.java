package com.github.kuangcp.sharding.manual;

import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-07-11 18:53
 */
@Component
public class AuthUtil {

    private final ThreadLocal<Long> orgIdLocal = new ThreadLocal<>();

    public void completeAuth(Long orgId) {
        orgIdLocal.set(orgId);
    }

    public Long getAuthedOrgId() {
        return orgIdLocal.get();
    }

    public void clearAuth() {
        orgIdLocal.remove();
    }
}
