package com.project.movies.popular.popularmovies1;

/**
 * Created by Fer on 04/02/2017.
 */

public enum MovieOrderType {
    POPULAR("popular"),
    TOP_RATED("top_rated");

    private final String value;

    MovieOrderType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
