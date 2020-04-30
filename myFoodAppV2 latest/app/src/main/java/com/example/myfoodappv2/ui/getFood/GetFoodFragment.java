package com.example.myfoodappv2.ui.getFood;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myfoodappv2.FoodDonation;
import com.example.myfoodappv2.HomeActivity;
import com.example.myfoodappv2.PickupSchedule;
import com.example.myfoodappv2.R;
import com.example.myfoodappv2.SharedViewModel;
import com.example.myfoodappv2.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;


public class GetFoodFragment extends Fragment {
    private SharedViewModel viewModel;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference PickupScheduleRef = db.collection("PickupSchedule");
    private FirebaseAuth fAuth;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private NumberPicker numberPicker;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //variables
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        fAuth = FirebaseAuth.getInstance();

        //views
        View view =  inflater.inflate(R.layout.fragment_get_food, container, false);
        FoodDonation foodDonation = viewModel.getFoodDonation();
        TextView textView_food = (TextView) view.findViewById(R.id.textView_getFood_food);
        TextView textView_amount = (TextView) view.findViewById(R.id.textView_getFood_amount);
        TextView textView_description = (TextView) view.findViewById(R.id.textView_getFood_description);
        TextView textView_address= (TextView) view.findViewById(R.id.textView_getFood_address);
        TextView textView_contactNo= (TextView) view.findViewById(R.id.textView_getFood_contact);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_getFood);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        numberPicker = (NumberPicker)view.findViewById(R.id.numberPicker);

        timePicker.setIs24HourView(false);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(viewModel.getFoodDonation().getAmount());

        //set values
        textView_food.setText(foodDonation.getFood());
        String amountStr = Integer.toString(foodDonation.getAmount()) + " pax";
        textView_amount.setText(amountStr);
        textView_description.setText(foodDonation.getDescription());
        textView_address.setText(foodDonation.getDonorAddress());
        textView_contactNo.setText(foodDonation.getDonorContactNo());
        Picasso.get().load(foodDonation.getImageUri()).placeholder(R.drawable.ic_image_placeholder).fit().centerCrop().into(imageView);
        //set value of date and time to current date and time
        Calendar c = Calendar.getInstance();
        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null);
        timePicker.setHour(c.get(Calendar.HOUR_OF_DAY));
        timePicker.setHour(c.get(Calendar.MINUTE));

        Button button_submit = (Button)view.findViewById(R.id.button_get);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                cal.set(Calendar.MONTH, datePicker.getMonth()+1);
                cal.set(Calendar.YEAR, datePicker.getYear());
                cal.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                cal.set(Calendar.MINUTE, timePicker.getMinute());

                Date date = cal.getTime();

                String foodDonationId = viewModel.getFoodDonation().getFoodDonationId();
                String currentUser = fAuth.getCurrentUser().getUid();;
                int pickupAmount = numberPicker.getValue();

                //add schedule
                PickupSchedule pickupSchedule = new PickupSchedule(foodDonationId, currentUser, date, pickupAmount);
                PickupScheduleRef.add(pickupSchedule);

                //update the amount of the foodDonation
                String currentDocument = "FoodDonation/" + foodDonationId;
                int amountBalance = viewModel.getFoodDonation().getAmount() - pickupAmount;
                db.document(currentDocument).update("amount", amountBalance);

                ((HomeActivity)getActivity()).clearViewModel();
                showToast();
                //Back to home fragment
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new HomeFragment());
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    public void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) getActivity().findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);
        toastText.setText("Request Successful");

        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.FILL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
