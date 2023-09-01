package com.example.daegurobus.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.BusDetailActivity;
import com.example.daegurobus.R;
import com.example.daegurobus.network.NetworkPresenter;
import com.example.daegurobus.databinding.BusListSearchBinding;
import com.example.daegurobus.model.RecentKeyword;

import java.util.ArrayList;

public class BusSearchRecentKeywordAdapter extends RecyclerView.Adapter<BusSearchRecentKeywordAdapter.ViewHolder> {
    private ArrayList<RecentKeyword> items;
    protected ArrayList<RecentKeyword> savedItems;
    private Context context;
    private String keyword;
    private NetworkPresenter presenter;
    private final String GAN = "1";
    private final String JI = "2";
    private final String SUN = "3";
    private final String FAST = "4";
    private final String WORK = "5";

    public BusSearchRecentKeywordAdapter(Context context, ArrayList<RecentKeyword> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_list_search, viewGroup, false);
        return new ViewHolder(view); // 뷰 홀더를 생성하여 리턴
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentKeyword item = items.get(position);
        if ("".equals(item.getRouteNum2())) {
            holder.binding.tvRouteNum.setText(item.getRouteNum());

        } else {
            holder.binding.tvRouteNum.setText(item.getRouteNum() + "(" + item.getRouteNum2() + ")");
        }

        switch (item.getRouteTp()) {
            case GAN:
                holder.binding.tpRoute.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_018EEC)));
                holder.binding.tpRoute.setText("간선");
                break;
            case JI:
                holder.binding.tpRoute.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_F7B744)));
                holder.binding.tpRoute.setText("지선");
                break;
            case SUN:
                holder.binding.tpRoute.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_008142)));
                holder.binding.tpRoute.setText("순환");
                break;
            case FAST:
                holder.binding.tpRoute.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_EB0220)));
                holder.binding.tpRoute.setText("급행");
                break;
            case WORK:
                holder.binding.tpRoute.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_8AD644)));
                holder.binding.tpRoute.setText("출근");
                break;
        }
        holder.binding.tvNodeName.setText(item.getStartNodeName() + " ↔ " + item.getEndNodeName());


        presenter = new NetworkPresenter(context);
//        holder.binding.igBookmark.setOnClickListener(new View.OnClickListener() {
//
//            boolean isButtonPressed = false;
//            @Override
//            public void onClick(View v) {
//                if (isButtonPressed) {
//                    presenter.bookmarkInsert("1", item.getRouteId(), "","","", new AuthBaseInterface<String>() {
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
//                    presenter.bookmarkDelete("1", item.getRouteId(), "", "","",new AuthBaseInterface<String>() {
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
                intent = new Intent(v.getContext(), BusDetailActivity.class);
                intent.putExtra("routeId", item.getRouteId());
                intent.putExtra("routeTp", item.getRouteTp());
                v.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<RecentKeyword> getItems() {
        if (items == null) {
            return new ArrayList<>();
        }

        return items;
    }

    public RecentKeyword getItem(int position) {
        if (items == null) {
            return null;
        }

        return items.get(position);
    }


    public void initItems(ArrayList<RecentKeyword> list) {
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

    public void initSavedItems(ArrayList<RecentKeyword> list) {
        if (savedItems == null) {
            savedItems = new ArrayList<>();
        } else {
            savedItems.clear();
        }

        savedItems.addAll(list);
        notifyDataSetChanged();
    }

    public String getKeyword() {
        return keyword;
    }


    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private BusListSearchBinding binding;

        ViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);


        }
    }
}