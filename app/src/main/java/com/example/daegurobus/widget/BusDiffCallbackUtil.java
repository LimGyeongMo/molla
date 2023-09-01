package com.example.daegurobus.widget;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.daegurobus.model.BusDetailInfo;

import java.util.ArrayList;

public class BusDiffCallbackUtil extends DiffUtil.Callback {

    private ArrayList<BusDetailInfo> oldAddressList;
    private ArrayList<BusDetailInfo> newAddressList;

    public BusDiffCallbackUtil(ArrayList<BusDetailInfo> oldAddressList, ArrayList<BusDetailInfo> newAddressList) {
        this.oldAddressList = oldAddressList;
        this.newAddressList = newAddressList;
    }

    @Override
    public int getOldListSize() {
        return oldAddressList.size();
    }

    @Override
    public int getNewListSize() {
        return newAddressList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if(oldAddressList.get(oldItemPosition).getTrafficLevel().equals(newAddressList.get(newItemPosition).getTrafficLevel())
                && oldAddressList.get(oldItemPosition).getBookmarkYn().equals(newAddressList.get(newItemPosition).getBookmarkYn())
                && oldAddressList.get(oldItemPosition).BusVisbleYn.equals(newAddressList.get(newItemPosition).BusVisbleYn)
                && oldAddressList.get(oldItemPosition).getBusNum().equals(newAddressList.get(newItemPosition).getBusNum())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (oldAddressList.get(oldItemPosition).getTrafficLevel().equals(newAddressList.get(newItemPosition).getTrafficLevel())
                && oldAddressList.get(oldItemPosition).getBookmarkYn().equals(newAddressList.get(newItemPosition).getBookmarkYn())
                && oldAddressList.get(oldItemPosition).BusVisbleYn.equals(newAddressList.get(newItemPosition).BusVisbleYn)
                && oldAddressList.get(oldItemPosition).getBusNum().equals(newAddressList.get(newItemPosition).getBusNum())) {
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

}
