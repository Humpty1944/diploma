package chessModel;


import android.graphics.PointF;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import chessPiece.Bishop;
import chessPiece.King;
import chessPiece.Knight;
import chessPiece.Pawn;
import chessPiece.Piece;
import chessPiece.Queen;
import chessPiece.Rook;
import engine.work.UCIEngine;
import interfaces.ChessInterface;
import openings.Opening;
import views.ChessBoardView;

public class ChessModel {
    private Set<Piece> pieceBox = new HashSet<>();
    private String fen = "";
    private boolean isCasteling = false;
    public ChessInterface chessInterface = null;
    private ChessBoardView boardView = null;
    private ArrayList<Knight> whiteKnights = new ArrayList<>();
    private ArrayList<Knight> blackKnights = new ArrayList<>();
    private ArrayList<ChessHistoryStep> chessHistorySteps;
    private Player currPlayer = Player.WHITE;
    private King whiteKing;
    private King blackKing;
    private boolean isPromoted = false;
    private ChessMan promotedType = null;
    private byte isCastlingTurn = 0;
    private List<Opening> openings;
    private List<Opening> currOpenings;
    private String gameName = "";
    private String whitePlayer = "";
    private String blackPlayer = "";

    private Piece checkingPiece=null;
    private Piece checkPiece=null;

