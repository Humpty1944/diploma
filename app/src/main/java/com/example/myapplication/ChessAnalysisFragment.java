package com.example.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chessModel.ChessHistory;
import chessModel.ChessHistoryStep;
import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Player;
import chessModel.Square;
import chessPiece.King;
import chessPiece.Piece;
import engine.ThreadControl;
import engine.work.EngineOptions;
import engine.work.UCIEngine;
import engine.work.UCIEngineBase;
import helpClass.TinyDB;
import interfaces.ChessHistoryInterfaceCallbacks;
import interfaces.ChessInterface;
import openings.Opening;
import views.ChessBoardView;
import views.ChessHistoryAdapter;
import views.PromotionView;

public class ChessAnalysisFragment extends Fragment implements ChessInterface {
    View view;
    ChessModel chessModel;
    TextView text;
    RecyclerView recyclerView;
    private ChessHistoryAdapter mAdapter;
    private List<ChessHistoryStep> chessHistory = new ArrayList<>();

    PromotionView promotionview;
    ConstraintLayout layout;
    TextView openingName;
    TextView gameName;
    ImageButton forward;
    ImageButton backward;
    int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;
    ConstraintLayout.LayoutParams lParams = new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
    private FrameLayout frameLayout;
    ChessBoardView chessView;
    List<Opening> openingList;
   public TextView textAnalysis;
    String depth="20";
    private boolean isFirst = true;

    private Thread engineMonitor;
    private EngineOptions engineOptions = new EngineOptions();
    private static String engineDir = "DroidFish/uci";
    private static String engineLogDir = "DroidFish/uci/logs";
    UCIEngine uciEngine;

    public interface OnDataPass {
        public void onDataPass(List<ChessHistoryStep>  data);
    }
    OnDataPass dataPasser;
    public ChessAnalysisFragment() {
        super(R.layout.chess_analysis);
    }

    public ChessAnalysisFragment(List<Opening> openings) {
        super(R.layout.chess_analysis);
        this.openingList = openings;
    }

    public ChessAnalysisFragment(ChessModel model, List<Opening> openings) {
        super(R.layout.chess_analysis);
        this.chessModel = model;
        this.openingList = openings;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chess_analysis, container, false);

