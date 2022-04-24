package chessPiece;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;

import chessModel.ChessHistory;
import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Player;
import chessModel.Square;

public class King extends Piece{
    private Piece checkingPiece=null;
    public King(int col, int row, Player player, ChessMan rank, Integer resID, ChessModel chessModel) {
        super(col, row, player, rank, resID, chessModel);
    }
public void setCheckingPiece(Piece piece){
        this.checkingPiece=piece;
}
    @Override
    public boolean canMove(Square from, Square to) {
        Queen queen = new Queen();
//        if (checkingPiece!=null&&checkingPiece.canMove(checkingPiece.getSquare(), to)){
//            return false;
//        }
        if (queen.canMove(from, to)) {
            int deltaCol = Math.abs(from.getCol() - to.getCol());
            int deltaRow = Math.abs(from.getRow() - to.getRow());
            return deltaCol == 1 && deltaRow == 1 || deltaCol + deltaRow == 1;
        }

        return false;
    }

    @Override
    public ArrayList<PointF> helpCoordCheck(float xCord, float yCoard, float cellSize) {
        return helpCoord( xCord,  yCoard,  cellSize);
    }

    @Override
    public ArrayList<PointF> helpCoord(float xCord, float yCoard, float cellSize) {
        ArrayList<PointF> coord = new ArrayList<>();
        int col = getCol()-1;
        int row = getRow()+1;
        for (;col<getCol()+1;col++){
            if (col>7||row>7||col<0){
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(col, row));
            if(checkingPiece==null||
                    (checkingPiece!=null&&!checkingPiece.canMove(checkingPiece.getSquare(), new Square(col, row)))||
                    (checkingPiece!=null&checkingPiece.getRow()==row&&checkingPiece.getCol()==col)) {
                if (piece == null && col <= 7) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer() && col <= 7) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));

                }
            }

        }

        for(;row>=getRow()-1;row--){
            if (row<0||col>7||row>7){
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(col, row));
            if(checkingPiece==null||(checkingPiece!=null&&!checkingPiece.canMove(checkingPiece.getSquare(), new Square(col, row))) ||
                    (checkingPiece!=null&checkingPiece.getRow()==row&&checkingPiece.getCol()==col)) {
                if (piece == null && row >= 0) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer() && row >= 0) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));

                }
            }
        }
