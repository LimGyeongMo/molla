package com.example.daegurobus.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.BusSearchInfoActivity;
import com.example.daegurobus.BusStationDetailActivity;
import com.example.daegurobus.adapter.BusStationRecentKeywordAdapter;
import com.example.daegurobus.adapter.BusStationRcAdapter;
import com.example.daegurobus.app.MyPreferencesManager;
import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.network.resultInterface.BusStationAuto;
import com.example.daegurobus.model.RecentStationKeyword;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.model.stationResult;

import com.example.daegurobus.adapter.BusStationAtAdapter;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.ActivityBusStationFragmentBinding;
import com.example.daegurobus.widget.BusRecentSearchDialog;

import java.util.ArrayList;


public class BusStationFragment extends BusBaseFragment {


    private ActivityBusStationFragmentBinding binding;
    private ArrayList<RecentStationKeyword> data;
    private String keyword;
    private String savedKeyword;
    private String savedItem;
    private BusStationAtAdapter autoComlicationAdapter;
    private BusStationRcAdapter searchResultAdapter;
    private BusStationRecentKeywordAdapter stationRecentKeywordAdapter;

    private String autoSearch = "1";
    private String SearchResult = "2";
    private String clickSearch = "3";

    private static final int TAB_BUS = 0;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_bus_station_fragment, container, false);

        initLayout();

        View view = binding.getRoot();
        return view;
    }


    private void initLayout() {
        myPreferencesManager = MyPreferencesManager.getInstance(getContext());
        initEditLayout();
        initKeyboardTop();
        refreshRecentSearchKeyword();
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecentSearchView();
        refreshRecentSearchKeyword();
        binding.etKeyword.clearFocus();
        binding.etKeyword.setText("");
    }

    private void initEditLayout() {

        binding.etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                refreshInputClearView();
                savedKeyword = s.toString();
                requestGetStationList(autoSearch, savedKeyword );
                binding.searchResultRecyclerView.setVisibility(View.GONE);

                if (savedKeyword.length() == 0) {
                    binding.atRecyclerview.setVisibility(View.GONE);
                    refreshRecentSearchKeyword();
                } else {
                    binding.atRecyclerview.setVisibility(View.VISIBLE);
                    binding.loRecentSearchKeyword.setVisibility(View.INVISIBLE);
                    binding.loSearchEmpty.loView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        binding.etKeyword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // 엔터키가 눌렸을 때 동작 구현
                    binding.atRecyclerview.setVisibility(View.GONE);
                    String input = binding.etKeyword.getText().toString();
                    requestGetStationList(SearchResult,input);
                    binding.searchResultRecyclerView.setVisibility(View.VISIBLE);
                    hideKeyboard(v);
                    requestFocus(v);
                    return true;
                }
                return false;
            }
        });

        binding.etKeyword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.keyWord.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.color_01CAFF)));
                    binding.keyboardTopBtn.setVisibility(View.VISIBLE);
                    refreshInputClearView();

                } else {
                    binding.keyWord.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.color_999999)));
                    binding.keyboardTopBtn.setVisibility(View.GONE);
                }
            }
        });


        binding.ivClear.setOnClickListener(view -> {
            binding.etKeyword.setText("");
            refreshInputClearView();
            refreshRecentSearchKeyword();
            binding.ivRight1.setVisibility(View.VISIBLE);
            binding.searchResultRecyclerView.setVisibility(View.INVISIBLE);
            showKeyboard(binding.etKeyword);
        });

        binding.ivRight1.setOnClickListener(view -> {
            String input = binding.etKeyword.getText().toString();

            if (input.length() == 0) {
                showFailView(R.drawable.img_dalsoo_none_1, "검색결과가 없습니다.");
            } else {
                binding.atRecyclerview.setVisibility(View.GONE);
                requestGetStationList(SearchResult, input);
                binding.searchResultRecyclerView.setVisibility(View.VISIBLE);
            }
            requestFocus(view);
            hideKeyboard(view);
        });
    }
    // 자동완성 검색결과 "1"
    private void rvAutocompletion() {

        autoComlicationAdapter = new BusStationAtAdapter(getContext());
        autoComlicationAdapter.setOnItemClickListener((View, Position) -> {
            stationResult items = autoComlicationAdapter.getItem(Position);

            processSearchKeyword(items.getStationId());
            binding.atRecyclerview.setVisibility(View.GONE);
            binding.keyboardTopBtn.setVisibility(View.GONE);
            binding.searchResultRecyclerView.setVisibility(View.VISIBLE);
            binding.loSearchEmpty.loView.setVisibility(View.GONE);
            binding.loResultEmpty.loView.setVisibility(View.GONE);

            hideKeyboard(View);
        });

        binding.atRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.atRecyclerview.setAdapter(autoComlicationAdapter);
        autoComlicationAdapter.setKeyword(keyword);

    }

    private void processSearchKeyword(String input) {
        hideKeyboard(binding.etKeyword);

        binding.etKeyword.clearFocus();

        requestGetStationList(clickSearch, input);

    }
    // 텍스트 박스 검색완료시 "2", 자동완성 클릭시 결과리스트 "3"
    private void rvSearchResult(ArrayList<stationResult> stationList) {

        searchResultAdapter = new BusStationRcAdapter(getContext());
        searchResultAdapter.setOnItemClickListener((view, position) -> {
            stationResult items = searchResultAdapter.getItem(position);

            Intent intent;
            intent = new Intent(getActivity(), BusStationDetailActivity.class);
            intent.putExtra("stationId", items.getStationId());
            getActivity().startActivity(intent);
            data = new ArrayList<>();

            RecentStationKeyword recentStationKeyword = new RecentStationKeyword();
            recentStationKeyword.setStationCode(items.getStationCode());
            recentStationKeyword.setStationId(items.getStationId());
            recentStationKeyword.setStationName(items.getStationName());
            recentStationKeyword.setStationNo(items.getStationNo());
            recentStationKeyword.setStationDirection(items.getStationDirection());
            recentStationKeyword.setKeyword(items.getKeyword());
            recentStationKeyword.setSubwayNum(items.getSubwayNum());
            recentStationKeyword.setStationBookmark(items.getBookMark());

            data.add(recentStationKeyword);
            requestRecentPage(recentStationKeyword);
        });

        binding.searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.searchResultRecyclerView.setAdapter(searchResultAdapter);
        searchResultAdapter.initItems(stationList);
    }

    private void initRecentSearchView() {
        initRecyclerViewRecentKeyword();
        binding.tvDeleteAll.setOnClickListener(View -> {

            new BusRecentSearchDialog(getContext())
                    .setCallbackListener(new BusRecentSearchDialog.CallbackListener() {
                        @Override
                        public void click() {
                            myPreferencesManager.setRecentStationSearchBus(new ArrayList<>());
                            stationRecentKeywordAdapter.initItems(myPreferencesManager.getRecentStationSearchBus());
                            stationRecentKeywordAdapter.notifyDataSetChanged();
                            binding.loSearchEmpty.loView.setVisibility(View.VISIBLE);
                            binding.loRecentSearchKeyword.setVisibility(View.INVISIBLE);
                        }
                    })
                    .show();
        });

    }

    private void initRecyclerViewRecentKeyword() {
        stationRecentKeywordAdapter = new BusStationRecentKeywordAdapter(getContext(), myPreferencesManager.getRecentStationSearchBus());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvRecentSearchKeyword.setLayoutManager(layoutManager);
        binding.rvRecentSearchKeyword.setAdapter(stationRecentKeywordAdapter);

        if (stationRecentKeywordAdapter.getItemCount() == 0) {
            binding.loSearchEmpty.loView.setVisibility(View.VISIBLE);
            binding.loRecentSearchKeyword.setVisibility(View.INVISIBLE);
        } else {
            binding.loSearchEmpty.loView.setVisibility(View.INVISIBLE);
            binding.loRecentSearchKeyword.setVisibility(View.VISIBLE);
        }

        stationRecentKeywordAdapter.initItems(myPreferencesManager.getRecentStationSearchBus());
        stationRecentKeywordAdapter.notifyDataSetChanged();

    }

    private void requestRecentPage(RecentStationKeyword recentStationKeyword) {
        savedItem = searchResultAdapter.getKeyword();

        for (int i = 0; i < stationRecentKeywordAdapter.getItems().size(); i++) {
            if (savedItem.equals(stationRecentKeywordAdapter.getItem(i).getStationName())) {
                stationRecentKeywordAdapter.getItems().remove(i);
                break;
            }
        }

        stationRecentKeywordAdapter.getItems().add(0, recentStationKeyword);

        int maxSearchKeywordCount = getResources().getInteger(R.integer.bus_max_count_saver_search_item);

        if (stationRecentKeywordAdapter.getItems().size() > maxSearchKeywordCount) {
            stationRecentKeywordAdapter.getItems().remove(maxSearchKeywordCount);
        }
        myPreferencesManager.setRecentStationSearchBus(stationRecentKeywordAdapter.getItems());


        stationRecentKeywordAdapter.notifyDataSetChanged();

    }

    private void setButtonStyle(View button, int textColor, int strokeColor) {
//        GradientDrawable btnBackground = (GradientDrawable) button.getBackground();
//        btnBackground.setStroke(1, getResources().getColor(strokeColor));
        ((TextView) button).setTextColor(getResources().getColor(textColor));
        button.setBackground(getResources().getDrawable(strokeColor));
    }

    private void initKeyboardTop() {

        binding.ivKeyboardBtn.setOnClickListener(View -> {
            hideKeyboard(View);
            binding.etKeyword.clearFocus();
            binding.keyboardTopBtn.setVisibility(View.INVISIBLE);
        });

        binding.tvIntBtn.setOnClickListener(View -> {
            binding.etKeyword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


            setButtonStyle(binding.tvIntBtn, R.color.color_FFFFFF, R.drawable.bg_round_line_15);
            setButtonStyle(binding.tvStandardBtn, R.color.color_white_80, R.drawable.bg_round_line_15_white_80);
            setButtonStyle(binding.tvDashBtn, R.color.color_white_80, R.drawable.bg_round_line_15_white_80);
        });

        binding.tvStandardBtn.setOnClickListener(View -> {
            binding.etKeyword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);

            setButtonStyle(binding.tvIntBtn, R.color.color_white_80, R.drawable.bg_round_line_15_white_80);
            setButtonStyle(binding.tvStandardBtn, R.color.color_FFFFFF, R.drawable.bg_round_line_15);
            setButtonStyle(binding.tvDashBtn, R.color.color_white_80, R.drawable.bg_round_line_15_white_80);
        });

        binding.tvDashBtn.setOnClickListener(View -> {
            String inputKeyword = binding.etKeyword.getText().toString();
            binding.etKeyword.setText(inputKeyword + "-");
            binding.etKeyword.setSelection(binding.etKeyword.length());

            setButtonStyle(binding.tvIntBtn, R.color.color_white_80, R.drawable.bg_round_line_15_white_80);
            setButtonStyle(binding.tvStandardBtn, R.color.color_white_80, R.drawable.bg_round_line_15_white_80);
            setButtonStyle(binding.tvDashBtn, R.color.color_FFFFFF, R.drawable.bg_round_line_15);
        });
    }


    private void refreshRecentSearchKeyword() {
        binding.loRecentSearchKeyword.setVisibility(myPreferencesManager.getRecentStationSearchBus().size() > 0 ? View.VISIBLE : View.GONE);
        binding.loRecentSearchKeyword.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(v.getVisibility() == View.VISIBLE) {
                    binding.nestScrollView.setBackgroundColor(getContext().getColor(R.color.white));
                } else {
                    binding.nestScrollView.setBackgroundColor(getContext().getColor(R.color.color_E6E6E6));
                }
            }
        });
    }

    private void refreshInputClearView() {
        binding.ivClear.setVisibility(binding.etKeyword.getText().toString().length() > 0 ? View.VISIBLE : View.GONE);
    }

    private void requestGetStationList(String gbn, String keyword) {

        RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.GET_STATION_LIST);
        requestBus.addParam(gbn);
        requestBus.addParam(keyword);
        requestBus.commit();
        busNetworkPresenter.getStationList(requestBus, new BusStationAuto() {
            @Override
            public void success(ArrayList<stationResult> stationList) {
                if (gbn.equals(autoSearch)) {
                    rvAutocompletion();
                    autoComlicationAdapter.setKeyword(keyword);
                    autoComlicationAdapter.initItems(stationList);
                } else {
                    rvSearchResult(stationList);
                    if (gbn.equals(SearchResult)) {
                        searchResultAdapter.setKeyword(keyword);
                    } else {
                        searchResultAdapter.setKeyword(savedKeyword);
                    }
                }
            }

            @Override
            public void error(String message) {
                if (gbn.equals(autoSearch)) {
                } else {
                    showFailView(R.drawable.img_dalsoo_none_1, message);
                }

            }
        });
    }

    private void showFailView(int iconRes, String failMessage) {

        binding.loResultEmpty.loView.setVisibility(View.VISIBLE);
        binding.loResultEmpty.ivIcon.setImageDrawable(getActivity().getDrawable(iconRes));
        binding.loResultEmpty.tvFailMessage.setText(failMessage);
        binding.loResultEmpty.ivFailSubCursor2.setVisibility(View.GONE);
        binding.loResultEmpty.tvFailSubMessage.setText("버스검색");
        binding.loResultEmpty.loBottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof BusSearchInfoActivity) {
                    ((BusSearchInfoActivity) getActivity()).moveTab(TAB_BUS);
                }
            }
        });
    }

}


