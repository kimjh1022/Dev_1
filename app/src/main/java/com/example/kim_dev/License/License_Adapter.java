package com.example.kim_dev.License;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kim_dev.R;

import java.util.ArrayList;

public class License_Adapter extends RecyclerView.Adapter<License_Adapter.LicenseViewHolder> {

    private ArrayList<License_data> arrayList = new ArrayList<>();
    private Context context;
    //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
    //선택한 액티비티에 대한 context를 가져올 때 필요하다.

    public License_Adapter(ArrayList<License_data> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    public License_Adapter.LicenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_license_item, parent, false);
        License_Adapter.LicenseViewHolder holder = new License_Adapter.LicenseViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull License_Adapter.LicenseViewHolder holder, int position) {

        holder.l_name.setText(arrayList.get(position).get_l_name());
        holder.l_num.setText(String.valueOf(arrayList.get(position).get_l_num()));
        holder.l_date.setText(arrayList.get(position).get_l_date());
        holder.l_org.setText(String.valueOf(arrayList.get(position).get_l_org()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class LicenseViewHolder extends RecyclerView.ViewHolder {
        TextView l_name;
        TextView l_num;
        TextView l_date;
        TextView l_org;

        public LicenseViewHolder(@NonNull View itemView) {
            super(itemView);
            this.l_name = itemView.findViewById(R.id.l_name);
            this.l_num = itemView.findViewById(R.id.l_num);
            this.l_date = itemView.findViewById(R.id.l_date);
            this.l_org = itemView.findViewById(R.id.l_org);
        }
    }
}
























