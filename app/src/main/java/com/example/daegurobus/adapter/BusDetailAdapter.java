package com.example.daegurobus.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.R;
import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.network.NetworkPresenter;
import com.example.daegurobus.network.resultInterface.AuthBaseInterface;
import com.example.daegurobus.databinding.BusDetailListBinding;
import com.example.daegurobus.model.BusDetailInfo;
import com.example.daegurobus.model.BusDetailInfo1;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.widget.BusDiffCallbackUtil;


import java.util.ArrayList;

public class BusDetailAdapter extends BaseRecyclerViewAdapter<BusDetailInfo, BusDetailAdapter.ViewHolder> {

    private ArrayList<BusDetailInfo1> dataList;
    private NetworkPresenter presenter;
    private BusDetailInfo savedItem;

    private final String GAN = "1";
    private final String JI = "2";
    private final String SUN = "3";
    private final String FAST = "4";
    private final String WORK = "5";
    private final String GUNWI = "6";
    private int selectedPosition = -1;

    public BusDetailAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindView(ViewHolder holder, int position) {
        BusDetailInfo item = getItem(position);
        savedItem = item;
            if (position == 0) {
                holder.binding.loForm.setPadding(0, 20,0, 0);
                holder.binding.trafficTop.setVisibility(View.INVISIBLE);
                holder.binding.trafficBotton.setVisibility(View.VISIBLE);
                holder.binding.vBottomLine.setVisibility(View.VISIBLE);
                holder.binding.loBottom.setVisibility(View.GONE);
            } else if (position == getItemCount() - 1) {
                holder.binding.loForm.setPadding(0, 0,0, 0);
                holder.binding.trafficTop.setVisibility(View.VISIBLE);
                holder.binding.trafficBotton.setVisibility(View.INVISIBLE);
                holder.binding.vBottomLine.setVisibility(View.GONE);
                holder.binding.loBottom.setVisibility(View.VISIBLE);
            } else {
                holder.binding.loForm.setPadding(0, 0,0, 0);
                holder.binding.trafficBotton.setVisibility(View.VISIBLE);
                holder.binding.trafficTop.setVisibility(View.VISIBLE);
                holder.binding.vBottomLine.setVisibility(View.VISIBLE);
                holder.binding.loBottom.setVisibility(View.GONE);
            }

        holder.binding.tvStationName.setText(item.getStationName());
        holder.binding.tvBusCode.setText(item.getStationNo());
        holder.binding.tvVehicleTime.setText(item.getStartvehicleTime() + "~" + item.getEndvehicleTime());

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

        switch (item.getTrafficLevel()) {
            case "0":
                holder.binding.trafficBotton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_CCCCCC)));
                break;
            case "1":
                holder.binding.trafficBotton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_CCCCCC)));
                break;
            case "2":
                holder.binding.trafficBotton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_0CC500)));
                break;
            case "3":
                holder.binding.trafficBotton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_FFDD00)));
                break;
            case "4":
                holder.binding.trafficBotton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_DC0700)));
                break;
        }
        if (position != 0) {
            BusDetailInfo items = getItem(position - 1);

            switch (items.getTrafficLevel()) {
                case "0":
                    holder.binding.trafficTop.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_CCCCCC)));
                    break;
                case "1":
                    holder.binding.trafficTop.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_CCCCCC)));
                    break;
                case "2":
                    holder.binding.trafficTop.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_0CC500)));
                    break;
                case "3":
                    holder.binding.trafficTop.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_FFDD00)));
                    break;
                case "4":
                    holder.binding.trafficTop.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_DC0700)));
                    break;
            }
        }

        try {
            if (position != getItemCount() - 1) {
                BusDetailInfo item1 = getItem(position + 1);

                if (!item1.getUpdownCd().equals(item.getUpdownCd())) {
                    holder.binding.loVihicle.setVisibility(View.VISIBLE);
                    holder.binding.igOrd.setVisibility(View.INVISIBLE);
                } else {
                    holder.binding.loVihicle.setVisibility(View.INVISIBLE);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        presenter = new NetworkPresenter(context);
        holder.binding.igBookmark.setOnClickListener(View -> {

            if ("Y".equals(item.getBookmarkYn())) {
                RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.DELETE_BOOKMARK);
                requestBus.addParam("3");
                requestBus.addParam(item.getRouteId());
                requestBus.addParam(item.getStationId());
                requestBus.addParam(item.getUpdownCd());
                requestBus.addParam(item.getStationOrd());
                requestBus.commit();
                presenter.bookmarkDelete(requestBus, new AuthBaseInterface<String>() {
                    @Override
                    public void success(String item) {
                        if (item.equals("00")) {
                            savedItem.setBookmarkYn("N");
                            holder.binding.igBookmark.setImageResource(R.drawable.common_off_star_40);
                        } else {
                            holder.binding.igBookmark.setImageResource(R.drawable.common_on_star_40);
                        }
                    }

                    @Override
                    public void error(String message) {
                    }

                    @Override
                    public void errorAuth() {
                    }
                });
            } else {
                RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.INSERT_BOOKMARK);
                requestBus.addParam("3");
                requestBus.addParam(item.getRouteId());
                requestBus.addParam(item.getStationId());
                requestBus.addParam(item.getUpdownCd());
                requestBus.addParam(item.getStationOrd());
                requestBus.commit();
                presenter.bookmarkInsert(requestBus, new AuthBaseInterface<String>() {
                    @Override
                    public void success(String item) {
                        if (item.equals("00")) {
                            savedItem.setBookmarkYn("Y");
                            holder.binding.igBookmark.setImageResource(R.drawable.common_on_star_40);
                        } else {
                            holder.binding.igBookmark.setImageResource(R.drawable.common_off_star_40);
                        }
                    }
                    @Override
                    public void error(String message) {
                    }
                    @Override
                    public void errorAuth() {
                    }
                });
            }
        });

        if (item.getBookmarkYn().equals("Y")) {
            holder.binding.igBookmark.setImageResource(R.drawable.common_on_star_40);
        } else {
            holder.binding.igBookmark.setImageResource(R.drawable.common_off_star_40);
        }

        if ("Y".equals(item.getStationWifiYn())) {
            holder.binding.tvWifi.setVisibility(View.VISIBLE);
        }


        try {
            if (item.BusVisbleYn != null) {
                if (item.BusVisbleYn.equals("Y")) {
                    holder.binding.loBusLocation.setVisibility(View.VISIBLE);

                    switch (item.RouteTp) {
                        case GAN:
                            holder.binding.igBusLocation.setImageResource(R.drawable.ic_bus_color_20_pt_blue);
                            break;
                        case JI:
                            holder.binding.igBusLocation.setImageResource(R.drawable.ic_bus_color_20_pt_yellow);
                            break;
                        case SUN:
                            holder.binding.igBusLocation.setImageResource(R.drawable.ic_bus_color_20_pt_green);
                            break;
                        case FAST:
                            holder.binding.igBusLocation.setImageResource(R.drawable.ic_bus_color_20_pt_dark_red);
                            break;
                        case WORK:
                            holder.binding.igBusLocation.setImageResource(R.drawable.ic_bus_color_20_pt_light_green);
                            break;
                        case GUNWI:
                            holder.binding.igBusLocation.setImageResource(R.drawable.ic_bus_color_20_pt_indigo);
                            break;
                    }
                    holder.binding.tvBusLocationNum.setText(item.getBusNum().substring(5));
                } else {
                    holder.binding.loBusLocation.setVisibility(View.INVISIBLE);
                }
            } else {
                holder.binding.loBusLocation.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            if (position == selectedPosition) {
                holder.binding.loForm.setBackgroundColor(context.getColor(R.color.color_FF923D_10));
            } else {
                holder.binding.loForm.setBackgroundColor(context.getColor(R.color.white));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ViewHolder holder;
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_detail_list, viewGroup, false);
        holder = new ViewHolder(view);

        return holder;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public void updateDiffCallbackListItems(ArrayList<BusDetailInfo> newAddresses) {
        BusDiffCallbackUtil diffCallbackUtil = new BusDiffCallbackUtil(getItems(), newAddresses);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallbackUtil);

        initItems(newAddresses);
        diffResult.dispatchUpdatesTo(this);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private BusDetailListBinding binding;

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            binding.loForm.setOnClickListener(this);
            binding.igBookmark.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getLayoutPosition());
        }
    }
}
