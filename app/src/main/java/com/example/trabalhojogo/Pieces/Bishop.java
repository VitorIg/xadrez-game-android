package com.example.trabalhojogo.Pieces;

import com.example.trabalhojogo.Coordinates;
import com.example.trabalhojogo.Position;

import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(boolean white) {
        super(white);
    }
    @Override
    public ArrayList<Coordinates> getAllowedMoves(Coordinates coordinates, Position[][] board) {
        ArrayList<Coordinates> allowedMoves = new ArrayList<>();
        int x = coordinates.getX();
        int y = coordinates.getY();

        for (int i = 1; x + i < 8 && y + i < 8; i++) {
            if (board[x + i][y + i].getPiece() == null) {
                allowedMoves.add(new Coordinates(x + i, y + i));
            } else {
                if (board[x + i][y + i].getPiece().isWhite() != board[x][y].getPiece().isWhite()) {
                    allowedMoves.add(new Coordinates(x + i, y + i));
                }
                break;
            }
        }

        for (int i = 1; x - i >= 0 && y + i < 8; i++) {
            if (board[x - i][y + i].getPiece() == null) {
                allowedMoves.add(new Coordinates(x - i, y + i));
            } else {
                if (board[x - i][y + i].getPiece().isWhite() != board[x][y].getPiece().isWhite()) {
                    allowedMoves.add(new Coordinates(x - i, y + i));
                }
                break;
            }
        }

        for (int i = 1; x - i >= 0 && y - i >= 0; i++) {
            if (board[x - i][y - i].getPiece() == null) {
                allowedMoves.add(new Coordinates(x - i, y - i));
            } else {
                if (board[x - i][y - i].getPiece().isWhite() != board[x][y].getPiece().isWhite()) {
                    allowedMoves.add(new Coordinates(x - i, y - i));
                }
                break;
            }
        }

        for (int i = 1; x + i < 8 && y - i >= 0; i++) {
            if (board[x + i][y - i].getPiece() == null) {
                allowedMoves.add(new Coordinates(x + i, y - i));
            } else {
                if (board[x + i][y - i].getPiece().isWhite() != board[x][y].getPiece().isWhite()) {
                    allowedMoves.add(new Coordinates(x + i, y - i));
                }
                break;
            }
        }

        return allowedMoves;
    }
}
