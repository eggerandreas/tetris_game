package com.tetris.game;


import java.util.Random;

enum TetrisFigureType{

    SQUARE_SHAPED,
    T_SHAPED,
    L_SHAPED,
    LINE_SHAPED,
    Z_SHAPED,
    INV_L_SHAPED,
    INV_Z_SHAPED;

    private static final TetrisFigureType[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();
    public static TetrisFigureType getRandomTetrisFigure() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}

public class TetrisFigure {
    SimpleBlock[] blocks;
    TetrisFigureType type;

    TetrisFigure(TetrisFigureType type, int tetraId) {
        Coordinate[] coordinates;

        switch (type) {
            case SQUARE_SHAPED:
                coordinates = new Coordinate[]{
                        new Coordinate(0, 10),
                        new Coordinate(1, 10),
                        new Coordinate(1, 11),
                        new Coordinate(0, 11)
                };
                blocks = this.blocksGenerator(tetraId, 1, coordinates);
                break;
            case INV_L_SHAPED:
                coordinates = new Coordinate[]{
                        new Coordinate(0, 10),
                        new Coordinate(0, 11),
                        new Coordinate(1, 10),
                        new Coordinate(2, 10)
                };
                blocks = this.blocksGenerator(tetraId, 2, coordinates);
                break;
            case L_SHAPED:
                coordinates = new Coordinate[]{
                        new Coordinate(0, 11),
                        new Coordinate(0, 10),
                        new Coordinate(1, 11),
                        new Coordinate(2, 11)
                };
                blocks = this.blocksGenerator(tetraId, 3, coordinates);
                break;
            case T_SHAPED:
                coordinates = new Coordinate[]{
                        new Coordinate(1, 10),
                        new Coordinate(0, 10),
                        new Coordinate(1, 11),
                        new Coordinate(2, 10)
                };
                blocks = this.blocksGenerator(tetraId, 4, coordinates);
                break;
            case Z_SHAPED:
                coordinates = new Coordinate[]{
                        new Coordinate(1, 11),
                        new Coordinate(1, 10),
                        new Coordinate(0, 10),
                        new Coordinate(2, 11)
                };
                blocks = this.blocksGenerator(tetraId, 5, coordinates);
                break;
            case INV_Z_SHAPED:
                coordinates = new Coordinate[]{
                        new Coordinate(1, 11),
                        new Coordinate(0, 11),
                        new Coordinate(1, 10),
                        new Coordinate(2, 10)
                };
                blocks = this.blocksGenerator(tetraId, 6, coordinates);
                break;
            case LINE_SHAPED:
                coordinates = new Coordinate[]{
                        new Coordinate(0, 10),
                        new Coordinate(1, 10),
                        new Coordinate(2, 10),
                        new Coordinate(3, 10)
                };
                blocks = this.blocksGenerator(tetraId, 7, coordinates);
                break;
        }
    }

    private TetrisFigure(SimpleBlock[] blocks) {

        this.blocks = blocks;
    }

    private SimpleBlock[] blocksGenerator(int tetraId, int colour, Coordinate[] coordinates) {
        SimpleBlock[] blocks = new SimpleBlock[coordinates.length];
        for (int itr = 0; itr < coordinates.length; itr++) {
            blocks[itr] = new SimpleBlock(colour, tetraId, coordinates[itr], SimpleBlockState.ON_TETRISFIGURE);
        }
        return blocks;
    }

    TetrisFigure copy(int tetraId) {
        SimpleBlock[] newBlocks = new SimpleBlock[this.blocks.length];
        for (int itr = 0; itr < this.blocks.length; itr++) {
            newBlocks[itr] = this.blocks[itr].copy();
            newBlocks[itr].tetraId = tetraId;
        }
        return new TetrisFigure(newBlocks);
    }

    void moveDown() {

        for (SimpleBlock block : blocks) {
            block.coordinate.y++;
        }
    }

    void moveLeft() {

        for (SimpleBlock block : blocks) {
            block.coordinate.x--;
        }
    }

    void moveRight() {

        for (SimpleBlock block : blocks) {
            block.coordinate.x++;
        }
    }

    void performClockWiseRotation() {

        SimpleBlock referenceBlock = blocks[0];
        for (SimpleBlock block : blocks) {
            Coordinate baseCoordinate = Coordinate.sub(block.coordinate, referenceBlock.coordinate);
            block.coordinate = Coordinate.add(Coordinate.rotateAntiClock(baseCoordinate), referenceBlock.coordinate);
        }
    }
}
