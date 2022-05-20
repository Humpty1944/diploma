package chessPiece;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;

import chessModel.ChessHistory;
import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Player;
import chessModel.Square;

public abstract class Piece {
    private int row;
    private int col;
    private Player player;
    private int moveCount=0;

    private ChessMan chessMan;
    private Integer resID;
    private ChessModel chessModel;
    private int currLength = 0;
    private boolean prevTurn=false;
    private ArrayList<Square> possibleMoves = new ArrayList<>();
    public Piece(int col, int row, Player player, ChessMan rank, Integer resID, ChessModel chessModel) {
        this.col = col;
        this.row = row;
        this.player = player;
        this.chessMan = rank;
        this.resID = resID;
        this.chessModel=chessModel;

    }
    public abstract boolean  canMove(Square from, Square to);
    public abstract ArrayList<PointF> helpCoordCheck(float xCord, float yCoard, float cellSize);

public abstract ArrayList<PointF> helpCoord(float xCord, float yCoard, float cellSize);

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ChessMan getChessMan() {
        return chessMan;
    }

    public void setChessMan(ChessMan chessMan) {
        this.chessMan = chessMan;
    }

    public Integer getResID() {
        return resID;
    }

    public void setResID(Integer resID) {
        this.resID = resID;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount += moveCount;
    }
    public void setSameMoveCount(int moveCount){
        this.moveCount = moveCount;
    }
    public ChessModel getChessModel() {
        return chessModel;
    }

    public void setChessModel(ChessModel chessModel) {
        this.chessModel = chessModel;
    }

    public int getCurrLength() {
        return currLength;
    }

    public void setCurrLength(int currLength) {
        this.currLength = currLength;
    }

    public boolean isPrevTurn() {
        return prevTurn;
    }

    public void setPrevTurn(boolean prevTurn) {
        this.prevTurn = prevTurn;
    }

    public Square getSquare(){
        return new Square(col,row);
    }
    boolean isClearHorizontallyBetween(Square from, Square to) {
        if (from.getRow() != to.getRow()) {
            return false;
        }
        int gap = Math.abs(from.getCol() - to.getCol()) - 1;
        if (gap == 0) {
            if(to.getCol()> from.getCol()){
                if(getChessModel().pieceAt(new Square(to.getCol(), to.getRow()))!=null&&
                        getChessModel().pieceAt(new Square(to.getCol(), to.getRow())).getPlayer()==getPlayer()){
                    return false;
                }
            }
            if(to.getCol()< from.getCol()){
                if(getChessModel().pieceAt(new Square(to.getCol(), to.getRow()))!=null&&
                        getChessModel().pieceAt(new Square(to.getCol(), to.getRow())).getPlayer()==getPlayer()){
                    return false;
                }
            }
            return true;
        }
        for (int i = 1; i <= gap; i++) {
            int nextCol = to.getCol() > from.getCol() ? from.getCol() + i : from.getCol() - i;
            if (getChessModel().pieceAt(new Square(nextCol, from.getRow())) != null) {
                return false;
            }
        }
        return true;
    }
    boolean isClearVerticallyBetween(Square from, Square to) {
        if (from.getCol() != to.getCol()) {
            return false;
        }
        int gap = Math.abs(from.getRow() - to.getRow()) - 1;
        if (gap == 0) {
            if(to.getRow()> from.getRow()){
                if(getChessModel().pieceAt(new Square(to.getCol(), to.getRow()))!=null&&
                        getChessModel().pieceAt(new Square(to.getCol(), to.getRow())).getPlayer()==getPlayer()){
                    return false;
                }
            }
            if(to.getRow()< from.getRow()){
                if(getChessModel().pieceAt(new Square(to.getCol(), to.getRow()))!=null&&
                        getChessModel().pieceAt(new Square(to.getCol(), to.getRow())).getPlayer()==getPlayer()){
                    return false;
                }
            }
            return true;
        }

        for (int i = 1; i <= gap; i++) {
            int nextRow = to.getRow() > from.getRow() ? from.getRow() + i : from.getRow() - i;
            if (getChessModel().pieceAt(new Square(from.getCol(), nextRow)) != null) {
                return false;
            }
        }
        return true;
    }
    boolean isClearDiagonally(Square from, Square to) {
        if (Math.abs(from.getCol() - to.getCol()) !=
                Math.abs(from.getRow() - to.getRow())) {
            return false;
        }
        int gap = Math.abs(from.getCol() - to.getCol()) - 1;
        for (int i = 1; i <= gap; i++) {
            int nextCol = to.getCol() > from.getCol() ? from.getCol() + i : from.getCol() - i;
            int nextRow = to.getRow() > from.getRow() ? from.getRow() + i : from.getRow() - i;
            if (getChessModel().pieceAt(new Square(nextCol,nextRow)) != null) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Square> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMoves(Square possibleMove) {
        this.possibleMoves.add(possibleMove);
    }
    public void erasePossibleMoves(){
        this.possibleMoves = new ArrayList<>();
    }

    public boolean checkProtect(Square from, Square to){
        boolean res=false;
        if(canMove(from,to)){
            row= to.getRow();
            col= to.getCol();
            if(!ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), ChessHistory.checkPiece.getSquare())){
                res=true;
            }
        }
        row= from.getRow();
        col= from.getCol();
        return res;
    }
    public String getShortName(){
        if(chessMan==ChessMan.QUEEN){
            return "Q";
        }else if (chessMan==ChessMan.KING){
            return "K";
        }else if (chessMan==ChessMan.BISHOP){
            return "B";
        }else if (chessMan==ChessMan.KNIGHT){
            return "N";
        }else if (chessMan==ChessMan.ROOK){
            return "R";
        }
        return "";
    }
    public  String turnToNotation(){

        String result = "";
        String fromCh = Character.toString((char) ((char) (getCol() + '0') + 49));
        result+=fromCh+(getRow()+1);
        return result;
    }
}
