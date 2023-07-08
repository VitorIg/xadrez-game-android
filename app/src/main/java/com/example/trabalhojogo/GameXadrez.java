package com.example.trabalhojogo;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trabalhojogo.Pieces.Bishop;
import com.example.trabalhojogo.Pieces.King;
import com.example.trabalhojogo.Pieces.Knight;
import com.example.trabalhojogo.Pieces.Pawn;
import com.example.trabalhojogo.Pieces.Piece;
import com.example.trabalhojogo.Pieces.Queen;
import com.example.trabalhojogo.Pieces.Rook;
import java.util.ArrayList;


    public class GameXadrez extends AppCompatActivity implements View.OnClickListener {

        public Boolean FirstPlayerTurn;
        public ArrayList<Coordinates> listOfCoordinates = new ArrayList<>();
        public Position[][] Board = new Position[8][8];
        public Position[][] Board2 = new Position[8][8];
        public Boolean AnythingSelected = false;
        public Coordinates lastPos = null ;
        public Coordinates clickedPosition = new Coordinates(0, 0);
        public TextView game_over;
        public TextView[][] DisplayBoard = new TextView[8][8];
        public TextView[][] DisplayBoardBackground = new TextView[8][8];
        public ArrayList<Position[][]> LastMoves = new ArrayList<>();
        public LinearLayout pawn_choices;
        public int numberOfMoves;

        Piece bKing,bQueen,bKnight1,bKnight2,bRook1,bRook2,bBishop1,bBishop2,bPawn1,bPawn2,bPawn3,bPawn4,bPawn5,bPawn6,bPawn7,bPawn8;
        Piece wKing,wQueen,wKnight1,wKnight2,wRook1,wRook2,wBishop1,wBishop2,wPawn1,wPawn2,wPawn3,wPawn4,wPawn5,wPawn6,wPawn7,wPawn8;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            makeStatusBarTransparent();
            setContentView(R.layout.activity_game_xadrez);
            initializeBoard();

            Button btnReset = findViewById(R.id.btnReset);
            btnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initializeBoard();
                }
            });
        }

        private void makeStatusBarTransparent() {
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
        private void initializeBoard() {

            bKing = new King(false);
            wKing = new King(true);

            bQueen = new Queen(false);
            wQueen = new Queen(true);

            bRook1 = new Rook(false);
            bRook2 = new Rook(false);
            wRook1 = new Rook(true);
            wRook2 = new Rook(true);

            bKnight1 = new Knight(false);
            bKnight2 = new Knight(false);
            wKnight1 = new Knight(true);
            wKnight2 = new Knight(true);

            bBishop1 = new Bishop(false);
            bBishop2 = new Bishop(false);
            wBishop1 = new Bishop(true);
            wBishop2 = new Bishop(true);

            bPawn1 = new Pawn(false);
            bPawn2 = new Pawn(false);
            bPawn3 = new Pawn(false);
            bPawn4 = new Pawn(false);
            bPawn5 = new Pawn(false);
            bPawn6 = new Pawn(false);
            bPawn7 = new Pawn(false);
            bPawn8 = new Pawn(false);

            wPawn1 = new Pawn(true);
            wPawn2 = new Pawn(true);
            wPawn3 = new Pawn(true);
            wPawn4 = new Pawn(true);
            wPawn5 = new Pawn(true);
            wPawn6 = new Pawn(true);
            wPawn7 = new Pawn(true);
            wPawn8 = new Pawn(true);

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Board[i][j] = new Position(null);
                    Board2[i][j] = new Position(null);
                }
            }
            Board[0][7].setPiece(wRook1);
            Board[0][6].setPiece(wPawn1);
            Board[1][7].setPiece(wKnight1);
            Board[1][6].setPiece(wPawn2);
            Board[2][7].setPiece(wBishop1);
            Board[2][6].setPiece(wPawn3);
            Board[3][7].setPiece(wQueen);
            Board[3][6].setPiece(wPawn4);
            Board[4][7].setPiece(wKing);
            Board[4][6].setPiece(wPawn5);
            Board[5][7].setPiece(wBishop2);
            Board[5][6].setPiece(wPawn6);
            Board[6][7].setPiece(wKnight2);
            Board[6][6].setPiece(wPawn7);
            Board[7][7].setPiece(wRook2);
            Board[7][6].setPiece(wPawn8);

            Board[0][0].setPiece(bRook1);
            Board[0][1].setPiece(bPawn1);
            Board[1][0].setPiece(bKnight1);
            Board[1][1].setPiece(bPawn2);
            Board[2][0].setPiece(bBishop1);
            Board[2][1].setPiece(bPawn3);
            Board[3][0].setPiece(bQueen);
            Board[3][1].setPiece(bPawn4);
            Board[4][0].setPiece(bKing);
            Board[4][1].setPiece(bPawn5);
            Board[5][0].setPiece(bBishop2);
            Board[5][1].setPiece(bPawn6);
            Board[6][0].setPiece(bKnight2);
            Board[6][1].setPiece(bPawn7);
            Board[7][0].setPiece(bRook2);
            Board[7][1].setPiece(bPawn8);

            int boardSize = 8; // Tamanho do tabuleiro

            DisplayBoard = new TextView[boardSize][boardSize];
            DisplayBoardBackground = new TextView[boardSize][boardSize];

            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    int resourceId = getResources().getIdentifier("R" + i + j, "id", getPackageName());
                    int backgroundResourceId = getResources().getIdentifier("R0" + i + j, "id", getPackageName());

                    DisplayBoard[i][j] = (TextView) findViewById(resourceId);
                    DisplayBoardBackground[i][j] = (TextView) findViewById(backgroundResourceId);
                }
            }

            for (int g = 0; g < 8; g++) {
                for (int h = 0; h < 8; h++) {
                    if (Board[g][h].getPiece() == null) {
                        Board2[g][h].setPiece(null);
                    } else {
                        Board2[g][h].setPiece(Board[g][h].getPiece());
                    }
                }
            }

            numberOfMoves = 0;
            AnythingSelected = false;
            FirstPlayerTurn = true;
            setBoard();
            deselectPiece();
        }

        private void deselectPiece() {
            AnythingSelected = false;
            if (lastPos != null) {
                DisplayBoardBackground[lastPos.getX()][lastPos.getY()].setBackgroundResource(calculateBoardColorResource(lastPos.getX(), lastPos.getY()));
                // usada para desmarcar a peça quando celecionada
            }
        }

        private void setBoard() {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Piece p = Board[i][j].getPiece();

                    if (p != null) {
                        int resourceId = getResourceIdForPiece(p);
                        if (resourceId != 0) {
                            DisplayBoard[i][j].setBackgroundResource(resourceId);
                        }
                    } else {
                        DisplayBoard[i][j].setBackgroundResource(0);
                    }
                }
            }
            isKingInDanger();
        }

        private int getResourceIdForPiece(Piece p) {
            if (p instanceof King) {
                return p.isWhite() ? R.drawable.wking : R.drawable.bking;
            } else if (p instanceof Queen) {
                return p.isWhite() ? R.drawable.wqueen : R.drawable.bqueen;
            } else if (p instanceof Rook) {
                return p.isWhite() ? R.drawable.wrook : R.drawable.brook;
            } else if (p instanceof Bishop) {
                return p.isWhite() ? R.drawable.wbishop : R.drawable.bbishop;
            } else if (p instanceof Knight) {
                return p.isWhite() ? R.drawable.wknight : R.drawable.bknight;
            } else if (p instanceof Pawn) {
                return p.isWhite() ? R.drawable.wpawn : R.drawable.bpawn;
            } else {
                return 0;
            }
        }

