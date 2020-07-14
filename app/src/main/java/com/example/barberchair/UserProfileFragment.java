package com.example.barberchair;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {
    ImageView avatartvv, covertvv;
    TextView nametv;
    TextView emailtv;
    TextView  Phonenumber;
    FloatingActionButton fabb;
    ProgressDialog pd;

    FirebaseUser user;
    FirebaseAuth nAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
   StorageReference storageReference;

    String storagePath = "Users_Profile_Cover_Imgs/";

    Uri image_uri;
    //checking for profile or cover
    String ProfileOrCover;

    //Permissions Dialog
    private  static final  int CAMERA_REQUEST_CODE  = 100;
    private  static  final  int STORAGE_REQUEST_CODE =200;
    private  static  final  int IMAGE_PICK_GALLERY_CODE =300;
    private  static  final  int IMAGE_PICK_CAMERA_CODE =400;


    String cameraPermissions[];
    String storagePermission[];

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        nAuth = FirebaseAuth.getInstance();
        user=nAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference =getInstance().getReference();

        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // init views
        avatartvv = view.findViewById(R.id.avatarcustomer);
        nametv = view.findViewById(R.id.nametvcustomer);
        emailtv = view.findViewById(R.id.emailtvcustomer);
        Phonenumber = view.findViewById(R.id.phonetvcustomer);  // init views
        fabb = view.findViewById(R.id.fabcustomer);
        covertvv = view.findViewById(R.id.coverViewcustomer);
        //Init progressdialog
        pd = new ProgressDialog(getActivity());

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // check until required data is gotten

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String name = "" + dataSnapshot1.child("name").getValue();
                    String email = "" + dataSnapshot1.child("email").getValue();
                    String phone = "" + dataSnapshot1.child("phone").getValue();
                    String image = "" + dataSnapshot1.child("image").getValue();
                    String cover = "" + dataSnapshot1.child("cover").getValue();

                    //set the data
                    nametv.setText(name);
                    emailtv.setText(email) ;
                    Phonenumber.setText(phone);
                    try {
                        //if image is already set then load
                        Picasso.get().load(image).into(avatartvv);
                    } catch (Exception e){
                        // so if there nothing then set image as default
                        Picasso.get().load(R.drawable.ic_camera_alt_black_24dp).into(avatartvv);

                    }
                    try {
                        //if image is already set then load
                        Picasso.get().load(cover).into(covertvv);
                    } catch (Exception e){
                        // so if there nothing then set image as default


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        fabb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    private boolean checkStoragePermission(){
        //check if Storage permission is enabled or not
        //return true if enabled
        //return false if not enabled

        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;

    }
    private void requestStoragePermission(){

        //runtime request camera
        requestPermissions(storagePermission,STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        //check if Storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result &&  result1;

    }
    private void requestCameraPermission(){

        //runtime request camera
        requestPermissions(cameraPermissions,CAMERA_REQUEST_CODE);
    }

    private void showEditProfileDialog() {
        /*Show dialog containing options
        1 Edit profile Picture
        2 Edit cover Photo
        3 Edit Name
        4 Edit Phone
        * */
        //Options to show in the dialog
        String options[] = {" Edit profile Picture","Edit cover Photo","Edit Name "," Edit Phone "};
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set title
        builder.setTitle("Choose Action");
        //Set items in to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle this
                if(which == 0){
                    //Edit profile clicked
                    pd.setMessage("Upadting Profile Picture");
                    ProfileOrCover = "image";
                    showImagePicDialog();

                }
                else if (which == 1){
                    // Edit Cover clicked
                    pd.setMessage("Upadting cover photo");
                    ProfileOrCover = "cover";
                    showImagePicDialog();
                }
                else if (which == 2){
                    //Edit Name clicked
                    pd.setMessage("Upadting Name");
                    showNamePhoneUpdateDialog("name");

                }
                else if(which == 3){
                    //Edit Phone clicked
                    pd.setMessage("Upadting PhoneNumber");
                    showNamePhoneUpdateDialog("phone");
                }

            }
        });
        builder.create().show();
    }

    private void showNamePhoneUpdateDialog(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update "+key);//update name or phone

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);

        //add edit text
        final EditText editText = new EditText(getActivity());
        editText.setHint("Enter "+key);//Edit name or Edit phone
        linearLayout.addView(editText );
        builder.setView(linearLayout);

        //add buttons in dialog

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString().trim();

                //validate id user has entered something or not
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String,Object> result = new HashMap<>();
                    result.put(key,value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Updated...",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity()," "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Toast.makeText(getActivity(),"Please Enter " +key ,Toast.LENGTH_SHORT).show();
                }

            }
        });
        // add button in dialog to cancel
        builder.setNegativeButton(   "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //create and show dialog
        builder.create().show();

    }

    private void showImagePicDialog() {
        //Show dialog containing options Camera and Gallery To pick the Image

        /*Show dialog containing options
        1 Edit profile Picture
        2 Edit cover Photo
        3 Edit Name
        4 Edit Phone
        * */
        //Options to show in the dialog
        String options[] = {" Camera"," Gallery "};
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set title
        builder.setTitle("Pick Image from");
        //Set items in to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle this
                if(which == 0){
                    //camera clicked
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else{
                        pickFromCamera();
                    }



                }
                else if (which == 1){
                    // Gallery clicked
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        pickFromGallery();
                    }

                }


            }
        });
        builder.create().show();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //this method called when user press allow or Deny from permission request Dialog
        //here he will handle permission cases

        switch (requestCode){
            case CAMERA_REQUEST_CODE: {
                // picking from camera code first if camera and storage permissions allowed or  not
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        // permission enabled
                        pickFromCamera();
                    } else {
                        Toast.makeText(getActivity(), "Please enable Camera & Storage Permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                // picking from camera code first if camera and storage permissions allowed or  not
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        // permission enabled
                        pickFromGallery();
                    } else {
                        Toast.makeText(getActivity(), "Please enable  Storage Permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //This method will be Called after picking image from Camera or Gallery
        if(resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){

                //image is picked from gallery,get uri of image
                image_uri = data.getData();

                uploadProfileOrCoverPhoto(image_uri);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //image is picked from Camera,get uri of image
                uploadProfileOrCoverPhoto(image_uri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void uploadProfileOrCoverPhoto(final Uri uri) {
        pd.show();
        //this is used for both the cover photo and the profile picture


        String filePathAndName = storagePath+ ""+ProfileOrCover+"_"+user.getUid();

        StorageReference storageReference2 = storageReference.child(filePathAndName);
        storageReference2.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image is uploaded to storage,now get it's url and store in user's database
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        //check if image is uploaded or not and uri is received

                        if (uriTask.isSuccessful()){
                            //image uploaded
                            // add / update url in user's database

                            HashMap<String,Object> results = new HashMap<>();
                            results.put(ProfileOrCover,downloadUri.toString());

                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //adding image to the database successfully
                                            pd.dismiss();
                                            Toast.makeText(getActivity(),"Image Updated...",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //error adding  url in database of user
                                            pd.dismiss();
                                            Toast.makeText(getActivity(),"Error Updating image...",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                        else {
                            pd.dismiss();
                            Toast.makeText(getActivity(),"some error occured", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //there was some error,get and show error message,dismiss progress dialog
                        pd.dismiss();
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void pickFromGallery() {
        //pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        //Intent of picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        //put image uri
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //Intent to start camera

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }
}
