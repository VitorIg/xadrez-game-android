package com.example.trabalhojogo.Pieces;

import com.example.trabalhojogo.Coordinates;
import com.example.trabalhojogo.Position;

import java.util.ArrayList;
public class Pawn extends Piece {
    public Pawn(boolean white) {
        super(white);
    }
    @Override
    public ArrayList<Coordinates> getAllowedMoves(Coordinates coordinates, Position[][] board) {
        ArrayList<Coordinates> allowedMoves = new ArrayList<>();
        int x = coordinates.getX();
        int y = coordinates.getY();

        if (isWhitePawn()) {
            addWhitePawnMoves(x, y, allowedMoves, board);
        } else {
            addBlackPawnMoves(x, y, allowedMoves, board);
        }

        return allowedMoves;
    }
    private boolean isWhitePawn() {
        return isWhite();
    }
    private void addWhitePawnMoves(int x, int y, ArrayList<Coordinates> allowedMoves, Position[][] board) {
        int forwardDirection = -1;
        int startingRow = 6;

        addPawnForwardMove(x, y, forwardDirection, allowedMoves, board);

        if (y == startingRow) {
            addPawnForwardMove(x, y, 2 * forwardDirection, allowedMoves, board);
        }

        addPawnCaptureMove(x + 1, y + forwardDirection, allowedMoves, board);
        addPawnCaptureMove(x - 1, y + forwardDirection, allowedMoves, board);
    }
    private void addBlackPawnMoves(int x, int y, ArrayList<Coordinates> allowedMoves, Position[][] board) {
        int forwardDirection = 1;
        int startingRow = 1;

        addPawnForwardMove(x, y, forwardDirection, allowedMoves, board);

        if (y == startingRow) {
            addPawnForwardMove(x, y, 2 * forwardDirection, allowedMoves, board);
        }

        addPawnCaptureMove(x + 1, y + forwardDirection, allowedMoves, board);
        addPawnCaptureMove(x - 1, y + forwardDirection, allowedMoves, board);
    }
    private void addPawnForwardMove(int x, int y, int direction, ArrayList<Coordinates> allowedMoves, Position[][] board) {
        int newX = x;
        int newY = y + direction;

        if (isValidMove(newX, newY) && board[newX][newY].getPiece() == null) {
            allowedMoves.add(new Coordinates(newX, newY));
        }
    }
    private void addPawnCaptureMove(int x, int y, ArrayList<Coordinates> allowedMoves, Position[][] board) {
        if (isValidMove(x, y) && board[x][y].getPiece() != null && board[x][y].getPiece().isWhite() != isWhite()) {
            allowedMoves.add(new Coordinates(x, y));
        }
    }
    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
}
