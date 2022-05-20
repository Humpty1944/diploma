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
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import java.util.Set;

import chessModel.ChessHistory;
import chessModel.ChessHistoryStep;
import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Player;
import chessModel.Square;
import chessPiece.King;
import chessPiece.Piece;
//import engine.ThreadControl;
import engine.work.EngineOptions;
import engine.work.UCIEngine;
import engine.work.UCIEngineBase;
import helpClass.FenUtil;
import helpClass.TinyDB;
import interfaces.ChessHistoryInterfaceCallbacks;
import interfaces.ChessInterface;
import openings.Opening;
import views.ChessBoardView;
import views.ChessHistoryAdapter;
import views.PromotionView;
import views.ScaleView;

public class ChessAnalysisFragment extends Fragment implements ChessInterface {
    View view;
    ChessModel chessModel;
    String fen = "";
    String multiPv;
    RecyclerView recyclerView;
    private ChessHistoryAdapter mAdapter;
    private List<ChessHistoryStep> chessHistory = new ArrayList<>();
    private FenUtil fenUtil = new FenUtil();
    PromotionView promotionview;
    ScaleView scaleView;
    ConstraintLayout layout;
    TextView openingName;
    TextView gameName;
    TextView upPlayer;
    TextView downPlayer;
    ImageButton forward;
    ImageButton backward;
    ImageButton reset;
    int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;
    ConstraintLayout.LayoutParams lParams = new ConstraintLayout.LayoutParams(wrapContent, wrapContent);
    private FrameLayout frameLayout;
    ChessBoardView chessView;
    List<Opening> openingList;
    ScrollView scrollView;
    LinearLayout layoutScroll;
    ArrayList<TextView> textViewArrayList = new ArrayList<>();
    String depth = "20";
    private EngineOptions engineOptions = new EngineOptions();
    private static String engineDir = "DroidFish/uci";
    private static String engineLogDir = "DroidFish/uci/logs";
    UCIEngine uciEngine;
    boolean isReset=false;
    public interface OnDataPass {
        public void onDataPass(ChessModel data);
    }

    OnDataPass dataPasser;

    public ChessAnalysisFragment() {
        super(R.layout.chess_analysis);
    }

    public ChessAnalysisFragment(List<Opening> openings) {
        super(R.layout.chess_analysis);
        this.openingList = openings;
    }

