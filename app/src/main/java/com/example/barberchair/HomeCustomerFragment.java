package com.example.barberchair;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.barberchair.Model.ModelUser;
import com.example.barberchair.ViewHolder.AdapterUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeCustomerFragment extends Fragment {
    RecyclerView recyclerViews;
    List<ModelUser> userList;
    AdapterUsers adapterUsers;

    public HomeCustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view= inflater.inflate(R.layout.fragment_home_customer, container, false);
        recyclerViews = view.findViewById(R.id.recyclercustomer);
        recyclerViews.setHasFixedSize(true);



        userList = new ArrayList<>();

        getAllUsers();
        return view;

    }
    private void getAllUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ModelUser modelUser = dataSnapshot.getValue(ModelUser.class);

                    if(!modelUser.getUserid().equals(firebaseUser.getUid())){
                        userList.add(modelUser);
                    }

                    adapterUsers = new AdapterUsers(getActivity(),userList);
                    recyclerViews.setAdapter(adapterUsers);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
