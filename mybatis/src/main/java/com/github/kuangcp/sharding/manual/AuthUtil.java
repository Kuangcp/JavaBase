package com.github.kuangcp.sharding.manual;

import org.springframework.stereotype.Component;

/**
 * @author https://github.com/kuangcp on 2021-07-11 18:53
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
