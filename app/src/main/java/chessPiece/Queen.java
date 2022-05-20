package chessPiece;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;

import chessModel.ChessHistory;
import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Player;
import chessModel.Square;

public class Queen extends Piece{

    public Queen(){
        super(-1,-1,Player.BLACK,ChessMan.QUEEN, 0, new ChessModel());
    }
    public Queen(int col, int row, Player player, ChessMan rank, Integer resID, ChessModel chessModel) {
        super(col, row, player, rank, resID, chessModel);
    }


    public  boolean canRookMove(Square from, Square to) {
        if (from.getCol() == to.getCol() && isClearVerticallyBetween(from, to) ||
                from.getRow() == to.getRow() && isClearHorizontallyBetween(from, to)) {

            return true;
        }
        return false;

    }

    public boolean canBishopMove(Square from, Square to) {


        if (Math.abs(from.getCol() - to.getCol()) ==
                Math.abs(from.getRow() - to.getRow())) {
            return isClearDiagonally(from, to);
        }
        return false;
    }
    @Override
    public boolean canMove(Square from, Square to) {
       // if(getChessModel().getCheckPiece()!=null&&getChessModel().getCheckPiece()!=this){
        if(getChessModel().getCheckingPiece()!=null&&getChessModel().getCheckingPiece()!=this){
            if(canRookMove(from, to) || canBishopMove(from, to)){
                return (getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(), to) &&
                        getChessModel().getCheckingPiece().canMove(to,getChessModel().getCheckPiece().getSquare())) ||
                        (getChessModel().getCheckingPiece().getRow() == to.getRow() && getChessModel().getCheckingPiece().getCol() == to.getCol());
            }
        }
        return canRookMove(from, to) || canBishopMove(from, to);
    }

    @Override
    public ArrayList<PointF> helpCoordCheck(float xCord, float yCoard, float cellSize) {
        ArrayList<PointF> coord = new ArrayList<>();
        for (int i = getRow(); i <= 7; i++) {
            if (i == getRow()) {
                continue;
            }
            Square piece = new Square(getCol(), i);
            if(!(canRookMove(getSquare(), piece) || canBishopMove(getSquare(), piece))){
                continue;
            }
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
            Square piece = new Square(getCol(), i);
            if(!(canRookMove(getSquare(), piece) || canBishopMove(getSquare(), piece))){
                continue;
            }
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
        for (int i = getCol(); i <= 7; i++) {
            if (i == getCol()) {
                continue;
            }
            Square piece = new Square(i, getRow());
            if(!(canRookMove(getSquare(), piece) || canBishopMove(getSquare(), piece))){
                continue;
            }
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
            Square piece = new Square(i, getRow());
            if(!(canRookMove(getSquare(), piece) || canBishopMove(getSquare(), piece))){
                continue;
            }
            if(getChessModel().getCheckingPiece().getCol()==i&&getChessModel().getCheckingPiece().getRow()==getRow()){
                coord.add(new PointF(xCord + i * cellSize + cellSize / 2, yCoard + (7 - getRow()) * cellSize + cellSize / 2));
              //  return coord;
            }
            if( getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(),new Square(i, getRow()))&&
                    getChessModel().getCheckingPiece().canMove(new Square(i, getRow()),getChessModel().getCheckPiece().getSquare())&&
                    (i!=getChessModel().getCheckPiece().getSquare().getCol()&&getRow()!=getChessModel().getCheckPiece().getSquare().getRow())){
                coord.add(new PointF(xCord + i * cellSize + cellSize / 2, yCoard + (7 - getRow()) * cellSize + cellSize / 2));
              //  return coord;
            }

        }

        int col = getCol();
        int row = getRow();
        while (col >= 0 && row <= 7) {
            if (row == getRow() || col == getCol()) {
                col--;
                row++;
                continue;
            }
            Square piece = new Square(col, row);
            if(!(canRookMove(getSquare(), piece) || canBishopMove(getSquare(), piece))){
                col--;
                row++;
                continue;
            }
//            if (piece == null) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//            } else if (piece.getPlayer() != getPlayer()) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//                break;
//            } else {
//                break;
//            }

            if(getChessModel().getCheckingPiece().getCol()==col&&getChessModel().getCheckingPiece().getRow()==row){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
               // return coord;
            }
            if( getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(),new Square(col, row))&&
                    getChessModel().getCheckingPiece().canMove(new Square(col, row),getChessModel().getCheckPiece().getSquare())&&
                    (col!=getChessModel().getCheckPiece().getSquare().getCol()&&row!=getChessModel().getCheckPiece().getSquare().getRow())){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
              //  return coord;
            }

            col--;
            row++;
        }
        col = getCol();
        row = getRow();
        while (col <= 7 && row <= 7) {
            if (row == getRow() || col == getCol()) {
                col++;
                row++;
                continue;
            }
            Square piece = new Square(col, row);
            if(!(canRookMove(getSquare(), piece) || canBishopMove(getSquare(), piece))){
                col++;
                row++;
                continue;
            }
//            if (piece == null) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//            } else if (piece.getPlayer() != getPlayer()) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//                break;
//            } else {
//                break;
//            }
            if(getChessModel().getCheckingPiece().getCol()==col&&getChessModel().getCheckingPiece().getRow()==row){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
               // return coord;
            }
            if( getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(),new Square(col, row))&&
                    getChessModel().getCheckingPiece().canMove(new Square(col, row),getChessModel().getCheckPiece().getSquare())&&
                    (col!=getChessModel().getCheckPiece().getSquare().getCol()&&row!=getChessModel().getCheckPiece().getSquare().getRow())){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
              //  return coord;
            }
            col++;
            row++;
        }
        col = getCol();
        row = getRow();
        while (col <= 7 && row >= 0) {
            if (row == getRow() || col == getCol()) {
                col++;
                row--;
                continue;
            }
            Square piece = new Square(col, row);
            if(!(canRookMove(getSquare(), piece) || canBishopMove(getSquare(), piece))){
                col++;
                row--;
                continue;
            }
//            if (piece == null) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//            } else if (piece.getPlayer() != getPlayer()) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//                break;
//            } else {
//                break;
//            }
            if(getChessModel().getCheckingPiece().getCol()==col&&getChessModel().getCheckingPiece().getRow()==row){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
              //  return coord;
            }
            if( getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(),new Square(col, row))&&
                    getChessModel().getCheckingPiece().canMove(new Square(col, row),getChessModel().getCheckPiece().getSquare())&&
                    (col!=getChessModel().getCheckPiece().getSquare().getCol()&&row!=getChessModel().getCheckPiece().getSquare().getRow())){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
               // return coord;
            }
            col++;
            row--;
        }
        col = getCol();
        row = getRow();
        while (col >= 0 && row >= 0) {
            if (row == getRow() || col == getCol()) {
                col--;
                row--;
                continue;
            }
            Square piece = new Square(col, row);
            if(!(canRookMove(getSquare(), piece) || canBishopMove(getSquare(), piece))){
                col--;
                row--;
                continue;
            }
//            if (piece == null) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//            } else if (piece.getPlayer() != getPlayer()) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//                break;
//            } else {
//                break;
//            }
            if(getChessModel().getCheckingPiece().getCol()==col&&getChessModel().getCheckingPiece().getRow()==row){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
             //   return coord;
            }
            if( getChessModel().getCheckingPiece().canMove(getChessModel().getCheckingPiece().getSquare(),new Square(col, row))&&
                    getChessModel().getCheckingPiece().canMove(new Square(col, row),getChessModel().getCheckPiece().getSquare())&&
                    (col!=getChessModel().getCheckPiece().getSquare().getCol()&&row!=getChessModel().getCheckPiece().getSquare().getRow())){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
             //   return coord;
            }
            col--;
            row--;
        }
        return coord;
    }

    @Override
    public ArrayList<PointF> helpCoord(float xCord, float yCoard, float cellSize) {
        ArrayList<PointF> coord = new ArrayList<>();
        erasePossibleMoves();
        if(getChessModel().getCheckPiece()!=null){
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

            int col = getCol();
            int row = getRow();
            while (col >= 0 && row <= 7) {
                if (row == getRow() || col == getCol()) {
                    col--;
                    row++;
                    continue;
                }
                Piece piece = getChessModel().pieceAt(new Square(col, row));
                if (piece == null) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer()) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                    break;
                } else {
                    break;
                }
                col--;
                row++;
            }
            col = getCol();
            row = getRow();
            while (col <= 7 && row <= 7) {
                if (row == getRow() || col == getCol()) {
                    col++;
                    row++;
                    continue;
                }
                Piece piece = getChessModel().pieceAt(new Square(col, row));
                if (piece == null) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer()) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                    break;
                } else {
                    break;
                }
                col++;
                row++;
            }
            col = getCol();
            row = getRow();
            while (col <= 7 && row >= 0) {
                if (row == getRow() || col == getCol()) {
                    col++;
                    row--;
                    continue;
                }
                Piece piece = getChessModel().pieceAt(new Square(col, row));
                if (piece == null) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer()) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                    break;
                } else {
                    break;
                }
                col++;
                row--;
            }
            col = getCol();
            row = getRow();
            while (col >= 0 && row >= 0) {
                if (row == getRow() || col == getCol()) {
                    col--;
                    row--;
                    continue;
                }
                Piece piece = getChessModel().pieceAt(new Square(col, row));
                if (piece == null) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                } else if (piece.getPlayer() != getPlayer()) {
                    coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                    break;
                } else {
                    break;
                }
                col--;
                row--;
            }
        }
        return coord;
    }


}
