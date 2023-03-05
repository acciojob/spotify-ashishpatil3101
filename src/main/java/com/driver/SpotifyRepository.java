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
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }
        Playlist playlist = new Playlist(title);
        // adding playlist to playlists list
        playlists.add(playlist);

        List<Song> temp= new ArrayList<>();
        for(Song song : songs){
            if(song.getLength()==length){
                temp.add(song);
            }
        }
        playlistSongMap.put(playlist,temp);

        User curUser= new User();
        boolean flag= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                flag= true;
                break;
            }
        }
        if (flag==false){
            throw new Exception("User does not exist");
        }

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        userslist.add(curUser);
        playlistListenerMap.put(playlist,userslist);

        creatorPlaylistMap.put(curUser,playlist);

        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(curUser)){
            userplaylists=userPlaylistMap.get(curUser);
        }
        userplaylists.add(playlist);
        userPlaylistMap.put(curUser,userplaylists);
        return playlist;

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {

        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }
        Playlist playlist = new Playlist(title);
        // adding playlist to playlists list
        playlists.add(playlist);

        List<Song> temp= new ArrayList<>();
        for(Song song : songs){
            if(songTitles.contains(song.getTitle())){
                temp.add(song);
            }
        }
        playlistSongMap.put(playlist,temp);

        User curUser= new User();
        boolean flag= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                flag= true;
                break;
            }
        }
        if (flag==false){
            throw new Exception("User does not exist");
        }

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        userslist.add(curUser);
        playlistListenerMap.put(playlist,userslist);

        creatorPlaylistMap.put(curUser,playlist);

        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(curUser)){
            userplaylists=userPlaylistMap.get(curUser);
        }
        userplaylists.add(playlist);
        userPlaylistMap.put(curUser,userplaylists);

        return playlist;


    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {

        boolean flag =false;
        Playlist playlist = new Playlist();
        for(Playlist curplaylist: playlists){
            if(curplaylist.getTitle().equals(playlistTitle)){
                playlist=curplaylist;
                flag=true;
                break;
            }
        }
        if (flag==false){
            throw new Exception("Playlist does not exist");
        }

        User curUser= new User();
        boolean flag2= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                flag2= true;
                break;
            }
        }
        if (flag2==false){
            throw new Exception("User does not exist");
        }

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        if(!userslist.contains(curUser))
            userslist.add(curUser);
        playlistListenerMap.put(playlist,userslist);

        if(creatorPlaylistMap.get(curUser)!=playlist)
            creatorPlaylistMap.put(curUser,playlist);

        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(curUser)){
            userplaylists=userPlaylistMap.get(curUser);
        }
        if(!userplaylists.contains(playlist))userplaylists.add(playlist);
        userPlaylistMap.put(curUser,userplaylists);


        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User curUser= new User();
        boolean flag2= false;
        for(User user: users){
            if(user.getMobile().equals(mobile)){
                curUser=user;
                flag2= true;
                break;
            }
        }
        if (flag2==false){
            throw new Exception("User does not exist");
        }

        Song song = new Song();
        boolean flag = false;
        for(Song cursong : songs){
            if(cursong.getTitle().equals(songTitle)){
                song=cursong;
                flag=true;
                break;
            }
        }
        if (flag==false){
            throw new Exception("Song does not exist");
        }

        List<User> users = new ArrayList<>();
        if(songLikeMap.containsKey(song)){
            users=songLikeMap.get(song);
        }
        if (!users.contains(curUser)){
            users.add(curUser);
            songLikeMap.put(song,users);
            song.setLikes(song.getLikes()+1);



            Album album = new Album();
            for(Album curAlbum : albumSongMap.keySet()){
                List<Song> temp = albumSongMap.get(curAlbum);
                if(temp.contains(song)){
                    album=curAlbum;
                    break;
                }
            }



            Artist artist = new Artist();
            for(Artist curArtist : artistAlbumMap.keySet()){
                List<Album> temp = artistAlbumMap.get(curArtist);
                if(temp.contains(album)){
                    artist=curArtist;
                    break;
                }
            }

            artist.setLikes(artist.getLikes()+1);
        }
        return song;
    }
    public String mostPopularArtist() {

        String name="";
        int maxLikes = Integer.MIN_VALUE;
        for(Artist art : artists){
            maxLikes= Math.max(maxLikes,art.getLikes());
        }
        for(Artist art : artists){
            if(maxLikes==art.getLikes()){
                name=art.getName();
            }
        }
        return name;
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
