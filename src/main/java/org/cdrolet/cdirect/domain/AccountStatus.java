package org.cdrolet.cdirect.domain;

/**
 * Created by c on 4/15/16.
 */

public enum AccountStatus {
    FREE_TRIAL,
    FREE_TRIAL_EXPIRED,
    ACTIVE,
    SUSPENDED,
    CANCELLED;

    public boolean isActive() {
        return this.equals(FREE_TRIAL) || this.equals(ACTIVE);
    }
}
