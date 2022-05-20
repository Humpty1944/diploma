//package views;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//
//import androidx.annotation.Nullable;
//
//import com.example.myapplication.R;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//import chessModel.ChessMan;
//import chessModel.Player;
//import chessPiece.King;
//import chessPiece.Piece;
//import interfaces.ChessCreateInterface;
//
//public class PieceSelectView extends View {
//
//
//    ChessCreateInterface chessCreateInterface;
//    View view;
//    ArrayList<RadioButton> chessPieces = new ArrayList<>();
//
//    RadioButton whiteKing;
//    RadioButton whiteQueen;
//    RadioButton whiteRook;
//    RadioButton whiteBishop;
//    RadioButton whiteKnight;
//    RadioButton whitePawn;
//    RadioButton blackKing;
//    RadioButton blackQueen;
//    RadioButton blackRook;
//    RadioButton blackBishop;
//    RadioButton blackKnight;
//    RadioButton blackPawn;
//
//    RadioGroup white;
//    RadioGroup black;
//
//    private final Set<Integer> imgChess;
//
//    {
//        imgChess = new HashSet<>();
//        imgChess.add(R.drawable.chess_king_black);
//        imgChess.add(R.drawable.chess_king_white);
//        imgChess.add(R.drawable.chess_queen_black);
//        imgChess.add(R.drawable.chess_queen_white);
//        imgChess.add(R.drawable.chess_rook_black);
//        imgChess.add(R.drawable.chess_rook_white);
//        imgChess.add(R.drawable.chess_kinght_black);
//        imgChess.add(R.drawable.chess_knight_white);
//        imgChess.add(R.drawable.chess_bishop_black);
//        imgChess.add(R.drawable.chess_bishop_white);
//        imgChess.add(R.drawable.chess_pawn_black);
//        imgChess.add(R.drawable.chess_pawn_white);
//    }
//
//    private final Map<Integer, Bitmap> bitmaps = new HashMap<Integer, Bitmap>();
//    private void loadBitmap() {
//        for (Integer it : imgChess) {
//            bitmaps.put(it, BitmapFactory.decodeResource(getResources(), it));
//
//        }
//    }
//    boolean isCurrentClick = false;
//
////    public void setView(View view) {
////        this.view = view;
////        getButtons();
////    }
//
//
//     public PieceSelectView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//         loadBitmap();
//        LayoutInflater inflater = LayoutInflater.from(context);
//        view = inflater.inflate(R.layout.pieces_select_view, null, false);
//        getButtons();
//
//    }
//
//    public PieceSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        loadBitmap();
//
//        LayoutInflater inflater = LayoutInflater.from(context);
//        view = inflater.inflate(R.layout.pieces_select_view, null, false);
//
//        getButtons();
//
//    }
////
////    public PieceSelectView(Context context) {
////        super(context);
////        LayoutInflater inflater = LayoutInflater.from(context);
////         view = inflater.inflate(R.layout.pieces_select_view, null, false);
////        getButtons();
//////        whiteKing = view.findViewById(R.id.king_white);
//////        whiteQueen = view.findViewById(R.id.queen_white);
//////        whiteRook = view.findViewById(R.id.rook_white);
//////        whiteBishop = view.findViewById(R.id.bishop_white);
//////        whiteKnight = view.findViewById(R.id.knight_white);
//////        whitePawn = view.findViewById(R.id.pawn_white);
//////        blackKing = view.findViewById(R.id.king_black);
//////        blackQueen = view.findViewById(R.id.queen_black);
//////        blackRook = view.findViewById(R.id.rook_black);
//////        blackBishop = view.findViewById(R.id.bishop_black);
//////        blackKnight = view.findViewById(R.id.knight_black);
//////        blackPawn = view.findViewById(R.id.pawn_black);
//////
//////        white = view.findViewById(R.id.radioGroupWhite);
//////        black = view.findViewById(R.id.radioGroupBlack);
//////        addClickListener();
////
////    }
//
//    private void getButtons() {
////        white = new RadioGroup(getContext());
////        white.setOrientation(RadioGroup.HORIZONTAL);
////        black = new RadioGroup(getContext());
////        black.setOrientation(RadioGroup.HORIZONTAL);
////        RadioButton b = new RadioButton(getContext());
////        b.setBackgroundResource(R.drawable.chess_king_white);
////        chessPieces.add(b);
////        white.addView(b);
////        b = new RadioButton(getContext());
////        b.setBackgroundResource(R.drawable.chess_queen_white);
////        chessPieces.add(b);
////        white.addView(b);
////        b = new RadioButton(getContext());
////        b.setBackgroundResource(R.drawable.chess_rook_white);
////        chessPieces.add(b);
////        white.addView(b);
////        b = new RadioButton(getContext());
////        b.setBackgroundResource(R.drawable.chess_bishop_white);
////        chessPieces.add(b);
////        white.addView(b);
////        b = new RadioButton(getContext());
////        b.setBackgroundResource(R.drawable.chess_knight_white);
////        chessPieces.add(b);
////        white.addView(b);
////        b = new RadioButton(getContext());
////        b.setBackgroundResource(R.drawable.chess_pawn_white);
////        chessPieces.add(b);
////        white.addView(b);
////
////        b = new RadioButton(getContext());
////        b.setBackgroundResource(R.drawable.chess_king_black);
////        chessPieces.add(b);
////        black.addView(b);
////        b = new RadioButton(getContext());
////        b.setBackgroundResource(R.drawable.chess_queen_black);
////        chessPieces.add(b);
////        black.addView(b);
////        b = new RadioButton(getContext());
////        b.setBackgroundResource(R.drawable.chess_rook_black);
////        chessPieces.add(b);
////        black.addView(b);
////        b = new RadioButton(getContext());
////        b.setBackgroundResource(R.drawable.chess_bishop_black);
////        chessPieces.add(b);
////        black.addView(b);
////        b = new RadioButton(getContext());
////        b.setBackgroundResource(R.drawable.chess_kinght_black);
////        chessPieces.add(b);
////        black.addView(b);
////        b = new RadioButton(getContext());
////        b.setBackgroundResource(R.drawable.chess_pawn_black);
////        chessPieces.add(b);
////        black.addView(b);
//
//
//        whiteKing = view.findViewById(R.id.king_white);
//        chessPieces.add(whiteKing);
//        whiteQueen = view.findViewById(R.id.queen_white);
//        chessPieces.add(whiteQueen);
//        whiteRook = view.findViewById(R.id.rook_white);
//        chessPieces.add(whiteRook);
//        whiteBishop = view.findViewById(R.id.bishop_white);
//        chessPieces.add(whiteBishop);
//        whiteKnight = view.findViewById(R.id.knight_white);
//        chessPieces.add(whiteKnight);
//        whitePawn = view.findViewById(R.id.pawn_white);
//        chessPieces.add(whitePawn);
//        blackKing = view.findViewById(R.id.king_black);
//        chessPieces.add(blackKing);
//        blackQueen = view.findViewById(R.id.queen_black);
//        chessPieces.add(blackQueen);
//        blackRook = view.findViewById(R.id.rook_black);
//        chessPieces.add(blackRook);
//        blackBishop = view.findViewById(R.id.bishop_black);
//        chessPieces.add(blackBishop);
//        blackKnight = view.findViewById(R.id.knight_black);
//        chessPieces.add(blackKnight);
//        blackPawn = view.findViewById(R.id.pawn_black);
//        chessPieces.add(blackPawn);
//
////        white = view.findViewById(R.id.radioGroupWhite);
////        black = view.findViewById(R.id.radioGroupBlack);
//        addClickListener();
//    }
//
//    private void addClickListener() {
//        for (int i = 0; i < chessPieces.size(); i++) {
//            chessPieces.get(i).setOnClickListener(new View.OnClickListener() {
//                @SuppressLint("ResourceAsColor")
//                @Override
//                public void onClick(View view) {
//                    isCurrentClick = true;
//                   view.setBackgroundColor(R.color.purple_700);
//                    Object[] info = getCurrPiece(view.getId());
//                    chessCreateInterface.sendInfo(info);
//
//                }
//            });
//        }
////        white.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(RadioGroup radioGroup, int i) {
////                if (i != -1 && isCurrentClick) {
////                    black.clearCheck();
////                }
////            }
////        });
////        black.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(RadioGroup radioGroup, int i) {
////                if (i != -1 && isCurrentClick) {
////                    black.clearCheck();
////                }
////            }
////        });
//    }
//
//    private Object[] getCurrPiece(int id) {
//        if (id == R.id.king_white) {
//            return new Object[]{ChessMan.KING, Player.WHITE};
//        }
//        if (id == R.id.queen_white) {
//            return new Object[]{ChessMan.QUEEN, Player.WHITE};
//        }
//        if (id == R.id.rook_white) {
//            return new Object[]{ChessMan.ROOK, Player.WHITE};
//        }
//        if (id == R.id.bishop_white) {
//            return new Object[]{ChessMan.BISHOP, Player.WHITE};
//        }
//        if (id == R.id.knight_white) {
//            return new Object[]{ChessMan.KNIGHT, Player.WHITE};
//        }
//        if (id == R.id.pawn_white) {
//            return new Object[]{ChessMan.PAWN, Player.WHITE};
//        }
//        if (id == R.id.king_black) {
//            return new Object[]{ChessMan.KING, Player.BLACK};
//        }
//        if (id == R.id.queen_black) {
//            return new Object[]{ChessMan.QUEEN, Player.BLACK};
//        }
//        if (id == R.id.rook_black) {
//            return new Object[]{ChessMan.ROOK, Player.BLACK};
//        }
//        if (id == R.id.bishop_black) {
//            return new Object[]{ChessMan.BISHOP, Player.BLACK};
//        }
//        if (id == R.id.knight_black) {
//            return new Object[]{ChessMan.KNIGHT, Player.BLACK};
//        }
//        if (id == R.id.pawn_black) {
//            return new Object[]{ChessMan.PAWN, Player.BLACK};
//        }
//        return null;
//
//    }
//
//    public void setChessCreateInterface(ChessCreateInterface chessCreateInterface) {
//        this.chessCreateInterface = chessCreateInterface;
//    }
//}
