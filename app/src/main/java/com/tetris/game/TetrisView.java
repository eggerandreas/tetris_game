package com.tetris.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class TetrisView extends View {

    private int yOffset;
    private Paint paint;
    public GameState gameState = GameActivity.gameState;


    public TetrisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        yOffset = 10;
    }


    // set the tetris block colors
    private int getBlockColorCode(int color) {
        switch (color) {
            case 1:
                return Color.BLUE;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.RED;
            case 4:
                return Color.GREEN;
            case 5:
                return Color.CYAN;
            case 6:
                return Color.MAGENTA;
            case 7:
                return Color.GRAY;
            default:
                return Color.TRANSPARENT;
        }

    }

    // this function draws the game matrix
    private void DrawMatrix(SimpleBlock[][] matrix, Canvas canvas) {
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 20; j++) {
                if (matrix[i][j].state == SimpleBlockState.ON_EMPTY)
                    continue;

                int color = this.getBlockColorCode(matrix[i][j].colour);
                Paint p = new Paint();
                p.setColor(color);
                canvas.drawRect(42 + j * 50, yOffset + i * 50 + 2, 88 + j * 50, yOffset + (i + 1) * 50 - 2, p);
            }
        }
    }

    // this function clears the matrix -> make it empty
    private void Clear(SimpleBlock[][] matrix, Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 20; j++) {
                canvas.drawRect(42 + j * 50, yOffset + i * 50 + 2, 88 + j * 50, yOffset + (i + 1) * 50 - 2, p);
            }
        }
    }

    // this matrix draws a tetris figure
    private void DrawTetrisFigure(TetrisFigure tetrisFigure, Canvas canvas) {
        for (SimpleBlock block : tetrisFigure.blocks) {
            int color = this.getBlockColorCode(block.colour);
            Paint p = new Paint();
            p.setColor(color);
            canvas.drawRect(42 + block.coordinate.x * 50, yOffset + block.coordinate.y * 50 + 2, 88 + block.coordinate.x * 50, yOffset + (block.coordinate.y + 1) * 50 - 2, p);

        }
    }

    // this function sets the boundary
    private void Boundary(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5f);
        canvas.drawLine(40, yOffset, 40, yOffset + 1200, paint);
        canvas.drawLine(40, yOffset, 1040, yOffset, paint);
        canvas.drawLine(1040, yOffset, 1040, yOffset + 1200, paint);
        canvas.drawLine(1040, yOffset + 1200, 40, yOffset + 1200, paint);
    }

    // this function draws the grid / lines
    private void grid(Canvas canvas) {
        paint.setStrokeWidth(3f);
        for (int i = 90; i < 1040; i = i + 50) {
            canvas.drawLine(i, yOffset, i, yOffset + 1200, paint);
        }
        for (int j = 50; j < 1200; j = j + 50) {
            canvas.drawLine(40, yOffset + j, 1040, yOffset + j, paint);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5f);
        Boundary(canvas);
        grid(canvas);


        if (gameState.status) {
            Clear(gameState.board, canvas);
            DrawMatrix(gameState.board, canvas);
            DrawTetrisFigure(gameState.falling, canvas);
        } else {
            DrawMatrix(gameState.board, canvas);
            DrawTetrisFigure(gameState.falling, canvas);
        }

    }

}

