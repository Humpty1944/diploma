package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.adapters.ViewBindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import chessModel.ChessHistory;
import chessModel.ChessHistoryStep;
import chessModel.ChessMan;
import chessModel.ChessModel;
import chessModel.Player;
import chessModel.Square;
import chessPiece.Piece;
import interfaces.ChessHistoryInterfaceCallbacks;
import interfaces.ChessInterface;
import openings.Opening;
import views.ChessBoardView;
import views.ChessHistoryAdapter;
import views.PromotionView;

public class ChessOpeningFragment extends Fragment implements ChessInterface {
            View view;
            ChessBoardView chessView;
            TextView name;
            TextView eco;
            TextView steps;
            Opening opening;
            ChessModel model;

    public ChessOpeningFragment() {
        super(R.layout.chess_opening);
    }
    public ChessOpeningFragment(Opening opening) {
        super(R.layout.chess_opening);
        this.opening=opening;

    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chess_opening, container, false);
        chessView =  view.findViewById(R.id.chessBoardOpening);
        name=view.findViewById(R.id.text_view_opening_name);
        eco=view.findViewById(R.id.text_view_opening_name_code);
        steps=view.findViewById(R.id.text_view_opening_steps);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model= new ChessModel();
        model.setPieceBox(opening.readFen());
        ChessHistory.currCountStep=0;
        name.setText(opening.getName());
        eco.setText(opening.getNumber());
        String newLine = opening.getSquares(opening.getSteps());
        steps.setText(newLine);
        // ChessHistory.isLoad=false;
        chessView.chessInterface = this;
        model.chessInterface= this;
        model.setBoardView(chessView);
        chessView.setIsClickable(false);
        chessView.invalidate();

       // chessView.setMainActivity(this);
       // promotionview.chessInterface=this;

    }

    @Override
    public Piece pieceAt(Square square) {
        return model.pieceAt(square);
    }

    @Override
    public void movePiece(Square from, Square to) {

    }

    @Override
    public void promotionView(Player player, Piece piece) {

    }

    @Override
    public void promotePawn(ChessMan chessMan, Piece piece) {

    }

    @Override
    public void updateHistory(boolean isNext, ChessHistoryStep step, int block) {

    }

    @Override
    public void backMovePiece(ChessHistoryStep move) {

    }

    @Override
    public void displayCurrOpening(Opening opening) {

    }

    @Override
    public void sendAndDisplayAnalysis(int block) {

    }

    @Override
    public void updatePromotion(ChessHistoryStep step) {

    }


}
