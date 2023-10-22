package com.example.trabalhojogo;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.trabalhojogo.Pieces.Bishop;
import com.example.trabalhojogo.Pieces.King;
import com.example.trabalhojogo.Pieces.Knight;
import com.example.trabalhojogo.Pieces.Pawn;
import com.example.trabalhojogo.Pieces.Piece;
import com.example.trabalhojogo.Pieces.Queen;
import com.example.trabalhojogo.Pieces.Rook;

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
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private ImageView buttonMicrofone;
    private static final int RecordAudioRequestCode = 1;
    private HashMap<String, View> voiceCommandsMap = new HashMap<>();
    Piece bKing,bQueen,bKnight1,bKnight2,bRook1,bRook2,bBishop1,bBishop2,bPawn1,bPawn2,bPawn3,bPawn4,bPawn5,bPawn6,bPawn7,bPawn8;
    Piece wKing,wQueen,wKnight1,wKnight2,wRook1,wRook2,wBishop1,wBishop2,wPawn1,wPawn2,wPawn3,wPawn4,wPawn5,wPawn6,wPawn7,wPawn8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeStatusBarTransparent();
        setContentView(R.layout.activity_game_xadrez);
        initializeBoard();
        calculateCellSize();

        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeBoard();
            }
        });

        editText = findViewById(R.id.textofalado);
        buttonMicrofone = findViewById(R.id.buttonMic);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        speechRecognizerIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
        );
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
            }

            @Override
            public void onBeginningOfSpeech() {
                editText.setText("");
                editText.setHint("Escutando...");
            }

            @Override
            public void onRmsChanged(float rmsdB) {
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int error) {
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (data != null && !data.isEmpty()) {
                    String spokenText = data.get(0).toLowerCase();
                    Toast.makeText(GameXadrez.this, "Texto Reconhecido: " + spokenText, Toast.LENGTH_SHORT).show();

                    View buttonToClick = voiceCommandsMap.get(spokenText);

                    if (buttonToClick != null) {
                        // Clique no botão associado ao comando de voz
                        buttonToClick.performClick();
                    } else {
                        // Comando de voz não reconhecido
                        Toast.makeText(GameXadrez.this, "Comando de voz não reconhecido", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });

        final Handler handler = new Handler();
        final int delay = 2000;

        Runnable runnable = new Runnable() {
            public void run() {
                // esse inicia o reconhecimento de voz automaticamente a cada 2 segundos
                if (ContextCompat.checkSelfPermission(GameXadrez.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                } else {
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                handler.postDelayed(this, delay);
            }
        };

        // essa parte vai inicia a repetição a cada 2 segundos
        handler.postDelayed(runnable, delay);

        buttonMicrofone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    speechRecognizer.stopListening();
                }
                return false;
            }
        });
    }

    private void calculateCellSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;
        int numColumns = 8; // esse é o número de colunas do tabuleiro
        int cellSize = screenWidth / numColumns;

        GridLayout gridLayout2 = findViewById(R.id.gridLayout2);
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        LinearLayout LinearLayout = findViewById(R.id.layoutLetras);

        for (int i = 0; i < gridLayout2.getChildCount(); i++) {
            TextView cell = (TextView) gridLayout2.getChildAt(i);
            ViewGroup.LayoutParams params = cell.getLayoutParams();
            params.width = cellSize;
            params.height = cellSize;
            cell.setLayoutParams(params);
        }

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            TextView cell = (TextView) gridLayout.getChildAt(i);
            ViewGroup.LayoutParams params = cell.getLayoutParams();
            params.width = cellSize;
            params.height = cellSize;
            cell.setLayoutParams(params);
        }

        for (int i = 0; i < LinearLayout.getChildCount(); i++) {
            TextView cell = (TextView) LinearLayout.getChildAt(i);
            ViewGroup.LayoutParams params = cell.getLayoutParams();
            params.width = cellSize;
            params.height = cellSize;
            cell.setLayoutParams(params);
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    RecordAudioRequestCode
            );
        }
    }

    private void makeStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                RecordAudioRequestCode
        );
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

        int boardSize = 8;

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

        ////////////////////////////////////////////////////// click por voz

        Button btn1 = findViewById(R.id.btnReset);
        TextView A1 = findViewById(R.id.R07);
        TextView A2 = findViewById(R.id.R06);
        TextView A3 = findViewById(R.id.R05);
        TextView A4 = findViewById(R.id.R04);
        TextView A5 = findViewById(R.id.R03);
        TextView A6 = findViewById(R.id.R02);
        TextView A7 = findViewById(R.id.R01);
        TextView A8 = findViewById(R.id.R00);

        TextView B1 = findViewById(R.id.R17);
        TextView B2 = findViewById(R.id.R16);
        TextView B3 = findViewById(R.id.R15);
        TextView B4 = findViewById(R.id.R14);
        TextView B5 = findViewById(R.id.R13);
        TextView B6 = findViewById(R.id.R12);
        TextView B7 = findViewById(R.id.R11);
        TextView B8 = findViewById(R.id.R10);

        TextView C1 = findViewById(R.id.R27);
        TextView C2 = findViewById(R.id.R26);
        TextView C3 = findViewById(R.id.R25);
        TextView C4 = findViewById(R.id.R24);
        TextView C5 = findViewById(R.id.R23);
        TextView C6 = findViewById(R.id.R22);
        TextView C7 = findViewById(R.id.R21);
        TextView C8 = findViewById(R.id.R20);

        TextView D1 = findViewById(R.id.R37);
        TextView D2 = findViewById(R.id.R36);
        TextView D3 = findViewById(R.id.R35);
        TextView D4 = findViewById(R.id.R34);
        TextView D5 = findViewById(R.id.R33);
        TextView D6 = findViewById(R.id.R32);
        TextView D7 = findViewById(R.id.R31);
        TextView D8 = findViewById(R.id.R30);

        TextView E1 = findViewById(R.id.R47);
        TextView E2 = findViewById(R.id.R46);
        TextView E3 = findViewById(R.id.R45);
        TextView E4 = findViewById(R.id.R44);
        TextView E5 = findViewById(R.id.R43);
        TextView E6 = findViewById(R.id.R42);
        TextView E7 = findViewById(R.id.R41);
        TextView E8 = findViewById(R.id.R40);

        TextView F1 = findViewById(R.id.R57);
        TextView F2 = findViewById(R.id.R56);
        TextView F3 = findViewById(R.id.R55);
        TextView F4 = findViewById(R.id.R54);
        TextView F5 = findViewById(R.id.R53);
        TextView F6 = findViewById(R.id.R52);
        TextView F7 = findViewById(R.id.R51);
        TextView F8 = findViewById(R.id.R50);

        TextView G1 = findViewById(R.id.R67);
        TextView G2 = findViewById(R.id.R66);
        TextView G3 = findViewById(R.id.R65);
        TextView G4 = findViewById(R.id.R64);
        TextView G5 = findViewById(R.id.R63);
        TextView G6 = findViewById(R.id.R62);
        TextView G7 = findViewById(R.id.R61);
        TextView G8 = findViewById(R.id.R60);

        TextView H1 = findViewById(R.id.R77);
        TextView H2 = findViewById(R.id.R76);
        TextView H3 = findViewById(R.id.R75);
        TextView H4 = findViewById(R.id.R74);
        TextView H5 = findViewById(R.id.R73);
        TextView H6 = findViewById(R.id.R72);
        TextView H7 = findViewById(R.id.R71);
        TextView H8 = findViewById(R.id.R70);

        voiceCommandsMap.put("resetar", btn1);
        voiceCommandsMap.put("clique no botão a", btn1);

        voiceCommandsMap.put("a um", A1);
        voiceCommandsMap.put("a1", A1);
        voiceCommandsMap.put("A1", A1);
        voiceCommandsMap.put("A 1", A1);

        voiceCommandsMap.put("a dois", A2);
        voiceCommandsMap.put("a2", A2);
        voiceCommandsMap.put("A2", A2);
        voiceCommandsMap.put("A 2", A2);

        voiceCommandsMap.put("a 3", A3);
        voiceCommandsMap.put("a3", A3);
        voiceCommandsMap.put("A3", A3);
        voiceCommandsMap.put("atriz", A3);

        voiceCommandsMap.put("a 4", A4);
        voiceCommandsMap.put("a4", A4);
        voiceCommandsMap.put("A4", A4);
        voiceCommandsMap.put("a quatro", A4);

        voiceCommandsMap.put("a 5", A5);
        voiceCommandsMap.put("A 5", A5);
        voiceCommandsMap.put("a5", A5);
        voiceCommandsMap.put("A5", A5);
        voiceCommandsMap.put("a cinco", A5);

        voiceCommandsMap.put("a 6", A6);
        voiceCommandsMap.put("A 6", A6);
        voiceCommandsMap.put("a6", A6);
        voiceCommandsMap.put("A6", A6);
        voiceCommandsMap.put("a seis", A6);
        voiceCommandsMap.put("Assis ", A6);

        voiceCommandsMap.put("a 7", A7);
        voiceCommandsMap.put("A 7", A7);
        voiceCommandsMap.put("a7", A7);
        voiceCommandsMap.put("A7", A7);
        voiceCommandsMap.put("a sete", A7);

        voiceCommandsMap.put("a 8", A8);
        voiceCommandsMap.put("A 8", A8);
        voiceCommandsMap.put("a8", A8);
        voiceCommandsMap.put("A8", A8);
        voiceCommandsMap.put("a oito", A8);

        voiceCommandsMap.put("B 1", B1);
        voiceCommandsMap.put("b 1", B1);
        voiceCommandsMap.put("b1", B1);
        voiceCommandsMap.put("B1", B1);

        voiceCommandsMap.put("b 2", B2);
        voiceCommandsMap.put("B 2", B2);
        voiceCommandsMap.put("b2", B2);
        voiceCommandsMap.put("B2", B2);
        voiceCommandsMap.put("b dois", B2);

        voiceCommandsMap.put("b 3", B3);
        voiceCommandsMap.put("B 3", B3);
        voiceCommandsMap.put("b3", B3);
        voiceCommandsMap.put("B3", B3);
        voiceCommandsMap.put("b três", B3);

        voiceCommandsMap.put("b 4", B4);
        voiceCommandsMap.put("B 4", B4);
        voiceCommandsMap.put("b4", B4);
        voiceCommandsMap.put("B4", B4);
        voiceCommandsMap.put("b quatro", B4);

        voiceCommandsMap.put("b 4", B4);
        voiceCommandsMap.put("B 4", B4);
        voiceCommandsMap.put("b4", B4);
        voiceCommandsMap.put("B4", B4);
        voiceCommandsMap.put("b quatro", B4);

        voiceCommandsMap.put("b 5", B5);
        voiceCommandsMap.put("B 5", B5);
        voiceCommandsMap.put("b5", B5);
        voiceCommandsMap.put("B5", B5);
        voiceCommandsMap.put("b cinco", B5);

        voiceCommandsMap.put("b 6", B6);
        voiceCommandsMap.put("B 6", B6);
        voiceCommandsMap.put("b6", B6);
        voiceCommandsMap.put("B6", B6);
        voiceCommandsMap.put("b seis", B6);

        voiceCommandsMap.put("b 7", B7);
        voiceCommandsMap.put("B 7", B7);
        voiceCommandsMap.put("b7", B7);
        voiceCommandsMap.put("B7", B7);
        voiceCommandsMap.put("b sete", B7);

        voiceCommandsMap.put("b 8", B8);
        voiceCommandsMap.put("B 8", B8);
        voiceCommandsMap.put("b8", B8);
        voiceCommandsMap.put("B8", B8);
        voiceCommandsMap.put("b oito", B8);

        voiceCommandsMap.put("c 1", C1);
        voiceCommandsMap.put("C 1", C1);
        voiceCommandsMap.put("c1", C1);
        voiceCommandsMap.put("C1", C1);
        voiceCommandsMap.put("c um", C1);

        voiceCommandsMap.put("c 2", C2);
        voiceCommandsMap.put("C 2", C2);
        voiceCommandsMap.put("c2", C2);
        voiceCommandsMap.put("c2", C2);
        voiceCommandsMap.put("c dois", C2);

        voiceCommandsMap.put("c 3", C3);
        voiceCommandsMap.put("C 3", C3);
        voiceCommandsMap.put("c3", C3);
        voiceCommandsMap.put("C3", C3);
        voiceCommandsMap.put("c três", C3);

        voiceCommandsMap.put("c 4", C4);
        voiceCommandsMap.put("C 4", C4);
        voiceCommandsMap.put("c4", C4);
        voiceCommandsMap.put("C4", C4);
        voiceCommandsMap.put("c quatro", C4);

        voiceCommandsMap.put("c 5", C5);
        voiceCommandsMap.put("C 5", C5);
        voiceCommandsMap.put("c5", C5);
        voiceCommandsMap.put("C5", C5);
        voiceCommandsMap.put("c cinco", C5);

        voiceCommandsMap.put("c 6", C6);
        voiceCommandsMap.put("C 6", C6);
        voiceCommandsMap.put("c6", C6);
        voiceCommandsMap.put("C6", C6);
        voiceCommandsMap.put("c seis", C6);

        voiceCommandsMap.put("c 7", C7);
        voiceCommandsMap.put("C 7", C7);
        voiceCommandsMap.put("c7", C7);
        voiceCommandsMap.put("C7", C7);
        voiceCommandsMap.put("c sete", C7);

        voiceCommandsMap.put("c 8", C8);
        voiceCommandsMap.put("C 8", C8);
        voiceCommandsMap.put("c8", C8);
        voiceCommandsMap.put("C8", C8);
        voiceCommandsMap.put("c oito", C8);

        voiceCommandsMap.put("d 1", D1);
        voiceCommandsMap.put("D 1", D1);
        voiceCommandsMap.put("d1", D1);
        voiceCommandsMap.put("D1", D1);
        voiceCommandsMap.put("de um", D1);

        voiceCommandsMap.put("d 2", D2);
        voiceCommandsMap.put("D 2", D2);
        voiceCommandsMap.put("d2", D2);
        voiceCommandsMap.put("D2", D2);
        voiceCommandsMap.put("de dois", D2);

        voiceCommandsMap.put("d 3", D3);
        voiceCommandsMap.put("D 3", D3);
        voiceCommandsMap.put("d3", D3);
        voiceCommandsMap.put("D3", D3);
        voiceCommandsMap.put("de três", D3);

        voiceCommandsMap.put("d 4", D4);
        voiceCommandsMap.put("D 4", D4);
        voiceCommandsMap.put("d4", D4);
        voiceCommandsMap.put("D4", D4);
        voiceCommandsMap.put("de quatro", D4);

        voiceCommandsMap.put("d 5", D5);
        voiceCommandsMap.put("D 5", D5);
        voiceCommandsMap.put("d5", D5);
        voiceCommandsMap.put("D5", D5);
        voiceCommandsMap.put("de cinco", D5);

        voiceCommandsMap.put("d 6", D6);
        voiceCommandsMap.put("D 6", D6);
        voiceCommandsMap.put("d6", D6);
        voiceCommandsMap.put("D6", D6);
        voiceCommandsMap.put("de seis", D6);

        voiceCommandsMap.put("d 7", D7);
        voiceCommandsMap.put("D 7", D7);
        voiceCommandsMap.put("d7", D7);
        voiceCommandsMap.put("D7", D7);
        voiceCommandsMap.put("de sete", D7);

        voiceCommandsMap.put("d 8", D8);
        voiceCommandsMap.put("D 8", D8);
        voiceCommandsMap.put("d8", D8);
        voiceCommandsMap.put("D8", D8);
        voiceCommandsMap.put("de oito", D8);

        voiceCommandsMap.put("e 1", E1);
        voiceCommandsMap.put("E 1", E1);
        voiceCommandsMap.put("e1", E1);
        voiceCommandsMap.put("E1", E1);
        voiceCommandsMap.put("e um", E1);

        voiceCommandsMap.put("e 2", E2);
        voiceCommandsMap.put("E 2", E2);
        voiceCommandsMap.put("e2", E2);
        voiceCommandsMap.put("E2", E2);
        voiceCommandsMap.put("e dois", E2);

        voiceCommandsMap.put("e 3", E3);
        voiceCommandsMap.put("E 3", E3);
        voiceCommandsMap.put("e3", E3);
        voiceCommandsMap.put("E3", E3);
        voiceCommandsMap.put("e três", E3);

        voiceCommandsMap.put("e 4", E4);
        voiceCommandsMap.put("E 4", E4);
        voiceCommandsMap.put("e4", E4);
        voiceCommandsMap.put("E4", E4);
        voiceCommandsMap.put("e quatro", E4);

        voiceCommandsMap.put("e 5", E5);
        voiceCommandsMap.put("E 5", E5);
        voiceCommandsMap.put("e5", E5);
        voiceCommandsMap.put("E5", E5);
        voiceCommandsMap.put("e cinco", E5);

        voiceCommandsMap.put("e 6", E6);
        voiceCommandsMap.put("E 6", E6);
        voiceCommandsMap.put("e6", E6);
        voiceCommandsMap.put("E6", E6);
        voiceCommandsMap.put("e seis", E6);

        voiceCommandsMap.put("e 7", E7);
        voiceCommandsMap.put("E 7", E7);
        voiceCommandsMap.put("e7", E7);
        voiceCommandsMap.put("E7", E7);
        voiceCommandsMap.put("e sete", E7);

        voiceCommandsMap.put("e 8", E8);
        voiceCommandsMap.put("E 8", E8);
        voiceCommandsMap.put("e8", E8);
        voiceCommandsMap.put("E8", E8);
        voiceCommandsMap.put("e oito", E8);

        voiceCommandsMap.put("f 1", F1);
        voiceCommandsMap.put("F 1", F1);
        voiceCommandsMap.put("f1", F1);
        voiceCommandsMap.put("F1", F1);
        voiceCommandsMap.put("f um", F1);

        voiceCommandsMap.put("f 2", F2);
        voiceCommandsMap.put("F 2", F2);
        voiceCommandsMap.put("f2", F2);
        voiceCommandsMap.put("F2", F2);
        voiceCommandsMap.put("f dois", F2);

        voiceCommandsMap.put("f 3", F3);
        voiceCommandsMap.put("F 3", F3);
        voiceCommandsMap.put("f3", F3);
        voiceCommandsMap.put("F3", F3);
        voiceCommandsMap.put("f três", F3);

        voiceCommandsMap.put("f 4", F4);
        voiceCommandsMap.put("F 4", F4);
        voiceCommandsMap.put("f4", F4);
        voiceCommandsMap.put("F4", F4);
        voiceCommandsMap.put("f quatro", F4);

        voiceCommandsMap.put("f 5", F5);
        voiceCommandsMap.put("F 5", F5);
        voiceCommandsMap.put("f5", F5);
        voiceCommandsMap.put("F5", F5);
        voiceCommandsMap.put("f cinco", F5);

        voiceCommandsMap.put("f 6", F6);
        voiceCommandsMap.put("F 6", F6);
        voiceCommandsMap.put("f6", F6);
        voiceCommandsMap.put("F6", F6);
        voiceCommandsMap.put("f seis", F6);

        voiceCommandsMap.put("f 7", F7);
        voiceCommandsMap.put("F 7", F7);
        voiceCommandsMap.put("f7", F7);
        voiceCommandsMap.put("F7", F7);
        voiceCommandsMap.put("f sete", F7);

        voiceCommandsMap.put("f 8", F8);
        voiceCommandsMap.put("F 8", F8);
        voiceCommandsMap.put("f8", F8);
        voiceCommandsMap.put("F8", F8);
        voiceCommandsMap.put("f oito", F8);

        voiceCommandsMap.put("g 1", G1);
        voiceCommandsMap.put("G 1", G1);
        voiceCommandsMap.put("g1", G1);
        voiceCommandsMap.put("G1", G1);
        voiceCommandsMap.put("g um", G1);

        voiceCommandsMap.put("g 2", G2);
        voiceCommandsMap.put("G 2", G2);
        voiceCommandsMap.put("g2", G2);
        voiceCommandsMap.put("G2", G2);
        voiceCommandsMap.put("g dois", G2);

        voiceCommandsMap.put("g 3", G3);
        voiceCommandsMap.put("G 3", G3);
        voiceCommandsMap.put("g3", G3);
        voiceCommandsMap.put("G3", G3);
        voiceCommandsMap.put("g três", G3);

        voiceCommandsMap.put("g 4", G4);
        voiceCommandsMap.put("G 4", G4);
        voiceCommandsMap.put("g4", G4);
        voiceCommandsMap.put("G4", G4);
        voiceCommandsMap.put("g quatro", G4);

        voiceCommandsMap.put("g 5", G5);
        voiceCommandsMap.put("G 5", G5);
        voiceCommandsMap.put("g5", G5);
        voiceCommandsMap.put("G5", G5);
        voiceCommandsMap.put("g cinco", G5);

        voiceCommandsMap.put("g 6", G6);
        voiceCommandsMap.put("G 6", G6);
        voiceCommandsMap.put("g6", G6);
        voiceCommandsMap.put("G6", G6);
        voiceCommandsMap.put("g seis", G6);

        voiceCommandsMap.put("g 7", G7);
        voiceCommandsMap.put("G 7", G7);
        voiceCommandsMap.put("g7", G7);
        voiceCommandsMap.put("G7", G7);
        voiceCommandsMap.put("g sete", G7);

        voiceCommandsMap.put("g 8", G8);
        voiceCommandsMap.put("G 8", G8);
        voiceCommandsMap.put("g8", G8);
        voiceCommandsMap.put("G8", G8);
        voiceCommandsMap.put("g oito", G8);

        voiceCommandsMap.put("h 1", H1);
        voiceCommandsMap.put("H 1", H1);
        voiceCommandsMap.put("h1", H1);
        voiceCommandsMap.put("H1", H1);
        voiceCommandsMap.put("h um", H1);

        voiceCommandsMap.put("h 2", H2);
        voiceCommandsMap.put("H 2", H2);
        voiceCommandsMap.put("h2", H2);
        voiceCommandsMap.put("H2", H2);
        voiceCommandsMap.put("h dois", H2);

        voiceCommandsMap.put("h 3", H3);
        voiceCommandsMap.put("H 3", H3);
        voiceCommandsMap.put("h3", H3);
        voiceCommandsMap.put("H3", H3);
        voiceCommandsMap.put("h três", H3);

        voiceCommandsMap.put("h 4", H4);
        voiceCommandsMap.put("H 4", H4);
        voiceCommandsMap.put("h4", H4);
        voiceCommandsMap.put("H4", H4);
        voiceCommandsMap.put("h quatro", H4);

        voiceCommandsMap.put("h 5", H5);
        voiceCommandsMap.put("H 5", H5);
        voiceCommandsMap.put("h5", H5);
        voiceCommandsMap.put("H5", H5);
        voiceCommandsMap.put("h cinco", H5);

        voiceCommandsMap.put("h 6", H6);
        voiceCommandsMap.put("H 6", H6);
        voiceCommandsMap.put("h6", H6);
        voiceCommandsMap.put("H6", H6);
        voiceCommandsMap.put("h seis", H6);

        voiceCommandsMap.put("h 7", H7);
        voiceCommandsMap.put("H 7", H7);
        voiceCommandsMap.put("h7", H7);
        voiceCommandsMap.put("H7", H7);
        voiceCommandsMap.put("h sete", H7);

        voiceCommandsMap.put("h 8", H8);
        voiceCommandsMap.put("H 8", H8);
        voiceCommandsMap.put("h8", H8);
        voiceCommandsMap.put("H8", H8);
        voiceCommandsMap.put("h oito", H8);
    }

