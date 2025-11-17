package com.example.appdietas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appdietas.login.LoginActivity;

import java.util.Arrays;
import java.util.List;

public class LandingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LinearLayout layoutDots;
    private ImageView[] dots;
    private LandingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing);

        SharedPreferences prefs = getSharedPreferences("appPrefs", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);

        if (isFirstRun) {
            // !isFirstRun para la version FINAL IMPORTANTEEEEEE
            // Ya ha abierto la app antes → ir directamente al MainActivity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        viewPager = findViewById(R.id.landingViewPager);
        layoutDots = findViewById(R.id.layoutDots);

        List<Integer> layouts = Arrays.asList(
                R.layout.landing_slide1,
                R.layout.landing_slide2,
                R.layout.landing_slide3
        );

        adapter = new LandingAdapter(layouts);
        viewPager.setAdapter(adapter);

        addDots(layouts.size());
        setCurrentDot(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentDot(position);

                if (position == layouts.size() - 1) {
                    // Buscar el botón dentro del último slide
                    Button buttonStart = viewPager.findViewById(R.id.buttonContinuar);
                    if (buttonStart != null) {
                        buttonStart.setOnClickListener(v -> {
                            SharedPreferences.Editor editor = getSharedPreferences("appPrefs", MODE_PRIVATE).edit();
                            editor.putBoolean("isFirstRun", false);
                            editor.apply();
                            Intent intent = new Intent(LandingActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    }
                }
            }
        });
    }

    private void addDots(int count) {
        dots = new ImageView[count];
        layoutDots.removeAllViews();

        for (int i = 0; i < count; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.dot_inactive);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            layoutDots.addView(dots[i], params);
        }
    }

    private void setCurrentDot(int index) {
        for (int i = 0; i < dots.length; i++) {
            dots[i].setImageResource(i == index ? R.drawable.dot_active : R.drawable.dot_inactive);
        }
    }
}