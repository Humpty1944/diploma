package chessPiece;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;

import chessModel.ChessHistory;
import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Player;
import chessModel.Square;

public class Knight extends Piece {


    public Knight(int col, int row, Player player, ChessMan rank, Integer resID, ChessModel chessModel) {
        super(col, row, player, rank, resID, chessModel);
    }


    @Override
    public boolean canMove(Square from, Square to) {
//        if (ChessHistory.checkingPiece != null) {
//            if (Math.abs(from.getCol() - to.getCol()) == 2 && Math.abs(from.getRow() - to.getRow()) == 1 ||
//                    Math.abs(from.getCol() - to.getCol()) == 1 && Math.abs(from.getRow() - to.getRow()) == 2) {
//                if (ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(), to)) {
//                    if (ChessHistory.checkingPiece.canMove(to, ChessHistory.checkPiece.getSquare())) {
//                        return true;
//                    }
//                }
//            }
//        }

        return (Math.abs(from.getCol() - to.getCol()) == 2 && Math.abs(from.getRow() - to.getRow()) == 1 ||
                Math.abs(from.getCol() - to.getCol()) == 1 && Math.abs(from.getRow() - to.getRow()) == 2);

    }

    @Override
    public ArrayList<PointF> helpCoordCheck(float xCord, float yCoard, float cellSize) {
        ArrayList<PointF> coord = new ArrayList<>();
        erasePossibleMoves();
        int colLL = getCol() - 2;
        int colL = getCol() - 1;
        int colRR = getCol() + 2;
        int colR = getCol() + 1;

        int rowUU = getRow() + 2;
        int rowU = getRow() + 1;
        int rowDD = getRow() - 2;
        int rowD = getRow() - 1;
        if (colLL >= 0 && rowU <= 7&&
                ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(colLL, rowU))&&ChessHistory.checkingPiece.canMove(new Square(colLL, rowU), ChessHistory.checkPiece.getSquare())
                && (getChessModel().pieceAt(new Square(colLL, rowU)) == null
                || getChessModel().pieceAt(new Square(colLL, rowU)).getPlayer() != getPlayer())) {
            coord.add(new PointF(xCord + colLL * cellSize + cellSize / 2, yCoard + (7 - rowU) * cellSize + cellSize / 2));
            setPossibleMoves(new Square(colLL, rowU));
        }
        if (colL >= 0 && rowUU <= 7&&
                ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(colL, rowUU))&&ChessHistory.checkingPiece.canMove(new Square(colL, rowUU), ChessHistory.checkPiece.getSquare()) &&
                (getChessModel().pieceAt(new Square(colL, rowUU)) == null
                || getChessModel().pieceAt(new Square(colL, rowUU)).getPlayer() != getPlayer())) {
            coord.add(new PointF(xCord + colL * cellSize + cellSize / 2, yCoard + (7 - rowUU) * cellSize + cellSize / 2));
            setPossibleMoves(new Square(colL, rowUU));
        }
        if (colRR <= 7 && rowU <= 7&&
                ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(colRR, rowU))&&ChessHistory.checkingPiece.canMove(new Square(colRR, rowU), ChessHistory.checkPiece.getSquare()) &&
                (getChessModel().pieceAt(new Square(colRR, rowU)) == null
                || getChessModel().pieceAt(new Square(colRR, rowU)).getPlayer() != getPlayer())) {
            coord.add(new PointF(xCord + colRR * cellSize + cellSize / 2, yCoard + (7 - rowU) * cellSize + cellSize / 2));
            setPossibleMoves(new Square(colRR, rowU));
        }
        if (colR <= 7 && rowUU <= 7&&
                ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(colR, rowUU))&&ChessHistory.checkingPiece.canMove(new Square(colR, rowUU), ChessHistory.checkPiece.getSquare()) &&
                (getChessModel().pieceAt(new Square(colR, rowUU)) == null
                || getChessModel().pieceAt(new Square(colR, rowUU)).getPlayer() != getPlayer())) {
            coord.add(new PointF(xCord + colR * cellSize + cellSize / 2, yCoard + (7 - rowUU) * cellSize + cellSize / 2));
            setPossibleMoves(new Square(colR, rowUU));
        }
        if (colRR <= 7 && rowD >= 0&&
                ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(colRR, rowD))&&ChessHistory.checkingPiece.canMove(new Square(colRR, rowD), ChessHistory.checkPiece.getSquare()) &&
                (getChessModel().pieceAt(new Square(colRR, rowD)) == null
                || getChessModel().pieceAt(new Square(colRR, rowD)).getPlayer() != getPlayer())) {
            coord.add(new PointF(xCord + colRR * cellSize + cellSize / 2, yCoard + (7 - rowD) * cellSize + cellSize / 2));
            setPossibleMoves(new Square(colRR, rowD));
        }
        if (colR <= 7 && rowDD >= 0&&
                ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(colR, rowDD))&&ChessHistory.checkingPiece.canMove(new Square(colR, rowDD), ChessHistory.checkPiece.getSquare()) &&
                (getChessModel().pieceAt(new Square(colR, rowDD)) == null
                || getChessModel().pieceAt(new Square(colR, rowDD)).getPlayer() != getPlayer())) {
            coord.add(new PointF(xCord + colR * cellSize + cellSize / 2, yCoard + (7 - rowDD) * cellSize + cellSize / 2));
            setPossibleMoves(new Square(colR, rowDD));
        }
        if (colLL >= 0 && rowD >= 0&&
                ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(colLL, rowD))&&ChessHistory.checkingPiece.canMove(new Square(colLL, rowD), ChessHistory.checkPiece.getSquare()) &&
                (getChessModel().pieceAt(new Square(colLL, rowD)) == null
                || getChessModel().pieceAt(new Square(colLL, rowD)).getPlayer() != getPlayer())) {
            coord.add(new PointF(xCord + colLL * cellSize + cellSize / 2, yCoard + (7 - rowD) * cellSize + cellSize / 2));
            setPossibleMoves(new Square(colLL, rowD));
        }
        if (colL >= 0 && rowDD >= 0&&
                ChessHistory.checkingPiece.canMove(ChessHistory.checkingPiece.getSquare(),new Square(colLL, rowU))&&ChessHistory.checkingPiece.canMove(new Square(colLL, rowU), ChessHistory.checkPiece.getSquare()) && (getChessModel().pieceAt(new Square(colL, rowDD)) == null
                || getChessModel().pieceAt(new Square(colL, rowDD)).getPlayer() != getPlayer())) {
            coord.add(new PointF(xCord + colL * cellSize + cellSize / 2, yCoard + (7 - rowDD) * cellSize + cellSize / 2));
            setPossibleMoves(new Square(colL, rowDD));
        }
        return coord;
    }

    @Override
    public ArrayList<PointF> helpCoord(float xCord, float yCoard, float cellSize) {
        ArrayList<PointF> coord = new ArrayList<>();
        if(ChessHistory.checkingPiece!=null){
            coord =  helpCoordCheck( xCord, yCoard, cellSize);
        }else {

            erasePossibleMoves();
            int colLL = getCol() - 2;
            int colL = getCol() - 1;
            int colRR = getCol() + 2;
            int colR = getCol() + 1;

            int rowUU = getRow() + 2;
            int rowU = getRow() + 1;
            int rowDD = getRow() - 2;
            int rowD = getRow() - 1;

            if (colLL >= 0 && rowU <= 7
                    && (getChessModel().pieceAt(new Square(colLL, rowU)) == null
                    || getChessModel().pieceAt(new Square(colLL, rowU)).getPlayer() != getPlayer())) {
                coord.add(new PointF(xCord + colLL * cellSize + cellSize / 2, yCoard + (7 - rowU) * cellSize + cellSize / 2));
                setPossibleMoves(new Square(colLL, rowU));
            }
            if (colL >= 0 && rowUU <= 7 && (getChessModel().pieceAt(new Square(colL, rowUU)) == null
                    || getChessModel().pieceAt(new Square(colL, rowUU)).getPlayer() != getPlayer())) {
                coord.add(new PointF(xCord + colL * cellSize + cellSize / 2, yCoard + (7 - rowUU) * cellSize + cellSize / 2));
                setPossibleMoves(new Square(colL, rowUU));
            }
            if (colRR <= 7 && rowU <= 7 && (getChessModel().pieceAt(new Square(colRR, rowU)) == null
                    || getChessModel().pieceAt(new Square(colRR, rowU)).getPlayer() != getPlayer())) {
                coord.add(new PointF(xCord + colRR * cellSize + cellSize / 2, yCoard + (7 - rowU) * cellSize + cellSize / 2));
                setPossibleMoves(new Square(colRR, rowU));
            }
            if (colR <= 7 && rowUU <= 7 && (getChessModel().pieceAt(new Square(colR, rowUU)) == null
                    || getChessModel().pieceAt(new Square(colR, rowUU)).getPlayer() != getPlayer())) {
                coord.add(new PointF(xCord + colR * cellSize + cellSize / 2, yCoard + (7 - rowUU) * cellSize + cellSize / 2));
                setPossibleMoves(new Square(colR, rowUU));
            }
            if (colRR <= 7 && rowD >= 0 && (getChessModel().pieceAt(new Square(colRR, rowD)) == null
                    || getChessModel().pieceAt(new Square(colRR, rowD)).getPlayer() != getPlayer())) {
                coord.add(new PointF(xCord + colRR * cellSize + cellSize / 2, yCoard + (7 - rowD) * cellSize + cellSize / 2));
                setPossibleMoves(new Square(colRR, rowD));
            }
            if (colR <= 7 && rowDD >= 0 && (getChessModel().pieceAt(new Square(colR, rowDD)) == null
                    || getChessModel().pieceAt(new Square(colR, rowDD)).getPlayer() != getPlayer())) {
                coord.add(new PointF(xCord + colR * cellSize + cellSize / 2, yCoard + (7 - rowDD) * cellSize + cellSize / 2));
                setPossibleMoves(new Square(colR, rowDD));
            }
            if (colLL >= 0 && rowD >= 0 && (getChessModel().pieceAt(new Square(colLL, rowD)) == null
                    || getChessModel().pieceAt(new Square(colLL, rowD)).getPlayer() != getPlayer())) {
                coord.add(new PointF(xCord + colLL * cellSize + cellSize / 2, yCoard + (7 - rowD) * cellSize + cellSize / 2));
                setPossibleMoves(new Square(colLL, rowD));
            }
            if (colL >= 0 && rowDD >= 0 && (getChessModel().pieceAt(new Square(colL, rowDD)) == null
                    || getChessModel().pieceAt(new Square(colL, rowDD)).getPlayer() != getPlayer())) {
                coord.add(new PointF(xCord + colL * cellSize + cellSize / 2, yCoard + (7 - rowDD) * cellSize + cellSize / 2));
                setPossibleMoves(new Square(colL, rowDD));
            }
        }
        return coord;
    }

}
