package com.example.daegurobus.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.daegurobus.R;

import com.example.daegurobus.adapter.CommonNoticeAdapter;
import com.example.daegurobus.databinding.CommonActivityNoticeBinding;
import com.example.daegurobus.databinding.CommonActivityNoticeListBinding;
import com.example.daegurobus.model.NoticeList;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.network.resultInterface.GetNoticeListInterface;
import com.example.daegurobus.widget.MyDividerItemDecoration;

public class BusNoticelistFragment extends BusBaseFragment {
    private CommonActivityNoticeListBinding binding;
    private CommonNoticeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_activity_notice_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = CommonActivityNoticeListBinding.bind(getView());

        initLayout();
    }

    private void initLayout() {
        requestNotice();
    }


    private void initRecyclerView() {
        adapter = new CommonNoticeAdapter(getContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), MyDividerItemDecoration.VERTICAL_LIST, R.drawable.common_list_divider_1_f2f2f2));
        binding.recyclerView.setAdapter(adapter);

    }

    private void requestNotice() {
        RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.GET_NOTICE_LIST);
        requestBus.addParam("3");
        requestBus.addParam("3");
        requestBus.addParam("1");
        requestBus.addParam("1");
        requestBus.commit();
        busNetworkPresenter.getNoticeList(requestBus, new GetNoticeListInterface() {
            @Override
            public void success(NoticeList noticeList) {
                initRecyclerView();
                adapter.initItem(noticeList);
            }

        });
    }

}
