/**

        * File: Culminating Assignment | Battleship

        * Author: Peter Zheng

        * Date Created: May 26, 2026

        * Date Last Modified: June 3, 2026

        */
                        // IMPORTANT NOTE: PREVIOUS UPDATES WERE DONE ON "CULMINATING" Assingment.
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
        int SIZE = 10;

        char[][] playerBoard;
        char[][] enemyBoard;

        Button[][] playerButtons;
        Button[][] enemyButtons;

        boolean playerTurn;
        boolean setupMode;

        int shipsPlaced;

        Random rand;
        Label title;
        GridPane playerGrid;
        GridPane enemyGrid;

        //starts javafx
	public static void main(String args[]) {

                launch(args);
	}
        
        //------- Start (Game Setup)-------
        @Override
      
        public void start(Stage stage){

        System.out.println("Game Started");

        playerBoard = new char[SIZE][SIZE];
        enemyBoard = new char[SIZE][SIZE];

        playerButtons = new Button[SIZE][SIZE];
        enemyButtons = new Button[SIZE][SIZE];

        rand = new Random();

        setupMode = true;
        playerTurn = true;
        shipsPlaced = 0;

        initializeBoards();
        
        HBox boards = createGrid();

        title = new Label("Place 2 Ships (Length 2) on the left grid");
        title.setFont(new Font(16));

        Button restartButn = new Button("Restart game");
        restartButn.setOnAction(e -> restartGame()); 
        // -> Lambda operator, used for nameless functions, takes input and does action

        VBox root = new VBox(title, boards, restartButn);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);
        
        //javafx scenes/title
        Scene scene = new Scene(root, 600, 380);
        stage.setScene(scene);
        stage.setTitle("BattleShip");
        stage.show();

        placeEnemyShips();
        }

        //------- Methods-------

        public HBox createGrid(){

                playerGrid = new GridPane();
                enemyGrid = new GridPane();

                playerGrid.setHgap(2);
                playerGrid.setVgap(2);

                enemyGrid.setHgap(2);
                enemyGrid.setVgap(2);

                // Player grid
                for (int r = 0; r < SIZE; r++){
                        for (int c = 0; c < SIZE; c++ ){
                                Button butn = new Button();
                                butn.setPrefSize(40, 40);

                                int row = r;
                                int col = c;

                                butn.setOnAction(e -> placePlayerShip(row, col));

                                playerButtons[r][c] = butn;
                                playerGrid.add(butn, c, r);
                        }
                }

                // Enemy grid
                for (int r = 0; r < SIZE; r++){
                        for (int c = 0; c < SIZE; c++){

                                Button butn = new Button();
                                butn.setPrefSize(40, 40);

                                int row = r;
                                int col = c;

                                butn.setOnAction(e -> attackEnemy(row, col));

                                enemyButtons[r][c] = butn;
                                enemyGrid.add(butn, c, r);
                        }
                }
                VBox left = new VBox(new Label("YOUR BOARD"), playerGrid);
                VBox right = new VBox(new Label("ENEMY BOARD"), enemyGrid);

                left.setAlignment(Pos.CENTER);
                right.setAlignment(Pos.CENTER);

                return new HBox(50, left, right);
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
                
        // ------- player attacking ---------
        public void attackEnemy(int row, int col){

                if (setupMode) return; // if its still in setup mode, stop.
                if (!playerTurn) return; // if not playerturn, stop.

                if (enemyBoard[row][col] == 'X' || enemyBoard[row][col] == 'O') return; // if
                //  theres a x or 0 stop.

                if (enemyBoard[row][col] == 'S') {
                        enemyBoard[row][col] = 'X';
                        enemyButtons[row][col].setText("X");
                        enemyButtons[row][col].setStyle("-fx-background-color: red;"); // hit
                }

                else {
                        enemyBoard[row][col] = 'O';
                        enemyButtons[row][col].setText("O");
                        enemyButtons[row][col].setStyle("-fx-background-color: lightblue;"); // miss
                }

                if (checkWin(enemyBoard)) {
                        showWin("You win!");
                        return;
                }
                
                playerTurn = false;
                enemyAttack();
                

        }

        public void enemyAttack(){
                if (setupMode) return;
                int row;
                int col;

                do { // picks random tile (not picked before)
                        row = rand.nextInt(SIZE);
                        col = rand.nextInt(SIZE);
                } while (playerBoard[row][col] == 'X' || playerBoard[row][col] == 'O');

                if (playerBoard[row][col] == 'S'){
                        playerBoard[row][col] = 'X';
                        playerButtons[row][col].setText("X");
                        playerButtons[row][col].setStyle("-fx-background-color: red;");
                }

                else {
                        playerBoard[row][col] = 'O';
                        playerButtons[row][col].setText("O");
                        playerButtons[row][col].setStyle("-fx-background-color: lightblue;");
                }

                if (checkWin(playerBoard)) {
                        showWin("Enemy has won!");
                        return;
                }
                playerTurn = true;
        }
        

        public void placePlayerShip(int row, int col){
                if (!setupMode) return; // exits if false

                if (playerBoard[row][col] == '~'){
                        playerBoard[row][col] = 'S';
                        playerButtons[row][col].setStyle("-fx-background-color: green;");
                        shipsPlaced++;
                }

                if (shipsPlaced >= 4) {
                        setupMode = false;
                        title.setText("Start attacking!");
                }


        }
                public void placeEnemyShips(){  // placing enemy ships (randomized)
                int count = 0;
                while (count < 4){
                        int r = rand.nextInt(SIZE);
                        int c = rand.nextInt(SIZE);

                        if (enemyBoard[r][c] == '~'){
                                enemyBoard[r][c] = 'S';
                                count++;
                        }
                }
        }

        public boolean checkWin(char[][] board){ // checking if the player/enemy has won the game 
                for (int r = 0; r < SIZE; r++){
                        for (int c = 0; c < SIZE; c++){
                                if (board[r][c] =='S'){
                                        return false;
                                }
                        }
                }
                return true;
        }

        public void showWin(String message){ //Showing that the player won
                title.setText(message);

                for (int r = 0; r < SIZE; r++){
                        for (int c = 0; c < SIZE; c++){
                                playerButtons[r][c].setDisable(true);
                                enemyButtons[r][c].setDisable(true);
                        }
                }
                        
        }

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
        
}
