package me.dannysuen.fivehundredpx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.animals_btn)
    public void onAnimalsButtonClick() {
        Intent intent = new Intent(this, PhotosActivity.class);
        startActivity(intent);
    }
}
