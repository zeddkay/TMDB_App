package com.example.zukhrufkhan.dps924zukhrufprojectv1;

/**
 * Created by zukhrufkhan on 2017-12-06.
 */

/*
Class for ActorResult, used for creating and setting the data for ActorResult objects
 */

import java.io.Serializable;

public class ActorResult implements Serializable  {


    private final int id;
    private final String popularity;
    private final String alsoKnownAs;
    private final String placeOfBirth;
    private final String biography;
    private final String birthday;
    private final String name;
    private final String profilePath;


    ActorResult(ActorResult.Builder builder) {

        id = builder.id;
        popularity = builder.popularity;
        alsoKnownAs = builder.alsoKnownAs;
        placeOfBirth = builder.placeOfBirth;
        biography = builder.biography;
        birthday = builder.birthday;
        name = builder.name;
        profilePath = builder.profilePath;
    }

    ActorResult() {

        id = -1;
        popularity =  "";
        alsoKnownAs = "";
        placeOfBirth = "";
        biography = "";
        birthday = "";
        name = "";
        profilePath = "not null";
    }

    public static class Builder {

        private int id;
        private String popularity;

        private String alsoKnownAs;
        private String placeOfBirth;
        private String biography;
        private String birthday;
        private String name;
        private String profilePath;


        public Builder(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public ActorResult.Builder setId(int id) {
            this.id = id;
            return this;
        }

        public ActorResult.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public ActorResult.Builder setBirthday(String birthday) {
            this.birthday = birthday;
            return this;
        }

        public ActorResult.Builder setPopularity(String popularity) {
            this.popularity = popularity;
            return this;
        }


        public ActorResult.Builder setBiography(String biography) {
            this.biography = biography;
            return this;
        }

        public ActorResult.Builder setAlsoKnownAs(String alsoKnownAs) {
            this.alsoKnownAs = alsoKnownAs;
            return this;
        }

        public ActorResult.Builder setPlaceOfBirth(String placeOfBirth) {
            this.placeOfBirth = placeOfBirth;
            return this;
        }

        public ActorResult.Builder setProfilePath(String profilePath) {
            this.profilePath = profilePath;
            return this;
        }

        public ActorResult build() {
            return new ActorResult(this);
        }

    }

    public static ActorResult.Builder newBuilder(int id, String title) {
        return new ActorResult.Builder(id, title);
    }



    public int getId() {
        return id;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getBiography() {
        return biography;
    }

    public String getAlsoKnownAs() {
        return alsoKnownAs;
    }

    public String getPlaceOfBirth() { return placeOfBirth; }

    public String getBirthday() { return birthday; }

    public String getName() { return name; }

    public String getProfilePath() { return profilePath;}

    @Override
    public String toString() {
        return name;
    }


}
