package com.genius.data;

import com.genius.model.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class FileHandler {

    private static final String DATA_STORE_FILE = "F:\\Documents\\java\\AP course\\Genius-clone\\src\\main\\resources\\db\\data-store.json";  // Path to the file where the data will be saved

    // Save DataStore to a file
    public static void saveDataStore() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())  // Register the custom LocalDate adapter
                .create();
// Inside saveDataStore method:
        JsonObject dataStoreJson = new JsonObject();
        JsonArray accountsArray = new JsonArray();
        for (Account account : DataStore.accounts.values()) {
            JsonObject accountJson = gson.toJsonTree(account).getAsJsonObject();

            // Add the accountType to each account
            if (account instanceof User) {
                accountJson.addProperty("accountType", "user");
            } else if (account instanceof Artist) {
                accountJson.addProperty("accountType", "artist");
            } else if (account instanceof Admin) {
                accountJson.addProperty("accountType", "admin");
            }

            accountsArray.add(accountJson);
        }
        dataStoreJson.add("accounts", accountsArray);
        dataStoreJson.add("songs", gson.toJsonTree(DataStore.songs));
        dataStoreJson.add("albums", gson.toJsonTree(DataStore.albums));
        dataStoreJson.add("lyricEdits", gson.toJsonTree(DataStore.lyricEdits));

        try (FileWriter writer = new FileWriter(DATA_STORE_FILE)) {
            gson.toJson(dataStoreJson, writer);  // Serialize the entire structure into JSON
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load DataStore from a file
    public static void loadDataStore() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())  // Register the custom LocalDate adapter
                .create();

        File file = new File(DATA_STORE_FILE);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Data store file does not exist. Skipping load.");
            return;  // Exit if the file doesn't exist
        }
        try (Reader reader = new FileReader(DATA_STORE_FILE)) {
// Deserialize the JSON back into the DataStore maps/lists
            Type songMapType = new TypeToken<Map<String, Song>>(){}.getType();
            Type albumMapType = new TypeToken<Map<String, Album>>(){}.getType();
            Type lyricEditListType = new TypeToken<List<LyricEdit>>(){}.getType();

            JsonReader jsonReader = new JsonReader(reader);
            JsonObject dataStoreJson = gson.fromJson(jsonReader, JsonObject.class);

// Deserialize the accounts field, but account type must be identified
            JsonArray accountArray = dataStoreJson.getAsJsonArray("accounts");

            for (JsonElement element : accountArray) {
                JsonObject accountJson = element.getAsJsonObject();
                String accountType = accountJson.get("accountType").getAsString();

                Account account;
                if ("user".equals(accountType)) {
                    account = gson.fromJson(accountJson, User.class);
                } else if ("artist".equals(accountType)) {
                    account = gson.fromJson(accountJson, Artist.class);
                } else if ("admin".equals(accountType)) {
                    account = gson.fromJson(accountJson, Admin.class);
                } else {
                    throw new IllegalArgumentException("Unknown account type: " + accountType);
                }

                // Add the deserialized account to the DataStore
                DataStore.accounts.put(account.getUsername(), account);
            }

// Deserialize songs, albums, and lyricEdits similarly
            DataStore.songs.putAll(gson.fromJson(dataStoreJson.get("songs"), songMapType));
            DataStore.albums.putAll(gson.fromJson(dataStoreJson.get("albums"), albumMapType));
            DataStore.lyricEdits.addAll(gson.fromJson(dataStoreJson.get("lyricEdits"), lyricEditListType));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Data store file malfunction found.try cleaning datastore file.");
        }
    }
}