row++;
        for (;col>getCol()-1;col--){
            if (col<0||row<0||col>7){
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(col, row));
            if(checkingPiece==null||(checkingPiece!=null&&!checkingPiece.canMove(checkingPiece.getSquare(), new Square(col, row)))||
                    (checkingPiece!=null&checkingPiece.getRow()==row&&checkingPiece.getCol()==col)) {
                if (piece == null && col >= 0) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer() && col >= 0) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));

                }
            }
        }
        for(;row<getRow()+1;row++){
            if (row>7||col<0||row<0){
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(col, row));
            if(checkingPiece==null||(checkingPiece!=null&&!checkingPiece.canMove(checkingPiece.getSquare(), new Square(col, row)))||
                    (checkingPiece!=null&checkingPiece.getRow()==row&&checkingPiece.getCol()==col)) {
                if (piece == null && row <= 7) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer() && row <= 7) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));

                }
            }
        }
        return coord;
    }

    public boolean isCheck(){

        int gap = Math.abs(getRow() - 7);
        for (int i = 1; i <= gap; i++) {
            int nextRow = getRow() + i ;
            Piece piece = getChessModel().pieceAt(new Square(getCol(), nextRow));
            if (piece != null&&piece.getPlayer()==getPlayer()) {
                break;
            }
            else{
               if( piece != null&&piece.canMove(new Square(getCol(), getRow()), new Square(getCol(),nextRow))){
                   return true;
               }
            }
        }
        gap = Math.abs(getRow() - 0);
        for (int i = 1; i <= gap; i++) {
            int nextRow = getRow() - i ;
            Piece piece = getChessModel().pieceAt(new Square(getCol(), nextRow));
            if (piece != null&&piece.getPlayer()==getPlayer()) {
                break;
            }
            else{
                if(piece != null&& piece.canMove(new Square(getCol(), getRow()), new Square(getCol(),nextRow))){
                    checkingPiece =piece;
                    ChessHistory.checkingPiece=piece;
                    return true;
                }
            }
        }

        gap = Math.abs(getCol() - 7) ;
        for (int i = 1; i <= gap; i++) {
            int nextCol = getCol() + i ;
            Piece piece =getChessModel().pieceAt(new Square(nextCol, getRow()));
            if (piece != null&& piece!= null&&piece.getPlayer()==getPlayer()) {
                break;
            }
            else{
                if(piece != null&& piece.canMove(new Square(getCol(), getRow()), new Square(nextCol,getRow()))){
                    checkingPiece =piece;
                    ChessHistory.checkingPiece=piece;
                    return true;
                }
            }
        }
        gap = Math.abs(getCol() - 0) ;
        for (int i = 1; i <= gap; i++) {
            int nextCol = getCol() - i ;
            Piece piece =getChessModel().pieceAt(new Square(nextCol, getRow()));
            if ( piece!= null&&piece.getPlayer()==getPlayer()) {
                break;
            }
            else{
                if(piece != null&& piece.canMove(new Square(getCol(), getRow()), new Square(nextCol,getRow()))){
                    checkingPiece =piece;
                    ChessHistory.checkingPiece=piece;
                    return true;
                }
            }
        }
         gap = Math.abs(getCol() - 7) ;
        for (int i = 1; i <= gap; i++) {
            int nextCol = getCol() + i ;
            int nextRow = getRow() + i ;
            Piece piece =getChessModel().pieceAt(new Square(nextCol, nextRow));
            if (piece != null&&piece.getPlayer()==getPlayer()) {
                break;
            }
            else{
                if(piece != null&& piece.canMove(new Square(getCol(), getRow()), new Square(nextCol,nextRow))){
                    checkingPiece =piece;
                    ChessHistory.checkingPiece=piece;
                    return true;
                }
            }
        }
        gap = Math.abs(getCol() - 7) ;
        for (int i = 1; i <= gap; i++) {
            int nextCol = getCol() + i ;
            int nextRow = getRow() - i ;
            Piece piece =getChessModel().pieceAt(new Square(nextCol, nextRow));
            if (piece != null&&piece.getPlayer()==getPlayer()) {
                break;
            }
            else{
                if(piece != null&& piece.canMove(new Square(getCol(), getRow()), new Square(nextCol,nextRow))){
                    checkingPiece =piece;
                    ChessHistory.checkingPiece=piece;
                    return true;
                }
            }
        }
        gap = Math.abs(getCol() - 0) ;
        for (int i = 1; i <= gap; i++) {
            int nextCol = getCol() - i ;
            int nextRow = getRow() - i ;
            Piece piece =getChessModel().pieceAt(new Square(nextCol, nextRow));
            if (piece != null&&piece.getPlayer()==getPlayer()) {
                break;
            }
            else{
                if(piece != null&& piece.canMove(new Square(getCol(), getRow()), new Square(nextCol,nextRow))){
                    checkingPiece =piece;
                    ChessHistory.checkingPiece=piece;
                    return true;
                }
            }
        }
        gap = Math.abs(getCol() - 0) ;
        for (int i = 1; i <= gap; i++) {
            int nextCol = getCol() - i ;
            int nextRow = getRow() + i ;
            Piece piece =getChessModel().pieceAt(new Square(nextCol, nextRow));
            if (piece != null&&piece.getPlayer()==getPlayer()) {
                break;
            }
            else{
                if(piece != null&& piece.canMove(new Square(getCol(), getRow()), new Square(nextCol,nextRow))){
                    checkingPiece =piece;
                    ChessHistory.checkingPiece=piece;
                    return true;
                }
            }
        }
        return  false;
    }
}
