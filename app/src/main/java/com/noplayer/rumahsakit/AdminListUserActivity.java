package com.noplayer.rumahsakit;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminListUserActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdminAdapterListUser adminAdapterListUser;

    public static ArrayList<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_user);
        recyclerView = findViewById(R.id.recycler4);
        getUser();
    }

    private void getUser(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.GET_ALL_USER, response -> {
            try {
                JSONObject object1 = new JSONObject(response);
                if (object1.getBoolean("success")) {
                    JSONArray user = new JSONArray(object1.getString("user"));
                    for (int i=0; i<user.length(); i++){
                        JSONObject daftar = user.getJSONObject(i);

                        User user1 = new User();
                        user1.setId(daftar.getString("id"));
                        user1.setName(daftar.getString("name"));
                        user1.setEmail(daftar.getString("email"));
                        user1.setMobile(daftar.getString("mobile"));
                        user1.setAddress(daftar.getString("address"));
                        user1.setBirthdate(daftar.getString("birthdate"));
                        user1.setGender(daftar.getString("gender"));
                        userList.add(user1);
                    }
                    adminAdapterListUser = new AdminAdapterListUser(userList, getApplicationContext());
                    recyclerView.setAdapter(adminAdapterListUser);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Get Failed Failed", Toast.LENGTH_SHORT).show();
            }
        },error -> {
            error.printStackTrace();
        }){
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
