package views;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

import interfaces.ChessHistoryInterfaceCallbacks;
import openings.Opening;

public class MasterAdapter  extends RecyclerView.Adapter<MasterAdapter.MyViewHolder>{

    private List<String> masterList;
    private ChessHistoryInterfaceCallbacks chessHistoryInterfaceCallbacks;

    public MasterAdapter(List<String> openingList,ChessHistoryInterfaceCallbacks chessHistoryInterfaceCallbacks){
        this.masterList=openingList;
        this.chessHistoryInterfaceCallbacks=chessHistoryInterfaceCallbacks;
    }
    public void setOpeningList(List<String> openings){
        this.masterList=openings;
    }
    @NonNull
    @Override
    public MasterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.master_list, parent, false);

        return new MasterAdapter.MyViewHolder(itemView);
    }
    int row_index=-1;

    @Override
    public void onBindViewHolder(@NonNull MasterAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String master = masterList.get(position);
        holder.name.setText(master);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    chessHistoryInterfaceCallbacks.onClick(masterList.get(holder.getAdapterPosition()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return masterList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        MyViewHolder(View view) {
            super(view);
            this.name = view.findViewById(R.id.masterName);

        }
    }
}