package mechanic.com.mechanic.BusinessHelper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mechanic.com.mechanic.BusinessObject.MechanicBO;
import mechanic.com.mechanic.MainActivity;
import mechanic.com.mechanic.R;

/**
 * Created by Sailesh GB on 4/19/2017.
 */

public class DataShowAdapter extends  RecyclerView.Adapter<DataShowAdapter.MyViewHolder> {
    private List<MechanicBO> mechanicBOs;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView carbon, silicon, materialType, temp,poringTemp, manganes, boxWeight, materialName;


        public MyViewHolder(View view) {
            super(view);
            context = itemView.getContext();
            carbon = (TextView) view.findViewById(R.id.carbonShow);
            silicon= (TextView) view.findViewById(R.id.siliconShow);
            manganes = (TextView) view.findViewById(R.id.manganesShow);
            temp = (TextView) view.findViewById(R.id.temperatureShow);
            poringTemp = (TextView) view.findViewById(R.id.poringTemperatureShow);
            boxWeight = (TextView) view.findViewById(R.id.boxWeightShow);
            materialType = (TextView) view.findViewById(R.id.materialTypeShow);
            materialName = (TextView) view.findViewById(R.id.materialNameShow);

            boxWeight.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(ListUtil.isNotNullOrEmpty(mechanicBOs)) {
                        mechanicBOs.remove(getAdapterPosition());
                    }
                    return false;
                }
            });

            boxWeight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ListUtil.isNotNullOrEmpty(mechanicBOs)) {
                        MechanicBO mechanicBO = mechanicBOs.get(getAdapterPosition());
                        showData(mechanicBO);
                    }
                }
            });
            materialName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ListUtil.isNotNullOrEmpty(mechanicBOs)) {
                        MechanicBO mechanicBO = mechanicBOs.get(getAdapterPosition());
                        showData(mechanicBO);
                    }
                }
            });
        }
    }

    public DataShowAdapter(List<MechanicBO> mechanicBOs){
        this.mechanicBOs = mechanicBOs;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.design_show, parent, false);

        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        MechanicBO mechanicBO = mechanicBOs.get(position);
        if(StringUtil.isNotNullOrEmpty(mechanicBO.getCarbon())) {
            holder.carbon.setText("C: " + mechanicBO.getCarbon());
        }else{
            holder.carbon.setText("C: " + " ");
        }
        if(StringUtil.isNotNullOrEmpty(mechanicBO.getSilicon())) {
            holder.silicon.setText("S: " + mechanicBO.getSilicon());
        }else{
            holder.silicon.setText("S: " + " ");
        }
        if(StringUtil.isNotNullOrEmpty(mechanicBO.getManganes())) {
            holder.manganes.setText("M: " + mechanicBO.getManganes());
        }else{
            holder.manganes.setText("M: " + " ");
        }
        if(StringUtil.isNotNullOrEmpty(mechanicBO.getTemperature())) {
            holder.temp.setText(mechanicBO.getTemperature() + " :T");
        }else{
            holder.temp.setText(" "+ " :T");
        }
        if(StringUtil.isNotNullOrEmpty(mechanicBO.getPoringTemperature())) {
            holder.poringTemp.setText(mechanicBO.getPoringTemperature() + " :PT");
        }else{
            holder.poringTemp.setText(" "+ " :PT");
        }
        if(StringUtil.isNotNullOrEmpty(mechanicBO.getBoxWeight())) {
            holder.boxWeight.setText(mechanicBO.getBoxWeight());
        }else{
            holder.boxWeight.setText(" ");
        }
        if(StringUtil.isNotNullOrEmpty(mechanicBO.getMaterialType())) {
            holder.materialType.setText(mechanicBO.getMaterialType() + " :MT");
        }else{
            holder.materialType.setText(" "+ " :MT");
        }
        if(StringUtil.isNotNullOrEmpty(mechanicBO.getMaterialName())) {
            holder.materialName.setText(mechanicBO.getMaterialName());
        }else{
            holder.materialName.setText("");
        }


    }

    @Override
    public int getItemCount() {
       return mechanicBOs.size();
    }

    public void showData(MechanicBO mechanicBO){
            Intent intent = new Intent(context,MainActivity.class);
            intent.putExtra("MechanicBO",mechanicBO);
            context.startActivity(intent);
    }


}
