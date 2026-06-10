/**
 *
 *        * File: Culminating Assignment | Battleship
 *
 *        * Author: Peter Zheng
 *
 *        * Date Created: May 26, 2026
 *
 *        * Date Last Modified: June 3, 2026
 *
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
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class PracticeProblem extends Application {

    int SIZE = 10; //board size

    // The Game Boards
    char[][] playerBoard;
    char[][] enemyBoard;

    // Buttons (UI)
    Button[][] playerButtons;
    Button[][] enemyButtons;

    boolean playerTurn;
    boolean setupMode;
    boolean placingHorizontal = true;

    int shipsPlaced;

    Random rand;
    Label title;

    GridPane playerGrid;
    GridPane enemyGrid;

    Rectangle shipPreview;

    int shipLength = 2;

    // launching
    public static void main(String args[]) {
        launch(args);
    }

    // Starting Game
    @Override
    public void start(Stage stage) {

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

        title = new Label("Drag ship onto your board (2 ships)");
        title.setFont(new Font(16));

        // Ship Preview
        // the draggable ship
        shipPreview = new Rectangle(80, 40);
        shipPreview.setFill(Color.GREEN);

        // dragging
        shipPreview.setOnMouseDragged(this::dragShip);

        //dropping
        shipPreview.setOnMouseReleased(this::dropShip);

        //Buttons
        Button restartButn = new Button("Restart game");
        Button rotateButn = new Button("Rotate Ship");

        // rotatation (horizontal/vertical)
        rotateButn.setOnAction(e -> {
            placingHorizontal = !placingHorizontal;

            if (placingHorizontal) {
                shipPreview.setWidth(80);
                shipPreview.setHeight(40);
                title.setText("Horizontal Ship");
            } else {
                shipPreview.setWidth(40);
                shipPreview.setHeight(80);
                title.setText("Vertical Ship");
            }
        });

        // restarts/resets everything 
        restartButn.setOnAction(e -> restartGame());

        HBox controls = new HBox(10, restartButn, rotateButn);
        controls.setAlignment(Pos.CENTER);

        VBox root = new VBox(title, shipPreview, boards, controls);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 900, 550);
        stage.setScene(scene);
        stage.setTitle("BattleShip");
        stage.show();

        //places enemy ships at the start
        placeEnemyShips();
    }

    // ------------- the grid -------------

    public HBox createGrid() {

        playerGrid = new GridPane();
        enemyGrid = new GridPane();

        playerGrid.setHgap(0);
        playerGrid.setVgap(0);

        enemyGrid.setHgap(0);
        enemyGrid.setVgap(0);

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {

                // player grid
                Button p = new Button();
                p.setPrefSize(40, 40);
                playerButtons[r][c] = p;
                playerGrid.add(p, c, r);

                // enemy grid
                Button e = new Button();
                e.setPrefSize(40, 40);

                int row = r;
                int col = c;

                //clicking enemy grid (attack)
                e.setOnAction(ev -> attackEnemy(row, col));

                enemyButtons[r][c] = e;
                enemyGrid.add(e, c, r);
            }
        }

        VBox left = new VBox(new Label("YOUR BOARD"), playerGrid);
        VBox right = new VBox(new Label("ENEMY BOARD"), enemyGrid);

        left.setAlignment(Pos.CENTER);
        right.setAlignment(Pos.CENTER);

        return new HBox(50, left, right);
    }

    // initalizing boards
    public void initializeBoards() {
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                playerBoard[r][c] = '~';
                enemyBoard[r][c] = '~';
            }
    }

    // ------------- dragging & dropping ships -------------

    //ships follow mouse
    public void dragShip(MouseEvent e) {
        shipPreview.setLayoutX(e.getSceneX() - 20);
        shipPreview.setLayoutY(e.getSceneY() - 20);
    }

    //places ship from mouse position
    public void dropShip(MouseEvent e) {

        if (!setupMode) return;

        int col = (int)((e.getSceneX() - playerGrid.localToScene(0,0).getX()) / 40); // fixes ship dragging placement
        int row = (int)((e.getSceneY() - playerGrid.localToScene(0,0).getY()) / 40); // fixes ship dragging placement

        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) return;

        if (canPlace(row, col)) {
            placeShip(row, col);
        } else {
            title.setText("Invalid placement!");
        }
    }

    // ship placement validation
    public boolean canPlace(int row, int col) {

        if (placingHorizontal) {
            if (col + shipLength > SIZE) return false;
            for (int i = 0; i < shipLength; i++)
                if (playerBoard[row][col + i] != '~') return false;
        } else {
            if (row + shipLength > SIZE) return false;
            for (int i = 0; i < shipLength; i++)
                if (playerBoard[row + i][col] != '~') return false;
        }

        return true;
    }

    // placing ship
    public void placeShip(int row, int col) {

        for (int i = 0; i < shipLength; i++) {

            if (placingHorizontal) {
                playerBoard[row][col + i] = 'S';
                playerButtons[row][col + i].setStyle("-fx-background-color: green;");
            } else {
                playerBoard[row + i][col] = 'S';
                playerButtons[row + i][col].setStyle("-fx-background-color: green;");
            }
        }

        shipsPlaced++;

        if (shipsPlaced >= 2) {
            setupMode = false;
            title.setText("Start attacking!");
            shipPreview.setVisible(false);
            enemyGrid.setDisable(false);
        } else {
            title.setText("Ships placed: " + shipsPlaced + "/2");
        }
    }

    // ------------- the game -------------

    // player attacking
    public void attackEnemy(int row, int col) {

        if (setupMode || !playerTurn) return;

        if (enemyBoard[row][col] == 'X' || enemyBoard[row][col] == 'O') return;

        if (enemyBoard[row][col] == 'S') {
            enemyBoard[row][col] = 'X';
            enemyButtons[row][col].setText("X");
            enemyButtons[row][col].setStyle("-fx-background-color: red;");
        } else {
            enemyBoard[row][col] = 'O';
            enemyButtons[row][col].setText("O");
            enemyButtons[row][col].setStyle("-fx-background-color: lightblue;");
        }

        if (checkWin(enemyBoard)) {
            showWin("You win!");
            return;
        }

        playerTurn = false;
        enemyAttack();
    }

    //enemy attacking
    public void enemyAttack() {

        int r, c;

        do {
            r = rand.nextInt(SIZE);
            c = rand.nextInt(SIZE);
        } while (playerBoard[r][c] == 'X' || playerBoard[r][c] == 'O');

        if (playerBoard[r][c] == 'S') {
            playerBoard[r][c] = 'X';
            playerButtons[r][c].setText("X");
            playerButtons[r][c].setStyle("-fx-background-color: red;");
        } else {
            playerBoard[r][c] = 'O';
            playerButtons[r][c].setText("O");
            playerButtons[r][c].setStyle("-fx-background-color: lightblue;");
        }

        if (checkWin(playerBoard)) {
            showWin("Enemy wins!");
            return;
        }

        playerTurn = true;
    }

    // ------------- enemy -------------

    public void placeEnemyShips() {

        int shipsToPlace = 2;
        int shipLength = 2;

        int placed = 0;

        while (placed < shipsToPlace) {

            boolean horizontal = rand.nextBoolean();
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);

            if (!canPlaceEnemyShip(row, col, horizontal, shipLength)) continue;

            for (int i = 0; i < shipLength; i++) {

                if (horizontal) {
                    enemyBoard[row][col + i] = 'S';
                    //enemyButtons[row][col + i].setStyle("-fx-background-color: gray;"); - debugging purpose (shows enemy ship)
                } else {
                    enemyBoard[row + i][col] = 'S';
                    //enemyButtons[row + i][col].setStyle("-fx-background-color: gray;"); - debugging purpose (shows enemy ship)
                }
            }

            placed++;
        }
    }

    public boolean canPlaceEnemyShip(int row, int col, boolean horizontal, int length) {
        if (horizontal) {
            if (col + length > SIZE) return false;
            for (int i = 0; i < length; i++)
                if (enemyBoard[row][col + i] != '~') return false;
        } else {
            if (row + length > SIZE) return false;
            for (int i = 0; i < length; i++)
                if (enemyBoard[row + i][col] != '~') return false;
        }
        return true;
    }

    // ------------- checking win/showing win -------------

    public boolean checkWin(char[][] board) {

        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                if (board[r][c] == 'S') return false;

        return true;
    }

    // shows win screen
    public void showWin(String msg) {
        title.setText(msg);

        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                playerButtons[r][c].setDisable(true);
                enemyButtons[r][c].setDisable(true);
            }
    }

    // ------------- resetting the game -------------

    public void restartGame() {

        setupMode = true;
        playerTurn = true;
        shipsPlaced = 0;

        playerBoard = new char[SIZE][SIZE];
        enemyBoard = new char[SIZE][SIZE];

        initializeBoards();

        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++) {
                playerButtons[r][c].setText("");
                playerButtons[r][c].setStyle("");
                playerButtons[r][c].setDisable(false);

                enemyButtons[r][c].setText("");
                enemyButtons[r][c].setStyle("");
                enemyButtons[r][c].setDisable(false);
            }

        shipPreview.setVisible(true);
        title.setText("Drag ship onto your board");
    }
}