    public void reset() {
        currPlayer = Player.WHITE;
        pieceBox = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            addPiece((new Rook(0 + i * 7, 0, Player.WHITE, ChessMan.ROOK, R.drawable.chess_rook_white, this)));
            addPiece((new Rook(0 + i * 7, 7, Player.BLACK, ChessMan.ROOK, R.drawable.chess_rook_black, this)));
            Knight whiteKnight = new Knight(1 + i * 5, 0, Player.WHITE, ChessMan.KNIGHT, R.drawable.chess_knight_white, this);
            Knight blackKnight = new Knight(1 + i * 5, 7, Player.BLACK, ChessMan.KNIGHT, R.drawable.chess_kinght_black, this);
            whiteKnights.add(whiteKnight);
            blackKnights.add(blackKnight);
            addPiece(whiteKnight);
            addPiece(blackKnight);
            addPiece((new Bishop(2 + i * 3, 0, Player.WHITE, ChessMan.BISHOP, R.drawable.chess_bishop_white, this)));
            addPiece((new Bishop(2 + i * 3, 7, Player.BLACK, ChessMan.BISHOP, R.drawable.chess_bishop_black, this)));

        }
        for (int i = 0; i < 8; i++) {
            addPiece((new Pawn(i, 1, Player.WHITE, ChessMan.PAWN, R.drawable.chess_pawn_white, this)));
            addPiece((new Pawn(i, 6, Player.BLACK, ChessMan.PAWN, R.drawable.chess_pawn_black, this)));
        }
        addPiece((new Queen(3, 0, Player.WHITE, ChessMan.QUEEN, R.drawable.chess_queen_white, this)));
        addPiece((new Queen(3, 7, Player.BLACK, ChessMan.QUEEN, R.drawable.chess_queen_black, this)));
        whiteKing = new King(4, 0, Player.WHITE, ChessMan.KING, R.drawable.chess_king_white, this);
        blackKing = (new King(4, 7, Player.BLACK, ChessMan.KING, R.drawable.chess_king_black, this));
        addPiece(whiteKing);
        addPiece(blackKing);
    }

    public void cleanAll() {
        reset();
        isCasteling = false;
        promotedType = null;
        chessHistorySteps = new ArrayList<>();
        fen = "";
        gameName = "";
        whitePlayer = "";
        blackPlayer = "";
        checkingPiece=null;
        checkPiece=null;
        whiteKnights=new ArrayList<>();
        blackKnights=new ArrayList<>();
    }

    public void setPieceBox(Set<Piece> pieces) {

        blackKnights = new ArrayList<>();
        whiteKnights = new ArrayList<>();
        for (Piece p : pieces) {
            p.setChessModel(this);
            if (p.getPlayer() == Player.WHITE) {
                if (p.getChessMan() == ChessMan.KING) {
                    whiteKing = (King) p;
                } else if (p.getChessMan() == ChessMan.KNIGHT) {
                    whiteKnights.add((Knight) p);
                } else if (p.getChessMan() == ChessMan.PAWN) {
                    if (p.getRow() != 1) {
                        p.setMoveCount(1);
                        p.setCurrLength(1);
                    }
                }
            } else {
                if (p.getChessMan() == ChessMan.KING) {
                    blackKing = (King) p;
                } else if (p.getChessMan() == ChessMan.KNIGHT) {
                    blackKnights.add((Knight) p);
                } else if (p.getChessMan() == ChessMan.PAWN) {
                    if (p.getRow() != 6) {
                        p.setMoveCount(1);
                        p.setCurrLength(1);
                    }
                }
            }


        }
        this.pieceBox = pieces;
    }

    public ChessModel() {
        // reset();
        ChessHistory.chessModel = this;
        ChessHistory.isCheck = false;
        ChessHistory.currPlayer = Player.WHITE;
        ChessHistory.checkingPiece = null;
        ChessHistory.checkPiece = null;
        checkingPiece=null;
        checkPiece=null;
        currPlayer = Player.WHITE;
        if (!ChessHistory.isLoad) {
            ChessHistory.chessHistory = new ArrayList<>();
            chessHistorySteps = new ArrayList<>();
        }
        reset();

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void backMovePiece(ChessHistoryStep step) {
        Square from = step.getFromSquare();
        Square to = step.getToSquare();
        Piece movingPiece = step.getChessPiece();
        if (movingPiece == null) {
            return;
        }
//        Piece movingPiece = pieceAt(to.getRow(), to.getCol());
        if (step.getIsCasteling() != 0) {
            unMoveCasteling(movingPiece, step.getIsCasteling());
        } else {

            movingPiece.setCol(from.getCol());
            movingPiece.setRow(from.getRow());
            pieceBox.removeIf(x -> x.getRow() == to.getRow() && x.getCol() == to.getCol());
            pieceBox.add(movingPiece);
            // movingPiece.setMoveCount(movingPiece.getMoveCount() - 1);

            if (step.getCapturePiece() != null) {
                pieceBox.add(step.getCapturePiece());
                if (step.getCapturePiece() instanceof Knight) {
                    if (movingPiece.getPlayer() == Player.BLACK) {
                        blackKnights.add((Knight) step.getCapturePiece());
                    } else {
                        whiteKnights.add((Knight) step.getCapturePiece());
                    }
                }
            }
            if (step.isPromoted()) {
                movingPiece = unPromoted(movingPiece);
            }
        }
        int count = movingPiece.getMoveCount();
        movingPiece.setSameMoveCount(count - 1);
        // movingPiece.setMoveCount(count-1);

        step.setChessPiece(movingPiece);
        ChessHistory.currPlayer = ChessHistory.currPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
        currPlayer = currPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
        if (whiteKing.isCheck() && currPlayer == Player.WHITE) {
           // ChessHistory.checkingPiece = movingPiece;
            ChessHistory.checkPiece = whiteKing;
            checkPiece = whiteKing;
            //checkingPiece=movingPiece;
            ChessHistory.isCheck = true;

            if (boardView != null) {
                boardView.setCheckKing(whiteKing);
            }
        } else if (currPlayer == Player.WHITE) {
            ChessHistory.checkingPiece = null;
            ChessHistory.checkPiece = null;
            checkPiece = null;
            ChessHistory.isCheck = false;
            checkingPiece=null;
            if (boardView != null) {
                boardView.setCheckKing(null);
            }
        }
        if (blackKing.isCheck() && currPlayer == Player.BLACK) {
          //  ChessHistory.checkingPiece = movingPiece;
            ChessHistory.checkPiece = blackKing;
            ChessHistory.isCheck = true;
            checkPiece = blackKing;
            //checkingPiece=movingPiece;
            if (boardView != null) {
                boardView.setCheckKing(blackKing);
            }
        } else if (currPlayer == Player.BLACK) {
            ChessHistory.checkingPiece = null;
            ChessHistory.checkPiece = null;
            ChessHistory.isCheck = false;
            checkingPiece=null;
            checkPiece = null;
            if (boardView != null) {
                boardView.setCheckKing(null);
            }
        }
        ChessHistory.currCountStep--;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void moveForwardPiece(ChessHistoryStep step) {
        Square from = step.getFromSquare();
        Square to = step.getToSquare();
        if (from == null) {
            ChessHistory.currCountStep++;
            return;
        }
        Piece movingPiece = pieceAt(from.getRow(), from.getCol());
        if (movingPiece == null) {
            return;
        }
        Piece piece = pieceAt(to.getRow(), to.getCol());
        byte isStepCast = step.getIsCasteling();

        if (isStepCast == 1) {
            Piece pieceCast = pieceAt(movingPiece.getRow(), movingPiece.getCol() + 3);
            // moveCasteling(movingPiece, pieceAt(movingPiece.getRow(), movingPiece.getCol()+3));

            movingPiece.setCol(movingPiece.getCol() + 2);
            pieceCast.setCol(pieceCast.getCol() - 2);

        } else if (isStepCast == 2) {
            Piece pieceCast = pieceAt(movingPiece.getRow(), movingPiece.getCol() - 4);

            movingPiece.setCol(movingPiece.getCol() - 2);
            piece.setCol(pieceCast.getCol() + 3);
            // moveCasteling(movingPiece, pieceAt(movingPiece.getRow(), movingPiece.getCol()-4));
        }
        // }
        if ((to.getRow() == 0 || to.getRow() == 7) && movingPiece instanceof Pawn) {
            // if(chessInterface!=null) {
            movingPiece = promotion(movingPiece, step.getPromotedType());
            //chessInterface.promotionView(movingPiece.getPlayer(), movingPiece);
            // }
        }
        if (piece != null && isStepCast != 1 && isStepCast != 2) {
            pieceBox.remove(piece);
            if (piece instanceof Knight) {
                if (movingPiece.getPlayer() == Player.BLACK) {
                    blackKnights.remove(piece);
                } else {
                    whiteKnights.remove(piece);
                }
            }
        }
        Log.v("d",movingPiece.getCol()+" "+ movingPiece.getRow());
        if (isStepCast != 1 && isStepCast != 2) {
            movingPiece.setCol(to.getCol());
            movingPiece.setRow(to.getRow());
        }
        step.setChessPiece(movingPiece);
        Log.v("d",pieceAt(movingPiece.getRow(), movingPiece.getCol()).getChessMan()+" "+pieceAt(movingPiece.getRow(), movingPiece.getCol()).getPlayer());
        ChessHistory.currPlayer = step.getChessPiece().getPlayer() == Player.WHITE ? Player.BLACK : Player.WHITE;
        currPlayer = ChessHistory.currPlayer;
        if (whiteKing.isCheck() && currPlayer == Player.WHITE) {
           // ChessHistory.checkingPiece = movingPiece;
            ChessHistory.checkPiece = whiteKing;
            ChessHistory.isCheck = true;
            checkPiece = whiteKing;
          //  checkingPiece=movingPiece;
            if (boardView != null) {
                boardView.setCheckKing(whiteKing);
            }
        } else if (currPlayer == Player.WHITE) {
            ChessHistory.checkingPiece = null;
            ChessHistory.checkPiece = null;
            ChessHistory.isCheck = false;
            checkPiece = null;
            checkingPiece=null;
            if (boardView != null) {
                boardView.setCheckKing(null);
            }
        }
        if (blackKing.isCheck() && currPlayer == Player.BLACK) {
            //ChessHistory.checkingPiece = movingPiece;
            ChessHistory.checkPiece = blackKing;
            ChessHistory.isCheck = true;
           // checkingPiece=movingPiece;
            checkPiece = blackKing;
            if (boardView != null) {
                boardView.setCheckKing(blackKing);
            }
        } else if (currPlayer == Player.BLACK) {
            ChessHistory.checkingPiece = null;
            ChessHistory.checkPiece = null;
            ChessHistory.isCheck = false;
            checkPiece = null;
            checkingPiece=null;
            if (boardView != null) {
                boardView.setCheckKing(null);
            }
        }

        ChessHistory.currCountStep++;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void movePiece(Square from, Square to) {

        Piece movingPiece = pieceAt(from);
        isCastlingTurn = 0;
        movingPiece.setChessModel(this);
//        if(ChessHistory.currCountStep!=ChessHistory.chessHistory.size()){
//            System.out.println("asdsadsadasd");
//        }
        //ArrayList<ChessHistoryStep>steps = ChessHistory.chessHistory;
        // if(ChessHistory.currPlayer==movingPiece.getPlayer()) {
        if (currPlayer == movingPiece.getPlayer()) {
            isCasteling = isCastlingAllowed(from, to);
            if (isCasteling) {
                if (movingPiece.getPlayer() == Player.BLACK) {
                    if (from.getCol() < to.getCol() && from.getRow() == to.getRow()) {
                        movePiece(from.getCol(), from.getRow(), 7, to.getRow());
                    } else if (from.getRow() == to.getRow()) {
                        movePiece(from.getCol(), from.getRow(), 0, to.getRow());
                    }
                } else {
                    if (from.getCol() < to.getCol() && from.getRow() == to.getRow()) {
                        movePiece(from.getCol(), from.getRow(), 7, to.getRow());
                    } else if (from.getRow() == to.getRow()) {
                        movePiece(from.getCol(), from.getRow(), 0, to.getRow());
                    }
                }
            } else if (movingPiece.canMove(from, to)) {
                // ChessHistory.chessHistory=steps;
                movePiece(from.getCol(), from.getRow(), to.getCol(), to.getRow());
            } else if (movingPiece instanceof Pawn && checkEnPassant(movingPiece)) {
                movePiece(from.getCol(), from.getRow(), to.getCol(), to.getRow());
            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
        if (fromCol == toCol && fromRow == toRow) {
            return;
        }
        Piece movingPiece = pieceAt(fromRow, fromCol);

        Piece piece = pieceAt(toRow, toCol);

        // if(ChessHistory.currCountStep!=ChessHistory.chessHistory.size()&&!ChessHistory.isForward){
        if(chessHistorySteps==null){
            chessHistorySteps = new ArrayList<>();
        }
        if (ChessHistory.currCountStep != chessHistorySteps.size() && !ChessHistory.isForward) {
            int diff = Math.abs(ChessHistory.currCountStep - chessHistorySteps.size());
            removeHistory(ChessHistory.currCountStep);
            if (chessInterface != null) {
                chessInterface.updateHistory(false, null, ChessHistory.currCountStep);
                ChessHistory.currCountStep = chessHistorySteps.size();
            }
            //System.out.println("problem");
        }
        // List<ChessHistoryStep> a =  ChessHistory.chessHistory;
        if(whiteKnights.size()>2){
            System.out.println("white  "+whiteKnights.size());

        }
        if(blackKnights.size()>2){
            System.out.println("bla problem "+blackKnights.size());
        }
        if (piece != null) {
            if (piece.getPlayer() == movingPiece.getPlayer() && !isCasteling) {
                return;
            } else if (!isCasteling) {
                pieceBox.remove(piece);
                if (piece instanceof Knight) {
                    if (piece.getPlayer() == Player.BLACK) {
                        blackKnights.remove(piece);
                    } else {
                        whiteKnights.remove(piece);
                    }
                }
            }
        }
        ChessHistoryStep step;
        if (movingPiece instanceof Pawn) {
            // movingPiece.setMoveCount(movingPiece.getMoveCount()+1);// what is this&
            movingPiece.setCurrLength(Math.abs(fromRow - toRow));

            if (checkEnPassant(movingPiece)) {
                piece = pieceAt(fromRow, toCol);
                pieceBox.remove(piece);

            }

        }
        if (isCasteling) {
            if (piece.getMoveCount() != 0 || movingPiece.getMoveCount() != 0) {
                return;
            }
            moveCasteling(movingPiece, piece);


        } else {
            movingPiece.setCol(toCol);
            movingPiece.setRow(toRow);
        }
        if ((toRow == 0 || toRow == 7) && movingPiece instanceof Pawn) {
            if (chessInterface != null) {
                chessInterface.promotionView(movingPiece.getPlayer(), movingPiece);
            }
        }
        //= new ChessHistoryStep(fromRow, fromCol, toRow, toCol, movingPiece);

        movingPiece.setMoveCount(1);
        // if(ChessHistory.currPlayer==Player.BLACK){
        if (currPlayer == Player.BLACK) {
            //int whiteTurn =  ChessHistory.chessHistory.get(ChessHistory.chessHistory.size() - 1).getWhiteNumberTurn();
            int whiteTurn = chessHistorySteps.get(chessHistorySteps.size() - 1).getWhiteNumberTurn();
            step = new ChessHistoryStep(fromRow, fromCol, toRow, toCol, movingPiece, whiteTurn);
            step.setCurrMoveCount(chessHistorySteps.get(chessHistorySteps.size() - 1).getCurrMoveCount() + 1);

            if (whiteKing.isCheck() || checkFromKnight(whiteKing)) {
                //   System.out.println("white check");
               // whiteKing.setCheckingPiece(movingPiece);
                ChessHistory.isCheck = true;
                ChessHistory.checkPiece = whiteKing;
                checkPiece = whiteKing;
               // checkingPiece=movingPiece;
                //   ChessHistory.checkingPiece=movingPiece;
                if (boardView != null) {
                    boardView.setCheckKing(whiteKing);
                }
            } else {
                whiteKing.setCheckingPiece(null);
                ChessHistory.checkingPiece = null;
                ChessHistory.isCheck = false;
                ChessHistory.checkPiece = null;
                ChessHistory.checkingPiece = null;
                checkPiece = null;
                checkingPiece=null;
//                if(boardView!=null) {
//                    boardView.setCheckKing(null);
//                }
            }
            ChessHistory.currPlayer = Player.WHITE;
            currPlayer = Player.WHITE;
        } else {
            //  if(ChessHistory.chessHistory.size()==0){
            if (chessHistorySteps.size() == 0) {
                step = new ChessHistoryStep(fromRow, fromCol, toRow, toCol, movingPiece, 1);
                step.setCurrMoveCount(1);
            } else {
                // int whiteTurn =  ChessHistory.chessHistory.get(ChessHistory.chessHistory.size() - 1).getWhiteNumberTurn();
                int whiteTurn = chessHistorySteps.get(chessHistorySteps.size() - 1).getWhiteNumberTurn();
                step = new ChessHistoryStep(fromRow, fromCol, toRow, toCol, movingPiece, whiteTurn + 1);
                step.setCurrMoveCount(chessHistorySteps.get(chessHistorySteps.size() - 1).getCurrMoveCount() + 1);
                // step.setCurrMoveCount(ChessHistory.chessHistory.get(ChessHistory.chessHistory.size() - 1).getCurrMoveCount()+1);
            }
            //ChessHistory.checkingPiece = movingPiece;
            // Piece checkingPiece = blackKing.isCheck();
            if (blackKing.isCheck() || checkFromKnight(blackKing)) {
                // System.out.println("black check");
                ChessHistory.isCheck = true;
               // blackKing.setCheckingPiece(movingPiece);
                ChessHistory.checkPiece = blackKing;
                checkPiece = blackKing;
               // checkingPiece=movingPiece;
                // ChessHistory.checkingPiece=movingPiece;
                if (boardView != null) {
                    boardView.setCheckKing(blackKing);
                }
            } else {
                blackKing.setCheckingPiece(null);
                ChessHistory.checkingPiece = null;
                ChessHistory.isCheck = false;
                ChessHistory.checkPiece = null;
                ChessHistory.checkingPiece = null;
                checkPiece = null;
                checkingPiece=null;
//                if(boardView!=null) {
//                    boardView.setCheckKing(null);
//                }
            }
            ChessHistory.currPlayer = Player.BLACK;
            currPlayer = Player.BLACK;

        }
        if (piece != null && !isCasteling) {
            step.setCapturePiece(piece);
        }

        step.setPromoted(isPromoted);
        step.setPromotedType(promotedType);
        if (!ChessHistory.isForward) {
            if (isCastlingTurn != 0) {
                step.setIsCasteling(isCastlingTurn);
                isCasteling = false;

            }

            ChessHistory.chessHistory.add(step);
            chessHistorySteps.add(step);
            checkOpenings(step);

            if (chessInterface != null) {
                chessInterface.sendAndDisplayAnalysis(chessHistorySteps.size());
                chessInterface.updateHistory(true, step, -1);
            }

        }
        if (ChessHistory.isForward) {
            ChessHistory.isForward = false;
        }
        isPromoted = false;

        ChessHistory.currCountStep = step.getCurrMoveCount();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkOpenings(ChessHistoryStep step) {
        currOpenings = openings;

        if (ChessHistory.currCountStep <= 15 && currOpenings != null && pieceBox.size() >= 30) {
            currOpenings = currOpenings.stream().
                    filter(x -> runSquare(x)).collect(Collectors.toList());

            if (currOpenings.size() != 0) {
                chessInterface.displayCurrOpening(currOpenings.get(0));
            }
            currOpenings = openings;
        }
    }

    private boolean runSquare(Opening opening) {

        List<Square> squareList = opening.getListSquares();

        for (int i = 0; i < squareList.size(); i++) {
            if (chessHistorySteps.size() != squareList.size()) {
                return false;
            }
            if (chessHistorySteps.get(i).getChessPiece() != null && chessHistorySteps.get(i).getToSquare().getCol() == squareList.get(i).getCol() &&
                    chessHistorySteps.get(i).getToSquare().getRow() == squareList.get(i).getRow()) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean checkFromKnight(King currKing) {
        if (currKing.getPlayer() == Player.BLACK) {
            for (int i = 0; i < whiteKnights.size(); i++) {
                if (whiteKnights.get(i).canMove(whiteKnights.get(i).getSquare(), currKing.getSquare())) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < blackKnights.size(); i++) {
                if (blackKnights.get(i).canMove(blackKnights.get(i).getSquare(), currKing.getSquare())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkEnPassant(Piece movingPiece) {
        Square to = null;
        Pawn pieceMove = (Pawn) movingPiece;
        if (movingPiece.getPlayer() == Player.WHITE) {

            to = new Square(movingPiece.getCol() - 1, movingPiece.getRow());
            Piece piece = pieceAt(to);
            if (chessHistorySteps.size() >= 1 && piece != null && chessHistorySteps.get(chessHistorySteps.size() - 1).getChessPiece() != null) {

                if (chessHistorySteps.get(chessHistorySteps.size() - 1).getChessPiece().getCol() != piece.getCol() &&
                        chessHistorySteps.get(chessHistorySteps.size() - 1).getChessPiece().getRow() != piece.getRow()) {
                    return false;
                }

            }
            if (pieceMove.enPassant(new Square(movingPiece.getCol(), movingPiece.getRow()), to)) {
                return true;
            }
            to = new Square(movingPiece.getCol() + 1, movingPiece.getRow());
            piece = pieceAt(to);
            if (chessHistorySteps.size() >= 1 && piece != null && chessHistorySteps.get(chessHistorySteps.size() - 1).getChessPiece() != null) {
                if (chessHistorySteps.get(chessHistorySteps.size() - 1).getChessPiece().getCol() != piece.getCol() &&
                        chessHistorySteps.get(chessHistorySteps.size() - 1).getChessPiece().getRow() != piece.getRow()) {
                    return false;
                }
            }
            if (pieceMove.enPassant(new Square(movingPiece.getCol(), movingPiece.getRow()), to)) {
                return true;
            }
        } else {

            to = new Square(movingPiece.getCol() - 1, movingPiece.getRow());
            Piece piece = pieceAt(to);
            if (chessHistorySteps.size() >= 1 && piece != null && chessHistorySteps.get(chessHistorySteps.size() - 1).getChessPiece() != null) {
                if (chessHistorySteps.get(chessHistorySteps.size() - 1).getChessPiece().getCol() != piece.getCol() &&
                        chessHistorySteps.get(chessHistorySteps.size() - 1).getChessPiece().getRow() != piece.getRow()) {
                    return false;
                }
            }
            if (pieceMove.enPassant(new Square(movingPiece.getCol(), movingPiece.getRow()), to)) {
                return true;
            }
            to = new Square(movingPiece.getCol() + 1, movingPiece.getRow());
            piece = pieceAt(to);
            if (chessHistorySteps.size() >= 1 && piece != null && chessHistorySteps.get(chessHistorySteps.size() - 1).getChessPiece() != null) {
                if (chessHistorySteps.get(chessHistorySteps.size() - 1).getChessPiece().getCol() != piece.getCol() &&
                        chessHistorySteps.get(chessHistorySteps.size() - 1).getChessPiece().getRow() != piece.getRow()) {
                    return false;
                }
            }
            if (pieceMove.enPassant(new Square(movingPiece.getCol(), movingPiece.getRow()), to)) {
                return true;
            }
        }
        return false;
    }

    private void moveCasteling(Piece movingPiece, Piece piece) {
        if (piece.getMoveCount() != 0 || movingPiece.getMoveCount() != 0) {
            return;
        }
        if (piece.getCol() - movingPiece.getCol() > 0) {
            int col = piece.getCol();
            movingPiece.setCol(col - 1);
            piece.setCol(col - 2);
            piece.setSameMoveCount(1);
            // movingPiece.setMoveCount(1);
            isCastlingTurn = 1;

        } else {
            int col = piece.getCol();
            movingPiece.setCol(col + 2);
            piece.setCol(col + 3);
            piece.setSameMoveCount(1);
            //movingPiece.setMoveCount(1);
            isCastlingTurn = 2;
        }

    }

    private void unMoveCasteling(Piece movingPiece, byte Casteling) {
        if (Casteling == 1) {
            Piece piece = pieceAt(movingPiece.getRow(), movingPiece.getCol() - 1);
            pieceBox.remove(piece);
            // int col = movingPiece.getCol();
            movingPiece.setCol(movingPiece.getCol() - 2);
            piece.setCol(piece.getCol() + 2);
            piece.setSameMoveCount(0);
            pieceBox.add(piece);
        } else if (Casteling == 2) {
            Piece piece = pieceAt(movingPiece.getRow(), movingPiece.getCol() + 1);
            // int col = movingPiece.getCol();
            pieceBox.remove(piece);
            movingPiece.setCol(movingPiece.getCol() + 2);
            piece.setCol(piece.getCol() - 3);
            piece.setSameMoveCount(0);
            pieceBox.add(piece);
        }

    }


    private void removeHistory(int block) {
        // ChessHistory.currCountStep=chessHistorySteps.size();
        for (int i = chessHistorySteps.size() - 1; i >= block; i--) {
            chessHistorySteps.remove(chessHistorySteps.get(i));
            //  ChessHistory.chessHistory.remove(  ChessHistory.chessHistory.get(i));

            // ChessHistory.currCountStep--;
        }
    }

    public synchronized Piece pieceAt(Square square) {
        return pieceAt(square.getRow(), square.getCol());
    }

    private synchronized Piece pieceAt(int row, int col) {
        for (Piece p : pieceBox) {
            if (col == p.getCol() && row == p.getRow()) {
                return p;
            }

        }
//        for (ChessPiece p : pieceBox) {
//            if (col == p.getCol() && row == p.getRow()) {
//                return p;
//            }
//
//        }
        return null;
    }

    public void clear() {
        pieceBox.clear();
    }


    private void addPiece(Piece piece) {
        pieceBox.add(piece);
    }

    public Set<Piece> getPieces() {
        return pieceBox;
    }

    private boolean isClearHorizontallyBetween(Square from, Square to) {
        if (from.getRow() != to.getRow()) {
            return false;
        }
        int gap = Math.abs(from.getCol() - to.getCol()) - 1;
        if (gap == 0) {
            return true;
        }
        for (int i = 1; i <= gap; i++) {
            int nextCol = to.getCol() > from.getCol() ? from.getCol() + i : from.getCol() - i;
            if (pieceAt(new Square(nextCol, from.getRow())) != null) {
                return false;
            }
        }
        return true;
    }


    private boolean isCastlingAllowed(Square from, Square to) {
        Piece pieceFrom = pieceAt(from);
        Piece pieceTo = pieceAt(to);
        if (pieceFrom.getMoveCount() != 0) {
            return false;
        }
        if (Math.abs(from.getCol() - to.getCol()) == 2) {
            return pieceFrom instanceof King && isClearHorizontallyBetween(from, to);
        }
        return false;
    }


    public Piece promotion(Piece piece, ChessMan chessMan) {
        //    System.out.println("Prom"+piece.getRow());
        Piece newPice = null;
        if (chessMan == ChessMan.ROOK) {
            int id = piece.getPlayer() == Player.BLACK ? R.drawable.chess_rook_black : R.drawable.chess_rook_white;
            newPice = new Rook(piece.getCol(), piece.getRow(), piece.getPlayer(), ChessMan.ROOK, id, this);
        } else if (chessMan == ChessMan.QUEEN) {
            int id = piece.getPlayer() == Player.BLACK ? R.drawable.chess_queen_black : R.drawable.chess_queen_white;
            newPice = new Queen(piece.getCol(), piece.getRow(), piece.getPlayer(), ChessMan.QUEEN, id, this);
        } else if (chessMan == ChessMan.BISHOP) {
            int id = piece.getPlayer() == Player.BLACK ? R.drawable.chess_bishop_black : R.drawable.chess_bishop_white;
            newPice = new Bishop(piece.getCol(), piece.getRow(), piece.getPlayer(), ChessMan.BISHOP, id, this);
        } else if (chessMan == ChessMan.KNIGHT) {
            int id = piece.getPlayer() == Player.BLACK ? R.drawable.chess_kinght_black : R.drawable.chess_knight_white;
            newPice = new Knight(piece.getCol(), piece.getRow(), piece.getPlayer(), ChessMan.KNIGHT, id, this);
        }

        pieceBox.remove(piece);
        chessHistorySteps.get(chessHistorySteps.size() - 1).setPromoted(true);
        chessHistorySteps.get(chessHistorySteps.size() - 1).setPromotedType(chessMan);
        pieceBox.add(newPice);
        if (chessInterface != null) {
            chessInterface.sendAndDisplayAnalysis(chessHistorySteps.size());
            chessInterface.updatePromotion(chessHistorySteps.get(chessHistorySteps.size() - 1));
           // chessInterface.updateHistory(true, chessHistorySteps.get(chessHistorySteps.size() - 1), -1);
        }
        if (boardView != null) {
            boardView.invalidate();
        }
        return newPice;
    }

    private Piece unPromoted(Piece piece) {
        int id = piece.getPlayer() == Player.BLACK ? R.drawable.chess_pawn_black : R.drawable.chess_pawn_white;
        Piece newPiece = new Pawn(piece.getCol(), piece.getRow(), piece.getPlayer(), ChessMan.PAWN, id, this);
        pieceBox.remove(piece);
        pieceBox.add(newPiece);
        boardView.invalidate();
        return newPiece;
    }

    public void setBoardView(ChessBoardView boardView) {
        this.boardView = boardView;
    }

    public King getWhiteKing() {
        return whiteKing;
    }

    public King getBlackKing() {
        return blackKing;
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(Player currPlayer) {
        this.currPlayer = currPlayer;
    }

    public synchronized ArrayList<ChessHistoryStep> getChessHistorySteps() {
        return chessHistorySteps;
    }

    public synchronized void setChessHistorySteps(ArrayList<ChessHistoryStep> steps) {
        chessHistorySteps = steps;
    }

    public synchronized void setChessHistorySteps(ChessHistoryStep step) {
        if(chessHistorySteps==null){
            chessHistorySteps = new ArrayList<>();
        }
        chessHistorySteps.add(step);
        ChessHistory.currCountStep++;
    }

    public List<Opening> getOpenings() {
        return openings;
    }

    public void setOpenings(List<Opening> openings) {
        this.openings = openings;
        this.currOpenings = openings;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(String whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(String blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public Piece getCheckingPiece() {
        return checkingPiece;
    }
    public Piece setCheckingPiece(Piece piece) {
        return checkingPiece=piece;
    }

    public Piece getCheckPiece() {
        return checkPiece;
    }

    public void setCheckPiece(Piece checkPiece) {
        this.checkPiece = checkPiece;
    }
}
