package views;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Path;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import chessModel.ChessHistoryStep;
import interfaces.ChessHistoryInterfaceCallbacks;
import openings.Opening;

public class OpeningAdapter extends RecyclerView.Adapter<OpeningAdapter.MyViewHolder>{

    private List<Opening> openingList;
    private ChessHistoryInterfaceCallbacks chessHistoryInterfaceCallbacks;
    private boolean isLoad=false;
    public OpeningAdapter(List<Opening> openingList,ChessHistoryInterfaceCallbacks chessHistoryInterfaceCallbacks){
        this.openingList=openingList;
        this.chessHistoryInterfaceCallbacks=chessHistoryInterfaceCallbacks;
    }
    public void setOpeningList(List<Opening> openings){
        this.openingList=openings;
    }
    @NonNull
    @Override
    public OpeningAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.opening_list, parent, false);

        return new OpeningAdapter.MyViewHolder(itemView);
    }
  int row_index=-1;

    @Override
    public void onBindViewHolder(@NonNull OpeningAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Opening opening = openingList.get(position);
        holder.code.setText(opening.getNumber());
        holder.name.setText(opening.getName());
        // holder.turn.setText(turn.turnToNotationShow(turn.getChessPiece().getRow(),turn.getChessPiece().getCol()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    view.setBackgroundColor(R.color.currStep);
              //  view.getBackground().setAlpha(50);
                Toast.makeText(view.getContext(), "aaa",Toast.LENGTH_LONG).show();
                row_index=position;

                try {
                    chessHistoryInterfaceCallbacks.onClick(openingList.get(holder.getAdapterPosition()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return openingList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView code;
        MyViewHolder(View view) {
            super(view);
            this.name = view.findViewById(R.id.code);
            this.code = view.findViewById(R.id.name);
        }
    }
}
