package com.example.daegurobus.widget;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.example.daegurobus.R;
import com.example.daegurobus.adapter.RecentKeywordAdapter;
import com.example.daegurobus.app.MyPreferencesManager;
import com.example.daegurobus.databinding.BusRecentSearchDialogBinding;
import com.example.daegurobus.fragment.BusSearchFragment;

import java.util.ArrayList;

public class BusRecentSearchDialog extends BaseDialog {

    private BusRecentSearchDialogBinding binding;

    private MyPreferencesManager myPreferencesManager;
    private RecentKeywordAdapter recentKeywordAdapter;
    private int position;

    private CallbackListener callbackListener;

    public BusRecentSearchDialog(Context context){
        super(context);
    }

    public BusRecentSearchDialog setCallbackListener(CallbackListener BusRecentSearchDialogCallbackListener) {
        this.callbackListener = BusRecentSearchDialogCallbackListener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.bus_recent_search_dialog, null, false);
        setContentView(binding.getRoot());

        myPreferencesManager = MyPreferencesManager.getInstance(getContext());
        initialize();
        initLayout();
    }

    private void initialize() {
        getWindow().setGravity(Gravity.CENTER);
    }

    private void initLayout() {
        binding.tvCancel.setOnClickListener(view -> dismiss());
        binding.tvDelete.setOnClickListener(View -> {
            callbackListener.click();
            dismiss();
        });


    }

    public interface CallbackListener {
        void click();
    }
}
