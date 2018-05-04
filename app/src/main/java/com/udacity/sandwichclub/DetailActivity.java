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

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @BindView(R.id.also_known_label_tv) TextView alsoKnownLabelTv;
    @BindView(R.id.also_known_tv) TextView alsoKnownAsTv;
    @BindView(R.id.origin_label_tv) TextView originLabelTv;
    @BindView(R.id.origin_tv) TextView originTv;
    @BindView(R.id.description_label_tv) TextView descriptionLabelTv;
    @BindView(R.id.description_tv) TextView descriptionTv;
    @BindView(R.id.ingredients_label_tv) TextView ingredientsLabelTv;
    @BindView(R.id.ingredients_tv) TextView ingredientsTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

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
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.image_not_found)
                .into(ingredientsIv);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        populateOrGone(alsoKnownLabelTv, alsoKnownAsTv, sandwich.getAlsoKnownAs());
        populateOrGone(originLabelTv, originTv, sandwich.getPlaceOfOrigin());
        populateOrGone(descriptionLabelTv, descriptionTv, sandwich.getDescription());
        populateOrGone(ingredientsLabelTv, ingredientsTv, sandwich.getIngredients(), "- ", ";\r\n");
    }



    /*
     * aux methods
     * populate a field with the value
     * hide label and value elements if value is empty
     */

    private void populateOrGone(TextView labelTv, TextView valueTv, List<String> valueList){
        populateOrGone(labelTv, valueTv, valueList, "", ", ");
    }

    private void populateOrGone(TextView labelTv, TextView valueTv, List<String> valueList, String bullet, String separator){
        String valueString = "";
        if(valueList.size() > 0) {
            for (int i = 0; i < valueList.size(); i++) {
                valueString += bullet + valueList.get(i);
                valueString += (i != valueList.size() - 1) ? separator : "";
            }
        }
        populateOrGone(labelTv, valueTv, valueString);
    }

    private void populateOrGone(TextView labelTv, TextView valueTv, String value){
        if(!value.equals("")){
            valueTv.setText(value);
        }
        else{
            labelTv.setVisibility(View.GONE);
            valueTv.setVisibility(View.GONE);
        }
    }
}
