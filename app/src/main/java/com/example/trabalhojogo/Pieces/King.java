package com.example.trabalhojogo.Pieces;

import com.example.trabalhojogo.Coordinates;
import com.example.trabalhojogo.Position;

import java.util.ArrayList;
public class King extends Piece {
    public King(boolean white) {
        super(white);
    }
    @Override
    public ArrayList<Coordinates> getAllowedMoves(Coordinates coordinates, Position[][] board) {
        ArrayList<Coordinates> allowedMoves = new ArrayList<>();
        int x = coordinates.getX();
        int y = coordinates.getY();

        addMove(x + 1, y + 1, allowedMoves, board);
        addMove(x, y + 1, allowedMoves, board);
        addMove(x - 1, y + 1, allowedMoves, board);
        addMove(x + 1, y, allowedMoves, board);
        addMove(x - 1, y, allowedMoves, board);
        addMove(x + 1, y - 1, allowedMoves, board);
        addMove(x, y - 1, allowedMoves, board);
        addMove(x - 1, y - 1, allowedMoves, board);

        return allowedMoves;
    }
    private void addMove(int x, int y, ArrayList<Coordinates> allowedMoves, Position[][] board) {
        if (isValidMove(x, y) && isMoveAllowed(x, y, board)) {
            allowedMoves.add(new Coordinates(x, y));
        }
    }
    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
    private boolean isMoveAllowed(int x, int y, Position[][] board) {
        Piece piece = board[x][y].getPiece();
        return piece == null || piece.isWhite() != board[x][y].getPiece().isWhite();
    }
}
