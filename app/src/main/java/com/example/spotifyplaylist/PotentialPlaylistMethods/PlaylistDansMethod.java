package com.example.spotifyplaylist.PotentialPlaylistMethods;

import java.util.ArrayList;

public class PlaylistDansMethod {
    private final ArrayList<Song> playlistDANS = new ArrayList<>();

    public PlaylistDansMethod() {
    }

    public void addSong(Song song) {
        this.playlistDANS.add(song);
    }

    public void addSong(int index, Song song) {
        this.playlistDANS.add(index, song);
    }

    public Song get(int i){
        return playlistDANS.get(i);
    }

    public boolean containsSong(Song song) {
        boolean ants = false;
        for (int i = 0; i < playlistDANS.size(); i++) {
            // can go by linkwhatever spotify has
            if (song.getLink().equalsIgnoreCase(playlistDANS.get(i).getLink())){
                ants = true;
                break;
            }
        }
        return ants;
}

    //returns index of a certain song
    public int search(PlaylistDansMethod playlist, Song song){
        int j = -1;
        for (int i = 0; i < playlist.size(); i++) {
            if (song.getLink().equalsIgnoreCase(playlistDANS.get(i).getLink())){
                j = i;
                break;
            }
        }
        return j;
    }

    public int size(){
        return playlistDANS.size();
    }

    public ArrayList<Song> getSong(){
        return playlistDANS;
    }

    public void sort() {
        int n = playlistDANS.size();

        //sorts from lowest to highest score
        for(int f = 0; f < n - 1; f++) {
            int min_idx = f;
            for (int j = f + 1; j < n; j++){
                if (playlistDANS.get(j).getScore() < playlistDANS.get(min_idx).getScore())
                    min_idx = j;

                //tiebreaker for score
                else if (playlistDANS.get(j).getScore() == playlistDANS.get(min_idx).getScore()) {
                    //lower highscore = lower on list
                    if (playlistDANS.get(j).getMaxScore() < playlistDANS.get(min_idx).getMaxScore())
                        min_idx = j;

                    //highscore tiebreaker
                    else if (playlistDANS.get(j).getMaxScore() == playlistDANS.get(min_idx).getMaxScore())

                        //on less lists = lower on list, after this it leaves things in the input order i think
                        if (playlistDANS.get(j).getSongCount() < playlistDANS.get(min_idx).getSongCount())
                        min_idx = j;
                }

                Song temp =  playlistDANS.get(min_idx);
                playlistDANS.set(min_idx, playlistDANS.get(f));
                playlistDANS.set(f, temp);
            }
        }

    }

    public PlaylistDansMethod topList(){

        PlaylistDansMethod topStyle = new PlaylistDansMethod();

        for (int tops = 0; tops < playlistDANS.size(); tops++){
            topStyle.addSong(tops, playlistDANS.get(playlistDANS.size() - (tops + 1)));
            playlistDANS.get(playlistDANS.size() - (tops + 1)).setRank(tops + 1);
        }

return topStyle;
    }

    //work on these methods
    public String topArtist() {
        String topArtist = "";
        String checkArtist = "";
        int topScore = 0;
        int count = 0;
        for (int i = 0; i < playlistDANS.size(); i++) {
            count = 0;
            for (int j = 0; j < playlistDANS.size(); j++) {
                if ((playlistDANS.get(i).getArtist()).equalsIgnoreCase((playlistDANS.get(j)).getArtist()))
                    count++;
                if (count > topScore) {
                    topArtist = playlistDANS.get(i).getArtist();
                    topScore = count;
                }
            }
        }
        return (topArtist + " with " + topScore + " songs");
    }

    public int topArtistCount(){
        int topScore = 0;
        int count = 0;

        for (int i = 0; i < playlistDANS.size(); i++) {
            count = 0;

            for (int j = 0; j < playlistDANS.size(); j++) {
                if ((playlistDANS.get(i).getArtist()).equalsIgnoreCase(playlistDANS.get(j).getArtist()))
                    count++;
                }if (count > topScore) {
                    topScore = count;
            }
        }
        return topScore;
    }

    public String toString(){
        String str = "";
        for (Song song: getSong()){
            str = str + "\n" + song.toString();
        }
        return str;
    }

}

