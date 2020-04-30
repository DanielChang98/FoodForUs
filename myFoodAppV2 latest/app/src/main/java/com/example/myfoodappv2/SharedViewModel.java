package com.example.myfoodappv2;

import androidx.lifecycle.ViewModel;

import com.example.myfoodappv2.ui.donateFood.PlaceInfo;

public class SharedViewModel extends ViewModel {

   private FoodDonation foodDonation;
   private PlaceInfo placeInfo;
   public FoodDonation getFoodDonation() {
        return foodDonation;
    }


    public void setFoodDonation(FoodDonation foodDonation) {
        this.foodDonation = new FoodDonation(foodDonation.getDonorId(), foodDonation.getDate(),
                foodDonation.getFood(), foodDonation.getDescription(), foodDonation.getAmount(),
                foodDonation.getDonorAddress(), foodDonation.getDonorLocationLatitude(), foodDonation.getDonorLocationLongitude(),
                foodDonation.getDonorContactNo(), foodDonation.getImageUri());
    }

    public PlaceInfo getPlaceInfo() {
        return placeInfo;
    }

    public void setPlaceInfo(PlaceInfo placeInfo) {
        this.placeInfo = new PlaceInfo(placeInfo.getPlaceName(), placeInfo.getLatitude(), placeInfo.getLongitude());
    }
}
