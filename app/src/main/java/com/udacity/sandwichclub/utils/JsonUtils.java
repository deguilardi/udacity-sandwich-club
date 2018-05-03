package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String TAG = "JsonUtils";

    public static Sandwich parseSandwichJson (String sandwichString){
        final String J_NAME = "name";
        final String J_NAME_MAIN_NAME = "mainName";
        final String J_NAME_ALSO_KNOWN_AS = "alsoKnownAs";
        final String J_PLACE_ORIGIN = "placeOfOrigin";
        final String J_DESCRIPTION = "description";
        final String J_IMAGE = "image";
        final String J_INGREDIENTS = "ingredients";


        try {
            Sandwich sandwich = new Sandwich();
            JSONObject sandwichJson = new JSONObject(sandwichString);
            JSONObject sandwichName = sandwichJson.getJSONObject(J_NAME);

            sandwich.setMainName(sandwichName.getString(J_NAME_MAIN_NAME));
            sandwich.setAlsoKnownAs(getStringList(J_NAME_ALSO_KNOWN_AS, sandwichName));
            sandwich.setPlaceOfOrigin(sandwichJson.getString(J_PLACE_ORIGIN));
            sandwich.setDescription(sandwichJson.getString(J_DESCRIPTION));
            sandwich.setImage(sandwichJson.getString(J_IMAGE));
            sandwich.setIngredients(getStringList(J_INGREDIENTS, sandwichJson));
            return sandwich;
        }
        catch(JSONException e){
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /*
     * aux method
     * parse the JSONArray and returns as List<string>
     */
    private static List<String> getStringList(String field, JSONObject from) throws JSONException{
        JSONArray jsonArray = from.getJSONArray(field);
        List<String> output = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++) {
            output.add( jsonArray.getString(i) );
        }
        return output;
    }
}
