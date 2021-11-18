package com.example.spotifyplaylist.PotentialPlaylistMethods;

public class Song {
    //instance variables
    private String name = "";
    private String artist;
    private String album = "";
    private String id = "";
    private String link = "";
    private double score = 0;
    private int songCount = 0;
    private double maxScore = 0;
    private int rank = -1;

    //no arg and arg constructors
    public Song() { }

    public Song (String name, String artist, String album){
        this.name = name;
        this.artist = artist;
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public double getScore() {
        return score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    //adds one to song count
    public void addOneSongCount(){
        this.songCount += 1;
    }

    public void setScore(double score) {
        this.score = score;
    }

    //used to
    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getSongCount() {
        return songCount;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public String toString() {
        return "\nRanked " + rank + " overall" +
                "\n\tName: " + name +
                "\n\tArtist: " + artist +
                "\n\tAlbum: " + album +
                "\n\tScore: " + score +
                "\n\tLists on: " + songCount +
                "\n\tHighest Score: " + maxScore +
                "\n\tAvg. Score: " +  score / songCount;
    }

}
