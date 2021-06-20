package com.example.aisleassignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class NotesActivity extends AppCompatActivity {

    String auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notes_activity);

        auth = getIntent().getStringExtra("auth");

        RequestQueue requestQueue = Volley.newRequestQueue(NotesActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://testa2.aisle.co/V1/users/test_profile_list", null,
                response -> {
                    if (response.has("invites")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("data", response.toString());

                        DiscoverFragment discoverFragment = new DiscoverFragment();
                        discoverFragment.setArguments(bundle);
                        ProfileFragment profileFragment = new ProfileFragment();
                        NotesFragment notesFragment = new NotesFragment();
                        MatchesFragment matchesFragment = new MatchesFragment();
                        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, discoverFragment).commit();
                        bottomNav.setOnNavigationItemSelectedListener(item -> {
                            if (item.getItemId() == R.id.discover)
                                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, discoverFragment).commit();
                            else if (item.getItemId() == R.id.notes)
                                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, notesFragment).commit();
                            else if (item.getItemId() == R.id.matches)
                                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, matchesFragment).commit();
                            else if (item.getItemId() == R.id.profile)
                                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, profileFragment).commit();
                            return true;
                        });
                    } else {
                        Toast.makeText(NotesActivity.this, "Error Parsing Response", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NotesActivity.this, LoginActivity.class));
                    }
                },
                error -> Toast.makeText(NotesActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", auth);
                headers.put("Cookie", "__cfduid=df9b865983bd04a5de2cf5017994bbbc71618565720");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
