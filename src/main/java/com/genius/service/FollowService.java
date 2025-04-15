package com.genius.service;

import com.genius.data.DataStore;
import com.genius.model.*;

import java.util.ArrayList;
import java.util.List;

public class FollowService {

    public static void followArtist(User user, String artistUsername) {
        // only users could done this so no need for more checking
        Account acc = DataStore.accounts.get(artistUsername);
        if (!(acc instanceof Artist)) {
            throw new IllegalArgumentException("No such artist.");
        }
        user.followArtist(artistUsername);
    }

    public static void unfollowArtist(User user, String artistUsername) {
        user.unfollowArtist(artistUsername);
    }

    public static List<Artist> getFollowedArtists(User user) {
        List<Artist> followed = new ArrayList<>();
        for (String username : user.getFollowedArtists()) {
            Account acc = DataStore.accounts.get(username);
            if (acc instanceof Artist artist) followed.add(artist);
        }
        return followed;
    }

    public static List<Song> getNewSongsFromFollowedArtists(User user) {
        List<Song> songs = new ArrayList<>();
        for (String artistUsername : user.getFollowedArtists()) {
            Account acc = DataStore.accounts.get(artistUsername);
            if (acc instanceof Artist artist) {
                for (String songId : artist.getSongIds()) {
                    Song song = DataStore.songs.get(songId);
                    if (song != null) songs.add(song);
                }
            }
        }
        return songs;
    }

    public static List<Album> getNewAlbumsFromFollowedArtists(User user) {
        List<Album> albums = new ArrayList<>();
        for (String artistUsername : user.getFollowedArtists()) {
            Account acc = DataStore.accounts.get(artistUsername);
            if (acc instanceof Artist artist) {
                for (String albumId : artist.getAlbumIds()) {
                    Album album = DataStore.albums.get(albumId);
                    if (album != null) albums.add(album);
                }
            }
        }
        return albums;
    }

    public static boolean isFollowing(User loggedInUser, String username) {
        return loggedInUser.getFollowedArtists().contains(username);
    }
}
