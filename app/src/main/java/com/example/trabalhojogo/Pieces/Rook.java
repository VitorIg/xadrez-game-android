package com.example.trabalhojogo.Pieces;

import com.example.trabalhojogo.Coordinates;
import com.example.trabalhojogo.Position;

import java.util.ArrayList;
public class Rook extends Piece {
    public Rook(boolean white) {
        super(white);
    }
    @Override
    public ArrayList<Coordinates> getAllowedMoves(Coordinates coordinates, Position[][] board) {
        ArrayList<Coordinates> allowedMoves = new ArrayList<>();

        for (int i = coordinates.getX() + 1; i < 8; i++) {
            if (addMoveIfValid(i, coordinates.getY(), board, allowedMoves)) {
                break;
            }
        }

        for (int i = coordinates.getX() - 1; i >= 0; i--) {
            if (addMoveIfValid(i, coordinates.getY(), board, allowedMoves)) {
                break;
            }
        }

        for (int i = coordinates.getY() - 1; i >= 0; i--) {
            if (addMoveIfValid(coordinates.getX(), i, board, allowedMoves)) {
                break;
            }
        }

        for (int i = coordinates.getY() + 1; i < 8; i++) {
            if (addMoveIfValid(coordinates.getX(), i, board, allowedMoves)) {
                break;
            }
        }

        return allowedMoves;
    }

    private boolean addMoveIfValid(int x, int y, Position[][] board, ArrayList<Coordinates> allowedMoves) {
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            Coordinates c = new Coordinates(x, y);
            if (board[x][y].getPiece() == null || board[x][y].getPiece().isWhite() != isWhite()) {
                allowedMoves.add(c);
            }
            return board[x][y].getPiece() != null;
        }
        return true;
    }
}
