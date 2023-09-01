package com.example.daegurobus.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daegurobus.R;

import com.example.daegurobus.network.DEFINE;
import com.example.daegurobus.network.NetworkPresenter;
import com.example.daegurobus.network.resultInterface.AuthBaseInterface;
import com.example.daegurobus.databinding.BusListSearchBinding;
import com.example.daegurobus.model.BusResultSearch;
import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.util.BasicUtil;

public class busSearchRcAdapter extends BaseRecyclerViewAdapter<BusResultSearch,busSearchRcAdapter.ViewHolder> {

    private NetworkPresenter presenter;
    private final String GAN = "1";
    private final String JI = "2";
    private final String SUN = "3";
    private final String FAST = "4";
    private final String WORK = "5";
    private final String gunwi = "6";
    //private Context context;
    private String keyword;


    public busSearchRcAdapter(Context context) {
        super(context);
    }


    @Override
    public void onBindView(ViewHolder holder, int position) {
        BusResultSearch item = getItem(position);

        try {


        if ("".equals(item.getRouteNum2())) {
            holder.binding.tvRouteNum.setText(BasicUtil.convertHighlightText(item.getRouteNum(), keyword, context.getColor(R.color.color_01CAFF)));

        } else {
            holder.binding.tvRouteNum.setText(BasicUtil.convertHighlightText(item.getRouteNum() + "(" + item.getRouteNum2() + ")", keyword, context.getColor(R.color.color_01CAFF)));
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
            case gunwi:
                holder.binding.tpRoute.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color_183290)));
                holder.binding.tpRoute.setText("군위");
                break;
        }
        holder.binding.tvNodeName.setText(item.getStartNodeName() + " ↔ " + item.getEndNodeName());


       presenter = new NetworkPresenter(context);
        holder.binding.igBookmark.setOnClickListener(new View.OnClickListener() {

            boolean isButtonPressed = false;

            String query = "1"+ DEFINE.FD_DELIMETER +item.getRouteId()+ DEFINE.FD_DELIMETER + ""+ DEFINE.FD_DELIMETER +""+ DEFINE.FD_DELIMETER +"";
            @Override
            public void onClick(View v) {
                if (isButtonPressed) {

                    RequestBus requestBus = new RequestBus(DEFINE.MCODE,DEFINE.INSERT_BOOKMARK);
                    requestBus.addParam("1");
                    requestBus.addParam(item.getRouteId());
                    requestBus.addParam("");
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
                }else {
                    RequestBus requestBus = new RequestBus(DEFINE.MCODE, DEFINE.DELETE_BOOKMARK);
                    requestBus.addParam("1");
                    requestBus.addParam(item.getRouteId());
                    requestBus.addParam("");
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
                }
                isButtonPressed = !isButtonPressed;
            }
        });


        if ("Y".equals(item.getBookMark_yn())) {
            holder.binding.igBookmark.setImageResource(R.drawable.ic_on_star_40_pt);
        }else {
            holder.binding.igBookmark.setImageResource(R.drawable.ic_off_star_40_pt);
        }
        }
        catch (Exception ex) {
            Log.d("ERRIRS : ", ex.toString());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bus_list_search, viewGroup, false));
    }

    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private BusListSearchBinding binding;

        ViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);

            binding.igBookmark.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getLayoutPosition());
        }
    }
}

