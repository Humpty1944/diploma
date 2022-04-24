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


    private  boolean canRookMove(Square from, Square to) {
        if (from.getCol() == to.getCol() && isClearVerticallyBetween(from, to) ||
                from.getRow() == to.getRow() && isClearHorizontallyBetween(from, to)) {

            return true;
        }
        return false;

    }

    private boolean canBishopMove(Square from, Square to) {
        if (Math.abs(from.getCol() - to.getCol()) ==
                Math.abs(from.getRow() - to.getRow())) {
            return isClearDiagonally(from, to);
        }
        return false;
    }
    @Override
    public boolean canMove(Square from, Square to) {
        return canRookMove(from, to) || canBishopMove(from, to);
    }

    @Override
    public ArrayList<PointF> helpCoordCheck(float xCord, float yCoard, float cellSize) {
        ArrayList<PointF> coord = new ArrayList<>();
        for (int i = getRow(); i <= 7; i++) {
            if (i == getRow()) {
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(getCol(), i));
            if(ChessHistory.checkingPiece.getCol()==getCol()&&ChessHistory.checkingPiece.getRow()==i){
                coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
                return coord;
            }
            if( ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(getCol(), i))&&
                    ChessHistory.checkingPiece.canMove(new Square(getCol(), i), ChessHistory.checkPiece.getSquare())){
                coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
                return coord;
            }

        }

        for (int i = getRow(); i >= 0; i--) {
            if (i == getRow()) {
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(getCol(), i));
            if(ChessHistory.checkingPiece.getCol()==getCol()&&ChessHistory.checkingPiece.getRow()==i){
                coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
                return coord;
            }
            if( ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(getCol(), i))&&
                    ChessHistory.checkingPiece.canMove(new Square(getCol(), i), ChessHistory.checkPiece.getSquare())){
                coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
                return coord;
            }

        }
        for (int i = getCol(); i <= 7; i++) {
            if (i == getCol()) {
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(i, getRow()));
            if(ChessHistory.checkingPiece.getCol()==i&&ChessHistory.checkingPiece.getRow()==getRow()){
                coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
                return coord;
            }
            if( ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(i, getRow()))&&
                    ChessHistory.checkingPiece.canMove(new Square(i, getRow()), ChessHistory.checkPiece.getSquare())){
                coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
                return coord;
            }

        }
        for (int i = getCol(); i >= 0; i--) {
            if (i == getCol()) {
                continue;
            }
            Piece piece = getChessModel().pieceAt(new Square(i, getRow()));
            if(ChessHistory.checkingPiece.getCol()==i&&ChessHistory.checkingPiece.getRow()==getRow()){
                coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
                return coord;
            }
            if( ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(i, getRow()))&&
                    ChessHistory.checkingPiece.canMove(new Square(i, getRow()), ChessHistory.checkPiece.getSquare())){
                coord.add(new PointF(xCord + getCol() * cellSize + cellSize / 2, yCoard + (7 - i) * cellSize + cellSize / 2));
                return coord;
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
//            if (piece == null) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//            } else if (piece.getPlayer() != getPlayer()) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//                break;
//            } else {
//                break;
//            }

            if(ChessHistory.checkingPiece.getCol()==col&&ChessHistory.checkingPiece.getRow()==row){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
            }
            if( ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(col, row))&&
                    ChessHistory.checkingPiece.canMove(new Square(col, row), ChessHistory.checkPiece.getSquare())){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
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
//            if (piece == null) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//            } else if (piece.getPlayer() != getPlayer()) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//                break;
//            } else {
//                break;
//            }
            if(ChessHistory.checkingPiece.getCol()==col&&ChessHistory.checkingPiece.getRow()==row){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
            }
            if( ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(col, row))&&
                    ChessHistory.checkingPiece.canMove(new Square(col, row), ChessHistory.checkPiece.getSquare())){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
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
//            if (piece == null) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//            } else if (piece.getPlayer() != getPlayer()) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//                break;
//            } else {
//                break;
//            }
            if(ChessHistory.checkingPiece.getCol()==col&&ChessHistory.checkingPiece.getRow()==row){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
            }
            if( ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(col, row))&&
                    ChessHistory.checkingPiece.canMove(new Square(col, row), ChessHistory.checkPiece.getSquare())){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
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
//            if (piece == null) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//            } else if (piece.getPlayer() != getPlayer()) {
//                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
//                break;
//            } else {
//                break;
//            }
            if(ChessHistory.checkingPiece.getCol()==col&&ChessHistory.checkingPiece.getRow()==row){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
            }
            if( ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(col, row))&&
                    ChessHistory.checkingPiece.canMove(new Square(col, row), ChessHistory.checkPiece.getSquare())){
                coord.add(new PointF(xCord + col * cellSize + cellSize / 2, yCoard + (7 - row) * cellSize + cellSize / 2));
                return coord;
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
        if(ChessHistory.checkingPiece!=null){
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
