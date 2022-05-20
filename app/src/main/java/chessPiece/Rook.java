package chessPiece;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;

import chessModel.ChessHistory;
import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Player;
import chessModel.Square;

public class Rook extends Piece {
//    private ChessModel chessModel;

    public Rook(int col, int row, Player player, ChessMan rank, Integer resID, ChessModel chessModel) {
        super(col, row, player, rank, resID, chessModel);
    }


    boolean isClearVerticallyBetween(Square from, Square to) {
        if (from.getCol() != to.getCol()) {
            return false;
        }
        int gap = Math.abs(from.getRow() - to.getRow()) - 1;
        if (gap == 0) {
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

    boolean isClearHorizontallyBetween(Square from, Square to) {
        if (from.getRow() != to.getRow()) {
            return false;
        }
        int gap = Math.abs(from.getCol() - to.getCol()) - 1;
        if (gap == 0) {
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

    @Override
    public boolean canMove(Square from, Square to) {

      //  if(getChessModel().getCheckingPiece()!=null&&getChessModel().getCheckingPiece()!=this){
        if(getChessModel().getCheckingPiece()!=null&&getChessModel().getCheckingPiece()!=this){
            if( (from.getCol() == to.getCol() && isClearVerticallyBetween(from, to) ||
                    from.getRow() == to.getRow() && isClearHorizontallyBetween(from, to))) {


                return (getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(), to) &&
                        getChessModel().getCheckingPiece().canMove(to,getChessModel().getCheckPiece().getSquare())) ||
                        (getChessModel().getCheckingPiece().getRow() == to.getRow() && getChessModel().getCheckingPiece().getCol() == to.getCol());
            }


        }

       return  (from.getCol() == to.getCol() && isClearVerticallyBetween(from, to) ||
                from.getRow() == to.getRow() && isClearHorizontallyBetween(from, to)) ;
//
//            return true;
//        }
//        return false;

    }

    @Override
    public ArrayList<PointF> helpCoordCheck(float xCord, float yCoard, float cellSize) {
        ArrayList<PointF> coord = new ArrayList<>();

        for (int i = getRow(); i <= 7; i++) {
            if (i == getRow()) {
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(getCol(), i));
            if(getChessModel().getCheckingPiece().getCol()==getCol()&&getChessModel().getCheckingPiece().getRow()==i){
                coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
               // return coord;
            }
            if( getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(),new Square(getCol(), i))&&
                    getChessModel().getCheckingPiece().canMove(new Square(getCol(), i),getChessModel().getCheckPiece().getSquare())&&
                    (getCol()!=getChessModel().getCheckPiece().getSquare().getCol()&&i!=getChessModel().getCheckPiece().getSquare().getRow())){
                coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
               // return coord;
            }

        }

        for (int i = getRow(); i >= 0; i--) {
            if (i == getRow()) {
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(getCol(), i));
            if(getChessModel().getCheckingPiece().getCol()==getCol()&&getChessModel().getCheckingPiece().getRow()==i){
                coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
              //  return coord;
            }
            if( getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(),new Square(getCol(), i))&&
                    getChessModel().getCheckingPiece().canMove(new Square(getCol(), i),getChessModel().getCheckPiece().getSquare())&&
                    (getCol()!=getChessModel().getCheckPiece().getSquare().getCol()&&i!=getChessModel().getCheckPiece().getSquare().getRow())){
                coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
               //return coord;
            }

        }
        for (int i = getCol(); i <= 7; i++) {
            if (i == getCol()) {
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(i, getRow()));
            if(getChessModel().getCheckingPiece().getCol()==i&&getChessModel().getCheckingPiece().getRow()==getRow()){
                coord.add(new PointF(xCord + i * cellSize + cellSize / 2, yCoard + (7 - getRow()) * cellSize + cellSize / 2));
               // return coord;
            }
            if( getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(),new Square(i, getRow()))&&
                    getChessModel().getCheckingPiece().canMove(new Square(i, getRow()),getChessModel().getCheckPiece().getSquare())&&
                    (i!=getChessModel().getCheckPiece().getSquare().getCol()&&getRow()!=getChessModel().getCheckPiece().getSquare().getRow())){
                coord.add(new PointF(xCord + i * cellSize + cellSize / 2, yCoard + (7 - getRow()) * cellSize + cellSize / 2));
                //return coord;
            }

        }
        for (int i = getCol(); i >= 0; i--) {
            if (i == getCol()) {
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(i, getRow()));
            if(getChessModel().getCheckingPiece().getCol()==i&&getChessModel().getCheckingPiece().getRow()==getRow()){
                coord.add(new PointF(xCord + i * cellSize + cellSize / 2, yCoard + (7 - getRow()) * cellSize + cellSize / 2));
              //  return coord;
            }
            if( getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(),new Square(i, getRow()))&&
                    getChessModel().getCheckingPiece().canMove(new Square(i, getRow()),getChessModel().getCheckPiece().getSquare())&&
                    (i!=getChessModel().getCheckPiece().getSquare().getCol()&&getRow()!=getChessModel().getCheckPiece().getSquare().getRow())){
                coord.add(new PointF(xCord + i * cellSize + cellSize / 2, yCoard + (7 - getRow()) * cellSize + cellSize / 2));
                //return coord;
            }

        }
        return coord;
    }

    @Override
    public ArrayList<PointF> helpCoord(float xCord, float yCoard, float cellSize) {
        ArrayList<PointF> coord = new ArrayList<>();
        erasePossibleMoves();
        if(getChessModel().getCheckingPiece()!=null){
            coord =  helpCoordCheck( xCord, yCoard, cellSize);
        }else {
            for (int i = getRow(); i <= 7; i++) {
                if (i == getRow()) {
                    continue;
                }
                Piece piece = getChessModel().pieceAt(new Square(getCol(), i));
                if (piece == null) {
                    coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer()) {
                    coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
                    break;
                } else {
                    break;
                }

            }
            for (int i = getRow(); i >= 0; i--) {
                if (i == getRow()) {
                    continue;
                }
                Piece piece = getChessModel().pieceAt(new Square(getCol(), i));
                if (piece == null) {
                    coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer()) {
                    coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
                    break;
                } else {
                    break;
                }

            }
            for (int i = getCol(); i <= 7; i++) {
                if (i == getCol()) {
                    continue;
                }
                Piece piece = getChessModel().pieceAt(new Square(i, getRow()));
                if (piece == null) {
                    coord.add(new PointF(xCord + i * cellSize + cellSize / 2, yCoard + (7 - getRow()) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer()) {
                    coord.add(new PointF(xCord + i * cellSize + cellSize / 2, yCoard + (7 - getRow()) * cellSize + cellSize / 2));
                    break;
                } else {
                    break;
                }

            }
            for (int i = getCol(); i >= 0; i--) {
                if (i == getCol()) {
                    continue;
                }
                Piece piece = getChessModel().pieceAt(new Square(i, getRow()));
                if (piece == null) {
                    coord.add(new PointF(xCord + i * cellSize + cellSize / 2, yCoard + (7 - getRow()) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer()) {
                    coord.add(new PointF(xCord + i * cellSize + cellSize / 2, yCoard + (7 - getRow()) * cellSize + cellSize / 2));
                    break;
                } else {
                    break;
                }

            }
        }
        return coord;
    }

//    public ChessModel getChessModel() {
//        return chessModel;
//    }
//
//    public void setChessModel(ChessModel chessModel) {
//        this.chessModel = chessModel;
//    }

}
