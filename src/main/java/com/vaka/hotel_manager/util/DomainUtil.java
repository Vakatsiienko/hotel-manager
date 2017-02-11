package com.vaka.hotel_manager.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Created by Iaroslav on 12/6/2016.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainUtil {

    public static String formatUserName(String userName) {
        if (userName == null){
            throw new IllegalArgumentException("User name can't be null");
        }
        String withoutStartAndEndSpaces = userName.trim();
        String[] words = withoutStartAndEndSpaces.split(" ");
        StringJoiner spaceJoiner = new StringJoiner(" ");
        Arrays.stream(words).filter(w -> !w.equals("")).forEach(spaceJoiner::add);
        return spaceJoiner.toString();
    }
}
