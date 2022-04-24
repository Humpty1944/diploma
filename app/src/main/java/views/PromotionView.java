package views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import chessModel.ChessMan;
import chessModel.Player;
import chessModel.Square;
import chessPiece.Piece;
import interfaces.ChessInterface;

public class PromotionView extends View {
    private final float scaleFactor = 0.9f;
    private float cellSize = 130f;
    private float xPos = 0f;
    private float yPos = 0f;
    private int row=0;
    private int col=0;
    private static final int MAX_CLICK_DURATION = 1000;
    private Player player;
    private long pressStartTime;
    private Piece currPiece;
    public ChessInterface chessInterface = null;
    private final Map<Integer, Bitmap> bitmaps = new HashMap<Integer, Bitmap>();
    private final Set<Integer> imgChess;

    {
        imgChess = new HashSet<>();
        imgChess.add(R.drawable.chess_king_black);
        imgChess.add(R.drawable.chess_king_white);
        imgChess.add(R.drawable.chess_queen_black);
        imgChess.add(R.drawable.chess_queen_white);
        imgChess.add(R.drawable.chess_rook_black);
        imgChess.add(R.drawable.chess_rook_white);
        imgChess.add(R.drawable.chess_kinght_black);
        imgChess.add(R.drawable.chess_knight_white);
        imgChess.add(R.drawable.chess_bishop_black);
        imgChess.add(R.drawable.chess_bishop_white);

    }



    private final Paint paint = new Paint();

    public PromotionView(Context context) {
        super(context);
        loadBitmap();
    }

    public PromotionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadBitmap();
    }

    public PromotionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float chessBoardSize = Math.min(canvas.getHeight(), canvas.getWidth()) * scaleFactor;
        cellSize = chessBoardSize / 8;
        xPos = (canvas.getWidth()) / 2 -cellSize*4/2 ;
        yPos = (canvas.getHeight() - chessBoardSize) / 2;
        paint.setColor(Color.BLACK);
        paint.setAlpha(128);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        paint.setColor(getResources().getColor(R.color.promotionView));
        if(player==Player.BLACK) {
            drawPiece(canvas, 0, 0, R.drawable.chess_rook_black);
            drawPiece(canvas, 0, 1, R.drawable.chess_bishop_black);
            drawPiece(canvas, 0, 2, R.drawable.chess_kinght_black);
            drawPiece(canvas, 0, 3, R.drawable.chess_queen_black);
        }else{
            drawPiece(canvas, 0, 0, R.drawable.chess_rook_white);
            drawPiece(canvas, 0, 1, R.drawable.chess_bishop_white);
            drawPiece(canvas, 0, 2, R.drawable.chess_knight_white);
            drawPiece(canvas, 0, 3, R.drawable.chess_queen_white);
        }
       // canvas.drawRect(xPos + col * cellSize, yPos + row * cellSize, xPos + (col + 1) * cellSize, yPos + (row + 1) * cellSize, paint);
    }

    private void drawPiece(Canvas canvas, int row, int col, int resID) {
        canvas.drawRect(xPos + col * cellSize, yPos + row * cellSize, xPos + (col + 1) * cellSize, yPos + (row + 1) * cellSize, paint);
        Bitmap bitmap = bitmaps.get(resID);
        canvas.drawBitmap(bitmap, null, new RectF(xPos + col * cellSize, yPos + (row) * cellSize, xPos + (col + 1) * cellSize, yPos + (row + 1) * cellSize), paint);
    }

    private void loadBitmap() {
        for (Integer it : imgChess) {
            bitmaps.put(it, BitmapFactory.decodeResource(getResources(), it));

        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                pressStartTime = Calendar.getInstance().getTimeInMillis();
                break;
            }
            case MotionEvent.ACTION_UP: {
                long clickDuration = Calendar.getInstance().getTimeInMillis() - pressStartTime;
                if (clickDuration <= MAX_CLICK_DURATION) {
                    int currCol = (int) ((event.getX() - xPos) / cellSize);
                    int currRow = (int) ((event.getY() - yPos) / cellSize);

                    if(currRow!=0||currCol<0||currCol>4){
                        chessInterface.promotePawn(null, null);
                    }
                    else{
                        if (currCol==0){
                            chessInterface.promotePawn(ChessMan.ROOK, currPiece);
                        }else if(currCol==1){
                            chessInterface.promotePawn(ChessMan.BISHOP, currPiece);
                        }
                        else if(currCol==2){
                            chessInterface.promotePawn(ChessMan.KNIGHT, currPiece);
                        }
                        else if(currCol==3){
                            chessInterface.promotePawn(ChessMan.QUEEN, currPiece);
                        }


                    }
                }
            }
        }
        return true;
    }
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

    public Piece getCurrPiece() {
        return currPiece;
    }

    public void setCurrPiece(Piece currPiece) {
        this.currPiece = currPiece;
    }
}
