package com.example.myfoodappv2;

import androidx.lifecycle.ViewModel;

import com.example.myfoodappv2.ui.bonus.Voucher;

public class VoucherViewModel extends ViewModel {
    private Voucher voucher;
    public Voucher getVoucher(){return voucher;}

    public void setVoucher(Voucher voucher){
        this.voucher = new Voucher(voucher.getVoucherId(), voucher.getUserId(), voucher.getName(), voucher.getExpiredDate(),
                voucher.getImageId(), voucher.getAddress(), voucher.getStatus(), voucher.getValue());
    }

}
