package com.example.myfoodappv2.ui.donateFood;


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


import com.example.myfoodappv2.FoodDonation;
import com.example.myfoodappv2.HomeActivity;
import com.example.myfoodappv2.R;
import com.example.myfoodappv2.SharedViewModel;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class DonateFoodFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;

    private SharedViewModel viewModel;
    private EditText editText_food;
    private EditText editText_amount;
    private EditText editText_description;
    private TextView textView_location;
    private EditText editText_contactNo;
    private ImageView image_food;

    private Uri imageUri;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference foodDonationRef = db.collection("FoodDonation");
    private FirebaseAuth fAuth;
    private ImageView imageWarningFood;
    private ImageView imageWarningDesc;
    private ImageView imageWarningAmount;
    private ImageView imageWarningLocation;
    private ImageView imageWarningContact;
    private ImageView imageWarningImage;
    private ProgressBar progressBar;

    private UploadTask uploadImageTask;
    private FoodDonation foodDonation;
    private String donorId;
    private Date date;
    private String food;
    private String description;
    private int amount;
    private String donorAddress;
    private String donorContactNo;
    private LatLng geocode;
    private PlaceInfo placeInfo;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       //views
        View view = inflater.inflate(R.layout.fragment_donate_food, container, false);
        editText_food = view.findViewById(R.id.editText_donateFoodTitle);
        editText_description = view.findViewById(R.id.editText_donateFoodDescription);
        editText_amount = view.findViewById(R.id.editText_donateFoodAmount);
        textView_location = view.findViewById(R.id.textView_donateFoodLocation);
        editText_contactNo = view.findViewById(R.id.editText_donateFoodContactNo);
        image_food = view.findViewById(R.id.imageView_donateFoodImage);
        Button button_chooseLocation = view.findViewById(R.id.button_donateFoodLocation);
        Button button_uploadImage = view.findViewById(R.id.button_uploadFoodImage);
        Button button_donate = view.findViewById(R.id.button_donate);
        imageWarningFood = view.findViewById(R.id.image_dfWarningFood);
        imageWarningDesc = view.findViewById(R.id.image_dfWarningDescription);
        imageWarningAmount = view.findViewById(R.id.image_dfWarningAmount);
        imageWarningLocation = view.findViewById(R.id.image_dfWarningLocation);
        imageWarningContact = view.findViewById(R.id.image_dfWarningContact);
        imageWarningImage = view.findViewById(R.id.image_dfWarningImage);
        progressBar = view.findViewById(R.id.progressBar);

        imageWarningFood.setVisibility(View.INVISIBLE);
        imageWarningDesc.setVisibility(View.INVISIBLE);
        imageWarningAmount.setVisibility(View.INVISIBLE);
        imageWarningLocation.setVisibility(View.INVISIBLE);
        imageWarningContact.setVisibility(View.INVISIBLE);
        imageWarningImage.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        //variables
        fAuth = FirebaseAuth.getInstance();
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        if (viewModel.getFoodDonation() != null) {
            editText_food.setText(viewModel.getFoodDonation().getFood());
            editText_description.setText(viewModel.getFoodDonation().getDescription());
            editText_amount.setText(Integer.toString(viewModel.getFoodDonation().getAmount()));
            editText_contactNo.setText(viewModel.getFoodDonation().getDonorContactNo());
        }

        if(viewModel.getPlaceInfo() != null){
            textView_location.setText(viewModel.getPlaceInfo().getPlaceName());
        }

        textView_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLocation();
            }
        });

        button_chooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLocation();
            }
        });

        button_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        button_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upload data and image to firebase
                if (addFoodDonation(view)) {
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

            image_food.setImageURI(imageUri);
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
            final StorageReference fileReference = storageReference.child("FoodDonation/" + System.currentTimeMillis()
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
                        //create new FoodDonation object
                        foodDonation = new FoodDonation(donorId, date, food, description, amount, donorAddress, geocode.latitude, geocode.longitude, donorContactNo, uploadedImageUri.toString());
                        foodDonationRef.add(foodDonation);
                        viewModel.setFoodDonation(foodDonation);

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
        int tempAmount;
        if ((editText_amount.getText().toString().trim()).equals(""))
            tempAmount = 0;
        else
            tempAmount = Integer.parseInt(editText_amount.getText().toString());

        FoodDonation tempDonation = new FoodDonation("", Calendar.getInstance().getTime(),
                editText_food.getText().toString(), editText_description.getText().toString(),
                tempAmount, textView_location.getText().toString(), 0.000, 0.000,
                editText_contactNo.getText().toString(), "");
        viewModel.setFoodDonation(tempDonation);

        //open chooseLocationMapFragment
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, new ChooseLocationMapFragment());
        fragmentTransaction.commit();
    }

    //to add a document into the database
    public boolean addFoodDonation(View v) {
        boolean isComplete = true;
        //check whether the user fill in all the fields
        if ((editText_food.getText().toString().trim()).equals("")) {
            isComplete = false;
            imageWarningFood.setVisibility(View.VISIBLE);
        }else
            imageWarningFood.setVisibility(View.INVISIBLE);

        if ((editText_description.getText().toString().trim()).equals("")) {
            isComplete = false;
            imageWarningDesc.setVisibility(View.VISIBLE);
        }else
            imageWarningDesc.setVisibility(View.INVISIBLE);

        if ((editText_amount.getText().toString().trim()).equals("")) {
            isComplete = false;
            imageWarningAmount.setVisibility(View.VISIBLE);
        }else if(Integer.parseInt(editText_amount.getText().toString()) == 0){
            imageWarningAmount.setVisibility(View.VISIBLE);
            isComplete = false;
        }else
            imageWarningAmount.setVisibility(View.INVISIBLE);

        if ((textView_location.getText().toString().trim()).equals("")) {
            isComplete = false;
            imageWarningLocation.setVisibility(View.VISIBLE);
        }else
            imageWarningLocation.setVisibility(View.INVISIBLE);

        if ((editText_contactNo.getText().toString().trim()).equals("")) {
            isComplete = false;
            imageWarningContact.setVisibility(View.VISIBLE);
        }else
            imageWarningContact.setVisibility(View.INVISIBLE);

        if (imageUri == null) {
            isComplete = false;
            imageWarningImage.setVisibility(View.VISIBLE);
        }else
            imageWarningImage.setVisibility(View.INVISIBLE);

        if(isComplete && Integer.parseInt(editText_amount.getText().toString()) == 0){
            Toast.makeText(getActivity(), "Cannot donate 0 item", Toast.LENGTH_SHORT).show();
        }
        else if (isComplete) {
            food = editText_food.getText().toString();
            description = editText_description.getText().toString();
            amount = Integer.parseInt(editText_amount.getText().toString());
            donorAddress = textView_location.getText().toString();
            donorContactNo = editText_contactNo.getText().toString();
            geocode = convertAddress(donorAddress);
            donorId = fAuth.getCurrentUser().getUid();
            date = Calendar.getInstance().getTime();
        }else
            Toast.makeText(getActivity(), "Missing Info", Toast.LENGTH_SHORT).show();
        return isComplete;
    }

    //Function to convert address to latitude and longitude
    public LatLng convertAddress(String donorAddress) {
        Geocoder geoCoder = new Geocoder(getActivity());
        LatLng geocode = new LatLng(5.4164, 100.3327);//default penang
        if (donorAddress != null && donorAddress.trim() != "") {
            try {
                List<Address> addressList = geoCoder.getFromLocationName(donorAddress, 3);
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
        toastText.setText("Donation Added");

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
