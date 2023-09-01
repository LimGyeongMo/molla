package com.example.daegurobus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.daegurobus.R;
import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.network.NetworkPresenter;
import com.example.daegurobus.network.resultInterface.AuthBaseInterface;
import com.example.daegurobus.databinding.BusListStationBinding;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.model.stationResult;
import com.example.daegurobus.util.BasicUtil;
import com.google.android.flexbox.FlexboxLayoutManager;

public class BusStationRcAdapter extends BaseRecyclerViewAdapter<stationResult, BusStationRcAdapter.ViewHolder> {

    private String keyword;
    private NetworkPresenter presenter;


    public BusStationRcAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        stationResult item = getItem(position);
        holder.binding.busListName.setText(item.getStationName());
        holder.binding.busListName.setText(BasicUtil.convertHighlightText(item.getStationName(), keyword, context.getColor(R.color.color_01CAFF)));
        holder.binding.tvStart.setText(item.getStationDirection());
        holder.binding.stationCode.setText(item.getStationCode());

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
        holder.binding.flexBox.setLayoutManager(flexboxLayoutManager);

        String[] keywordList = item.getKeyword().split(",");

        RecyclerView.Adapter<RecyclerView.ViewHolder> rvAdapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_station_list_flexbox, parent, false);
                return new RecyclerView.ViewHolder(view) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
              TextView textView =  holder.itemView.findViewById(R.id.keyWord);
              textView.setText(keywordList[position]);
            }

            @Override
            public int getItemCount() {
                return keywordList.length;
            }

        };
        holder.binding.flexBox.setAdapter(rvAdapter);


        switch (item.getSubwayNum()) {
            case "1":
                holder.binding.tvSubwayNum1.setVisibility(View.VISIBLE);
                holder.binding.tvSubwayNum2.setVisibility(View.GONE);
                holder.binding.tvSubwayNum3.setVisibility(View.GONE);
                break;
            case "2":
                holder.binding.tvSubwayNum1.setVisibility(View.GONE);
                holder.binding.tvSubwayNum2.setVisibility(View.VISIBLE);
                holder.binding.tvSubwayNum3.setVisibility(View.GONE);
                break;
            case "3":
                holder.binding.tvSubwayNum1.setVisibility(View.GONE);
                holder.binding.tvSubwayNum2.setVisibility(View.GONE);
                holder.binding.tvSubwayNum3.setVisibility(View.VISIBLE);
                break;
            case "1,2":
                holder.binding.tvSubwayNum1.setVisibility(View.VISIBLE);
                holder.binding.tvSubwayNum2.setVisibility(View.VISIBLE);
                holder.binding.tvSubwayNum3.setVisibility(View.GONE);
                break;
            case "1,3":
                holder.binding.tvSubwayNum1.setVisibility(View.VISIBLE);
                holder.binding.tvSubwayNum2.setVisibility(View.GONE);
                holder.binding.tvSubwayNum3.setVisibility(View.VISIBLE);
                break;
            case "2,3":
                holder.binding.tvSubwayNum1.setVisibility(View.GONE);
                holder.binding.tvSubwayNum2.setVisibility(View.VISIBLE);
                holder.binding.tvSubwayNum3.setVisibility(View.VISIBLE);
                break;
            case "":
                holder.binding.tvSubwayNum1.setVisibility(View.GONE);
                holder.binding.tvSubwayNum2.setVisibility(View.GONE);
                holder.binding.tvSubwayNum3.setVisibility(View.GONE);
                break;
        }

        presenter = new NetworkPresenter(context);
        holder.binding.igBookmark.setOnClickListener(new View.OnClickListener() {

            boolean isButtonPressed = false;

            String query = "2"+ DEFINE.FD_DELIMETER +""+ DEFINE.FD_DELIMETER +  item.getStationId()+ DEFINE.FD_DELIMETER +""+ DEFINE.FD_DELIMETER +"";


            @Override
            public void onClick(View v) {
                if (isButtonPressed) {
                    RequestBus requestBus = new RequestBus(DEFINE.MCODE,DEFINE.INSERT_BOOKMARK);
                    requestBus.addParam("2");
                    requestBus.addParam("");
                    requestBus.addParam(item.getStationId());
                    requestBus.addParam("");
                    requestBus.addParam("");
                    requestBus.commit();

                    presenter.bookmarkInsert(requestBus, new AuthBaseInterface<String>() {
                        @Override
                        public void success(String item) {
                            holder.binding.igBookmark.setImageResource(R.drawable.ic_on_star_40_pt);
                        }
                        @Override
                        public void error(String message) {
                        }
                        @Override
                        public void errorAuth() {
                        }
                    });
                }else {
                    RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.INSERT_BOOKMARK);
                    requestBus.addParam("2");
                    requestBus.addParam("");
                    requestBus.addParam(item.getStationId());
                    requestBus.addParam("");
                    requestBus.addParam("");
                    requestBus.commit();

                    presenter.bookmarkDelete(requestBus, new AuthBaseInterface<String>() {
                        @Override
                        public void success(String item) {
                            holder.binding.igBookmark.setImageResource(R.drawable.ic_off_star_40_pt);
                        }
                        @Override
                        public void error(String message) {
                        }
                        @Override
                        public void errorAuth() {
                        }
                    });
                }
                isButtonPressed = !isButtonPressed;
            }
        });


        if ("Y".equals(item.getBookMark())) {
            holder.binding.igBookmark.setImageResource(R.drawable.ic_on_star_40_pt);
        }else {
            holder.binding.igBookmark.setImageResource(R.drawable.ic_off_star_40_pt);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_list_station, viewGroup, false)); // 뷰 홀더를 생성하여 리턴
    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private BusListStationBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getLayoutPosition());
        }

    }
}
