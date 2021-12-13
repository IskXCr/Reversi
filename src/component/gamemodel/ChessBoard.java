package component.gamemodel;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import controller.BlockStatus;
import controller.DebugConsole;
import controller.GameController;
import controller.output.OutputCategory;
import controller.output.OutputChannel;
import model.Chess;

public class ChessBoard extends GridPane {

    public StackPane grid[][];
    public int rowSize, colSize;
    public GameController controller;
    public int cellMinSize;

    /**
     * New Game Initialization
     *
     * @param <<code>controller</code> the specific GameController
     */
    public ChessBoard(GameController controller) {
        this.controller = controller;
        this.rowSize = controller.getRowSize();
        this.colSize = controller.getColSize();
        cellMinSize = 400 / this.rowSize;
        grid = new StackPane[rowSize][colSize];
        for (int row = 0; row < this.rowSize; row++) {
            grid[row] = new StackPane[this.colSize];
            for (int col = 0; col < this.colSize; col++) {

                final int tmpRow = row, tmpCol = col;

                //Initialize Grids
                grid[row][col] = new StackPane();
                grid[row][col].setMinHeight(cellMinSize);
                grid[row][col].setMinWidth(cellMinSize);

                this.add(grid[row][col], col, row);
            }
        }

        for (int row = 0; row < this.rowSize; row++) {
            this.getRowConstraints().add(new RowConstraints(0, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY,
                    Priority.ALWAYS, VPos.CENTER, true));
        }
        for (int col = 0; col < this.colSize; col++) {
            this.getColumnConstraints()
                    .add(new ColumnConstraints(0, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY,
                            Priority.ALWAYS, HPos.CENTER, true));
        }
        updateBoardColor(Color.hsb(15, 0.58, 0.27), Color.hsb(42, 0.36, 0.84));
        updateBoard();
        if (controller.getModifiability()) {
            bindToController();
        }
        ;
    }

    public void updateBoard() {
        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                //Remove the previous chess.
                if (grid[row][col].getChildren().size() > 1) {
                    grid[row][col].getChildren().remove(1);
                }

                BlockStatus currentPlayer = controller.getBlockInfo(row, col);

                //Dynamic Chess Size
                Chess chess = new Chess(15, Color.TRANSPARENT);
                grid[row][col].getChildren().add(chess);
                chess.radiusProperty().bind(Bindings.min(
                        grid[row][col].widthProperty().divide(2).multiply(0.618),
                        grid[row][col].heightProperty().divide(2).multiply(0.618)));

                if (currentPlayer == BlockStatus.WHITE_PLAYER) {
                    chess.setColor(Color.valueOf("WHITE"));
                } else if (currentPlayer == BlockStatus.BLACK_PLAYER) {
                    chess.setColor(Color.valueOf("BLACK"));
                }
            }

        }
        DebugConsole.writeToConsole(OutputChannel.STDOUT, OutputCategory.GUI_INFO, "Board Updated.");
    }

    // Below are color updaters

    public void updateGridColor(int rowIndex, int colIndex, Color color) {
        grid[rowIndex][colIndex].setBackground(new Background(new BackgroundFill(color, null, null)));
    }

    public void updateBoardColor(Color color1, Color color2) {
        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                this.grid[row][col].setBackground(
                        new Background(
                                new BackgroundFill((row + col) % 2 == 0 ? color1 : color2, null, null)));
            }
        }
        DebugConsole.writeToConsole(OutputChannel.STDOUT, OutputCategory.GUI_INFO, "Board Color Updated.");
    }

    private void bindToController() {
        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                final int tmpRow = row, tmpCol = col;
                grid[row][col].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent arg0) {
                        controller.onGridClick(tmpRow, tmpCol);
                        updateBoard();
                    }
                });
            }
        }
    }
}