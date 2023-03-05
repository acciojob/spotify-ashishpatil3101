package com.driver;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class SpotifyService {

    //Auto-wire will not work in this case, no need to change this and add autowire

    SpotifyRepository spotifyRepository = new SpotifyRepository();

    public User createUser(String name, String mobile){

        return spotifyRepository.createUser(name, mobile);
    }

    public Artist createArtist(String name) {

        return spotifyRepository.createArtist(name);
    }

    public Album createAlbum(String title, String artistName) {

        return spotifyRepository.createAlbum(title,artistName);

    }

    public Song createSong(String title, String albumName, int length) throws Exception {

        for(Album alb:spotifyRepository.albums){

            if(alb.getTitle()==albumName) return spotifyRepository.createSong(title,albumName,length);
        }

        throw new Exception("Album does not exist");

    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {

        for(User user:spotifyRepository.users){
            if(user.getMobile()==mobile) return spotifyRepository.createPlaylistOnLength(mobile, title, length);
        }
        throw new Exception("User does not exist");

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        for(User user:spotifyRepository.users){
            if(user.getMobile()==mobile) return spotifyRepository.createPlaylistOnName(mobile, title, songTitles);
        }
        throw new Exception("User does not exist");

    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        int f=0;
        int f2=0;
        for(User user:spotifyRepository.users){
            if(user.getMobile()==mobile) {
                f=1;
                break;
            }
        }
        if(f==0)   throw new Exception("User does not exist");

        for(Playlist alb:spotifyRepository.playlists){

            if(alb.getTitle()==playlistTitle) {
                f2=1;
            }
        }

       if(f2==0) throw new Exception("Playlist does not exist");

       return spotifyRepository.findPlaylist(mobile,playlistTitle);

    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        int f=0;
        int f2=0;
        for(User user:spotifyRepository.users){
            if(user.getMobile()==mobile) {
                f=1;
                break;
            }
        }
        if(f==0)   throw new Exception("User does not exist");

        for(Song sn:spotifyRepository.songs){

            if(sn.getTitle()==songTitle) {
                f2=1;
                break;
            }
        }
        if(f2==0) throw new Exception("Song does not exist");
        return spotifyRepository.likeSong(mobile,songTitle);

    }

    public String mostPopularArtist() {
      return spotifyRepository.mostPopularArtist();
    }

    public String mostPopularSong() {
       return spotifyRepository.mostPopularSong();
    }
}
