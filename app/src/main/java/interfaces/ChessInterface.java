package interfaces;



import java.util.ArrayList;

import chessModel.ChessHistory;
import chessModel.ChessHistoryStep;
import chessModel.ChessMan;
import chessModel.ChessPiece;
import chessModel.Player;
import chessModel.Square;
import chessPiece.Piece;
import openings.Opening;

public interface ChessInterface {
    public ArrayList<ChessHistoryStep> chessHistory = new ArrayList<>();
    public Piece pieceAt(Square square);
    public void movePiece(Square from, Square to);
    public void promotionView(Player player, Piece piece);
    public void promotePawn(ChessMan chessMan, Piece piece);
    public void updateHistory(boolean isNext, ChessHistoryStep step, int block);
    public void backMovePiece(ChessHistoryStep move);
    public void displayCurrOpening(Opening opening);
    public void sendAndDisplayAnalysis(ChessHistoryStep step);
}
