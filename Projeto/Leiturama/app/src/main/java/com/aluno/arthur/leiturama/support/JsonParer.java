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
        book.setIsbn(isbnArray.getJSONObject(0).getString("identifier"));
        book.setTitle(volumeInfo.getString("title"));
        book.setAuthor(jsonArrayToString(volumeInfo.getJSONArray("authors")));
        book.setPublisher(volumeInfo.getString("publisher"));
        book.setPublishedDate(volumeInfo.getString("publishedDate"));
        book.setDescription(volumeInfo.getString("description"));
        book.setPageCount(volumeInfo.getInt("pageCount"));
        book.setCategories(jsonArrayToString(volumeInfo.getJSONArray("categories")));

        return book;
    }

    public static String parseImgUrl(String jsonString) throws JSONException {

        JSONObject json = new JSONObject(jsonString);

        JSONObject volumeInfo = json.getJSONArray("items")
            .getJSONObject(0).getJSONObject("volumeInfo");

        String path = volumeInfo.getJSONObject("imageLinks")
            .getString("thumbnail");

        return path;
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
