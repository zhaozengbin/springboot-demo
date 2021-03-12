package com.zzb.sba.admin2x.enums;

import lombok.Getter;

@Getter
public enum EActiveProfile {
    UNKNOWN(true), TEST(false), RD(true), PRO(true);

    private boolean isSend;

    EActiveProfile(boolean isSend) {
        this.isSend = isSend;
    }

    public static EActiveProfile valueOfActiveProfile(String activeProfile) {
        return EActiveProfile.valueOf(activeProfile.toUpperCase());
    }
}
