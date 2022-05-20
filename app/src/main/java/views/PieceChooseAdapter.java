package views;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.ChessCreateInterface;
import interfaces.ChessHistoryInterfaceCallbacks;
import openings.Opening;

public class PieceChooseAdapter extends RecyclerView.Adapter<PieceChooseAdapter.MyViewHolder>{

    private List<Integer> pieces;
    private ChessHistoryInterfaceCallbacks chessCreateInterface;
    private boolean isLoad=false;
    private final Map<Integer, Bitmap> bitmaps = new HashMap<Integer, Bitmap>();
    BitmapFactory.Options options = new BitmapFactory.Options();
    public PieceChooseAdapter(List<Integer> pieces, ChessHistoryInterfaceCallbacks chessCreateInterface){
        this.pieces=pieces;
        this.chessCreateInterface=chessCreateInterface;
        options.inSampleSize=11;
    }
//    private void loadBitmap() {
//        for (Integer it : pieces) {
//            bitmaps.put(it, BitmapFactory.decodeResource(getResources(), it));
//
//        }
//    }
    public void setOpeningList(List<Integer> pieces){
        this.pieces=pieces;
    }
    @NonNull
    @Override
    public PieceChooseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.piece_choose, parent, false);

        return new PieceChooseAdapter.MyViewHolder(itemView);
    }
    int row_index=-1;

    public void setRow_index(int row_index) {
        this.row_index = row_index;
        notifyDataSetChanged();
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull PieceChooseAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Integer bitmap = pieces.get(position);
        int screenWidth = holder.itemView.getContext().getResources().getDisplayMetrics().widthPixels;
        holder.piece.setImageBitmap(BitmapFactory.decodeResource(holder.itemView.getContext().getResources(), bitmap,options));
        holder.piece.setMinimumWidth(screenWidth/7);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                //  view.getBackground().setAlpha(50);
              //  Toast.makeText(view.getContext(), "aaa",Toast.LENGTH_LONG).show();
                row_index=position;

                try {
                    chessCreateInterface.onClick(holder.getAdapterPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
            }
        });
        if(position==row_index){
            holder.itemView.setBackgroundColor(R.color.purple_700);
            holder.itemView.getBackground().setAlpha(50);
        }else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.itemView.getBackground().setAlpha(50);
        }


    }

    @Override
    public int getItemCount() {
        return pieces.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView piece;

        MyViewHolder(View view) {
            super(view);
            this.piece = view.findViewById(R.id.piece);

        }
    }
}
