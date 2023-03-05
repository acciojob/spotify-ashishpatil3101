package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;
    //user vs song which is liked
    HashMap<User,List<Song>> userVSsongLiked;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();
        userVSsongLiked=new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        for(User USER:users){
            if(USER.getName()==name && USER.getMobile()==mobile) return USER;
        }
        User new_user=new User(name,mobile);
        creatorPlaylistMap.put(new_user,new Playlist());

        users.add(new_user);
        return new_user;
    }

    public Artist createArtist(String name) {
        for(Artist A:artists){
            if(A.getName()==name) return A;
        }
        Artist new_artist=new Artist(name);
        artists.add(new_artist);
        artistAlbumMap.put(new_artist,new ArrayList<>());
        return new_artist;
    }

    public Album createAlbum(String title, String artistName) {
        Artist art=createArtist(artistName);
        Album new_album=null;
        for(Album alb:albums){

            if(alb.getTitle()==title){

               new_album=alb;
               break;

            }
        }
        if(new_album == null ) {
            new_album = new Album(title);
            albums.add(new_album);
            albumSongMap.put(new_album,new ArrayList<>());

            List<Album> art_alb=artistAlbumMap.getOrDefault(art,new ArrayList<>());
            boolean f=false;

            for(Album alb:art_alb){

                if(alb.getTitle()==title) {
                    f=true;
                }
            }
            if(f==false) art_alb.add(new_album);
        }
        return new_album;

    }

    public Song createSong(String title, String albumName, int length) throws Exception{

        Album albUM=null;
        for(Album alb:albums){

            if(alb.getTitle()==albumName) {

                albUM=alb;
                break;
            }
        }
        if(albUM==null) throw new Exception("Album does not exist");

        Song SNG=null;
        for(Song sng:songs) {
            if(sng.getTitle()==title && sng.getLength()==length){

                SNG=sng;
                break;
            }
        }
        if(SNG==null)
        {
            SNG=new Song(title,length);
            songs.add(SNG);
        }
        albumSongMap.getOrDefault(albUM,new ArrayList<>()).add(SNG);
        return SNG;

    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        User USER=null;
        for(User user:users){
            if(user.getMobile()==mobile) {
                USER=user;
                break;
            }
        }
        if(USER==null) throw new Exception("User does not exist");
        //user exist now

        Playlist playlist=null;

        for(Playlist pl:playlists){

            if(pl.getTitle()==title){
                playlist=pl;
                break;
            }
        }
        //playlist does not exist so add new playlist with songs of list
        if(playlist==null) playlistSongMap.put(new Playlist(title) ,new ArrayList<>());
        //add song
        for(Song sng:songs){

            if(sng.getLength()==length)   playlistSongMap.get(playlist).add(sng);
        }
        //playlist with listner
        if(!playlistListenerMap.containsKey(playlist)) playlistListenerMap.put(playlist,new ArrayList<>());

        int f=0;
        List<User> li=playlistListenerMap.get(playlist);

        for(User us:li){
            if(us==USER){
                f=1;
                break;
            }
        }
        if(f==0) playlistListenerMap.get(playlist).add(USER);
        //creator playlist
        creatorPlaylistMap.put(USER,playlist);
        //add playlist to users list
        if(!userPlaylistMap.containsKey(USER)) userPlaylistMap.put(USER,new ArrayList<>());
        f=3;
        List<Playlist> li2=userPlaylistMap.get(USER);

        for(Playlist us:li2){

            if(us==playlist){
                f=4;
                break;
            }
        }
        if(f==3) userPlaylistMap.get(USER).add(playlist);

        playlists.add(playlist);

        return playlist;

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        User USER=null;
        for(User user:users){
            if(user.getMobile()==mobile) {
                USER=user;
                break;
            }
        }
        if(USER==null) throw new Exception("User does not exist");
        //user exist now

        Playlist playlist=null;

        for(Playlist pl:playlists){

            if(pl.getTitle()==title){
                playlist=pl;
                break;
            }
        }
        //playlist does not exist so add new playlist with songs of list
        if(playlist==null) playlistSongMap.put(new Playlist(title) ,new ArrayList<>());

        for(Song sng:songs){

            for(String  sngTitle:songTitles){

                if(sng.getTitle()==sngTitle) {

                    playlistSongMap.get(playlist).add(sng);
                }
            }
        }
        //playlist with listner
        if(!playlistListenerMap.containsKey(playlist)) playlistListenerMap.put(playlist,new ArrayList<>());

        int f=0;
        List<User> li=playlistListenerMap.get(playlist);

        for(User us:li){
            if(us==USER){
                f=1;
                break;
            }
        }
        if(f==0) playlistListenerMap.get(playlist).add(USER);
        //creator playlist
        creatorPlaylistMap.put(USER,playlist);
        //add playlist to users list
        if(!userPlaylistMap.containsKey(USER)) userPlaylistMap.put(USER,new ArrayList<>());
        f=3;
        List<Playlist> li2=userPlaylistMap.get(USER);

        for(Playlist us:li2){

            if(us==playlist){
                f=4;
                break;
            }
        }
        if(f==3) userPlaylistMap.get(USER).add(playlist);

        playlists.add(playlist);

        return playlist;


    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        int f=0;
        int f2=0;
        User USER=null;
        for(User user:users){
            if(user.getMobile()==mobile) {
                f=1;
                USER=user;
                break;
            }
        }
        if(f==0)   throw new Exception("User does not exist");
        Playlist pl=null;
        for(Playlist alb:playlists){

            if(alb.getTitle()==playlistTitle) {
                f2=1;
            }
        }

        if(f2==0) throw new Exception("Playlist does not exist");
        //creator
        int f3=0;
        if(!creatorPlaylistMap.containsKey( USER)){
            f3=1;
            creatorPlaylistMap.put(USER,pl);
        }

        //listner
        int f4=0;
        List<User> li=playlistListenerMap.get(pl);

        for(User us:li){
            if(us==USER){
                f4=1;
                break;
            }
        }
        if(f4==0) playlistListenerMap.get(pl).add(USER);
//        if(f3==0  && f4==0) {
//
//
//        }
        return pl;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {


        Song  song=null;
        User USER=null;
        for(User user:users){
            if(user.getMobile()==mobile) {
                USER=user;
                break;
            }
        }
        if(USER==null)   throw new Exception("User does not exist");
        for(Song sn:songs){

            if(sn.getTitle()==songTitle) {
                song=sn;
                break;
            }
        }
        if(song==null)   throw new Exception("Song does not exist");
       if(!userVSsongLiked.containsKey(USER)) userVSsongLiked.put(USER,new ArrayList<>());

       List<Song> li=userVSsongLiked.get(USER);

       for(Song sn:li){
           if(sn.getTitle()==songTitle) return sn;
       }
       userVSsongLiked.get(USER).add(song);
       song.setLikes(song.getLikes()+1);

       return  song;



    }

    public String mostPopularArtist() {
        int ans=0;
        Artist ans2=null;

        for(Artist art:artistAlbumMap.keySet()){

            List<Album> li=artistAlbumMap.get(art);
            int temp=0;
            for(Album alb:li){

                List<Song> li2=albumSongMap.get(alb);

                for(Song sng:li2){
                    temp +=sng.getLikes();
                }
            }
           if(ans <= temp){
               ans2=art;
               ans=temp;
           }
        }
        return ans2.getName();
    }

    public String mostPopularSong() {
        int max=0;
        String ans="";
        for(Song sn:songs){
            if(sn.getLikes() > max) {
                ans = sn.getTitle();

                max = Math.max(max, sn.getLikes());
            }
        }

        return ""+ans;
    }
}
