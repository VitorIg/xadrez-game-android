package com.example.trabalhojogo.Pieces;

import com.example.trabalhojogo.Coordinates;
import com.example.trabalhojogo.Position;

import java.util.ArrayList;
public class Knight extends Piece{

    public Knight(boolean white) {
        super(white);
    }

    @Override
    public ArrayList<Coordinates> getAllowedMoves(Coordinates coordinates, Position[][] board) {
        ArrayList<Coordinates> allowedMoves = new ArrayList<>();
        int[][] knightMoves = { {-2, -1}, {-1, -2}, {1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1} };

        for (int[] move : knightMoves) {
            int targetX = coordinates.getX() + move[0];
            int targetY = coordinates.getY() + move[1];

            if (isValidMove(targetX, targetY, board)) {
                allowedMoves.add(new Coordinates(targetX, targetY));
            }
        }

        return allowedMoves;
    }

    private boolean isValidMove(int x, int y, Position[][] board) {
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            Piece targetPiece = board[x][y].getPiece();
            return targetPiece == null || targetPiece.isWhite() != isWhite();
        }
        return false;
    }
}
