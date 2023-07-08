package com.example.trabalhojogo.Pieces;

import com.example.trabalhojogo.Coordinates;
import com.example.trabalhojogo.Position;

import java.util.ArrayList;
public abstract class Piece {
    private boolean white;
    protected Piece(boolean white) {
        this.white = white;
    }
    public ArrayList<Coordinates> getAllowedMoves(Coordinates coordinates, Position[][] board) {
        ArrayList<Coordinates> allowedMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Coordinates c = new Coordinates(i, j);
                allowedMoves.add(c);
            }
        }
        return allowedMoves;
    }
    public boolean isWhite() {
        return white;
    }
}