    public ChessAnalysisFragment(List<Opening> openings, String fen) {
        super(R.layout.chess_analysis);
        this.openingList = null;
        this.fen = fen;
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
        promotionview=view.findViewById(R.id.promotion_view);
      //  promotionview = new PromotionView(getActivity().getApplicationContext());
        openingName = view.findViewById(R.id.text_view_opening_info);
        scrollView = view.findViewById(R.id.scrolViewRes);
        layoutScroll = view.findViewById(R.id.layoutScroll);
        gameName = view.findViewById(R.id.text_view_game_name);
        forward = view.findViewById(R.id.imageButtonNext);
        backward = view.findViewById(R.id.imageButtonBack);
        reset = view.findViewById(R.id.imageButtonReset);
        upPlayer = view.findViewById(R.id.text_view_firstPlayer);
        downPlayer = view.findViewById(R.id.text_view_secondPlayer);
        scaleView = view.findViewById(R.id.scaleView);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isReset=false;
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

                } else if (step.getCurrMoveCount() < ChessHistory.currCountStep) {
                    ChessHistory.isForward = false;
                    backMovePiece(step);

                }

            }
        });
        try {
            Boolean isLoad = getArguments().getBoolean("load");
            ChessHistory.currCountStep = 0;
            if (isLoad) {
                Toast.makeText(getActivity().getApplicationContext(), "Игра загружена", Toast.LENGTH_LONG).show();
                setHistory();

                ChessHistory.isForward = true;
                chessModel.reset();
                if (chessModel.getFen() != "") {
                    this.fen = chessModel.getFen();
                    chessModel.setPieceBox(ChessHistory.readFen(chessModel.getFen()));
                }
                mAdapter.setLoad(true);
                gameName.setText(chessModel.getGameName());
                upPlayer.setText(chessModel.getBlackPlayer());
                downPlayer.setText(chessModel.getWhitePlayer());

            } else {
                ChessHistory.isLoad = false;

                chessModel = new ChessModel();
                mAdapter.setLoad(false);
                if (!fen.equals("")) {

                    chessModel.setPieceBox(ChessHistory.readFen(fen));
                    chessModel.setFen(fen);
                    chessModel.setCurrPlayer(ChessHistory.currPlayer);
                    if (ChessHistory.currPlayer == Player.BLACK) {
                        ChessHistoryStep step = new ChessHistoryStep();
                        step.setWhiteNumberTurn(1);
                        chessModel.setChessHistorySteps(step);
                        chessHistory.add(step);
                    }
                }

            }
        } catch (Exception e) {
            try {
                ChessHistory.isLoad = false;
                fen = fenUtil.createFen(chessModel);
                chessModel.setFen(fen);
                if(ChessHistory.currPlayer==Player.BLACK){
                    chessHistory.add(chessModel.getChessHistorySteps().get(chessModel.getChessHistorySteps().size()-1));
                }
            } catch (Exception ex) {
                chessModel = new ChessModel();
            }

        }
        recyclerView.setAdapter(mAdapter);

        chessView.chessInterface = this;
        try {
            chessModel.chessInterface = this;
        } catch (Exception e) {
            chessModel = new ChessModel();
            ChessHistory.isLoad = false;
            mAdapter.setLoad(false);
            chessModel.chessInterface = this;
        }
        chessModel.setBoardView(chessView);
        chessView.setMainActivity(this);
        promotionview.chessInterface = this;
        promotionview.setVisibility(View.INVISIBLE);
        chessModel.setOpenings(openingList);


        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chessHistory.size() != 0 && chessHistory.size() >= ChessHistory.currCountStep + 1) {
                    chessView.setMovingPiece(null);
                    moveOnePiece(chessHistory.get(ChessHistory.currCountStep));
                }
            }
        });
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chessHistory.size() > 0 && ChessHistory.currCountStep > 0) {
                    chessView.setMovingPiece(null);
                    backMovePieceOne(chessHistory.get(ChessHistory.currCountStep - 1));
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                ChessHistory.checkingPiece = null;
//                ChessHistory.currCountStep = 0;
                ChessHistory.isForward = false;
//                ChessHistory.currPlayer = Player.WHITE;
//                ChessHistory.checkPiece = null;
//                ChessHistory.isCheck = false;
                chessView.setMovingPiece(null);
                chessView.reset();
                chessView.invalidate();
                reset();
                fen="";
                chessModel.cleanAll();
                chessHistory.clear();
                isReset=true;
                mAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(mAdapter);
                scaleView.setCurrText(0f);
                openingName.setText("");
                chessModel.setFen("");
                upPlayer.setText("");
                downPlayer.setText("");
                gameName.setText("");
                isReset=false;
//                uciEngine.writeLineToEngine("stop");
//                uciEngine.readLineFromEngine(50);
//                uciEngine.writeLineToEngine("ucinewgame");
            }
        });
        populateScrollView();
        try {
            analysisBegin();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void populateScrollView() {
        Integer multiPv;
        String val = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("multiPv", "1");
        try {
            multiPv = Integer.valueOf(val);
        } catch (Exception e) {
            multiPv = 1;
        }

        for (int i = 0; i < multiPv; i++) {
            TextView newTextView = new TextView(getActivity());
            newTextView.setTextSize(16);
            newTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            textViewArrayList.add(newTextView);
            layoutScroll.addView(newTextView);
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
            chessModel.moveForwardPiece(chessModel.getChessHistorySteps().get(i));
        }
        ChessHistory.currPlayer = chessModel.getCurrPlayer();
        sendAndDisplayAnalysis(ChessHistory.currCountStep);
        ChessBoardView chessView = view.findViewById(R.id.chessBoard);
        ChessHistory.isForward = false;
        chessView.invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void moveOnePiece(ChessHistoryStep step) {

        chessModel.moveForwardPiece(chessModel.getChessHistorySteps().get(ChessHistory.currCountStep));
        ChessHistory.currPlayer = chessModel.getCurrPlayer();
        sendAndDisplayAnalysis(ChessHistory.currCountStep);
        ChessHistory.isForward = false;
        mAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(ChessHistory.currCountStep);
        chessView.invalidate();
    }

    @Override
    public void promotionView(Player player, Piece piece) {

        promotionview.setPlayer(player);
        promotionview.setCurrPiece(piece);

        promotionview.setVisibility(View.VISIBLE);
      //  layout.addView(promotionview, lParams);
       promotionview.bringToFront();
        layout.invalidate();

    }

    @Override
    public void promotePawn(ChessMan chessMan, Piece piece) {

        if (chessMan == null || piece == null) {
            promotionview.setVisibility(View.INVISIBLE);
        } else {
            chessModel.promotion(piece, chessMan);

            promotionview.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void updateHistory(boolean isNext, ChessHistoryStep step, int block) {
        if (isNext) {
            chessHistory.add(step);
            mAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(chessHistory.size() - 1);
            recyclerView.invalidate();
            dataPasser.onDataPass(chessModel);
        } else {
            for (int i = chessHistory.size() - 1; i >= block; i--) {
                chessHistory.remove(chessHistory.get(i));
            }
            mAdapter.notifyDataSetChanged();
            if (chessHistory.size() > 0) {
                recyclerView.smoothScrollToPosition(chessHistory.size() - 1);
            }
            recyclerView.invalidate();
            dataPasser.onDataPass(chessModel);
        }
    }

    public void setHistory() {
        for (int i = 0; i < chessModel.getChessHistorySteps().size(); i++) {
            chessHistory.add(chessModel.getChessHistorySteps().get(i));
        }
        mAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(chessHistory.size() - 1);
        recyclerView.invalidate();
        dataPasser.onDataPass(chessModel);
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void backMovePiece(ChessHistoryStep move) {
        for (int i = ChessHistory.currCountStep - 1; i >= move.getCurrMoveCount(); i--) {
            chessModel.backMovePiece(chessModel.getChessHistorySteps().get(i));
        }
        ChessHistory.currPlayer = chessModel.getCurrPlayer();
        sendAndDisplayAnalysis(ChessHistory.currCountStep);
        ChessBoardView chessView = view.findViewById(R.id.chessBoard);
        chessView.invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void backMovePieceOne(ChessHistoryStep move) {

        if ((chessModel.getChessHistorySteps().size() - 1 == -1)) {
            chessModel.reset();
            reset();
//            ChessHistory.currCountStep = 0;
//            ChessHistory.isCheck = false;
//            ChessHistory.currPlayer = Player.WHITE;
//            ChessHistory.checkingPiece = null;
//            ChessHistory.checkPiece = null;
//            uciEngine.writeLineToEngine("stop");
//            uciEngine.readLineFromEngine(50);
//            uciEngine.writeLineToEngine("ucinewgame");
        } else {
            chessModel.backMovePiece(chessModel.getChessHistorySteps().get(ChessHistory.currCountStep - 1));
            ChessHistory.currPlayer = chessModel.getCurrPlayer();
            sendAndDisplayAnalysis(ChessHistory.currCountStep);
            recyclerView.smoothScrollToPosition(ChessHistory.currCountStep);

        }

        mAdapter.notifyDataSetChanged();
       // ChessBoardView chessView = view.findViewById(R.id.chessBoard);
        chessView.invalidate();
    }


    public void replayAnalysis(int block) {
        uciEngine.writeLineToEngine("stop");
        uciEngine.readLineFromEngine(50);
        Thread dataCombine = new Thread(() -> {
//            uciEngine.writeLineToEngine("stop");
//            uciEngine.readLineFromEngine(50);
            final UCIEngine uci = uciEngine;
            String res = "position startpos moves ";

            for (int i = 0; i < block; i++) {
                if (i == block - 1) {

                }
                res += chessModel.getChessHistorySteps().get(i).turnToNotation(chessModel.getChessHistorySteps().get(i).getFromSquare()) +
                        chessModel.getChessHistorySteps().get(i).turnToNotation(chessModel.getChessHistorySteps().get(i).getToSquare());
                if (i < block - 1) {
                    res += " ";
                }
            }

            uciEngine.writeLineToEngine(res);
            uciEngine.writeLineToEngine("go depth " + depth);

         //   Thread startupThread = new Thread(() -> {
                try {
                    String line = "";
                    int i = 1;
                    String firstLine = "1";
                    while (!line.contains("bestmove") ) {
                        line = uci.readLineFromEngine(50);
                        if (line!=null&&line.contains("cp")) {
                            String[] arr = formString(line, firstLine);
                            int finalI = i;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(arr!=null) {
                                        if (arr[0].length() > 6) {
                                            textViewArrayList.get(finalI - 1).setText(arr[0]);
                                            if (finalI == 1) {
                                                scaleView.setCurrText(Float.valueOf(arr[0].split(" ")[0]));
                                            }
                                        }
                                    }
                                }
                            });


                            i++;
                            if (i > Integer.valueOf(multiPv)) {
                                i = 1;
                            }

                        }
                    }


                } catch (Exception e) {
                    System.out.println(e.toString());
                }
           // });
            //startupThread.start();
        });

        try {
            dataCombine.start();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void displayCurrOpening(Opening opening) {
        openingName.setText(opening.getName());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendFen() {
        Thread waitBest = new Thread(() -> {
            uciEngine.writeLineToEngine("stop");
           String line =  uciEngine.readLineFromEngine(50);
            while(line!=null&&line!=""&&!line.contains("bestmove")){
                line =  uciEngine.readLineFromEngine(50);
            }
        });
        waitBest.start();
        Thread dataCombine = new Thread(() -> {
            try {
                waitBest.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final UCIEngine uci = uciEngine;
            String res = "position fen " + fenUtil.createFen(chessModel);

            uciEngine.writeLineToEngine(res);
            uciEngine.writeLineToEngine("go depth " + depth);
            Thread startupThread = new Thread(() -> {
                try {
//                    String newLine =  uci.readLineFromEngine(50);
//                    if(!newLine.split(" ")[2].equals("1")){
//                        newLine =  uci.readLineFromEngine(50);
//                        System.out.println("Here");
//                    }
                    String line = "1";
                    int i = 1;
                    String firstLine = "1";
                    int depth=1;
                    while (line!=null&&!line.contains("bestmove") &&line!="") {
                        line = uci.readLineFromEngine(150);
//                        try{
//                        if(!Integer.toString(depth).equals(line.split(" ")[2])){
//                            continue;
//                        }}
//                        catch (Exception e){
//                            continue;
//                        }
                        depth++;
                        if (line!=null&&line!=""&&line.contains("cp")) {

                            String[] arr = formString(line, firstLine);
                            int finalI = i;
                            getActivity().runOnUiThread(new Runnable() {
                               @Override
                                public void run() {

                                        if (arr != null) {
                                            if (arr[0].length() > 6) {
                                                textViewArrayList.get(finalI - 1).setText(arr[0]);
                                                if (finalI == 1) {
                                                    String newArr = arr[0].replace(",", ".");
                                                    scaleView.setCurrText(Float.valueOf(newArr.split(" ")[0]));
                                                }
                                            }
                                        }
                                    }

                            });
//                            String[] arr = formString(line, firstLine);
//                            if(arr!=null&&!isReset) {
//                                if (arr[0].length() > 6) {
//                                    textViewArrayList.get(i - 1).setText(arr[0]);
//                                    if (i == 1) {
//                                        scaleView.setCurrText(Float.valueOf(arr[0].split(" ")[0]));
//                                    }
//                                }

                                i++;
                                if (i > Integer.valueOf(multiPv)) {
                                    i = 1;
                                }
                            }

                        }
                    //}


                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            });
            startupThread.start();

        });

        try {
            dataCombine.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public  void sendAndDisplayAnalysis(int block) {
     //    if (fen.equals("")) {
        //    replayAnalysis(block);
       // } else {
            sendFen();
       // }


    }

    @Override
    public void updatePromotion(ChessHistoryStep step) {
        chessHistory.remove(chessHistory.get(chessHistory.size()-1));
        chessHistory.add(step);
        mAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(chessHistory.size() - 1);
        recyclerView.invalidate();
        dataPasser.onDataPass(chessModel);
    }

    public synchronized String[] formString(String line, String firstLine) {
        String res = "";
        String[] lineWord = line.split(" ");
        int pos = -1;
        String numOfPv = "0";
        for (int i = 0; i < lineWord.length; i++) {
            if (lineWord[i].contains(" ")) {
                continue;
            }
            if (pos != -1) {
                if (lineWord[pos].equals("cp")||lineWord[pos].equals("mate")) {
                    if(lineWord[pos].equals("mate")){
                        res+=lineWord[i];
                    }else {

                        res += String.format("%.2f", Float.parseFloat(lineWord[i]) / 100) + " ";
                    }
                    pos = -1;
                } else if (lineWord[pos].equals("pv")) {
                    try {
                        res += turnToNormal(lineWord[i]);
                    } catch (Exception e) {

                    }
                } else if (lineWord[pos].equals("multipv")) {
                    if (Integer.valueOf(firstLine) > Integer.valueOf(lineWord[i])) {
                        firstLine = lineWord[i];
                    }
                    numOfPv = lineWord[i];
                    pos = -1;
                }
            }
            if (lineWord[i].equals("cp") || lineWord[i].equals("pv")||lineWord[i].equals("mate") ) {

                pos = i;
            }
        }
        return new String[]{res, numOfPv};
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

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void analysisBegin() throws IOException {
        String mString = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("engineList", "");
        depth = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("depth", "20");

        String threads = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("treadsCount", "1");
        multiPv = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("multiPv", "1");
        String hashType = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("hashType", "16");
        if (mString.equals("") || mString.equals("1") || mString.equals("0")) {
            mString = "stockfish";
        }
        AssetManager assetManager = getContext().getAssets();
        String[] namesInclude = assetManager.list("arm64-v8a");
        Toast.makeText(getActivity().getApplicationContext(), "Текущий движок " + mString, Toast.LENGTH_LONG).show();
        if (!Arrays.stream(namesInclude).anyMatch(mString::equals)) {
            TinyDB tinyDB = new TinyDB(getContext());
            ArrayList<String> names = tinyDB.getListString("externalEngine");
            String finalMString = mString;
            String newString = names.stream().filter(carnet -> carnet.contains(finalMString)).findFirst().orElse("");
            mString = newString;
        }

//        UCIEngine uciEngine = null;
        String sep = File.separator;
        engineOptions.workDir = getContext().getExternalFilesDir(null) + sep + engineLogDir;
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
        chessView.setUciEngine(uciEngine);
        final UCIEngine uci = uciEngine;
        Thread prepareEngine = new Thread(() -> {
            uciEngine.clearOptions();
            uciEngine.writeLineToEngine("uci");

            String lineInfo = "1";
            int i = 0;
            while (lineInfo != null && !lineInfo.equals("uciok") && i < 35) {
                lineInfo = uci.readLineFromEngine(50);
                i++;
            }

            if (lineInfo!=null&&(lineInfo.contains("error") || lineInfo.contains("expt"))) {
                Toast.makeText(getContext(), "Ошибка при загрузке движка", Toast.LENGTH_LONG).show();
                chessView.setIsClickable(false);
                forward.setClickable(false);
                backward.setClickable(false);
                reset.setClickable(false);

            } else {
                String lineInfoSetting = "1";
                if (!threads.equals("")) {
                    uciEngine.setOption("Threads", threads);
                }
                lineInfoSetting = "1";
                i = 0;
                while (lineInfoSetting != null && !lineInfoSetting.equals("uciok") && i < 3) {
                    lineInfoSetting = uci.readLineFromEngine(50);
                    i++;
                }
                if (!hashType.equals("")) {
                    uciEngine.setOption("Hash", hashType);
                }
                lineInfoSetting = "1";
                i = 0;
                while (lineInfoSetting != null && !lineInfoSetting.equals("uciok") && i < 3) {
                    lineInfoSetting = uci.readLineFromEngine(50);
                    i++;
                }
                if (!multiPv.equals("")) {
                    uciEngine.setOption("MultiPV", multiPv);
                }
                lineInfoSetting = "1";
                i = 0;
                while (lineInfoSetting != null && !lineInfoSetting.equals("uciok") && i < 3) {
                    lineInfoSetting = uci.readLineFromEngine(50);
                    i++;
                }
                uciEngine.setOption("Use NNUE", false);
                lineInfoSetting = "1";
                i = 0;
                while (lineInfoSetting != null && !lineInfoSetting.equals("uciok") && i < 3) {
                    lineInfoSetting = uci.readLineFromEngine(50);
                    i++;
                }
                if (lineInfoSetting!=null&&(lineInfoSetting.contains("error") || lineInfoSetting.contains("expt"))) {
                    Toast.makeText(getContext(), "Ошибка при загрузке движка", Toast.LENGTH_LONG).show();
                    chessView.setIsClickable(false);
                    forward.setClickable(false);
                    backward.setClickable(false);
                    reset.setClickable(false);
                }
                uciEngine.writeLineToEngine("isready");

                uciEngine.readLineFromEngine(50);
                uciEngine.writeLineToEngine("ucinewgame");
            }

        });
        prepareEngine.start();

    }
@Override
public void onPause() {

    super.onPause();
    uciEngine.writeLineToEngine("quit");
}

    private void reset(){
        ChessHistory.checkingPiece = null;
        ChessHistory.currCountStep = 0;
        ChessHistory.currPlayer = Player.WHITE;
        ChessHistory.checkPiece = null;
        ChessHistory.isCheck = false;
        ChessHistory.isForward=false;
        for(int i=0;i<textViewArrayList.size();i++){
            textViewArrayList.get(i).setText("");
        }
        uciEngine.writeLineToEngine("stop");
        uciEngine.readLineFromEngine(50);
        uciEngine.writeLineToEngine("ucinewgame");

    }
}
