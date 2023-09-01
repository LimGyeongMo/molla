package com.example.daegurobus.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.R;
import com.example.daegurobus.BusStationDetailActivity;
import com.example.daegurobus.network.NetworkPresenter;
import com.example.daegurobus.databinding.BusListStationBinding;
import com.example.daegurobus.model.RecentStationKeyword;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;

public class BusStationRecentKeywordAdapter extends RecyclerView.Adapter<BusStationRecentKeywordAdapter.ViewHolder> {
    private ArrayList<RecentStationKeyword> items;
    protected ArrayList<RecentStationKeyword> savedItems;
    private Context context;

    private NetworkPresenter presenter;

    public BusStationRecentKeywordAdapter(Context context, ArrayList<RecentStationKeyword> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_list_station, viewGroup, false);
        return new ViewHolder(view); // 뷰 홀더를 생성하여 리턴
    }

    @Override
    public void onBindViewHolder(@NonNull BusStationRecentKeywordAdapter.ViewHolder holder, int position) {
        RecentStationKeyword item = items.get(position);
        holder.binding.busListName.setText(item.getStationName());
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
//        holder.binding.igBookmark.setOnClickListener(new View.OnClickListener() {
//
//            boolean isButtonPressed = false;
//            @Override
//            public void onClick(View v) {
//                if (isButtonPressed) {
//                    presenter.bookmarkInsert("2", "",  item.getStationId(),"","", new AuthBaseInterface<String>() {
//                        @Override
//                        public void success(String item) {
//                            holder.binding.igBookmark.setImageResource(R.drawable.ic_on_star_40_pt);
//                        }
//                        @Override
//                        public void error(String message) {
//                        }
//                        @Override
//                        public void errorAuth() {
//                        }
//                    });
//                }else {
//                    presenter.bookmarkDelete("2", "", item.getStationId(),"","", new AuthBaseInterface<String>() {
//                        @Override
//                        public void success(String item) {
//                            holder.binding.igBookmark.setImageResource(R.drawable.ic_off_star_40_pt);
//                        }
//                        @Override
//                        public void error(String message) {
//                        }
//                        @Override
//                        public void errorAuth() {
//                        }
//                    });
//                }
//                isButtonPressed = !isButtonPressed;
//            }
//        });

        holder.binding.igBookmark.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(v.getContext(), BusStationDetailActivity.class);
                intent.putExtra("stationId",item.getStationId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<RecentStationKeyword> getItems() {
        if (items == null) {
            return new ArrayList<>();
        }

        return items;
    }

    public RecentStationKeyword getItem(int position) {
        if (items == null) {
            return null;
        }

        return items.get(position);
    }

    public void initItems(ArrayList<RecentStationKeyword> list) {
        if (items == null) {
            items = new ArrayList<>();
            items.addAll(list);
        } else {
            // items.clear();
            items = list;
        }

        //  items.addAll(list);

        initSavedItems(items);

        notifyDataSetChanged();
    }

    public void initSavedItems(ArrayList<RecentStationKeyword> list) {
        if (savedItems == null) {
            savedItems = new ArrayList<>();
        } else {
            savedItems.clear();
        }

        savedItems.addAll(list);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private BusListStationBinding binding;
        ViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);


        }
    }
}
