package com.estasvegano.android.estasvegano.data.web;


public class UrlConstants {

    public static final String ID_REPLACEMENT = "id";
    public static final String BASE_URL = "http://evegano.free-node.ru/";
    public static final String CHECK = "/api/v1.0/check";
    public static final String ADD = "/api/v1.0/add";
    public static final String CATEGORIES = "/api/v1.0/categories";
    public static final String ADD_PRODUCER = "/api/v1.0/add-producer";
    private static final String ID_REPLACEMENT_BRACKETS = "{" + ID_REPLACEMENT + "}";

    public static final String ADD_IMAGE = "/api/v1.0/add/" + ID_REPLACEMENT_BRACKETS + "/photo";

    public static final String COMPLAIN = "/api/v1.0/" + ID_REPLACEMENT_BRACKETS + "/complain";

    public static final String PRODUCERS = "/api/v1.0/producers/";

    public static final String SUB_CATEGORIES = "/api/v1.0/categories/" + ID_REPLACEMENT_BRACKETS;

    public static final String PRODUCERS_TITLE_QUERY = "title";
}
