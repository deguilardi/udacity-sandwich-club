package com.udacity.sandwichclub;

import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        final Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        setTitle(sandwich.getMainName());
        populateUI(sandwich);

        final ImageView ingredientsIv = findViewById(R.id.image_iv);
        RequestCreator requestCreator = Picasso.with(this).load(sandwich.getImage());
        requestCreator.into(ingredientsIv, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
                ingredientsIv.setVisibility(View.GONE);
            }
        });
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        populateOrGone(R.id.also_known_label_tv, R.id.also_known_tv, sandwich.getAlsoKnownAs());
        populateOrGone(R.id.origin_label_tv, R.id.origin_tv, sandwich.getPlaceOfOrigin());
        populateOrGone(R.id.description_label_tv, R.id.description_tv, sandwich.getDescription());
        populateOrGone(R.id.ingredients_label_tv, R.id.ingredients_tv, sandwich.getIngredients(), "- ", ";\r\n");
    }



    /*
     * aux methods
     * populate a field with the value
     * hide label and value elements if value is empty
     */

    private void populateOrGone(int idLabel, int idValue, List<String> valueList){
        populateOrGone(idLabel, idValue, valueList, "", ", ");
    }

    private void populateOrGone(int idLabel, int idValue, List<String> valueList, String bullet, String separator){
        String valueString = "";
        if(valueList.size() > 0) {
            for (int i = 0; i < valueList.size(); i++) {
                valueString += bullet + valueList.get(i);
                valueString += (i != valueList.size() - 1) ? separator : "";
            }
        }
        populateOrGone(idLabel, idValue, valueString);
    }

    private void populateOrGone(int idLabel, int idValue, String value){
        TextView field = findViewById(idValue);
        if(!value.equals("")){
            field.setText(value);
        }
        else{
            findViewById(idLabel).setVisibility(View.GONE);
            field.setVisibility(View.GONE);
        }
    }
}
