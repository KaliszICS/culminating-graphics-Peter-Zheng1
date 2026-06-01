/**

        * File: Culminating Assignment | Battleship

        * Author: Peter Zheng

        * Date Created: May 26, 2026

        * Date Last Modified: June 1, 2026

        */
                        // IMPORTANT NOTE: PREVIOUS UPDATE WERE DONE ON "CULMINATING" Assingment.
                        // Instead of the "CULMINATING-GRAPHICS" Assignment. 
                        // New updates will be done on this assignment 

// importing javafx, might add more - temp comment
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Random;

public class PracticeProblem extends Application {


        // Variables
        int SIZE = 5;

        char[][] playerBoard;
        char[][] enemyBoard;

        Button[][] playerButtons;
        Button[][] enemyButtons;

        boolean playerTurn;
        boolean setupMode;

        int shipsPlaced;

        Random rand;
        Label title;
        GridPane grid;

        //starts javafx
	public static void main(String args[]) {

                launch(args);
	}

        //------- Start (Game Setup)-------
        @Override
        public void start(Stage stage){

        playerBoard = new char[SIZE][SIZE];
        enemyBoard = new char[SIZE][SIZE];

        playerButtons = new Button[SIZE][SIZE];
        enemyButtons = new Button[SIZE][SIZE];

        rand = new Random();

        setupMode = true;
        playerTurn = true;
        shipsPlaced = 0;

        initializeBoards();
        
        grid = createGrid();

        title = new Label("Place 2 Ships (Length 2) on the left grid");
        title.setFont(new Font(16));

        Button restartButn = new Button("Restart game");
        restartButn.setOnAction(e -> restartGame()); 
        // -> Lambda operator, used for nameless functions, takes input and does action

        VBox root = new VBox(title, grid, restartButn);
        
        //javafx scenes/title
        Scene scene = new Scene(root, 600, 380);
        stage.setScene(scene);
        stage.setTitle("BattleShip");
        stage.show();

        placeEnemyShips();
        }

        //------- Methods-------
        public void restartGame(){ // restarts game
                setupMode = true;
                playerTurn = true;
                shipsPlaced = 0;

                playerBoard = new char[SIZE][SIZE];
                enemyBoard = new char[SIZE][SIZE];

                initializeBoards();

                //resets text on buttons &  colors & styles (i.e: removes green, red, and blue)
                for (int r = 0; r < SIZE; r++){
                        for (int c = 0; c < SIZE; c++){
                                playerButtons[r][c].setText("");
                                playerButtons[r][c].setStyle("");

                                enemyButtons[r][c].setText("");
                                enemyButtons[r][c].setStyle("");

                                //enables clicking again - removes clicking after you won so must be reset
                                playerButtons[r][c].setDisable(false);
                                enemyButtons[r][c].setDisable(false);
                        }

                }

                        title.setText("Place 2 ships (Length 2) on left grid");

                        placeEnemyShips();
        }

        
                //------- Initialize-------
                public void initializeBoards(){
                        for (int r = 0; r < SIZE; r++){
                                for (int c = 0; c < SIZE; c++){
                                        playerBoard[r][c] = '~';
                                        enemyBoard[r][c] = '~';
                                }
                        }
                }
                //------- Grid -------

                // Player Grid
                public GridPane createGrid(){
                        GridPane grid = new GridPane();
                
                        for (int r = 0; r < SIZE; r++){
                                for (int c = 0; c < SIZE; c++){
                                        Button butn = new Button();
                                        butn.setPrefSize(40, 40);

                                        int row = r;
                                        int col = c;

                                        butn.setOnAction(e -> placePlayerShip(row, col));

                                        playerButtons[r][c] = butn;
                                        grid.add(butn, c, r);
                                }
                        }

                        // Enemy Grid
                        for (int r = 0; r < SIZE; r++){
                                for (int c = 0; c < SIZE; c++){

                                        Button butn = new Button();
                                        butn.setPrefSize(40, 40);

                                        int row = r;
                                        int col = c;

                                        butn.setOnAction(e -> attackEnemy(row, col));

                                        enemyButtons[r][c] = butn;
                                        grid.add(butn, c + 7, r);

                                }
                        }
                        return grid;

                }
        // ------- player attacking ---------
        public void attackEnemy(int row, int col){

                if (setupMode) return; // if its still in setup mode, stop.
                if (!playerTurn) return; // if not playerturn, stop.

                if (enemyBoard[row][col] == 'X' || enemyBoard[row][col] == '0') return; // if
                //  theres a x or 0 stop.

                if (enemyBoard[row][col] == 'S') {
                        enemyBoard[row][col] = 'X';
                        enemyButtons[row][col].setText("X");
                        enemyButtons[row][col].setStyle("-fx-background-color: lightblue;");
                }

                else {
                        enemyBoard[row][col] = '0';
                        enemyButtons[row][col].setText("0");
                        enemyButtons[row][col].setStyle("-fx-background-color: lightblue;");
                }

                if (checkWin(enemyBoard)) {
                        showWin("You win!");
                        return;
                }
                
                playerTurn = false;
                

        }
        public void checkWin(char[][] board){

        }

        public void showWin(String message){

        }
        
}