///////////

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
                //       isKingInDanger();
                return;
            } else if (clickedPiece.isWhite() != FirstPlayerTurn) {
                return;
            } else {
                listOfCoordinates.clear();
                listOfCoordinates = clickedPiece.getAllowedMoves(clickedPosition, Board);
                DisplayBoardBackground[clickedPosition.getX()][clickedPosition.getY()].setBackgroundResource(R.color.colorSelected);
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

                    DisplayBoard[lastPos.getX()][lastPos.getY()].setBackgroundResource(0);
                    resetColorAtLastPosition(lastPos);
                    AnythingSelected = false;
                    FirstPlayerTurn = !FirstPlayerTurn;

                }else{
                    resetColorAtLastPosition(lastPos);
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

                                if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece() instanceof King) {

                                    // Verifica se o rei foi morto
                                    if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece().isWhite() != FirstPlayerTurn) {
                                        // Exibe o toast de "game over"
                                        Toast.makeText(getApplicationContext(), "Game Over - Rei foi morto!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                Board[clickedPosition.getX()][clickedPosition.getY()].setPiece(Board[lastPos.getX()][lastPos.getY()].getPiece());
                                Board[lastPos.getX()][lastPos.getY()].setPiece(null);

                                DisplayBoard[lastPos.getX()][lastPos.getY()].setBackgroundResource(0);
                                resetColorAtLastPosition(lastPos);

                                AnythingSelected = false;
                                FirstPlayerTurn = !FirstPlayerTurn;
                            }else{
                                resetColorAtLastPosition(lastPos);
                                AnythingSelected = false;
                            }

                        }else{
                            resetColorAtLastPosition(lastPos);
                            listOfCoordinates.clear();
                            listOfCoordinates = Board[clickedPosition.getX()][clickedPosition.getY()].getPiece().getAllowedMoves(clickedPosition, Board);
                            DisplayBoardBackground[clickedPosition.getX()][clickedPosition.getY()].setBackgroundResource(R.color.colorSelected);
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
                    ArrayList<Coordinates> allowedMoves = piece.getAllowedMoves(coordinates, Board);
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
}
