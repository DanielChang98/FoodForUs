package com.example.myfoodappv2;

import androidx.lifecycle.ViewModel;

import com.example.myfoodappv2.ui.elderly.Elderly;
import com.example.myfoodappv2.ui.elderly.ElderlyPlaceInfo;

public class ElderlyViewModel extends ViewModel {
    private Elderly elderly;
    private ElderlyPlaceInfo elderlyPlaceInfo;
    public Elderly getElderly()  {return elderly; }

    public void setElderly(Elderly elderly) {
        this.elderly = new Elderly(elderly.getElderlyId(), elderly.getName(), elderly.getGender(), elderly.getDescription(),
                elderly.getAddress(), elderly.getStatus(), elderly.getElderlyLocationLatitude(), elderly.getElderlyLocationLongitude(),
                elderly.getContact(), elderly.getImageUri());
    }

    public ElderlyPlaceInfo getPlaceInfo() {
        return elderlyPlaceInfo;
    }

    public void setPlaceInfo(ElderlyPlaceInfo elderlyPlaceInfo) {
        this.elderlyPlaceInfo = new ElderlyPlaceInfo(elderlyPlaceInfo.getPlaceName(), elderlyPlaceInfo.getLatitude(), elderlyPlaceInfo.getLongitude());
    }
}
