package com.example.daegurobus.widget;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.daegurobus.BaseActivity;
import com.example.daegurobus.app.MyPreferencesManager;
import com.example.daegurobus.constant.LaunchIntent;

public class BusBaseActivity extends BaseActivity {
    protected MyPreferencesManager busPreferencesManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
    }

    private void initialize() {

        projectType = LaunchIntent.PROJECT_BUS;
        busPreferencesManager = MyPreferencesManager.getInstance(this);

    }
}
