package com.aluno.arthur.leiturama.support;

import com.aluno.arthur.leiturama.models.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParer {

    public static Book parseBook(String jsonString) throws JSONException {

        JSONObject json = new JSONObject(jsonString);

        JSONObject volumeInfo = json.getJSONArray("items")
            .getJSONObject(0).getJSONObject("volumeInfo");
        JSONArray isbnArray = volumeInfo.getJSONArray("industryIdentifiers");

        Book book = new Book();
        book.setIsbn(testKeyAndGetString(isbnArray.getJSONObject(0),"identifier"));
        book.setTitle(testKeyAndGetString(volumeInfo,"title"));
        book.setAuthor(jsonArrayToString(volumeInfo.getJSONArray("authors")));
        book.setPublisher(testKeyAndGetString(volumeInfo,"publisher"));
        book.setPublishedDate(testKeyAndGetString(volumeInfo,"publishedDate"));
        book.setDescription(testKeyAndGetString(volumeInfo,"description"));
        book.setPageCount(testKeyAndGetInt(volumeInfo,"pageCount"));
        book.setCategories(jsonArrayToString(testKeyAndGetJSONArray(volumeInfo,"categories")));

        return book;
    }

    public static String parseImgUrl(String jsonString) throws JSONException {

        JSONObject json = new JSONObject(jsonString);

        JSONObject volumeInfo = json.getJSONArray("items")
            .getJSONObject(0).getJSONObject("volumeInfo");

        JSONObject imgLinks = testKeyAndGetJSONObject(volumeInfo,"imageLinks");

        String path = testKeyAndGetString(imgLinks,"thumbnail");

        if (path == "unknown") path=null;

        return path;
    }

    private static String testKeyAndGetString(JSONObject jsonObject, String key) throws JSONException{

        if (jsonObject.has(key)){
            return jsonObject.getString(key);
        }
        else
            return "unknown";
    }

    private static int testKeyAndGetInt(JSONObject jsonObject, String key) throws JSONException{

        if (jsonObject.has(key)){
            return jsonObject.getInt(key);
        }
        else
            return 0;
    }

    private static JSONArray testKeyAndGetJSONArray(JSONObject jsonObject, String key) throws JSONException{

        if (jsonObject.has(key)){
            return jsonObject.getJSONArray(key);
        }
        else
            return new JSONArray();
    }

    private static JSONObject testKeyAndGetJSONObject(JSONObject jsonObject, String key) throws JSONException{

        if (jsonObject.has(key)){
            return jsonObject.getJSONObject(key);
        }
        else
            return new JSONObject();
    }

    private static String jsonArrayToString(JSONArray jsonArray) throws JSONException{
        String response = new String();

        for (int i=0; i<jsonArray.length();i++){
            response += jsonArray.getString(i);
            if (i < jsonArray.length()-1)
                response += ", ";
        }

        return response;
    }
}
