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

public class GamesAdapter  extends RecyclerView.Adapter<views.GamesAdapter.MyViewHolder>{

    private  List<String[]> info ;
    private ChessHistoryInterfaceCallbacks chessHistoryInterfaceCallbacks;
    private boolean isLoad=false;
    public GamesAdapter( List<String[]> gameName,ChessHistoryInterfaceCallbacks chessHistoryInterfaceCallbacks){
        this.info=gameName;
        this.chessHistoryInterfaceCallbacks=chessHistoryInterfaceCallbacks;
    }
    public void setOpeningList( List<String[]> gameName){
        this.info=gameName;
    }
    @NonNull
    @Override
    public views.GamesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_list, parent, false);

        return new views.GamesAdapter.MyViewHolder(itemView);
    }
    int row_index=-1;

    @Override
    public void onBindViewHolder(@NonNull views.GamesAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String[] game = info.get(position);
        holder.players.setText(game[2]+" vs. "+game[3]);
        holder.dateAndPlace.setText(game[0]+"\n"+game[1]);
//        holder.code.setText(opening.getNumber());
//        holder.name.setText(opening.getName());
        // holder.turn.setText(turn.turnToNotationShow(turn.getChessPiece().getRow(),turn.getChessPiece().getCol()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    view.setBackgroundColor(R.color.currStep);
                //  view.getBackground().setAlpha(50);
               // Toast.makeText(view.getContext(), "aaa",Toast.LENGTH_LONG).show();
                row_index=position;

                chessHistoryInterfaceCallbacks.onClick(position);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView players;
        TextView dateAndPlace;
        MyViewHolder(View view) {
            super(view);
            this.players = view.findViewById(R.id.textViewPlayers);
            this.dateAndPlace = view.findViewById(R.id.textViewDatePlace);
        }
    }
}