////////////////////////////////////////////////////

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.R00:
                    clickedPosition = new Coordinates(0, 0);
                    break;
                case R.id.R10:
                    clickedPosition.setX(1);
                    clickedPosition.setY(0);
                    break;
                case R.id.R20:
                    clickedPosition.setX(2);
                    clickedPosition.setY(0);
                    break;
                case R.id.R30:
                    clickedPosition.setX(3);
                    clickedPosition.setY(0);
                    break;
                case R.id.R40:
                    clickedPosition.setX(4);
                    clickedPosition.setY(0);
                    break;
                case R.id.R50:
                    clickedPosition.setX(5);
                    clickedPosition.setY(0);
                    break;
                case R.id.R60:
                    clickedPosition.setX(6);
                    clickedPosition.setY(0);
                    break;
                case R.id.R70:
                    clickedPosition.setX(7);
                    clickedPosition.setY(0);
                    break;

                case R.id.R01:
                    clickedPosition.setX(0);
                    clickedPosition.setY(1);
                    break;
                case R.id.R11:
                    clickedPosition.setX(1);
                    clickedPosition.setY(1);
                    break;
                case R.id.R21:
                    clickedPosition.setX(2);
                    clickedPosition.setY(1);
                    break;
                case R.id.R31:
                    clickedPosition.setX(3);
                    clickedPosition.setY(1);
                    break;
                case R.id.R41:
                    clickedPosition.setX(4);
                    clickedPosition.setY(1);
                    break;
                case R.id.R51:
                    clickedPosition.setX(5);
                    clickedPosition.setY(1);
                    break;
                case R.id.R61:
                    clickedPosition.setX(6);
                    clickedPosition.setY(1);
                    break;
                case R.id.R71:
                    clickedPosition.setX(7);
                    clickedPosition.setY(1);
                    break;

                case R.id.R02:
                    clickedPosition.setX(0);
                    clickedPosition.setY(2);
                    break;
                case R.id.R12:
                    clickedPosition.setX(1);
                    clickedPosition.setY(2);
                    break;
                case R.id.R22:
                    clickedPosition.setX(2);
                    clickedPosition.setY(2);
                    break;
                case R.id.R32:
                    clickedPosition.setX(3);
                    clickedPosition.setY(2);
                    break;
                case R.id.R42:
                    clickedPosition.setX(4);
                    clickedPosition.setY(2);
                    break;
                case R.id.R52:
                    clickedPosition.setX(5);
                    clickedPosition.setY(2);
                    break;
                case R.id.R62:
                    clickedPosition.setX(6);
                    clickedPosition.setY(2);
                    break;
                case R.id.R72:
                    clickedPosition.setX(7);
                    clickedPosition.setY(2);
                    break;

                case R.id.R03:
                    clickedPosition.setX(0);
                    clickedPosition.setY(3);
                    break;
                case R.id.R13:
                    clickedPosition.setX(1);
                    clickedPosition.setY(3);
                    break;
                case R.id.R23:
                    clickedPosition.setX(2);
                    clickedPosition.setY(3);
                    break;
                case R.id.R33:
                    clickedPosition.setX(3);
                    clickedPosition.setY(3);
                    break;
                case R.id.R43:
                    clickedPosition.setX(4);
                    clickedPosition.setY(3);
                    break;
                case R.id.R53:
                    clickedPosition.setX(5);
                    clickedPosition.setY(3);
                    break;
                case R.id.R63:
                    clickedPosition.setX(6);
                    clickedPosition.setY(3);
                    break;
                case R.id.R73:
                    clickedPosition.setX(7);
                    clickedPosition.setY(3);
                    break;

                case R.id.R04:
                    clickedPosition.setX(0);
                    clickedPosition.setY(4);
                    break;
                case R.id.R14:
                    clickedPosition.setX(1);
                    clickedPosition.setY(4);
                    break;
                case R.id.R24:
                    clickedPosition.setX(2);
                    clickedPosition.setY(4);
                    break;
                case R.id.R34:
                    clickedPosition.setX(3);
                    clickedPosition.setY(4);
                    break;
                case R.id.R44:
                    clickedPosition.setX(4);
                    clickedPosition.setY(4);
                    break;
                case R.id.R54:
                    clickedPosition.setX(5);
                    clickedPosition.setY(4);
                    break;
                case R.id.R64:
                    clickedPosition.setX(6);
                    clickedPosition.setY(4);
                    break;
                case R.id.R74:
                    clickedPosition.setX(7);
                    clickedPosition.setY(4);
                    break;

                case R.id.R05:
                    clickedPosition.setX(0);
                    clickedPosition.setY(5);
                    break;
                case R.id.R15:
                    clickedPosition.setX(1);
                    clickedPosition.setY(5);
                    break;
                case R.id.R25:
                    clickedPosition.setX(2);
                    clickedPosition.setY(5);
                    break;
                case R.id.R35:
                    clickedPosition.setX(3);
                    clickedPosition.setY(5);
                    break;
                case R.id.R45:
                    clickedPosition.setX(4);
                    clickedPosition.setY(5);
                    break;
                case R.id.R55:
                    clickedPosition.setX(5);
                    clickedPosition.setY(5);
                    break;
                case R.id.R65:
                    clickedPosition.setX(6);
                    clickedPosition.setY(5);
                    break;
                case R.id.R75:
                    clickedPosition.setX(7);
                    clickedPosition.setY(5);
                    break;

                case R.id.R06:
                    clickedPosition.setX(0);
                    clickedPosition.setY(6);
                    break;
                case R.id.R16:
                    clickedPosition.setX(1);
                    clickedPosition.setY(6);
                    break;
                case R.id.R26:
                    clickedPosition.setX(2);
                    clickedPosition.setY(6);
                    break;
                case R.id.R36:
                    clickedPosition.setX(3);
                    clickedPosition.setY(6);
                    break;
                case R.id.R46:
                    clickedPosition.setX(4);
                    clickedPosition.setY(6);
                    break;
                case R.id.R56:
                    clickedPosition.setX(5);
                    clickedPosition.setY(6);
                    break;
                case R.id.R66:
                    clickedPosition.setX(6);
                    clickedPosition.setY(6);
                    break;
                case R.id.R76:
                    clickedPosition.setX(7);
                    clickedPosition.setY(6);
                    break;

                case R.id.R07:
                    clickedPosition.setX(0);
                    clickedPosition.setY(7);
                    break;
                case R.id.R17:
                    clickedPosition.setX(1);
                    clickedPosition.setY(7);
                    break;
                case R.id.R27:
                    clickedPosition.setX(2);
                    clickedPosition.setY(7);
                    break;
                case R.id.R37:
                    clickedPosition.setX(3);
                    clickedPosition.setY(7);
                    break;
                case R.id.R47:
                    clickedPosition.setX(4);
                    clickedPosition.setY(7);
                    break;
                case R.id.R57:
                    clickedPosition.setX(5);
                    clickedPosition.setY(7);
                    break;
                case R.id.R67:
                    clickedPosition.setX(6);
                    clickedPosition.setY(7);
                    break;
                case R.id.R77:
                    clickedPosition.setX(7);
                    clickedPosition.setY(7);
                    break;
            }

            if (!AnythingSelected) {
                Piece clickedPiece = Board[clickedPosition.getX()][clickedPosition.getY()].getPiece();
                if (clickedPiece == null) {
                    isKingInDanger();
                    return;
                } else if (clickedPiece.isWhite() != FirstPlayerTurn) {
                    isKingInDanger();
                    return;
                } else {
                    listOfCoordinates.clear();
                    listOfCoordinates = clickedPiece.AllowedMoves(clickedPosition, Board);
                    DisplayBoardBackground[clickedPosition.getX()][clickedPosition.getY()].setBackgroundResource(R.color.colorSelected);
                    setColorAtAllowedPosition(listOfCoordinates);
                    AnythingSelected = true;
                }
            }



            else {
                if(Board[clickedPosition.getX()][clickedPosition.getY()].getPiece() == null){
                    if(moveIsAllowed(listOfCoordinates , clickedPosition)){

                        saveBoard();
                        if(Board[clickedPosition.getX()][clickedPosition.getY()].getPiece() instanceof King){
                            if(Board[clickedPosition.getX()][clickedPosition.getY()].getPiece().isWhite() != FirstPlayerTurn){
                                game_over.setVisibility(View.VISIBLE);
                            }
                        }
                        Board[clickedPosition.getX()][clickedPosition.getY()].setPiece(Board[lastPos.getX()][lastPos.getY()].getPiece());
                        Board[lastPos.getX()][lastPos.getY()].setPiece(null);

                        isKingInDanger();
                        resetColorAtAllowedPosition(listOfCoordinates);
                        DisplayBoard[lastPos.getX()][lastPos.getY()].setBackgroundResource(0);
                        resetColorAtLastPosition(lastPos);
                        AnythingSelected = false;
                        FirstPlayerTurn = !FirstPlayerTurn;
                        checkForPawn();

                    }else{
                        resetColorAtLastPosition(lastPos);
                        resetColorAtAllowedPosition(listOfCoordinates);
                        AnythingSelected = false;
                    }

                }else{
                    if(Board[clickedPosition.getX()][clickedPosition.getY()].getPiece() == null) {
                        isKingInDanger();
                        return;

                    }else{
                        if(Board[clickedPosition.getX()][clickedPosition.getY()].getPiece() !=null){
                            if(Board[clickedPosition.getX()][clickedPosition.getY()].getPiece().isWhite() != FirstPlayerTurn){
                                if(moveIsAllowed(listOfCoordinates , clickedPosition)){

                                    // Importe a classe Toast

                                    if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece() instanceof King) {

                                        // Verifica se o rei foi morto
                                        if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece().isWhite() != FirstPlayerTurn) {
                                            // Exibe o toast de "game over"
                                            Toast.makeText(getApplicationContext(), "Game Over - Rei foi morto!", Toast.LENGTH_SHORT).show();
                                            // Reinicia o jogo
                                            // initializeBoard();

                                        }
                                    }

                                    Board[clickedPosition.getX()][clickedPosition.getY()].setPiece(Board[lastPos.getX()][lastPos.getY()].getPiece());
                                    Board[lastPos.getX()][lastPos.getY()].setPiece(null);

                                    resetColorAtAllowedPosition(listOfCoordinates);
                                    DisplayBoard[lastPos.getX()][lastPos.getY()].setBackgroundResource(0);
                                    resetColorAtLastPosition(lastPos);

                                    AnythingSelected = false;
                                    FirstPlayerTurn = !FirstPlayerTurn;
                                    checkForPawn();
                                }else{
                                    resetColorAtLastPosition(lastPos);
                                    resetColorAtAllowedPosition(listOfCoordinates);
                                    AnythingSelected = false;
                                }

                            }else{


                                resetColorAtLastPosition(lastPos);
                                resetColorAtAllowedPosition(listOfCoordinates);

                                listOfCoordinates.clear();
                                listOfCoordinates = Board[clickedPosition.getX()][clickedPosition.getY()].getPiece().AllowedMoves(clickedPosition, Board);
                                DisplayBoardBackground[clickedPosition.getX()][clickedPosition.getY()].setBackgroundResource(R.color.colorSelected);
                                setColorAtAllowedPosition(listOfCoordinates);
                                AnythingSelected = true;
                            }
                        }
                    }
                }
            }

            isKingInDanger();
            lastPos = new Coordinates(clickedPosition.getX(), clickedPosition.getY());
            setBoard();
        }


        public void saveBoard(){
            numberOfMoves++;
            LastMoves.add(numberOfMoves-1 ,Board2 );

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    LastMoves.get(numberOfMoves-1)[i][j] = new Position(null);
                }
            }

            for(int g=0;g<8;g++){
                for(int h=0;h<8;h++){
                    if(Board[g][h].getPiece()==null){
                        LastMoves.get(numberOfMoves-1)[g][h].setPiece(null);
                    }else{
                        LastMoves.get(numberOfMoves-1)[g][h].setPiece(Board[g][h].getPiece());
                    }
                }
            }
        }


        public void undo(View v) {
            if (numberOfMoves > 0) {
                Position[][] lastMove = LastMoves.get(numberOfMoves - 1);

                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        Piece piece = lastMove[row][col].getPiece();
                        Board[row][col].setPiece(piece);
                    }
                }

                numberOfMoves--;

                setBoard();
                resetColorAtAllowedPosition( listOfCoordinates);
                isKingInDanger();
            }
        }



        private void resetColorAtAllowedPosition(ArrayList<Coordinates> listOfCoordinates) {
            for (Coordinates coordinate : listOfCoordinates) {
                int boardColorResource = calculateBoardColorResource(coordinate.getX(), coordinate.getY());
                DisplayBoardBackground[coordinate.getX()][coordinate.getY()].setBackgroundResource(boardColorResource);
            }
        }

        private void setColorAtAllowedPosition(ArrayList<Coordinates> list) {
            for (Coordinates coordinate : list) {
                int targetColorResource = determineTargetColorResource(coordinate);
                DisplayBoardBackground[coordinate.getX()][coordinate.getY()].setBackgroundResource(targetColorResource);
            }
        }

        private int calculateBoardColorResource(int x, int y) {
            return (x + y) % 2 == 0 ? R.color.colorBoardDark : R.color.colorBoardLight;
        }

        private int determineTargetColorResource(Coordinates coordinate) {
            if (Board[coordinate.getX()][coordinate.getY()].getPiece() == null) {
                return R.color.colorPositionAvailable;
            } else {
                return R.color.colorDanger;
            }
        }


        private boolean moveIsAllowed(ArrayList<Coordinates> piece, Coordinates coordinate) {
            for (Coordinates c : piece) {
                if (c.getX() == coordinate.getX() && c.getY() == coordinate.getY()) {
                    return true;
                }
            }
            return false;
        }

        private void resetColorAtLastPosition(Coordinates lastPos) {
            int boardColorResource = (lastPos.getX() + lastPos.getY()) % 2 == 0 ? R.color.colorBoardDark : R.color.colorBoardLight;
            DisplayBoardBackground[lastPos.getX()][lastPos.getY()].setBackgroundResource(boardColorResource);
        }


        private void isKingInDanger() {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Piece piece = Board[i][j].getPiece();
                    if (piece != null) {
                        Coordinates coordinates = new Coordinates(i, j);
                        ArrayList<Coordinates> allowedMoves = piece.AllowedMoves(coordinates, Board);
                        for (Coordinates move : allowedMoves) {
                            Piece targetPiece = Board[move.getX()][move.getY()].getPiece();
                            if (targetPiece instanceof King) {
                                setBackgroundBasedOnKing(move, piece);
                            }
                        }
                    }
                }
            }
        }

        private void setBackgroundBasedOnKing(Coordinates kingPosition, Piece piece) {
            int kingColorResource = piece.isWhite() ? R.color.colorKingInDanger : R.color.colorKingInDanger;
            int boardColorResource = (kingPosition.getX() + kingPosition.getY()) % 2 == 0 ? R.color.colorBoardDark : R.color.colorBoardLight;
            DisplayBoardBackground[kingPosition.getX()][kingPosition.getY()].setBackgroundResource(boardColorResource);
            if (piece.isWhite() != Board[kingPosition.getX()][kingPosition.getY()].getPiece().isWhite()) {
                DisplayBoardBackground[kingPosition.getX()][kingPosition.getY()].setBackgroundResource(kingColorResource);
            }
        }
        private void checkForPawn() {
            Piece clickedPiece = Board[clickedPosition.getX()][clickedPosition.getY()].getPiece();
            if (clickedPiece instanceof Pawn) {
                if (clickedPiece.isWhite() && clickedPosition.getY() == 0) {
                    showPawnChoices();
                } else if (!clickedPiece.isWhite() && clickedPosition.getY() == 7) {
                    showPawnChoices();
                    pawn_choices.setRotation(180);
                }
            }
            isKingInDanger();
        }

        private void showPawnChoices() {
        }

    }


