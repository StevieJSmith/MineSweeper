import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class Game implements ActionListener {

	private int boardSize; // size of the game board
	private int bombAmount; // amount of bombs on board
	private ArrayList<Point> bombCoordinates; // bomb's x & y positions
	private Random random;
	private JFrame gameFrame;
	private JPanel textPanel;
	private JPanel buttonPanel;
	private JButton[][] buttons; // Multidimensional Array of buttons
	private JLabel textField;
	int[][] numOfMines; // store the number of mines around a position

	public Game() {

		random = new Random();
		int count = 0;
		boardSize = 9;
		bombAmount = 20;
		bombCoordinates = new ArrayList<>();

		// bomb position generation (loop till the bomb amount is reached)
		while(bombCoordinates.size() < bombAmount) {
			int x = random.nextInt(boardSize);
			int y = random.nextInt(boardSize);
			Point newCoordinate = new Point(x,y); // new bomb position

			for(Point currentCoordinate: bombCoordinates) { // loop through all current positions
				if(newCoordinate.equals(currentCoordinate)) { // if the new bomb overlaps another bomb
					count++;
				}
			}
			if(count == 0) { // if no overlaps were found, add the new bomb position
				bombCoordinates.add(newCoordinate);
			}
		}

		gameFrame = new JFrame(); // instance of JFrame
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setVisible(true); // display frame when instance of game is called
		gameFrame.setLayout(new BorderLayout());

		System.out.println(bombCoordinates);

		textPanel = new JPanel(); // instance of JPanel
		textPanel.setVisible(true);
		textPanel.setBackground(new Color(50,50,50));

		buttonPanel = new JPanel();
		buttonPanel.setVisible(true);
		buttonPanel.setLayout(new GridLayout(boardSize,boardSize)); // ? x ? in a grid design

		textField = new JLabel();
		textField.setAlignmentX(JLabel.CENTER);
		textField.setAlignmentY(JLabel.CENTER);
		textField.setFont(new Font("Dialog", Font.BOLD, 20));
		textField.setForeground(new Color(255,165,0));
		textField.setText("Watch out sweeper, there are " + bombAmount + " Bombs!!!");

		numOfMines = new int[boardSize][boardSize];
		buttons = new JButton[boardSize][boardSize];

		for(int i = 0; i < buttons.length; i++){
			for(int j = 0; j < buttons.length; j++){
				buttons[i][j] = new JButton(); // current button
				buttonPanel.add(buttons[i][j]);
				buttons[i][j].setFont(new Font("Monospaced", Font.BOLD, 10));
				buttons[i][j].setText("");
				buttons[i][j].setBackground(new Color(50,50,50));
				buttons[i][j].setForeground(new Color(180, 180,180));
				buttons[i][j].setFocusable(false);
				buttons[i][j].addActionListener(this);
			}
		}

		textPanel.add(textField);
		gameFrame.add(textPanel, BorderLayout.NORTH); // assign 'textPanel' to the north of the Frame!
		gameFrame.add(buttonPanel);
		gameFrame.setSize(850,850);
		gameFrame.revalidate();
		gameFrame.setLocationRelativeTo(null); // centers the frame in the middle of the screen
		gameFrame.setVisible(true);

		getMines();



	}


	public void getMines() {
		for(int i = 0; i < numOfMines.length; i++){ // current coordinates (x,y)
			for(int j = 0; j < numOfMines.length; j++){

				boolean changed = false;
				int mineTotal = 0;

				for (Point bombCoordinate : bombCoordinates) { // loop through all mine positions

					if (j == bombCoordinate.x && i == bombCoordinate.y) { //

						numOfMines[i][j] = 99; // sets mines to 99
						changed = true;
						break;
					}
				}

				if(!changed) { // if it was not a mine, calculate how many mines are around

					for (Point bombCoordinate : bombCoordinates) { // positions around the location
						if ((j + 1 == bombCoordinate.x && i + 1 == bombCoordinate.y) // South East
								|| (j - 1 == bombCoordinate.x && i - 1 == bombCoordinate.y) // North West
								|| (j + 1 == bombCoordinate.x && i == bombCoordinate.y)  // etc ...
								|| (j == bombCoordinate.x && i + 1 == bombCoordinate.y)
								|| (j - 1 == bombCoordinate.x && i == bombCoordinate.y)
								|| (j == bombCoordinate.x && i - 1 == bombCoordinate.y)
								|| (j + 1 == bombCoordinate.x && i - 1 == bombCoordinate.y)
								|| (j - 1 == bombCoordinate.x && i + 1 == bombCoordinate.y)) {
							mineTotal++;
						}
						numOfMines[i][j] = mineTotal;
					}
				}
			}
		}
		for (int[] numOfMine : numOfMines) {
			for (int b = 0; b < numOfMines.length; b++) {
				System.out.print(numOfMine[b] + " ");
			}
			System.out.println();
		}
	}

	public void check(int x, int y) { // displays what is at the position clicked on

		boolean isGameOver = false;

		if(numOfMines[x][y] == 99) { // bomb is clicked ... oops :(
			buttons[x][y].setText("BANG!!!");
			gameOver(false);
			isGameOver = true;
		}

		if(!isGameOver) { // if game is not over
			buttons[x][y].setText(String.valueOf(numOfMines[x][y]));

			isWinner();

		}
	}

	public void gameOver(boolean winner) {

		if(winner) {
			textField.setForeground(Color.GREEN);
			textField.setText("You Won!");
		} else {
			textField.setForeground(Color.RED);
			textField.setText("Game Over!");
		}

		for(int i = 0; i < buttons.length; i++) {
			for(int j = 0; j < buttons.length; j++) { // loop through and disable the board
				buttons[i][j].setEnabled(false);

			}
		}
	}

	public void isWinner() {
		int spaces = 0;

		for(int i = 0; i < buttons.length; i++) {
			for(int j = 0; j < buttons.length; j++) { // loop through and disable the board
				if(buttons[i][j].getText().isEmpty()) {
					spaces++; // increases for all remaining buttons
				}

			}
		}
		if(spaces == bombAmount) { // if the remaining spaces count equal the bomb count then the player wins
			gameOver(true);
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		for(int i = 0; i < buttons.length; i++){
			for(int j = 0; j < buttons.length; j++){
				if(e.getSource() == buttons[i][j]) {
					check(i,j);
				}
			}
		}
	}

}
