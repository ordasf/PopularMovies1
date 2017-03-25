package com.project.movies.popular.popularmovies;

/**
 * Enum with the different possible sizes allowed by the movie database API
 */
public enum MoviePosterSize {
    W92("w92"),
    W154("w154"),
    W185("w185"),
    W342("w342"),
    W500("w500"),
    W780("w780"),
    ORIGINAL("original");

    private final String value;

    MoviePosterSize(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
