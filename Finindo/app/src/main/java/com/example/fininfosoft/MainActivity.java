package com.example.fininfosoft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fininfosoft.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements OnPersonClickListener{
    ActivityMainBinding binding;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    FirebaseUser user;
    MyAdapter adapter;
    OnPersonClickListener personClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Fire base initialisation
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        //Realm initialisation
        Realm.init(this);
          Realm realm = Realm.getDefaultInstance();


        List<Person> users = Arrays.asList(new Person("Ajay", 24, "Vizianagaram"),
                new Person("Uday", 22, "Kakinada"),
                new Person("Anil", 23, "Srikakulam"),
                new Person("Venkat", 24, "Bobbili"),
                new Person("Swaroop", 25, "Hyderabad"),
                new Person("Rama", 26, "Telangana"),
                new Person("Sai", 27, "Kerala"),
                new Person("Dileep", 21, "Bengaluru"),
                new Person("Rohith", 20, "Visakhapatnam"),
                new Person("Rajesh", 29, "Punjab"),
                new Person("Yogesh", 20, "Rajahmundry"));
        //inserting data into realm if table is empty
        if (realm.where(Person.class).count()==0){
            realm.beginTransaction();
            realm.insert(users);
            //realm.deleteAll();
            realm.commitTransaction();

        }
        //retrieve the data from the realm data base

        RealmResults<Person> sortByName = realm.where(Person.class).findAll().sort("name", Sort.ASCENDING);

        recyclerView = binding.recycelrViewId;
        //sending data to recyclerview adapter to display the data
        adapter = new MyAdapter(sortByName,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //checking current user is logged in or not
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        binding.menuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp(view);
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the Realm instance when the activity is destroyed

    }

    @Override
    public void onClick(int position) {
        System.out.println("Position "+position);
        Toast.makeText(this, "Clicked Position "+position, Toast.LENGTH_SHORT).show();

    }
    private void showPopUp(View v) {
        PopupMenu popupMenu = new PopupMenu(this,v);
        popupMenu.inflate(R.menu.menu_main);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                RealmResults<Person> sortedResults = null;
                Realm realm = Realm.getDefaultInstance();

                if (menuItem.getItemId() == R.id.menu_sort_name){
                    //query to sort by name
                    Toast.makeText(MainActivity.this, "Clicked on name", Toast.LENGTH_SHORT).show();
                    sortedResults = realm.where(Person.class).findAll().sort("name", Sort.ASCENDING);
                } else if (menuItem.getItemId() == R.id.menu_sort_age) {
                    //query to sort by age
                    Toast.makeText(MainActivity.this, "Clicked on Age", Toast.LENGTH_SHORT).show();
                    sortedResults = realm.where(Person.class).findAll().sort("age", Sort.ASCENDING);


                } else if (menuItem.getItemId()==R.id.menu_sort_city) {
                    //query to sort by city
                    Toast.makeText(MainActivity.this, "Clicked on city", Toast.LENGTH_SHORT).show();
                    sortedResults = realm.where(Person.class).findAll().sort("city", Sort.ASCENDING);

                }
                // Update the RecyclerView with the sorted data
                adapter.updateData(sortedResults);
                // Close the Realm instance
                realm.close();

                return false;

            }


        });
        popupMenu.show();
    }


}