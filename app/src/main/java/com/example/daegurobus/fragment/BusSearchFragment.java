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

import com.example.daegurobus.BusDetailActivity;
import com.example.daegurobus.BusSearchInfoActivity;
import com.example.daegurobus.R;

import com.example.daegurobus.adapter.BusSearchAtAdapter;
import com.example.daegurobus.adapter.BusSearchRecentKeywordAdapter;
import com.example.daegurobus.adapter.busSearchRcAdapter;
import com.example.daegurobus.app.MyPreferencesManager;
import com.example.daegurobus.network.BusInterface;
import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.databinding.ActivityBusSearchFragmentBinding;
import com.example.daegurobus.model.BusResultSearch;
import com.example.daegurobus.model.RecentKeyword;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.widget.BusRecentSearchDialog;


import java.util.ArrayList;


public class BusSearchFragment extends BusBaseFragment {


    private ArrayList<RecentKeyword> data;
    private busSearchRcAdapter searchResultAdapter;
    private BusSearchAtAdapter autoSearchAdapter;
    private BusSearchRecentKeywordAdapter recentKeywordAdapter;
    private MyPreferencesManager myPreferencesManager;

    private ActivityBusSearchFragmentBinding binding;
    private String Keyword;
    private String savedKeyword;
    private String savedItem;

    private String query;

    private final String autoSearch = "1";
    private final String SearchResult = "2";
    private final String clickSearch = "3";

