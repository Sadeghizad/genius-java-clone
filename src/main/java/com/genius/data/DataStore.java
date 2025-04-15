package com.genius.data;

import com.genius.model.*;

import java.util.*;

public class DataStore {
    public static final Map<String, Account> accounts = new HashMap<>();
    public static final Map<String, Song> songs = new HashMap<>();
    public static final Map<String, Album> albums = new HashMap<>();
    public static final List<LyricEdit> lyricEdits = new ArrayList<>();
}
