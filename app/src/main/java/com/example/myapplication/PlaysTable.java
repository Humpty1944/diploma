package com.example.myapplication;//package com.example.myapplication;
//
//import android.os.Build;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chessModel.ChessHistory;
import chessModel.ChessHistoryStep;
import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Player;
import chessModel.Square;
import chessPiece.Piece;
import engine.work.EngineOptions;
import interfaces.ChessHistoryInterfaceCallbacks;
import interfaces.ChessInterface;
import openings.Opening;
import pgn.PgnManager;
import views.ChessBoardView;
import views.ChessHistoryAdapter;
import views.GamesAdapter;
import views.MasterAdapter;
import views.OpeningAdapter;
import views.PromotionView;

//
public class PlaysTable extends Fragment {
    View view;
    TableLayout tableLayout;
   // private GamesAdapter mAdapter;
    MasterAdapter mAdapter;
    ConstraintLayout layout;
    RecyclerView recyclerView;
    int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;
    ConstraintLayout.LayoutParams lParams = new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.plays_table, container, false);
      //  tableLayout = (TableLayout) view.findViewById(R.id.tableLayoutPlays);
        layout = (ConstraintLayout) view.findViewById(R.id.layoutIdGame);
//        try {
//            populateWithPlays();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        for (int i = 0; i < 50; i++) {
////            TableRow row = new TableRow(getActivity());
////            TextView text = new TextView(getActivity());
////            text.setText("jfkdsfhn");
////            row.addView(text);
////            tableLayout.addView(row);
////        }
        return view;
       // return inflater.inflate(R.layout.plays_table, container, false);
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewGame);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        try {
            populateWithPlays();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onStart() {
        super.onStart();
        try {
            populateWithPlays();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void populateWithPlays() throws IOException {
        PgnManager manager = new PgnManager(getActivity().getApplicationContext());
       String[] fileNames = manager.getAllFilesName();
       List<String> fileInfo = new ArrayList<>();
       for(int i=0;i<fileNames.length;i++){
           String correctName = fileNames[i].replace("_", " ");
           fileInfo.add(correctName);
       }

        mAdapter = new MasterAdapter(fileInfo, new ChessHistoryInterfaceCallbacks() {
            @Override
            public void onClick(Object object)  {
               MainActivity mainActivity = (MainActivity) getActivity();
//                int pos = (Integer) object;
//                byte type=0;
//                try {
//                    ChessModel model = manager.readFile(fileNames[pos]);
//                    if(model==null){
//                        Toast.makeText(getContext(), "Файл нельзя загрузить из-за ошибки в тексте.", Toast.LENGTH_LONG).show();
//                    }else {
                       mainActivity.changeFragments(object);
//                    }
//                }
//               catch (Exception e){
//                    Toast.makeText(getContext(), "Файл нельзя загрузить из-за ошибки в тексте.", Toast.LENGTH_LONG).show();
//                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
//       for(int i=0;i<fileNames.length;i++){
//          String[] info = manager.readFileNameAndDate(fileNames[i]);
//           TableRow row = new TableRow(getActivity().getApplicationContext());
//           View tableView = LayoutInflater.from(getActivity().getApplicationContext()).
//                   inflate(R.layout.table_row_plays,row,false);
//           if(i%2==0){
//               row.setBackgroundColor(Color.GRAY);
//           }else{
//               row.setBackgroundColor(Color.WHITE);
//           }
//           row.setMinimumHeight(200);
//           TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//           row.setLayoutParams(lp);
//           TextView textView = new TextView(getActivity().getApplicationContext());
//           textView.setText(info[2]+" vs. "+info[3]);
//           textView.setWidth(490);
//           textView.setTextSize(15);
//           textView.setSingleLine(false);
//           textView.setLayoutParams(lp);
//           TextView editText = new TextView(getActivity().getApplicationContext());
//          editText.setWidth(400);
//           editText.setText(info[0]+"\n"+info[1]);
//           row.addView(textView);
//           TextView textempty= new TextView(getActivity().getApplicationContext());
//           textempty.setWidth(200);
//           //textempty.setText("dfsdfdsfdsfds");
//           row.addView(textempty);
//           row.addView(editText);
//
//           tableLayout.addView(row,i);
//
//           int finalI = i;
//           row.setOnClickListener(new View.OnClickListener() {
//               @Override
//               public void onClick(View view) {
//                   MainActivity mainActivity = (MainActivity) getActivity();
//                   byte type=0;
//                   ChessModel model = manager.readFile(fileNames[finalI]);
//                   mainActivity.changeFragment(type, model,null);
//               }
//           });


     //  }
//        for (int i = 0; i < 50; i++) {
//            TableRow row = new TableRow(getActivity().getApplicationContext());
//            row.setBackgroundColor(Color.RED);
//            row.setMinimumHeight(40);
//            TextView text = new TextView(getActivity().getApplicationContext());
//            text.setText("jfkdsfhn\nkdfjklsdf\nfsd");
//            TableRow.LayoutParams paramsExample = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
//            text.setTextSize(40);
//            text.setLayoutParams(paramsExample);
//
//            row.addView(text);
//            tableLayout.addView(row);
//        }
    }
}
//   private  ConstraintLayout constraintLayout;
//   View view;
//    TextView text;
//    RecyclerView recyclerView;
//    private ChessHistoryAdapter mAdapter;
//    private List<ChessHistoryStep> chessHistory = new ArrayList<>();
//    private Thread engineMonitor;
//    private EngineOptions engineOptions = new EngineOptions();
//  //  private MainActivity.PermissionState storagePermission = MainActivity.PermissionState.UNKNOWN;
//    // Used to load the 'myapplication' library on application startup.
//    PromotionView promotionView;
//    ConstraintLayout layout;
//    ChessBoardView chessView;
//    int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;
//    ConstraintLayout.LayoutParams lParams = new ConstraintLayout.LayoutParams( wrapContent, wrapContent);
////    private DrawerLayout drawerLayout;
////    private ActionBarDrawerToggle toggle;
////    private NavigationView navView;
//    ChessModel chessModel = new ChessModel();
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        view =  inflater.inflate(R.layout.chess_analysis, container, false);
//
//        recyclerView =  view.findViewById(R.id.recyclerView);
//        constraintLayout = (ConstraintLayout) view.findViewById(R.id.layoutId);
//
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
//        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        mAdapter = new ChessHistoryAdapter(chessHistory, new ChessHistoryInterfaceCallbacks(){
//            @Override
//            public void onClick(Object object) {
//                ChessHistoryStep step = (ChessHistoryStep) object;
//                if(step.getCurrMoveCount()> ChessHistory.currCountStep){
//                    ChessHistory.isForward=true;
//                    movePiece(step.getFromSquare(),step.getToSquare());
//                    ChessHistory.isForward=false;
//                }else if(step.getCurrMoveCount()<=ChessHistory.currCountStep) {
//                    backMovePiece(step);
//                }
//
//            }
//        });
//        recyclerView.setAdapter(mAdapter);
//
////        chessView.chessInterface = this;
////        chessModel.chessInterface=this;
////        chessModel.setBoardView(chessView);
////        chessView.setMainActivity(this);
////        promotionView = new PromotionView(getContext());
////        promotionView.chessInterface=this;
//        return inflater.inflate(R.layout.chess_analysis, container, false);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    @Override
//    public void onStart() {
//        chessView=  view.findViewById(R.id.chessBoard);
//        chessView.chessInterface = this;
//        chessModel.chessInterface=this;
//        chessModel.setBoardView(chessView);
//        chessView.setMainActivity(this);
//        promotionView = new PromotionView(getContext());
//        promotionView.chessInterface=this;
//        super.onStart();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
////        chessView.chessInterface = this;
////        chessModel.chessInterface=this;
////        chessModel.setBoardView(chessView);
////        chessView.setMainActivity(this);
////        promotionView = new PromotionView(getContext());
////        promotionView.chessInterface=this;
//        super.onViewStateRestored(savedInstanceState);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////        chessView.chessInterface = this;
////        chessModel.chessInterface=this;
////        chessModel.setBoardView(chessView);
////        chessView.setMainActivity(this);
////        promotionView = new PromotionView(getContext());
////        promotionView.chessInterface=this;
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//    @Override
//    public Piece pieceAt(Square square) {
//        return chessModel.pieceAt(square);
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    @Override
//    public void movePiece(Square from, Square to) {
//        chessModel.movePiece(from, to);
//        ChessBoardView chessView=view.findViewById(R.id.chessBoard);
//        chessView.invalidate();
//    }
//
//    @Override
//    public void promotionView(Player player, Piece piece) {
//        System.out.println("view");
//        promotionView.setPlayer(player);
//        promotionView.setCurrPiece(piece);
//        layout.addView(promotionView, lParams);
//    }
//
//    @Override
//    public void promotePawn(ChessMan chessMan, Piece piece) {
//        System.out.println("pawn");
//        if (chessMan==null||piece==null){
//            promotionView.setVisibility(View.GONE);
//        }else {
//            chessModel.promotion(piece, chessMan);
//            promotionView.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void updateHistory(boolean isNext, ChessHistoryStep step, int block) {
//        if (isNext){
//            chessHistory.add(step);
//            mAdapter.notifyDataSetChanged();
//            recyclerView.smoothScrollToPosition(chessHistory.size()-1);
//        }
//        else{
//            for(int i = chessHistory.size()-1; i>=block; i--){
//                chessHistory.remove(chessHistory.get(i));
//            }
//            mAdapter.notifyDataSetChanged();
//            //  mAdapter.notifyItemRangeRemoved(block+1, chessHistory.size()-block );
//            recyclerView.smoothScrollToPosition(chessHistory.size()-1);
//        }
//    }
//
//
//    @Override
//    public void backMovePiece(ChessHistoryStep move) {
//        System.out.println(chessHistory.indexOf(move));
//        System.out.println("size"+chessHistory.size());
//        for(int i=chessHistory.size()-1;i>chessHistory.indexOf(move);i--){
//            chessModel.backMovePiece(chessHistory.get(i));
//        }
//        ChessBoardView chessView=view.findViewById(R.id.chessBoard);
//        chessView.invalidate();
//    }
//
//    @Override
//    public void forwardMovePiece(ChessHistoryStep move) {
//
//    }
//}
