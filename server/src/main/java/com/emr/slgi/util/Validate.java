package com.emr.slgi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {

    public static final String MEMBER_STATUS_REGEX = "^R\\d{3}$";
    public static final String MEMBER_UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";
    //
    public static List<Boolean> regexValidate(Map<String, String> list) {

        List<Boolean> res = new ArrayList<>();

        list.forEach((regex, value) -> {

            if(regex == null || value == null) {
                res.add(false);
            }else{
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                res.add(matcher.matches());
            }

        });

        return res;

    }

}