        chessView = view.findViewById(R.id.chessBoard);
        recyclerView = view.findViewById(R.id.recyclerView);
        frameLayout = (FrameLayout) view.findViewById(R.id.your_placeholder);
        layout = (ConstraintLayout) view.findViewById(R.id.layoutId);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        promotionview = new PromotionView(getActivity().getApplicationContext());
        openingName = view.findViewById(R.id.text_view_opening_info);
        textAnalysis = view.findViewById(R.id.text_view_chess_anlysis);
        gameName=view.findViewById(R.id.text_view_game_name);
        forward=view.findViewById(R.id.imageButtonNext);
        backward=view.findViewById(R.id.imageButtonBack);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chessHistory = new ArrayList<>();
        mAdapter = new ChessHistoryAdapter(chessHistory, new ChessHistoryInterfaceCallbacks() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(Object object) {
                ChessHistoryStep step = (ChessHistoryStep) object;
                if (step.getCurrMoveCount() == ChessHistory.currCountStep) {
                    Toast.makeText(getActivity().getApplicationContext(), "Вы уже на данном шаге", Toast.LENGTH_LONG).show();
                    return;
                }
                if (step.getCurrMoveCount() > ChessHistory.currCountStep) {
                    ChessHistory.isForward = true;
                    moveMultiPiece(step.getFromSquare(), step.getToSquare(), step);
                    //movePiece(step.getFromSquare(),step.getToSquare());

                } else if (step.getCurrMoveCount() < ChessHistory.currCountStep) {
                    ChessHistory.isForward = false;
                    //  if(ChessHistory.isLoad&&isFirst){
                    //    moveMultiPiece(step.getFromSquare(), step.getToSquare(),  step);
                    //    isFirst=false;
                    // }else {
                    backMovePiece(step);
                    // }
                }

            }
        });
        try {
            Boolean isLoad = getArguments().getBoolean("load");
            if (isLoad) {
                Toast.makeText(getActivity().getApplicationContext(), "Игра загружена", Toast.LENGTH_LONG).show();
                setHistory();

                ChessHistory.isForward = true;
                chessModel.reset();
                mAdapter.setLoad(true);
                gameName.setText(chessModel.getGameName());

            } else {
                ChessHistory.isLoad = false;
                chessModel = new ChessModel();
                mAdapter.setLoad(false);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "NOOOOO", Toast.LENGTH_LONG);
            ChessHistory.isLoad = false;
            chessModel = new ChessModel();
            mAdapter.setLoad(false);
        }
        recyclerView.setAdapter(mAdapter);
        // chessModel= new ChessModel();
        ChessHistory.currCountStep = 0;
        // ChessHistory.isLoad=false;
        chessView.chessInterface = this;
        chessModel.chessInterface = this;
        chessModel.setBoardView(chessView);
        chessView.setMainActivity(this);
        promotionview.chessInterface = this;
        chessModel.setOpenings(openingList);

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chessModel.moveForwardPiece(chessHistory.get(chessHistory.size()-1));
                if(chessHistory.size()!=0&&chessHistory.size()>=ChessHistory.currCountStep+1) {
                    moveOnePiece(chessHistory.get(ChessHistory.currCountStep ));
                }
            }
        });
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chessModel.backMovePiece(chessHistory.get(chessHistory.size()-1));
                if(chessHistory.size()!=0) {
                    backMovePieceOne(chessHistory.get(ChessHistory.currCountStep - 1));
                }
            }
        });

        try {
            analysisBegin();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onStart() {
        super.onStart();
        chessView.chessInterface = this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public Piece pieceAt(Square square) {
        return chessModel.pieceAt(square);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void movePiece(Square from, Square to) {

        chessModel.movePiece(from, to);
        ChessBoardView chessView = view.findViewById(R.id.chessBoard);
        chessView.invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void moveMultiPiece(Square from, Square to, ChessHistoryStep step) {
        int currMove = ChessHistory.currCountStep;
        for (int i = currMove; i < step.getCurrMoveCount(); i++) {
            sendAndDisplayAnalysis(chessModel.getChessHistorySteps().get(i));
            chessModel.moveForwardPiece(chessModel.getChessHistorySteps().get(i));
//            chessModel.moveForwardPiece(chessHistory.get(i));
            // chessModel.movePiece(chessHistory.get(i).getFromSquare(),chessHistory.get(i).getToSquare());
            //chessModel.backMovePiece(chessHistory.get(i));
        }
        ChessBoardView chessView = view.findViewById(R.id.chessBoard);
        ChessHistory.isForward = false;
        chessView.invalidate();
    }
    public void moveOnePiece(ChessHistoryStep step) {
       // int currMove = ChessHistory.currCountStep;
     //   for (int i = currMove; i < step.getCurrMoveCount(); i++) {
        sendAndDisplayAnalysis(chessModel.getChessHistorySteps().get(ChessHistory.currCountStep));
            chessModel.moveForwardPiece(chessModel.getChessHistorySteps().get(ChessHistory.currCountStep));

//            chessModel.moveForwardPiece(chessHistory.get(i));
            // chessModel.movePiece(chessHistory.get(i).getFromSquare(),chessHistory.get(i).getToSquare());
            //chessModel.backMovePiece(chessHistory.get(i));
        //}
        ChessBoardView chessView = view.findViewById(R.id.chessBoard);
        ChessHistory.isForward = false;
        mAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(ChessHistory.currCountStep);
        chessView.invalidate();
    }
    @Override
    public void promotionView(Player player, Piece piece) {
        System.out.println("view");
        promotionview.setPlayer(player);
        promotionview.setCurrPiece(piece);
        layout.addView(promotionview, lParams);
    }

    @Override
    public void promotePawn(ChessMan chessMan, Piece piece) {
        System.out.println("pawn");
        if (chessMan == null || piece == null) {
            view.setVisibility(View.GONE);
        } else {
            chessModel.promotion(piece, chessMan);
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateHistory(boolean isNext, ChessHistoryStep step, int block) {
        if (isNext) {
            chessHistory.add(step);
            // mAdapter.notifyItemChanged(step.getCurrMoveCount()-1);
            mAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(chessHistory.size() - 1);
            recyclerView.invalidate();
            dataPasser.onDataPass(chessHistory);
        } else {
            for (int i = chessHistory.size() - 1; i >= block; i--) {
                chessHistory.remove(chessHistory.get(i));
            }
            mAdapter.notifyDataSetChanged();
            //  mAdapter.notifyItemRangeRemoved(block+1, chessHistory.size()-block );
            recyclerView.smoothScrollToPosition(chessHistory.size() - 1);
            recyclerView.invalidate();
            dataPasser.onDataPass(chessHistory);
        }
    }

    public void setHistory() {
        for (int i = 0; i < chessModel.getChessHistorySteps().size(); i++) {
            //chessHistory.add(ChessHistory.chessHistory.get(i));
            chessHistory.add(chessModel.getChessHistorySteps().get(i));
            //  mAdapter.notifyItemChanged(i);
        }
        mAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(chessHistory.size() - 1);
        recyclerView.invalidate();
        dataPasser.onDataPass(chessHistory);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void backMovePiece(ChessHistoryStep move) {
        // for(int i=chessHistory.size()-1;i>chessHistory.indexOf(move);i--){
        for (int i = ChessHistory.currCountStep - 1; i >= move.getCurrMoveCount(); i--) {
            //chessModel.backMovePiece(ChessHistory.chessHistory.get(i));
           // sendAndDisplayAnalysis(chessModel.getChessHistorySteps().get(i));
            chessModel.backMovePiece(chessModel.getChessHistorySteps().get(i));
            //chessModel.backMovePiece(chessHistory.get(i));
        }
        replayAnalysis(chessModel.getChessHistorySteps().size());
        ChessBoardView chessView = view.findViewById(R.id.chessBoard);
        chessView.invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void backMovePieceOne(ChessHistoryStep move) {
        // for(int i=chessHistory.size()-1;i>chessHistory.indexOf(move);i--){
       // for (int i = ChessHistory.currCountStep - 1; i >= move.getCurrMoveCount(); i--) {
            //chessModel.backMovePiece(ChessHistory.chessHistory.get(i));
        if((chessModel.getChessHistorySteps().size()-1==-1)){
            chessModel.reset();
            ChessHistory.currCountStep=0;
            ChessHistory.isCheck=false;
            ChessHistory.currPlayer=Player.WHITE;
            ChessHistory.checkingPiece=null;
            ChessHistory.checkPiece=null;
            uciEngine.writeLineToEngine("stop");
            uciEngine.readLineFromEngine(50);
            uciEngine.writeLineToEngine("ucinewgame");
        }else {
           // sendAndDisplayAnalysis(chessModel.getChessHistorySteps().get(ChessHistory.currCountStep-1));
            replayAnalysis(ChessHistory.currCountStep-1);
            chessModel.backMovePiece(chessModel.getChessHistorySteps().get(ChessHistory.currCountStep-1));
            recyclerView.smoothScrollToPosition(ChessHistory.currCountStep);

        }
            //chessModel.backMovePiece(chessHistory.get(i));
       // }
        mAdapter.notifyDataSetChanged();
        ChessBoardView chessView = view.findViewById(R.id.chessBoard);
        chessView.invalidate();
    }
List<Thread> threads = new ArrayList<>();
    public void replayAnalysis(int block){
        final UCIEngine uci = uciEngine;
        String res="position startpos moves ";
        for(int i=0;i<block;i++){
            res+= chessModel.getChessHistorySteps().get(i).turnToNotation(chessModel.getChessHistorySteps().get(i).getFromSquare()) +
                    chessModel.getChessHistorySteps().get(i).turnToNotation(chessModel.getChessHistorySteps().get(i).getToSquare())+" ";
        }

        uciEngine.writeLineToEngine("stop");
        uciEngine.readLineFromEngine(50);
       // threads.get(threads.size()-1).interrupt();
        uciEngine.writeLineToEngine("ucinewgame");
        uciEngine.writeLineToEngine(res);
        uciEngine.writeLineToEngine("go depth "+depth);
        Thread startupThread = new Thread(() -> {
            try {
                String line = "";
                while (!line.contains("bestmove")) {
                    line = uci.readLineFromEngine(50);
                    if (line.contains("cp")) {
                        textAnalysis.setText(formString(line));
                        System.out.println(line);
                    }
                }


            } catch (Exception e) {
                System.out.println(e.toString());
            }
        });
        threads.add(startupThread);
        try {
            startupThread.start();
            System.out.println("finish");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void displayCurrOpening(Opening opening) {
        openingName.setText(opening.getName());
    }

    @Override
    public void sendAndDisplayAnalysis(ChessHistoryStep step) {
        final UCIEngine uci = uciEngine;
        if (ChessHistory.currCountStep == 0) {
            uciEngine.writeLineToEngine("position startpos moves " + step.turnToNotation(step.getFromSquare()) + step.turnToNotation(step.getToSquare()));
        } else {
            uciEngine.writeLineToEngine("stop");
            uciEngine.readLineFromEngine(50);
            uciEngine.writeLineToEngine("position moves " + step.turnToNotation(step.getFromSquare()) + step.turnToNotation(step.getToSquare()));
        }
        uciEngine.writeLineToEngine("go depth "+depth);
       // ThreadControl startupThread = new ThreadControl("name", uciEngine, chessModel, this);
        Thread startupThread = new Thread(() -> {
            try {
                String line = "";
                while (!line.contains("bestmove")) {
                    line = uci.readLineFromEngine(50);
                    if (line.contains("cp")) {
                        String res = formString(line);
                        if(res!="") {
                            textAnalysis.setText(formString(line));
                        }
                        System.out.println(line);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        });

        try {
            startupThread.start();
           // startupThread.run();
            System.out.println("finish");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String formString(String line) {
        String res = "";
        String[] lineWord = line.split(" ");
        int pos = -1;
        for (int i = 0; i < lineWord.length; i++) {
            if (lineWord[i].contains(" ")) {
                continue;
            }
            if (pos != -1) {
                if (lineWord[pos].equals("cp")) {
                    res += String.format("%.2f", Float.parseFloat(lineWord[i]) / 100) + " ";
                    pos = -1;
                } else if (lineWord[pos].equals("pv")) {
                    try {
                        res += turnToNormal(lineWord[i]);
                    } catch (Exception e) {
                        System.out.println(lineWord[i]);
                    }
                }
            }
            if (lineWord[i].equals("cp") || lineWord[i].equals("pv")) {

                pos = i;
            }
        }
        return res;
    }

    private synchronized String turnToNormal(String line) {
        String from = line.substring(0, 2);
        String to = line.substring(2, 4);
        Square fromSquare = ChessHistory.fromAlgebraToSquare(from);
        Piece piece = chessModel.pieceAt(fromSquare);
        if (piece == null) {
            return "";
        } else {
            return piece.getShortName() + to + " ";
        }

    }
//    private void monitorLoop(UCIEngine uci) {
//        while (true) {
////            int timeout = getReadTimeout();
//            if (Thread.currentThread().isInterrupted())
//                return;
//            String s = uci.readLineFromEngine(50);
//            long t0 = System.currentTimeMillis();
//            while (s != null && !s.isEmpty()) {
//                if (Thread.currentThread().isInterrupted())
//                    return;
//                processEngineOutput(uci, s);
//                s = uci.readLineFromEngine(1);
//                long t1 = System.currentTimeMillis();
//                if (t1 - t0 >= 1000)
//                    break;
//            }
//            if ((s == null) || Thread.currentThread().isInterrupted())
//                return;
//            processEngineOutput(uci, s);
//            if (Thread.currentThread().isInterrupted())
//                return;
//            notifyGUI();
//            if (Thread.currentThread().isInterrupted())
//                return;
//        }
//    }
//    private synchronized void processEngineOutput(UCIEngine uci, String s) {
//        if (Thread.currentThread().isInterrupted())
//            return;
//
//        if (s == null) {
//            shutdownEngine();
//            return;
//        }
//
//        if (s.length() == 0)
//            return;
//
//
//    }
//    public final synchronized void shutdownEngine() {
//        if (uciEngine != null) {
//            engineMonitor.interrupt();
//            engineMonitor = null;
//            uciEngine.shutDown();
//            uciEngine = null;
//        }
//        engineState.setState(MainState.DEAD);
//    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void analysisBegin() throws IOException {
        String mString = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("engineList", "");
        depth =  PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("depth", "20");
        if (mString.equals( "")||mString.equals("1")||mString.equals("0")) {
            mString = "stockfish";
        }
        AssetManager assetManager = getContext().getAssets();
        String[] namesInclude =  assetManager.list("arm64-v8a");
        Toast.makeText(getActivity().getApplicationContext(), "Текущий движок "+mString, Toast.LENGTH_LONG).show();
        if( !Arrays.stream(namesInclude).anyMatch(mString::equals)){
            TinyDB tinyDB = new TinyDB(getContext());
            ArrayList<String> names = tinyDB.getListString("externalEngine");
            String finalMString = mString;
           String  newString =  names.stream().filter(carnet -> carnet.contains(finalMString)).findFirst().orElse("");
            mString=newString;
        }

//        UCIEngine uciEngine = null;
        String sep = File.separator;
        engineOptions.workDir = getContext().getExternalFilesDir(null) + sep + engineLogDir;
        // engineOptions.workDir = Environment.getExternalStorageDirectory() + sep + engineLogDir;
        uciEngine = UCIEngineBase.getEngine(mString,
                engineOptions,
                errMsg -> {
                    if (errMsg == null)
                        errMsg = "";
                    new UCIEngine.Report() {
                        @Override
                        public void reportError(String errMsg) {
                            Toast.makeText(getActivity().getApplicationContext(), errMsg, Toast.LENGTH_LONG).show();
                            return;
                        }
                    };
                }, getContext());
        uciEngine.initialize();
        // text = findViewById( R.id.text_view_id);
        chessView.setUciEngine(uciEngine);
        final UCIEngine uci = uciEngine;
//        engineMonitor = new Thread(() -> monitorLoop(uci));
//        engineMonitor.start();

        uciEngine.clearOptions();
        uciEngine.writeLineToEngine("uci");
        String lineInfo = "1";
        while (lineInfo != null & !lineInfo.equals("uciok")) {
            lineInfo = uci.readLineFromEngine(50);
        }
        uciEngine.writeLineToEngine("isready");
        uciEngine.readLineFromEngine(50);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        uciEngine.writeLineToEngine("quit");

    }
}
