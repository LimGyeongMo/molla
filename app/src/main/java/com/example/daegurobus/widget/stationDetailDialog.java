package com.example.daegurobus.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.BusStationDetailDialogBinding;



public class stationDetailDialog extends BaseDialog  {
    private BusStationDetailDialogBinding binding;
    private String type;
    public static final String SORT_BY_ROUTE = "1";
    public static final String SORT_BY_TIME = "2";
    public static final String SORT_BY_KIND = "3";


    private CallbackListener callbackListener;

    public stationDetailDialog(@NonNull Context context, String type) {
        super(context);
        this.type= type;
    }

    public stationDetailDialog setCallbackListener(CallbackListener BusStationDetailDialogCallbackListener) {
        this.callbackListener = BusStationDetailDialogCallbackListener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.bus_station_detail_dialog, null, false);
        setContentView(binding.getRoot());

        initialize();
        initLayout();

    }


    private void initialize() {
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    private void initLayout() {
        binding.rlClose.setOnClickListener(view -> dismiss());
        buttonClick(type);
        binding.sortByRoute.setOnClickListener(view -> {
            callbackListener.click(SORT_BY_ROUTE);
            buttonClick(SORT_BY_ROUTE);
        });

        binding.sortByTime.setOnClickListener(view -> {
            callbackListener.click(SORT_BY_TIME);
            buttonClick(SORT_BY_TIME);
        });

        binding.sortByKind.setOnClickListener(view -> {
            callbackListener.click(SORT_BY_KIND);
            buttonClick(SORT_BY_KIND);
        });
    }

    private void buttonClick(String type) {
        binding.sortByRoute.setUnCheck();
        binding.sortByKind.setUnCheck();
        binding.sortByTime.setUnCheck();

        if (type == null) {
            binding.sortByRoute.setCheck();
            return;
        }

        switch (type) {
            case SORT_BY_ROUTE:
                binding.sortByRoute.setCheck();
                break;

            case SORT_BY_TIME:
                binding.sortByTime.setCheck();
                break;

            case SORT_BY_KIND:
                binding.sortByKind.setCheck();
                break;

        }
    }


    public interface CallbackListener {
        void click(String sort);

    }
}
