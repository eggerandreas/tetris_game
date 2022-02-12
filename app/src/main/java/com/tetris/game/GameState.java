package com.tetris.game;

import android.content.Intent;
import android.util.SparseArray;

public class GameState {

    boolean status;
    int score;
    boolean pause;
    SimpleBlock [][] board;
    TetrisFigure falling;
    private int rows;
    private int columns;
    private Integer ctr;
    private SparseArray<TetrisFigure> tetrisFigure;

    GameState(int rows, int columns, TetrisFigureType fallingTetrisFigureType) {

        this.rows = rows;
        this.columns = columns;
        this.pause = false;
        ctr = 0;
        score = 0;
        this.status = true;

        board = new SimpleBlock[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                board[row][column] = new SimpleBlock(row, column);
            }
        }

        tetrisFigure = new SparseArray<>();

        falling = new TetrisFigure(fallingTetrisFigureType, this.ctr);

        tetrisFigure.put(this.ctr, falling);
    }

    private SimpleBlock getCoordinateBlock(Coordinate coordinate) {

        return this.board[coordinate.y][coordinate.x];
    }

    private boolean isConflicting(Coordinate coordinate) {

        if (coordinate.x < 0 || coordinate.x >= this.columns || coordinate.y < 0 || coordinate.y >= this.rows)
            return true;

        return this.getCoordinateBlock(coordinate).state == SimpleBlockState.ON_TETRISFIGURE;

    }

    private boolean canTetrisFigureDisplace(TetrisFigure tetrisFigure, Coordinate displacement) {

        for (SimpleBlock block : tetrisFigure.blocks) {
            if (block.state == SimpleBlockState.ON_TETRISFIGURE) {
                Coordinate shifted = Coordinate.add(block.coordinate, displacement);
                if (isConflicting(shifted)) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean moveFallingTetrisFigureDown() {

        if (canTetrisFigureDisplace(falling, new Coordinate(1, 0))) {
            falling.moveDown();
            return true;
        } else {
            return false;
        }

    }

    boolean moveFallingTetrisFigureLeft() {

        if (canTetrisFigureDisplace(falling, new Coordinate(0, -1))) {
            falling.moveLeft();
            return true;
        } else {
            return false;
        }
    }

    boolean moveFallingTetrisFigureRight() {

        if (canTetrisFigureDisplace(falling, new Coordinate(0, 1))) {
            falling.moveRight();
            return true;
        } else {
            return false;
        }
    }


    boolean rotateFallingTetrisFigureAntiClock() {
        if (falling.type != TetrisFigureType.SQUARE_SHAPED) {
            for (SimpleBlock block : falling.blocks) {
                if (block.state == SimpleBlockState.ON_EMPTY)
                    continue;

                SimpleBlock referenceBlock = falling.blocks[0];
                Coordinate baseCoordinate = Coordinate.sub(block.coordinate, referenceBlock.coordinate);
                if (isConflicting(Coordinate.add(Coordinate.rotateAntiClock(baseCoordinate), referenceBlock.coordinate))) {
                    return false;
                }
            }
            falling.performClockWiseRotation();
        }
        return true;
    }

    void paintTetrisFigure(TetrisFigure tetrisFigure) {
        for (SimpleBlock block : tetrisFigure.blocks) {
            if (block.state == SimpleBlockState.ON_EMPTY)
                continue;
            this.getCoordinateBlock(block.coordinate).set(block);
        }
    }


    void pushNewTetrisFigure(TetrisFigureType tetrisBlockType) {
        this.ctr++;

        falling = new TetrisFigure(tetrisBlockType, this.ctr);
        this.tetrisFigure.put(this.ctr, falling);
        for (SimpleBlock block : falling.blocks) {
            if (this.getCoordinateBlock(block.coordinate).state == SimpleBlockState.ON_TETRISFIGURE)
                this.status = false;
        }
    }

    // this function increments the score (game score)
    void incrementScore() {

        this.score++;
    }

    // this function removes a line on the bottom of the game board when the line is full
    void lineRemove() {
        boolean removeLines;
        do {
            removeLines = false;
            for (int row = this.rows - 1; row >= 0; row--) {
                boolean rowIsALine = true;
                for (int column = 0; column < this.columns; column++) {
                    if (this.board[row][column].state != SimpleBlockState.ON_TETRISFIGURE) {
                        rowIsALine = false;
                        break;
                    }
                }
                if (!rowIsALine) {
                    continue;
                }

                for (int column = 0; column < this.columns; column++) {
                    TetrisFigure tetrisFigure = this.tetrisFigure.get((this.board[row][column].tetraId));

                    SimpleBlock blockToClear = this.board[row][column];
                    blockToClear.setEmptyBlock(blockToClear.coordinate);

                    if (tetrisFigure == null) {
                        continue;
                    }

                    for (SimpleBlock block : tetrisFigure.blocks) {
                        if (block.state == SimpleBlockState.ON_EMPTY) {
                            continue;
                        }

                        if (block.coordinate.y == row && block.coordinate.x == column) {
                            block.state = SimpleBlockState.ON_EMPTY;

                            this.ctr++;
                            TetrisFigure upperTetrisFigure = tetrisFigure.copy(this.ctr);
                            this.tetrisFigure.put(this.ctr, upperTetrisFigure);
                            for (SimpleBlock upperBlock : upperTetrisFigure.blocks) {
                                if (upperBlock.coordinate.y >= block.coordinate.y) {
                                    upperBlock.state = SimpleBlockState.ON_EMPTY;
                                } else {
                                    this.getCoordinateBlock(upperBlock.coordinate).tetraId = upperBlock.tetraId;
                                }
                            }

                            this.ctr++;
                            TetrisFigure lowerTetrisFigure = tetrisFigure.copy(this.ctr);
                            this.tetrisFigure.put(this.ctr, lowerTetrisFigure);
                            for (SimpleBlock lowerBlock : lowerTetrisFigure.blocks) {
                                if (lowerBlock.coordinate.y <= block.coordinate.y) {
                                    lowerBlock.state = SimpleBlockState.ON_EMPTY;
                                } else {
                                    this.getCoordinateBlock(lowerBlock.coordinate).tetraId = lowerBlock.tetraId;
                                }
                            }

                            this.tetrisFigure.remove(block.tetraId);
                            break;
                        }

                    }
                }
                this.adjustTheMatrix();
                this.incrementScore();
                removeLines = true;
                break;
            }
        } while (removeLines);
    }

    private void adjustTheMatrix() {
        for (int row = this.rows - 1; row >= 0; row--) {
            for (int column = 0; column < this.columns; column++) {
                TetrisFigure T = (this.tetrisFigure).get((this.board[row][column].tetraId));

                if (T != null)
                    this.shiftTillBottom(T);
            }
        }
    }

    private void shiftTillBottom(TetrisFigure tetrisFigure) {
        boolean shiftTillBottom;
        do {
            boolean shouldShiftDown = true;
            shiftTillBottom = false;

            for (SimpleBlock block : tetrisFigure.blocks) {
                if (block.state == SimpleBlockState.ON_EMPTY)
                    continue;

                Coordinate newCoordinate = Coordinate.add(block.coordinate, new Coordinate(1, 0));

                if (isTetraPresent(newCoordinate, tetrisFigure))
                    continue;

                if (isConflicting(newCoordinate))
                    shouldShiftDown = false;
            }

            if (shouldShiftDown) {
                for (SimpleBlock block : tetrisFigure.blocks) {
                    if (block.state == SimpleBlockState.ON_EMPTY)
                        continue;

                    this.getCoordinateBlock(block.coordinate).setEmptyBlock(block.coordinate);


                    block.coordinate.y++;
                }

                for (SimpleBlock block : tetrisFigure.blocks) {
                    if (block.state == SimpleBlockState.ON_EMPTY)
                        continue;

                    this.getCoordinateBlock(block.coordinate).set(block);

                }
                shiftTillBottom = true;
            }
        } while (shiftTillBottom);
    }

    private boolean isTetraPresent(Coordinate coordinate, TetrisFigure tetrisFigure) {
        for (SimpleBlock block : tetrisFigure.blocks) {
            if (block.state == SimpleBlockState.ON_EMPTY)
                continue;

            if (Coordinate.isEqual(block.coordinate, coordinate))
                return true;

        }

        return false;
    }

    public void reset(){
        this.rows = 24;
        this.columns = 20;
        this.pause = false;
        ctr = 0;
        score = 0;
        this.status = true;

        board = new SimpleBlock[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                board[row][column] = new SimpleBlock(row, column);
            }
        }

        tetrisFigure = new SparseArray<>();

        falling = new TetrisFigure(TetrisFigureType.getRandomTetrisFigure(), this.ctr);

        tetrisFigure.put(this.ctr, falling);
    }
}
