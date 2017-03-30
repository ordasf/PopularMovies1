package com.project.movies.popular.popularmovies;

/**
 * Created by Fer on 04/02/2017.
 */

public enum MovieOrderType {
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    FAVOURITE("favourite");

    private final String value;

    MovieOrderType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MovieOrderType getFromString(String orderString) {

        for (MovieOrderType type : MovieOrderType.values()) {
            if (type.getValue().equals(orderString)) {
                return type;
            }
        }

        throw new IllegalArgumentException("The argument for the MovieOrderType enum was incorrect");
    }

}
