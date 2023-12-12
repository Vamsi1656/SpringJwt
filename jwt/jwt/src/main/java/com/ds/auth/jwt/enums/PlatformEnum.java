package com.ds.auth.jwt.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum PlatformEnum {
    MOBILE("Mobile"), WEB("Web"),API("Api");

    @Getter
    String value;

    private PlatformEnum(String value) {
        this.value = value;
    }

    private static Map<String, PlatformEnum> map = Collections.unmodifiableMap(initializeMapEnum());

    private static Map<String, PlatformEnum> initializeMapEnum() {
        Map<String, PlatformEnum> enumMap = new HashMap<>();
        for(PlatformEnum e : PlatformEnum.values()){
            enumMap.put(e.getValue(), e);
        }
        return enumMap;
    }

    public static PlatformEnum getEnumFromValue(String value) {
        if(map.containsKey(value)){
            return map.get(value);
        }
        return null;
    }

}
