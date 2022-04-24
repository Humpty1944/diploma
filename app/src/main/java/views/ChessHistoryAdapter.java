package views;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import chessModel.ChessHistory;
import chessModel.ChessHistoryStep;
import interfaces.ChessHistoryInterfaceCallbacks;

public class ChessHistoryAdapter extends RecyclerView.Adapter<ChessHistoryAdapter.MyViewHolder>{

    private List<ChessHistoryStep> chessHistory;
    private ChessHistoryInterfaceCallbacks chessHistoryInterfaceCallbacks;
    private boolean isLoad=false;
    public ChessHistoryAdapter(List<ChessHistoryStep> chessHistory,ChessHistoryInterfaceCallbacks chessHistoryInterfaceCallbacks){
        this.chessHistory=chessHistory;
        this.chessHistoryInterfaceCallbacks=chessHistoryInterfaceCallbacks;
    }
    public void setLoad(boolean load){
        this.isLoad=load;
    }
    public ChessHistoryAdapter(){
        this.chessHistory=new ArrayList<ChessHistoryStep>();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.turn_list, parent, false);
        return new MyViewHolder(itemView);
    }
    boolean isCurr=false;
    int row_index=-1;
    int prevIndex=-1;
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ChessHistoryStep turn = chessHistory.get(position);
        holder.turn.setText(turn.getCurrStep());
       // holder.turn.setText(turn.turnToNotationShow(turn.getChessPiece().getRow(),turn.getChessPiece().getCol()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(R.color.currStep);
                view.getBackground().setAlpha(50);
                isCurr=true;
                row_index=position;
                prevIndex=position;
                isLoad=true;
                chessHistoryInterfaceCallbacks.onClick(chessHistory.get(holder.getAdapterPosition()));
                notifyDataSetChanged();
            }
        });

       // if(prevIndex==position){
        if(ChessHistory.currCountStep==position+1){
            holder.itemView.setBackgroundColor(R.color.currStep);
            holder.itemView.getBackground().setAlpha(50);    //color on item selecting item
        }
        else{
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.itemView.getBackground().setAlpha(255);
//            if(ChessHistory.currCountStep==position+1){
//                holder.itemView.setBackgroundColor(R.color.currStep);
//                holder.itemView.getBackground().setAlpha(50);
//            }
//            if(!isLoad&&position==chessHistory.size()-1){
//                holder.itemView.setBackgroundColor(R.color.currStep);
//                holder.itemView.getBackground().setAlpha(50);
//            }//color on item unselecting item
        }
//        if(row_index==position){
//            holder.itemView.setBackgroundColor(R.color.currStep);
//               holder.itemView.getBackground().setAlpha(50);
//        }
//        else
//        {
//            holder.itemView.setBackgroundColor(Color.WHITE);
//                holder.itemView.getBackground().setAlpha(255);
//        }
//        if((position==0&&isLoad)||isCurr){
//            holder.itemView.setBackgroundColor(R.color.currStep);
//            holder.itemView.getBackground().setAlpha(50);
//            isLoad=false;
//        }else {
//            if (position == chessHistory.size() - 1 && !isLoad) {
//                holder.itemView.setBackgroundColor(R.color.currStep);
//                holder.itemView.getBackground().setAlpha(50);
//            } else {
//                holder.itemView.setBackgroundColor(Color.WHITE);
//                holder.itemView.getBackground().setAlpha(255);
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return chessHistory.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView turn;
        MyViewHolder(View view) {
            super(view);
            this.turn = view.findViewById(R.id.turn);

        }
    }
}