    private static final int TAB_STATION = 1;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_bus_search_fragment, container, false);

        initLayout();

        View view = binding.getRoot();

        return view;

    }

    private void initLayout() {
        myPreferencesManager = MyPreferencesManager.getInstance(getContext());

        initEdittext();
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

    private void initEdittext() {

        binding.etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                refreshInputClearView();
                savedKeyword = s.toString();
                requestGetBusList(autoSearch, savedKeyword);
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
                    requestGetBusList(SearchResult, input);
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
            binding.searchResultRecyclerView.setVisibility(View.INVISIBLE);
            showKeyboard(binding.etKeyword);
        });

        binding.ivRight1.setOnClickListener(view -> {
            String input = binding.etKeyword.getText().toString();

            if (input.length() == 0) {
                showFailView(R.drawable.img_dalsoo_none_1, "검색결과가 없습니다.");
            } else {
                binding.atRecyclerview.setVisibility(View.GONE);
                requestGetBusList(SearchResult, input);
                binding.searchResultRecyclerView.setVisibility(View.VISIBLE);
            }
            requestFocus(view);
            hideKeyboard(view);
        });


    }

    // 자동완성 검색결과 "1"
    private void rvAutocompletion() {

        autoSearchAdapter = new BusSearchAtAdapter(getContext());
        autoSearchAdapter.setOnItemClickListener((View, Position) -> {
            BusResultSearch items = autoSearchAdapter.getItem(Position);

            processSearchKeyword(items.getRouteId());
            binding.atRecyclerview.setVisibility(View.GONE);
            binding.keyboardTopBtn.setVisibility(View.GONE);
            binding.searchResultRecyclerView.setVisibility(View.VISIBLE);
            binding.loSearchEmpty.loView.setVisibility(View.GONE);
            binding.loResultEmpty.loView.setVisibility(View.GONE);

            hideKeyboard(View);

        });
        binding.atRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.atRecyclerview.setAdapter(autoSearchAdapter);
        autoSearchAdapter.setKeyword(Keyword);


    }

    private void processSearchKeyword(String input) {
        hideKeyboard(binding.etKeyword);

        binding.etKeyword.clearFocus();

        requestGetBusList(clickSearch, input);

    }

    // 텍스트 박스 검색완료시 "2", 자동완성 클릭시 결과리스트 "3"
    private void rvSearchResult(String busNum, ArrayList<BusResultSearch> results) {

        searchResultAdapter = new busSearchRcAdapter(getContext());

        searchResultAdapter.setOnItemClickListener((View, Position) -> {
            BusResultSearch items = searchResultAdapter.getItem(Position);

            Intent intent;
            intent = new Intent(getActivity(), BusDetailActivity.class);
            intent.putExtra("routeId", items.getRouteId());
            intent.putExtra("routeTp", items.getRouteTp());
            getActivity().startActivity(intent);
            data = new ArrayList<>();

            RecentKeyword recentKeyword = new RecentKeyword();
            recentKeyword.setRouteCode(items.getRouteCode());
            recentKeyword.setRouteId(items.getRouteId());
            recentKeyword.setRouteNum(items.getRouteNum());
            recentKeyword.setRouteNum2(items.getRouteNum2());
            recentKeyword.setRouteTp(items.getRouteTp());
            recentKeyword.setStartNodeName(items.getStartNodeName());
            recentKeyword.setEndNodeName(items.getEndNodeName());
            recentKeyword.setBusBookMarkYn(items.getBookMark_yn());
            data.add(recentKeyword);
            requestRecentPage(recentKeyword);
        });
        binding.searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.searchResultRecyclerView.setAdapter(searchResultAdapter);


//        if ("DGB".equals(busNum.substring(0,2))){
//            searchResultAdapter.setKeyword(busNum);
//        }else {
//            searchResultAdapter.setKeyword(savedKeyword);
//        }

        searchResultAdapter.initItems(results);
        searchResultAdapter.notifyDataSetChanged();

    }


    private void initRecentSearchView() {
        initRecyclerViewRecentKeyword();

        binding.tvDeleteAll.setOnClickListener(View -> {

            new BusRecentSearchDialog(getContext())
                    .setCallbackListener(new BusRecentSearchDialog.CallbackListener() {
                        @Override
                        public void click() {
                            myPreferencesManager.setRecentSearchBus(new ArrayList<>());
                            recentKeywordAdapter.initItems(myPreferencesManager.getRecentSearchBus());
                            recentKeywordAdapter.notifyDataSetChanged();
                            binding.loSearchEmpty.loView.setVisibility(View.VISIBLE);
                            binding.loRecentSearchKeyword.setVisibility(View.INVISIBLE);
                        }
                    })
                    .show();
        });

        recentKeywordAdapter.initItems(myPreferencesManager.getRecentSearchBus());
    }

    private void initRecyclerViewRecentKeyword() {
        recentKeywordAdapter = new BusSearchRecentKeywordAdapter(getContext(), myPreferencesManager.getRecentSearchBus());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvRecentSearchKeyword.setLayoutManager(layoutManager);
        binding.rvRecentSearchKeyword.setAdapter(recentKeywordAdapter);

    }

    private void setButtonStyle(View button, int textColor, int drawable) {
//        GradientDrawable btnBackground = (GradientDrawable) button.getBackground();
//        btnBackground.setStroke(1, getResources().getColor(strokeColor));
        button.setBackground(getResources().getDrawable(drawable));
        ((TextView) button).setTextColor(getResources().getColor(textColor));
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
        });

        binding.tvStandardBtn.setOnClickListener(View -> {
            binding.etKeyword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);

            setButtonStyle(binding.tvStandardBtn, R.color.color_FFFFFF, R.drawable.bg_round_line_15);
            setButtonStyle(binding.tvIntBtn, R.color.color_white_80, R.drawable.bg_round_line_15_white_80);
        });
    }


    private void refreshRecentSearchKeyword() {
        binding.loRecentSearchKeyword.setVisibility(myPreferencesManager.getRecentSearchBus().size() > 0 ? View.VISIBLE : View.GONE);
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

    private void requestRecentPage(RecentKeyword recentKeyword) {
        savedItem = searchResultAdapter.getKeyword();

        for (int i = 0; i < recentKeywordAdapter.getItems().size(); i++) {
            if (savedItem.equals(recentKeywordAdapter.getItem(i).getRouteNum())) {
                recentKeywordAdapter.getItems().remove(i);
                break;
            }
        }

        recentKeywordAdapter.getItems().add(0, recentKeyword);

        int maxSearchKeywordCount = getResources().getInteger(R.integer.bus_max_count_saver_search_item);

        if (recentKeywordAdapter.getItems().size() > maxSearchKeywordCount) {
            recentKeywordAdapter.getItems().remove(maxSearchKeywordCount);
        }

        myPreferencesManager.setRecentSearchBus(recentKeywordAdapter.getItems());
        recentKeywordAdapter.notifyDataSetChanged();

    }


    private void showFailView(int iconRes, String failMessage) {

        binding.loResultEmpty.loView.setVisibility(View.VISIBLE);
        binding.loResultEmpty.ivIcon.setImageDrawable(getActivity().getDrawable(iconRes));
        binding.loResultEmpty.tvFailMessage.setText(failMessage);
        binding.loResultEmpty.ivFailSubCursor1.setVisibility(View.GONE);
        binding.loResultEmpty.tvFailSubMessage.setText("정류장 검색");
        binding.loResultEmpty.loBottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof BusSearchInfoActivity) {
                    ((BusSearchInfoActivity) getActivity()).moveTab(TAB_STATION);
                }
            }
        });
    }


    private void refreshInputClearView() {
        binding.ivClear.setVisibility(binding.etKeyword.getText().toString().length() > 0 ? View.VISIBLE : View.GONE);
    }

    private void requestGetBusList(String gbn, String keyword) {
        //query = gbn + DEFINE.FD_DELIMETER + keyword;

        RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.GET_BUS_LIST);
        requestBus.addParam(gbn);
        requestBus.addParam(keyword);
        requestBus.commit();

        busNetworkPresenter.GetBusList(requestBus, new BusInterface() {
            @Override
            public void sucsess(ArrayList<BusResultSearch> atList) {

                if (gbn.equals(autoSearch)) {
                    rvAutocompletion();
                    autoSearchAdapter.setKeyword(keyword);
                    autoSearchAdapter.initItems(atList);
                } else {
                    rvSearchResult(keyword, atList);
                    if (gbn.equals(SearchResult)) {
                        searchResultAdapter.setKeyword(keyword);
                    } else {
                        searchResultAdapter.setKeyword(savedKeyword);
                    }
                }
            }

            public void error(String message) {
                if (gbn.equals(autoSearch)) {
//                    refreshRecentSearchKeyword();
                } else {
                    showFailView(R.drawable.img_dalsoo_none_1, message);
                }
            }

        });
    }
}