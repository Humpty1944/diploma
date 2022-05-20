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
        queen.setChessModel(getChessModel());
        queen.setPlayer(getPlayer());
//        if (checkingPiece!=null&&checkingPiece.canMove(checkingPiece.getSquare(), to)){
//            return false;
//        }
        if(getChessModel().getCheckingPiece()!=null&&getChessModel().getCheckingPiece()!=this){
            if (queen.canMove(from, to)) {
                int deltaCol = Math.abs(from.getCol() - to.getCol());
                int deltaRow = Math.abs(from.getRow() - to.getRow());
                if(deltaCol == 1 && deltaRow == 1 || deltaCol + deltaRow == 1){

                    return
                            (getChessModel().getCheckingPiece().getRow() == to.getRow() && getChessModel().getCheckingPiece().getCol() == to.getCol())||
                                    ( !(getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(), to)));
                }

            }
            else if (queen.canRookMove(from, to)||queen.canBishopMove(from, to)){
                int deltaCol = Math.abs(from.getCol() - to.getCol());
                int deltaRow = Math.abs(from.getRow() - to.getRow());
                if(deltaCol == 1 && deltaRow == 1 || deltaCol + deltaRow == 1){
                    return
                            (getChessModel().getCheckingPiece().getRow() == to.getRow() && getChessModel().getCheckingPiece().getCol() == to.getCol())||
                                    !(getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(), to))||
                                    (getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(), from)&&!(getChessModel().getCheckingPiece().canMove(from, to)));
                }
            }
        }
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
                    (checkingPiece!=null&checkingPiece.getRow()==row&&checkingPiece.getCol()==col)||
                    (getChessModel().getCheckingPiece()!=null&&(getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(), getSquare())&&
                            !(getChessModel().getCheckingPiece().canMove(getSquare(), new Square(col, row)))))) {
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
                    (checkingPiece!=null&checkingPiece.getRow()==row&&checkingPiece.getCol()==col)&&
                            !(getChessModel().getCheckingPiece().canMove(getSquare(), new Square(col, row)))) {
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
                    (checkingPiece!=null&checkingPiece.getRow()==row&&checkingPiece.getCol()==col)&&
                            !(getChessModel().getCheckingPiece().canMove(getSquare(), new Square(col, row)))) {
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
                    (checkingPiece!=null&checkingPiece.getRow()==row&&checkingPiece.getCol()==col)&&
                            !(getChessModel().getCheckingPiece().canMove(getSquare(), new Square(col, row)))) {
                if (piece == null && row <= 7) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer() && row <= 7) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));

                }
            }
        }

        Piece piece = getChessModel().pieceAt(new Square(0, getRow()));
        if(piece!=null&&piece.getMoveCount()==0&&getMoveCount()==0&&isClearHorizontallyBetween(getSquare(), piece.getSquare())){
            coord.add(new PointF(xCord + (getCol()-2) * cellSize + cellSize / 2, yCoard + (7 - getRow()) * cellSize + cellSize / 2));
        }
        piece = getChessModel().pieceAt(new Square(7, getRow()));
        if(piece!=null&&piece.getMoveCount()==0&&getMoveCount()==0&&isClearHorizontallyBetween(getSquare(), piece.getSquare())){
            coord.add(new PointF(xCord + (getCol()+2) * cellSize + cellSize / 2, yCoard + (7 - getRow()) * cellSize + cellSize / 2));
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
               if( piece != null&&piece.canMove(new Square(getCol(),nextRow),new Square(getCol(), getRow()) )){
                     getChessModel().setCheckingPiece(piece);
                   return true;
               }
               else if(piece!=null){
                  break;
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
                if(piece != null&& piece.canMove( new Square(getCol(),nextRow),new Square(getCol(), getRow()))){
                    checkingPiece =piece;
                      getChessModel().setCheckingPiece(piece);
                    return true;
                }else if(piece!=null){
                    break;
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
                if(piece != null&& piece.canMove(new Square(nextCol,getRow()),new Square(getCol(), getRow()) )){
                    checkingPiece =piece;
                      getChessModel().setCheckingPiece(piece);
                    return true;
                }else if(piece!=null){
                    break;
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
                if(piece != null&& piece.canMove(new Square(nextCol,getRow()),new Square(getCol(), getRow()) )){
                    checkingPiece =piece;
                      getChessModel().setCheckingPiece(piece);
                    return true;
                }else if(piece!=null){
                    break;
                }
            }
        }
         gap = Math.abs(getCol() - 7) ;
        for (int i = 1; i <= gap; i++) {
            int nextCol = getCol() + i ;
            int nextRow = getRow() + i ;
            if(nextCol>7||nextRow>7){
                break;
            }
            Piece piece =getChessModel().pieceAt(new Square(nextCol, nextRow));
            if (piece != null&&piece.getPlayer()==getPlayer()) {
                break;
            }
            else{
                if(piece != null&& piece.canMove(new Square(nextCol,nextRow),new Square(getCol(), getRow()) )){
                    checkingPiece =piece;
                      getChessModel().setCheckingPiece(piece);
                    return true;
                }else if(piece!=null){
                    break;
                }
            }
        }
        gap = Math.abs(getCol() - 7) ;
        for (int i = 1; i <= gap; i++) {
            int nextCol = getCol() + i ;
            int nextRow = getRow() - i ;
            if(nextCol>7||nextRow<0){
                break;
            }
            Piece piece =getChessModel().pieceAt(new Square(nextCol, nextRow));
            if (piece != null&&piece.getPlayer()==getPlayer()) {
                break;
            }
            else{
                if(piece != null&& piece.canMove(new Square(nextCol,nextRow),new Square(getCol(), getRow()))){
                    checkingPiece =piece;
                    getChessModel().setCheckingPiece(piece);
                    return true;
                }else if(piece!=null){
                    break;
                }
            }
        }
        gap = Math.abs(getCol() - 0) ;
        for (int i = 1; i <= gap; i++) {
            int nextCol = getCol() - i ;
            int nextRow = getRow() - i ;
            if(nextCol<0||nextRow<0){
                break;
            }
            Piece piece =getChessModel().pieceAt(new Square(nextCol, nextRow));
            if (piece != null&&piece.getPlayer()==getPlayer()) {
                break;
            }
            else{
                if(piece != null&& piece.canMove( new Square(nextCol,nextRow),new Square(getCol(), getRow()))){
                    checkingPiece =piece;
                      getChessModel().setCheckingPiece(piece);
                    return true;
                }else if(piece!=null){
                    break;
                }
            }
        }
        gap = Math.abs(getCol() - 0) ;
        for (int i = 1; i <= gap; i++) {
            int nextCol = getCol() - i ;
            int nextRow = getRow() + i ;
            if(nextCol<0||nextRow>7){
                break;
            }
            Piece piece =getChessModel().pieceAt(new Square(nextCol, nextRow));
            if (piece != null&&piece.getPlayer()==getPlayer()) {
                break;
            }
            else{
                if(piece != null&& piece.canMove( new Square(nextCol,nextRow),new Square(getCol(), getRow()))){
                    checkingPiece =piece;
                      getChessModel().setCheckingPiece(piece);
                    return true;
                }else if(piece!=null){
                    break;
                }
            }
        }
        return  false;
    }
}
