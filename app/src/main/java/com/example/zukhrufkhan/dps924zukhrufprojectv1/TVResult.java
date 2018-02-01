package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/**
 * Created by zukhrufkhan on 2017-12-06.
 */

/*
Class for TVResult, used for creating and setting the data for TVResult objects
 */

import java.io.Serializable;

public class TVResult implements Serializable {

    private final int id;
    private final String popularity;
    private final String firstAirDate;
    private final String voteAverage;
    private final String overview;
    private final String name;
    private final String originalName;
    private final String posterPath;
    private final String originalLanguage;
    private final String backdropPath;

    TVResult(TVResult.Builder builder) {

        id = builder.id;
        popularity = builder.popularity;
        firstAirDate = builder.firstAirDate;
        voteAverage = builder.voteAverage;
        overview = builder.overview;
        name = builder.name;
        posterPath = builder.posterPath;
        originalName = builder.originalName;
        originalLanguage = builder.originalLanguage;
        backdropPath = builder.backdropPath;
    }

    public static class Builder {

        private int id;
        private String popularity;
        private String firstAirDate;
        private String voteAverage;
        private String overview;
        private String name;
        private String posterPath;
        private String originalName;
        private String originalLanguage;
        private String backdropPath;


        public Builder(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public TVResult.Builder setId(int id) {
            this.id = id;
            return this;
        }

        public TVResult.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public TVResult.Builder setPosterPath(String posterPath) {
            this.posterPath = posterPath;
            return this;
        }

        public TVResult.Builder setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
            return this;
        }

        public TVResult.Builder setPopularity(String popularity) {
            this.popularity = popularity;
            return this;
        }

        public TVResult.Builder setOverview(String overview) {
            this.overview = overview;
            return this;
        }

        public TVResult.Builder setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
            return this;
        }

        public TVResult.Builder setFirstAirDate(String firstAirDate) {
            this.firstAirDate = firstAirDate;
            return this;
        }

        public TVResult.Builder setOriginalName(String originalName) {
            this.originalName = originalName;
            return this;
        }

        public TVResult.Builder setVoteAverage(String voteAverage) {
            this.voteAverage = voteAverage;
            return this;
        }

        public TVResult build() {
            return new TVResult(this);
        }

    }

    public static TVResult.Builder newBuilder(int id, String title) {
        return new TVResult.Builder(id, title);
    }



    public int getId() {
        return id;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getOverview() {
        return overview;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getFirstAirDate() { return firstAirDate; }

    public String getName() { return name; }

    public String getPosterPath() { return posterPath; }

    public String getBackdropPath() { return backdropPath; }

    public String getVoteAverage() { return voteAverage;}

    @Override
    public String toString() {
        return name;
    }




}
