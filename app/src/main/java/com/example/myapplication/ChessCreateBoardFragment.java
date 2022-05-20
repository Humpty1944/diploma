package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import chessModel.ChessHistory;
import chessModel.ChessHistoryStep;
import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Player;
import chessPiece.Bishop;
import chessPiece.King;
import chessPiece.Knight;
import chessPiece.Pawn;
import chessPiece.Piece;
import chessPiece.Queen;
import chessPiece.Rook;
import interfaces.ChessCreateInterface;
import interfaces.ChessHistoryInterfaceCallbacks;
import views.BoardView;
import views.ChessHistoryAdapter;
import views.PieceChooseAdapter;
//import views.PieceSelectView;
import views.PiecesView;

public class ChessCreateBoardFragment extends Fragment implements ChessCreateInterface {

    View view;
    BoardView boardView;
    PiecesView piecesView;
    Set<Piece> pieces = new HashSet<>();
   // PieceSelectView pieceSelectView;
    View pieceChoose;
    Object[] currInfo = new Object[]{null, null};
    RecyclerView recyclerViewWhite;
    private PieceChooseAdapter mAdapterWhite;
    RecyclerView recyclerViewBlack;
    private PieceChooseAdapter mAdapterBlack;
    ConstraintLayout layout;
    int wrapContent = ConstraintLayout.LayoutParams.WRAP_CONTENT;
    ConstraintLayout.LayoutParams lParams = new ConstraintLayout.LayoutParams(wrapContent, wrapContent);

    CheckBox whiteShort;
    CheckBox whiteLong;
    CheckBox blackShort;
    CheckBox blackLong;

    private final ArrayList<Integer> imgChessWhite;

    {
        imgChessWhite = new ArrayList<>();
        imgChessWhite.add(R.drawable.chess_king_white);
        imgChessWhite.add(R.drawable.chess_queen_white);
        imgChessWhite.add(R.drawable.chess_rook_white);
        imgChessWhite.add(R.drawable.chess_knight_white);
        imgChessWhite.add(R.drawable.chess_bishop_white);
        imgChessWhite.add(R.drawable.chess_pawn_white);
    }
    private final ArrayList<Integer> imgChessBlack;

    {
        imgChessBlack = new ArrayList<>();
        imgChessBlack.add(R.drawable.chess_king_black);
        imgChessBlack.add(R.drawable.chess_queen_black);
        imgChessBlack.add(R.drawable.chess_rook_black);
        imgChessBlack.add(R.drawable.chess_kinght_black);
        imgChessBlack.add(R.drawable.chess_bishop_black);
        imgChessBlack.add(R.drawable.chess_pawn_black);
    }

