package com.example.aisleassignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class DiscoverFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.discover_fragment,container,false);
        assert this.getArguments() != null;
        try {
            JSONObject data=new JSONObject(this.getArguments().getString("data"));
            TextView textView=view.findViewById(R.id.invite_detail);
            JSONObject invite=data.getJSONObject("invites").getJSONArray("profiles").getJSONObject(0);
            textView.setText(String.format("%s, %s", invite.getJSONObject("general_information").getString("first_name"), invite.getJSONObject("general_information").getInt("age")));
            JSONArray likes=data.getJSONObject("likes").getJSONArray("profiles");
            textView=view.findViewById(R.id.like_1_detail);
            textView.setText(likes.getJSONObject(0).getString("first_name"));
            textView=view.findViewById(R.id.like_2_detail);
            textView.setText(likes.getJSONObject(1).getString("first_name"));
            String inviteURL="";
            JSONArray invitePhoto=invite.optJSONArray("photos");
            for(int i = 0; i< Objects.requireNonNull(invitePhoto).length(); i++)
                if(invitePhoto.getJSONObject(i).getBoolean("selected"))
                {
                    inviteURL=invitePhoto.getJSONObject(i).getString("photo");
                }
            Glide.with(view).load(inviteURL).into((ImageView)view.findViewById(R.id.invite_image));
            Glide.with(view).load(likes.getJSONObject(0).getString("avatar")).into((ImageView)view.findViewById(R.id.like_1_image));
            Glide.with(view).load(likes.getJSONObject(1).getString("avatar")).into((ImageView)view.findViewById(R.id.like_2_image));
        } catch (JSONException e) {
            Toast.makeText(this.getContext(),"Error Parsing Data",Toast.LENGTH_SHORT).show();
        }
        return view;
    }
}
