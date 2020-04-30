package com.example.myfoodappv2.ui.elderly;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.myfoodappv2.ElderlyViewModel;
import com.example.myfoodappv2.HomeActivity;
import com.example.myfoodappv2.R;
import com.example.myfoodappv2.ui.home.HomeFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ElderlyFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ElderlyViewModel viewModel;
    private EditText elderlyName;
    private EditText elderlyGender;
    private EditText elderlyDescription;
    private TextView elderlyLocation;
    private EditText elderlyContactNo;
    private ImageView elderlyImage;

    private Uri imageUri;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference elderlyRef = db.collection("Elderly");
    private FirebaseAuth fAuth;
    private ImageView wName;
    private ImageView wDescription;
    private ImageView wGender;
    private ImageView wLocation;
    private ImageView wContact;
    private ImageView wImage;
    private ProgressBar progressBar;

    private Elderly elderly;
    private UploadTask uploadImageTask;
    private String elderlyId;
    private String name;
    private String description;
    private String address;
    private String status = "available";
    private String contact;
    private String gender;
    private LatLng geocode;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //views
        View view = inflater.inflate(R.layout.fragment_elderly, container, false);
        elderlyName = view.findViewById(R.id.editText_elderlyName);
        elderlyDescription = view.findViewById(R.id.editText_elderlyDescription);
        elderlyGender = view.findViewById(R.id.editText_elderlyGender);
        elderlyLocation = view.findViewById(R.id.textView_elderlyLocation);
        elderlyContactNo = view.findViewById(R.id.editText_elderlyContactNo);
        elderlyImage = view.findViewById(R.id.imageView_elderlyImage);
        Button button_elderlyLocation = view.findViewById(R.id.button_elderlyLocation);
        Button button_uploadElderlyImage = view.findViewById(R.id.button_uploadElderlyImage);
        Button button_registerElderly = view.findViewById(R.id.button_registerElderly );
        wName = view.findViewById(R.id.image_wName);
        wDescription = view.findViewById(R.id.image_wDescription);
        wGender = view.findViewById(R.id.image_wGender);
        wLocation = view.findViewById(R.id.image_wLocation);
        wContact = view.findViewById(R.id.image_wContact);
        wImage = view.findViewById(R.id.image_wImage);
        progressBar = view.findViewById(R.id.progressBar);

        wName.setVisibility(View.INVISIBLE);
        wDescription.setVisibility(View.INVISIBLE);
        wGender.setVisibility(View.INVISIBLE);
        wLocation.setVisibility(View.INVISIBLE);
        wContact.setVisibility(View.INVISIBLE);
        wImage.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        //variables
        fAuth = FirebaseAuth.getInstance();
        viewModel = ViewModelProviders.of(getActivity()).get(ElderlyViewModel.class);

        if (viewModel.getElderly() != null) {
            elderlyName.setText(viewModel.getElderly().getName());
            elderlyDescription.setText(viewModel.getElderly().getDescription());
            elderlyGender.setText(viewModel.getElderly().getGender());
            elderlyContactNo.setText(viewModel.getElderly().getContact());
        }

        if(viewModel.getPlaceInfo() != null){
            elderlyLocation.setText(viewModel.getPlaceInfo().getPlaceName());
        }

        elderlyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLocation();
            }
        });

        button_elderlyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLocation();
            }
        });

        button_uploadElderlyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        button_registerElderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upload data and image to firebase
                if (addElderly(view)) {
                    try {
                        progressBar.setVisibility(View.VISIBLE);
                        uploadFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        return view;
    }

    //Function to open file chooser of the device
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            elderlyImage.setImageURI(imageUri);
        }
    }

    //get the file extension of the image uploaded
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() throws IOException {
        if (imageUri != null) {
            //the file name of the uploaded file is the current time + the file extension
            final StorageReference fileReference = storageReference.child("Elderly/" + System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            //compressing the image
            Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 15, baos);
            byte[] data = baos.toByteArray();

            //uploading the image
            uploadImageTask = fileReference.putBytes(data);
            Task<Uri> urlTask = uploadImageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri uploadedImageUri = task.getResult();
                        //create new Elderly object
                        elderly = new Elderly(elderlyId, name, gender, description, address, status,
                                geocode.latitude, geocode.longitude, contact, uploadedImageUri.toString());
                        elderlyRef.add(elderly);
                        viewModel.setElderly(elderly);

                        //when upload complete, clear data in sharedViewModel
                        ((HomeActivity)getActivity()).clearViewModel();
                        showToast();
                        //Back to home fragment
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, new HomeFragment());
                        fragmentTransaction.commit();
                    } else {
                        Toast.makeText(getActivity(), "cannot upload file", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //Function to move to ChooseLocationMapFragment
    public void chooseLocation() {
        //save current info to shared view model
        /*int tempAmount;
        if ((editText_amount.getText().toString().trim()).equals(""))
            tempAmount = 0;
        else
            tempAmount = Integer.parseInt(editText_amount.getText().toString());*/

        Elderly tempElderly = new Elderly("", elderlyName.getText().toString(), elderlyGender.getText().toString(),
                elderlyDescription.getText().toString(), elderlyLocation.getText().toString(), status, 0.000,
                0.000, elderlyContactNo.getText().toString(), "");

        viewModel.setElderly(tempElderly);

        //open chooseElderlyFragment
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, new ChooseElderlyLocationMapFragment());
        fragmentTransaction.commit();
    }

    //to add a document into the database
    public boolean addElderly(View v) {
        boolean isComplete = true;
        //check whether the user fill in all the fields
        if ((elderlyName.getText().toString().trim()).equals("")) {
            isComplete = false;
            wName.setVisibility(View.VISIBLE);
        }else
            wName.setVisibility(View.INVISIBLE);

        if ((elderlyGender.getText().toString().trim()).equals("")) {
            isComplete = false;
            wGender.setVisibility(View.VISIBLE);
        }else
            wGender.setVisibility(View.INVISIBLE);

        if ((elderlyDescription.getText().toString().trim()).equals("")) {
            isComplete = false;
            wDescription.setVisibility(View.VISIBLE);
        }else
            wDescription.setVisibility(View.INVISIBLE);

        if ((elderlyLocation.getText().toString().trim()).equals("")) {
            isComplete = false;
            wLocation.setVisibility(View.VISIBLE);
        }else
            wLocation.setVisibility(View.INVISIBLE);

        if ((elderlyContactNo.getText().toString().trim()).equals("")) {
            isComplete = false;
            wContact.setVisibility(View.VISIBLE);
        }else
            wContact.setVisibility(View.INVISIBLE);

        if (imageUri == null) {
            isComplete = false;
            wImage.setVisibility(View.VISIBLE);
        }else
            wImage.setVisibility(View.INVISIBLE);

        if (isComplete) {
            name = elderlyName.getText().toString();
            gender = elderlyGender.getText().toString();
            description = elderlyDescription.getText().toString();
            address = elderlyLocation.getText().toString();
            status = "available";
            contact = elderlyContactNo.getText().toString();
            geocode = convertAddress(address);
            elderlyId = fAuth.getCurrentUser().getUid();
        }else
            Toast.makeText(getActivity(), "Missing Info", Toast.LENGTH_SHORT).show();
        return isComplete;
    }

    //Function to convert address to latitude and longitude
    public LatLng convertAddress(String elderlyAddress) {
        Geocoder geoCoder = new Geocoder(getActivity());
        LatLng geocode = new LatLng(5.4164, 100.3327);//default penang
        if (elderlyAddress != null && elderlyAddress.trim() != "") {
            try {
                List<Address> addressList = geoCoder.getFromLocationName(address, 3);
                for (int i = 0; i < addressList.size(); i++) {
                    geocode = new LatLng(addressList.get(i).getLatitude(), addressList.get(i).getLongitude());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return geocode;
    }


    public void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) getActivity().findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);
        toastText.setText("Elderly Registered");

        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.FILL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}

