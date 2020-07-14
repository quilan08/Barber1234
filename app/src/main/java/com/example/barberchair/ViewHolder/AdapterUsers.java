package com.example.barberchair.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberchair.Model.ModelUser;
import com.example.barberchair.R;
import com.example.barberchair.UsersFragment;
import com.google.android.gms.plus.model.people.Person;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;


public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.Myholder>{


    Context context;
    List<ModelUser> userList;

    public AdapterUsers(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview,parent,false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        //its here that everything will be arrange according to NEAREST location.

        String useName = userList.get(position).getName();
        String userPhone = userList.get(position).getPhone();
        final String userEmail = userList.get(position).getEmail();
        String userImage = userList.get(position).getImage();

        holder.phoneTv.setText(userPhone);
        holder.emailTv.setText(userEmail);
        holder.nametv.setText(useName);

        try{
            Picasso.get().load(userImage).placeholder(R.drawable.ic_camera_alt_black_24dp).into(holder.imageView);
        }
        catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,""+userEmail,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class Myholder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView phoneTv;
        public TextView emailTv;
        public TextView nametv;

        public Myholder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imagecd);
            nametv = itemView.findViewById(R.id.namecd);
              emailTv= itemView.findViewById(R.id.emailcd);
            phoneTv  = itemView.findViewById(R.id.phonecd);

        }
    }
}