    ImageButton reset;
    ImageButton delete;
    Button ok;
    Boolean isDeleting = false;
    RelativeLayout deletingView;
    LinearLayoutManager mLayoutManager;
    RadioButton blackTurn;
    RadioButton whiteTurn;
    RadioGroup turnGroup;
    ChessModel chessModel;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_chess, container, false);
        layout = (ConstraintLayout) view.findViewById(R.id.create_chess_layout);
        deletingView=view.findViewById(R.id.buttonDeleteView);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        LinearLayoutManager mLayoutManagerBlack = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mLayoutManagerBlack.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewWhite = view.findViewById(R.id.recyclerViewWhite);
        recyclerViewBlack=view.findViewById(R.id.recyclerViewBlack);
        recyclerViewWhite.setLayoutManager(mLayoutManager);
        recyclerViewBlack.setLayoutManager(mLayoutManagerBlack);
        turnGroup=view.findViewById(R.id.group_step_color);
        blackTurn = view.findViewById(R.id.radio_black_step);
        whiteTurn=view.findViewById(R.id.radio_white_step);
        reset=view.findViewById(R.id.resetPiecesButton);
        delete=view.findViewById(R.id.buttonDelete);
        ok=view.findViewById(R.id.buttonOk);

        boardView = view.findViewById(R.id.boardView);
        piecesView=view.findViewById(R.id.piecesView);

        whiteShort=view.findViewById(R.id.box_white_short);
        whiteLong=view.findViewById(R.id.box_white_long);
        blackShort=view.findViewById(R.id.box_black_short);
        blackLong=view.findViewById(R.id.box_black_long);

        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chessModel = new ChessModel();
        Drawable whiteIcon = getContext().getDrawable(R.drawable.chess_pawn_white);
        whiteIcon.setBounds(0,0,100,100);

        whiteTurn.setCompoundDrawables(null,null,whiteIcon, null);
        Drawable blackIcon = getContext().getDrawable(R.drawable.chess_pawn_black);
        blackIcon.setBounds(0,0,100,100);
        blackTurn.setCompoundDrawables(null,null,blackIcon, null);
       // whiteTurn.setImageBitmap(BitmapFactory.decodeResource(holder.itemView.getContext().getResources(), bitmap,options));

        piecesView.setChessCreateInterface(this);

        mAdapterWhite = new PieceChooseAdapter(imgChessWhite, new ChessHistoryInterfaceCallbacks() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(Object object) {
                if((Player) currInfo[1]==Player.BLACK){
                    mAdapterBlack.setRow_index(-1);
                }
                if(isDeleting){
                    isDeleting=false;
                    deletingView.setBackgroundColor(Color.WHITE);
                }
                Integer pos = (Integer) object;
                ChessMan chessMan = ChessMan.PAWN;
                if(pos==0){
                    chessMan=ChessMan.KING;
                }else if(pos==1){
                    chessMan=ChessMan.QUEEN;
                }
                else if(pos==2){
                    chessMan=ChessMan.ROOK;
                }
                else if(pos==3){
                    chessMan=ChessMan.KNIGHT;
                }
                else if(pos==4){
                    chessMan=ChessMan.BISHOP;
                }
                currInfo[0]=chessMan;
                currInfo[1]=Player.WHITE;

            }
        });
        mAdapterBlack = new PieceChooseAdapter(imgChessBlack, new ChessHistoryInterfaceCallbacks() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(Object object) {
                if((Player) currInfo[1]==Player.WHITE){
                    mAdapterWhite.setRow_index(-1);
                }
                if(isDeleting){
                    isDeleting=false;
                    deletingView.setBackgroundColor(Color.WHITE);
                }
                Integer pos = (Integer) object;
                ChessMan chessMan = ChessMan.PAWN;
                if(pos==0){
                    chessMan=ChessMan.KING;
                }else if(pos==1){
                    chessMan=ChessMan.QUEEN;
                }
                else if(pos==2){
                    chessMan=ChessMan.ROOK;
                }
                else if(pos==3){
                    chessMan=ChessMan.KNIGHT;
                }
                else if(pos==4){
                    chessMan=ChessMan.BISHOP;
                }
                currInfo[0]=chessMan;
                currInfo[1]=Player.BLACK;
                System.out.println(object);
            }
        });
        recyclerViewWhite.setAdapter(mAdapterWhite);
        recyclerViewBlack.setAdapter(mAdapterBlack);
        addClickHelpers();

    }

    private void addClickHelpers(){
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pieces.clear();
                piecesView.invalidate();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                deletingView.setBackgroundColor(R.color.purple_700);
                isDeleting=true;
                mAdapterBlack.setRow_index(-1);
                mAdapterWhite.setRow_index(-1);
//                View parent = (View) view.getParent();
//                if(parent.getId()==R.id.buttonDeleteView){
//                    parent.setBackgroundColor(R.color.purple_700);
//                }
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox()){
                    int indCheck = turnGroup.getCheckedRadioButtonId();
                    if(indCheck==R.id.radio_black_step){
                        ChessHistory.currPlayer=Player.BLACK;
                    }else{
                        ChessHistory.currPlayer=Player.WHITE;
                    }
                     MainActivity mainActivity = (MainActivity) getActivity();
                     mainActivity.changeToPlay(pieces);
                }
            }
        });
    }

    public boolean checkBox(){
        byte whiteKing=0;
        byte blackKing=0;
        byte whiteQueen=0;
        byte blackQueen=0;
        byte whiteKnight=0;
        byte blackKnight=0;
        byte whiteRook=0;
        byte blackRook=0;
        byte whiteBishop=0;
        byte blackBishop=0;
        byte whitePawn=0;
        byte blackPawn=0;
        chessModel.setPieceBox(pieces);
        if(pieces.size()>32){
            Toast.makeText(getContext(),"На доске больше 32 фигур", Toast.LENGTH_LONG).show();
            return false;
        }
        for(Piece p: pieces){
            if(p instanceof King){
                if(p.getPlayer()==Player.BLACK){
                    blackKing++;
                    if(((King) p).isCheck()){
                        Toast.makeText(getContext(),"Черный король находится в шахе", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if(!blackShort.isChecked()&&!blackLong.isChecked()){
                        p.setMoveCount(1);
                    }
                }else{
                    whiteKing++;
                    if(((King) p).isCheck()){
                        Toast.makeText(getContext(),"Белый король находится в шахе", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if(!whiteShort.isChecked()&&!whiteLong.isChecked()){
                        p.setMoveCount(1);
                    }
                }
            }
            if(p instanceof Rook){
                if(p.getPlayer()==Player.BLACK){
                    if(p.getCol()==0&&!blackLong.isChecked()){
                        p.setMoveCount(1);
                    }
                    if(p.getCol()==7&&!blackShort.isChecked()){
                        p.setMoveCount(1);
                    }
                    blackRook++;

                }else{
                    if(p.getCol()==0&&!whiteLong.isChecked()){
                        p.setMoveCount(1);
                    }
                    if(p.getCol()==7&&!whiteShort.isChecked()){
                        p.setMoveCount(1);
                    }
                    whiteRook++;
                }
            }
            if(p instanceof Queen) {
                if(p.getPlayer()==Player.BLACK){
                    blackQueen++;

                }else{
                    whiteQueen++;
                }

            }
            if(p instanceof Bishop) {
                if(p.getPlayer()==Player.BLACK){
                    blackBishop++;

                }else{
                    whiteBishop++;
                }

            }
            if(p instanceof Knight) {
                if(p.getPlayer()==Player.BLACK){
                    blackKnight++;

                }else{
                    whiteKnight++;
                }

            }
            if(p instanceof Pawn) {
                if(p.getPlayer()==Player.BLACK){
                    blackPawn++;

                }else{
                    whitePawn++;
                }

            }
        }

        if(whiteKing!=1||blackKing!=1){
            Toast.makeText(getContext(),"У каждой из сторон должно быть по одному королю", Toast.LENGTH_LONG).show();
            return false;
        }
        if(whitePawn>8||blackPawn>8){
            Toast.makeText(getContext(),"У каждой из сторон должно быть не более 8 пешек", Toast.LENGTH_LONG).show();
            return false;
        }
        if(whiteQueen>2||blackQueen>2){
            Toast.makeText(getContext(),"У каждой из сторон должно быть не более 2 королев", Toast.LENGTH_LONG).show();
            return false;
        }
        if(whiteRook>3||blackRook>3){
            Toast.makeText(getContext(),"У каждой из сторон должно быть не более 3 ладьи", Toast.LENGTH_LONG).show();
            return false;
        }
        if(whiteKnight>3||blackKnight>3){
            Toast.makeText(getContext(),"У каждой из сторон должно быть не более 3 коней", Toast.LENGTH_LONG).show();
            return false;
        }
        if(whiteBishop>3||blackBishop>3){
            Toast.makeText(getContext(),"У каждой из сторон должно быть не более 3 слонов", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public Piece getChosenPiece() {
        if(currInfo[0]!=null){
            Piece piece = createPiece();
            return piece;
        }
        return null;
    }

    @Override
    public Piece piecePos(int col, int row) {
        for (Piece p : pieces) {
            if (col == p.getCol() && row == p.getRow()) {
                return p;
            }

        }
        return null;
    }

    @Override
    public void addPiece(Piece piece,int col, int row) {
        piece.setRow(row);
        piece.setCol(col);
        piece.setChessModel(chessModel);
        pieces.add(piece);
        piecesView.invalidate();
    }

    @Override
    public void deletePiece(int col, int row) {
        pieces.remove(piecePos(col, row));
        piecesView.invalidate();
    }

    @Override
    public void sendInfo(Object[] objects) {
        System.out.println(objects[0]);
        currInfo=objects;
    }

    @Override
    public void onClick(int pos) {

    }

    @Override
    public boolean isDeleting() {
        return isDeleting;
    }

    private Piece createPiece(){
        try {
            ChessMan chessMan = (ChessMan) currInfo[0];
            Player player = (Player) currInfo[1];
            Piece newPice = null;
            if (chessMan == ChessMan.ROOK) {
                int id = player == Player.BLACK ? R.drawable.chess_rook_black : R.drawable.chess_rook_white;
                newPice = new Rook(-1, -1, player, ChessMan.ROOK, id, null);
            } else if (chessMan == ChessMan.QUEEN) {
                int id = player == Player.BLACK ? R.drawable.chess_queen_black : R.drawable.chess_queen_white;
                newPice = new Queen(-1, -1, player, ChessMan.QUEEN, id, null);
            } else if (chessMan == ChessMan.KING) {
                int id = player == Player.BLACK ? R.drawable.chess_king_black : R.drawable.chess_king_white;
                newPice = new King(-1, -1, player, ChessMan.KING, id, null);
            } else if (chessMan == ChessMan.BISHOP) {
                int id = player == Player.BLACK ? R.drawable.chess_bishop_black : R.drawable.chess_bishop_white;
                newPice = new Bishop(-1, -1, player, ChessMan.BISHOP, id, null);
            } else if (chessMan == ChessMan.KNIGHT) {
                int id = player == Player.BLACK ? R.drawable.chess_kinght_black : R.drawable.chess_knight_white;
                newPice = new Knight(-1, -1, player, ChessMan.KNIGHT, id, null);
            } else {
                int id = player == Player.BLACK ? R.drawable.chess_pawn_black : R.drawable.chess_pawn_white;
                newPice = new Pawn(-1, -1, player, ChessMan.PAWN, id, null);
            }
            return newPice;
        }catch (Exception e){
            return null;
        }
    }
}
