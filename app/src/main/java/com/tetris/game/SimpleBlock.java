package com.tetris.game;

enum SimpleBlockState{
    ON_EMPTY,
    ON_TETRISFIGURE
}

public class SimpleBlock {
    int colour;
    int tetraId;
    Coordinate coordinate;
    SimpleBlockState state;

    SimpleBlock(int row, int column) {
        this.colour = -1;
        this.tetraId = -1;
        this.coordinate = new Coordinate(row, column);
        this.state = SimpleBlockState.ON_EMPTY;
    }

    SimpleBlock(int colour, int tetraId, Coordinate coordinate, SimpleBlockState state) {
        this.colour = colour;
        this.tetraId = tetraId;
        this.coordinate = coordinate;
        this.state = state;

    }

    SimpleBlock copy() {

        return new SimpleBlock(colour, tetraId, coordinate, state);
    }

    void set(SimpleBlock B) {
        this.colour = B.colour;
        this.tetraId = B.tetraId;
        this.coordinate.y = B.coordinate.y;
        this.coordinate.x = B.coordinate.x;
        this.state = B.state;

    }

    void setEmptyBlock(Coordinate coordinate) {
        this.colour = -1;
        this.tetraId = -1;
        this.coordinate.x = coordinate.x;
        this.coordinate.y = coordinate.y;
        this.state = SimpleBlockState.ON_EMPTY;

    }
}
