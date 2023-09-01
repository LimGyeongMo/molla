package com.example.daegurobus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.daegurobus.constant.LaunchIntent;


import java.util.ArrayList;

//public class CommonEventListActivity extends BaseActivity {
//    private CommonActivityEventListBinding binding;
//
//    private static final int REQUEST_EVENT_ACTIVITY = 1;
//
//    private CommonEventAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this, R.layout.common_activity_event_list);
//
//        if (isInit()) {
//            initLayout();
//            initData();
//        } else {
//            showToast(getString(R.string.message_init_exception));
//            finish();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQUEST_EVENT_ACTIVITY:
//                if (resultCode == RESULT_OK) {
//                    finish();
//                }
//
//                break;
//        }
//    }
//
//    private boolean isInit() {
//        try {
//            projectType = getIntent().getExtras().getString(LaunchIntent.PROJECT);
//
//            if (projectType == null) {
//                return false;
//            }
//
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    private void initLayout() {
//        binding.titleView.getIconLeft1().setOnClickListener(view -> finish());
//
//        initRecyclerView();
//
//        binding.commonViewFloating.scrollTop.setOnClickListener(v -> {
//            binding.recyclerView.stopScroll();
//            binding.recyclerView.getLayoutManager().scrollToPosition(0);
//            binding.commonViewFloating.scrollTop.setVisibility(View.GONE);
//        });
//    }
//
//    private void initRecyclerView() {
//        adapter = new CommonEventAdapter(this);
//        adapter.setOnItemClickListener((view, position) -> {
//            CommonBoard commonBoard = adapter.getItem(position);
//
//            if ("Y".equals(commonBoard.getExtUrlYn())) {
//                BasicUtil.launchWebsite(this, commonBoard.getUrl2());
//            } else {
//                Intent intent = new Intent(this, CommonEventActivity.class);
//                intent.putExtra(LaunchIntent.PROJECT, projectType);
//                intent.putExtra(CommonEventActivity.SEQ, commonBoard.getSeq());
//                startActivityForResult(intent, REQUEST_EVENT_ACTIVITY);
//            }
//        });
//
//        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                refreshFloatingButton();
//            }
//        });
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        binding.recyclerView.setAdapter(adapter);
//    }
//
//    private void initData() {
//        requestEvents();
//    }
//
//    public void refreshFloatingButton() {
//        try {
//            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) binding.recyclerView.getLayoutManager());
//
//            // 첫번째 아이템이 안보일때, 그리고 마지막 아이템이 안보일때만 보이도록 설정
//            if (linearLayoutManager.findFirstVisibleItemPosition() > 0 && linearLayoutManager.findLastVisibleItemPosition() != (adapter.getItemCount() - 1)) {
//                binding.commonViewFloating.scrollTop.setVisibility(View.VISIBLE);
//            } else {
//                binding.commonViewFloating.scrollTop.setVisibility(View.GONE);
//            }
//        } catch (Exception e) {
//
//        }
//    }
//
//    private void showFailView(int iconRes, String failMessage, String failSubMessage) {
//        try {
//            binding.loResultEmpty.loView.setVisibility(View.VISIBLE);
//
//            binding.loResultEmpty.ivIcon.setImageDrawable(getDrawable(iconRes));
//            binding.loResultEmpty.tvFailMessage.setText(failMessage);
//            binding.loResultEmpty.tvFailSubMessage.setText(failSubMessage);
//        } catch (Exception e) {
//            LogUtil.e(e.toString());
//        }
//    }
//
//    private void requestEvents() {
//        showLoading(binding.loLoading);
//
//        switch (projectType) {
//            case LaunchIntent.PROJECT_DELIVERY:
//            case LaunchIntent.PROJECT_FLOWER:
//                networkPresenter.boards(projectType, CommonBoard.BOARD_EVENT, anInterface);
//                break;
//
//            case LaunchIntent.PROJECT_RESERVE:
//                reserveNetworkPresenter.boards(CommonBoard.BOARD_EVENT, anInterface);
//                break;
//
////            case LaunchIntent.PROJECT_FLOWER:
////                flowerNetworkPresenter.boards(projectType, CommonBoard.BOARD_EVENT, anInterface);
////                break;
//
//            default:
//                hideLoadingAll(binding.loLoading);
//                showFailView(R.drawable.common_no_result, getString(R.string.message_init_exception), "");
//                break;
//        }
//    }
//
//    private BaseMultiInterface<CommonBoard> anInterface = new BaseMultiInterface<CommonBoard>() {
//        @Override
//        public void success(ArrayList<CommonBoard> items) {
//            hideLoadingAll(binding.loLoading);
//
//            if (items != null && items.size() > 0) {
//                adapter.initItems(items);
//            } else {
//                showFailView(R.drawable.common_no_result, getString(R.string.have_no_event), "");
//            }
//        }
//
//        @Override
//        public void error(String message) {
//            hideLoadingAll(binding.loLoading);
//            showFailView(R.drawable.common_no_result, message, "");
//        }
//    };
//}