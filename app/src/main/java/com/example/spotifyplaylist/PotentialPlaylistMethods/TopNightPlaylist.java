/*
TopNightPlaylist
Dan Pike
7/24/2020
*/
package com.example.spotifyplaylist.PotentialPlaylistMethods;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TopNightPlaylist {
    public static void main(String[] args){
        //initialize things
        double score = 50;
        PlaylistDansMethod thisYearsTopPlaylist = new PlaylistDansMethod();

        //Playlist topPlayList2019 = new Playlist();


        //if you copy and paste the cells from excel directly it works fine
        File file1 = new File("C:\\Users\\Dan\\IdeaProjects\\TopNightPlaylist\\src\\TopThings\\TopNight2021data");
        //File file2 = new File(args[1]);
        String line;

        try {
            //creates scanner for the file
            Scanner input = new Scanner(file1);

            //while there is input
            while (input.hasNextLine()) {

                    //creates string of 1 line of input
                    line = input.nextLine();

                    //that line is split into an array
                    //String[] songSplit = line.split(" - ");
                    //tab split
                    String[] songSplit = line.split("\\t");

                    //inputs those array values into a new object
                    String songNameString = songSplit[0];
                    String artistString = songSplit[1];
                    String albumString = songSplit[2];

                    //create song
                    Song song = new Song(songNameString, artistString, albumString);
                    song.setScore(score);


                //if song has already been added
                    if (thisYearsTopPlaylist.containsSong(song)) {
                        //returns index of the song
                        int index = thisYearsTopPlaylist.search(thisYearsTopPlaylist, song);

                        //creates new value for score by taking old one and adding new one
                        double temp = (thisYearsTopPlaylist.get(index).getScore() + score);

                        //set score and add one to songs count
                        thisYearsTopPlaylist.get(index).setScore(temp);
                        thisYearsTopPlaylist.get(index).addOneSongCount();

                        //sets max score if applicable
                        if (score >= thisYearsTopPlaylist.get(index).getMaxScore())
                        thisYearsTopPlaylist.get(index).setMaxScore(score);
                    }

                    //if song has not been added
                    else if (!thisYearsTopPlaylist.containsSong(song)) {
                        thisYearsTopPlaylist.addSong(song);
                        song.setMaxScore(score);
                        song.addOneSongCount();
                    }



                //score keeps moving unless it is the end of someones list, then it resets
                    score--;
                    if (score == 0){
                        score = 50;
                    }
            }
            input.close();
    } catch (FileNotFoundException ex){
            ex.printStackTrace();
            //just to handle the exception i don't really need it to do anything
        }

        /*try {
            //creates scanner for the file
            Scanner input = new Scanner(file2);

            //while there is input
            while (input.hasNextLine()) {

                //creates string of 1 line of input
                line = input.nextLine();

                //that line is split into an array
                //String[] songSplit = line.split(" - ");
                //tab split
                String[] songSplit = line.split("\t");

                //inputs those array values into a new object
                String songNameString = songSplit[0];
                String artistString = songSplit[1];
                String albumString = songSplit[2];

                //create song
                Song song = new Song(songNameString, artistString, albumString);
                song.setScore(score);

                topPlayList2019.addSong(song);

            }
            input.close();
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
            //just to handle the exception i don't really need it to do anything
        }


       /* work on this method
       for (int i = 0; i < topPlayList2019.size(); i++){
            if (topPlayList2019.containsSong((thisYearsTopPlaylist.getSong(i)))



        }*/

        ///
        //Experimental scoring ideas
        //



        //adds avg score to each song, weights group thoughts on a song more while still rewarding low lists
/*
        double adderAvg = 0;
        for(int i = 0; i < thisYearsTopPlaylist.size(); i++) {
            adderAvg = thisYearsTopPlaylist.get(i).getScore() / thisYearsTopPlaylist.get(i).getSongCount();
            thisYearsTopPlaylist.get(i).setScore(thisYearsTopPlaylist.get(i).getScore() + adderAvg);
        }


        //adds top score to each song, weights higher songs more
        double adderTop = 0;
        for(int i = 0; i < thisYearsTopPlaylist.size(); i++) {
            adderTop = thisYearsTopPlaylist.get(i).getMaxScore();
            thisYearsTopPlaylist.get(i).setScore(thisYearsTopPlaylist.get(i).getScore() + adderTop);
        }

         */

        //sorts the playlist
        thisYearsTopPlaylist.sort();

        //creates the top playlist it is a new playlist in case i want to save them both after and the actual top playlist will be a different size
        PlaylistDansMethod topsforrael = thisYearsTopPlaylist.topList();
    }
}