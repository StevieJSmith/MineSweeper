import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class Game implements ActionListener {

	private int boardSize; // size of the game board
	ArrayList<Point> mineCoordinates; // bomb's x & y positions
	Point zeroCoordinate;
	private boolean isZero;

	private Random random;

	private final JFrame gameFrame;
	private JPanel textPanel;
	private JPanel dropdownPanel;
	private JPanel buttonPanel;
	 JButton[][] buttons; // Multidimensional Array of buttons
	private JLabel textField;
	private JLabel dropdownText;
	int[][] numOfMines; // store the number of mines around a position
	String[] choices = { "Easy", "Medium", "Hard" }; // difficulty settings
	private JComboBox<String> dropdownBox;

	public Game(int boardSize, int mineAmount, int difficulty) {

		random = new Random();
		int count = 0;
		int mines = 0;
		isZero = false;
		mineCoordinates = new ArrayList<>();
		zeroCoordinate = new Point();

		// bomb position generation (loop till the bomb amount is reached)
		while(mineCoordinates.size() < mineAmount) {
			int x = random.nextInt(boardSize);
			int y = random.nextInt(boardSize);

			Point newCoordinate = new Point(x,y); // new bomb position

			for(Point currentCoordinate: mineCoordinates) { // loop through all current positions
				if(newCoordinate.equals(currentCoordinate)) { // if the new bomb overlaps another bomb
					count++;
				}
			}
			if(count == 0) { // if no overlaps were found, add the new bomb position
				mineCoordinates.add(newCoordinate);
				mines++;
			}
			if(count == 10000) {
				break;
			}
		}

		gameFrame = new JFrame(); // instance of JFrame
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setVisible(true); // display frame when instance of game is called
		gameFrame.setLayout(new BorderLayout());

		textPanel = new JPanel(); // instance of JPanel
		textPanel.setVisible(true);
		textPanel.setBackground(new Color(50,50,50));

		buttonPanel = new JPanel();
		buttonPanel.setVisible(true);
		buttonPanel.setLayout(new GridLayout(boardSize,boardSize)); // ? x ? in a grid design

		textField = new JLabel();
		textField.setFont(new Font("Dialog", Font.BOLD, 26));
		textField.setForeground(new Color(255,165,0));
		textField.setText("Watch out sweeper, there are " + mines + " Bombs!!!");

		dropdownPanel = new JPanel();
		dropdownPanel.setVisible(true);
		dropdownPanel.setBackground(new Color(50,50,50));

		dropdownText = new JLabel("Select a difficulty:");;
		dropdownText.setFont(new Font("Dialog", Font.BOLD, 16));
		dropdownText.setForeground(new Color(255,165,0));


		dropdownBox = new JComboBox<>(choices);
		dropdownBox.setMaximumSize(dropdownBox.getPreferredSize());
		dropdownBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		dropdownBox.setSelectedIndex(difficulty);
		dropdownBox.addActionListener(this);


		numOfMines = new int[boardSize][boardSize]; // values for each space (99 = mine)
		buttons = new JButton[boardSize][boardSize]; // all possible spaces

		for(int i = 0; i < buttons.length; i++){ // create buttons
			for(int j = 0; j < buttons.length; j++){
				buttons[i][j] = new JButton(); // current button
				buttonPanel.add(buttons[i][j]);
				buttons[i][j].setFont(new Font("Monospaced", Font.BOLD, 16));
				buttons[i][j].setText("");
				buttons[i][j].setBackground(new Color(50,50,50));
				buttons[i][j].setForeground(new Color(180, 180,180));
				buttons[i][j].setFocusable(false);
				buttons[i][j].addActionListener(this);

				int finalI = i;
				int finalJ = j;
				buttons[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if(e.getButton() == 3) {
							if(!buttons[finalI][finalJ].getText().equals("O")) { // if the space has not been flagged
								buttons[finalI][finalJ].setForeground(new Color(180, 180,180));
								buttons[finalI][finalJ].setText("O");
							}else if(buttons[finalI][finalJ].getText().equals("O") && numOfMines[finalI][finalJ] == 99) { // if the space is flagged already and is a mine
								buttons[finalI][finalJ].setForeground(new Color(50,50,50));
								buttons[finalI][finalJ].setText("X");
							} else {
								buttons[finalI][finalJ].setText("");
							}
						}
					}
				});
			}
		}

		textPanel.add(textField);
		gameFrame.add(textPanel, BorderLayout.NORTH); // assign 'textPanel' to the north of the Frame!
		dropdownPanel.add(dropdownText);
		dropdownPanel.add(dropdownBox);
		gameFrame.add(dropdownPanel, BorderLayout.SOUTH);
		gameFrame.add(buttonPanel);
		gameFrame.setSize(550,550);
		gameFrame.revalidate();
		gameFrame.setLocationRelativeTo(null); // centers the frame in the middle of the screen

		getMines();



	}

	public void getMines() {
		for(int i = 0; i < numOfMines.length; i++){ // current coordinates (x,y)
			for(int j = 0; j < numOfMines.length; j++){

				boolean changed = false;
				int mineTotal = 0;

				for (Point mineCoordinate : mineCoordinates) { // loop through all mine positions

					if (j == mineCoordinate.x && i == mineCoordinate.y) { //
						buttons[i][j].setText("X");
						buttons[i][j].setForeground(new Color(50,50,50));
						numOfMines[i][j] = 99; // sets mines to 99
						changed = true;
						break;
					}
				}

				if(!changed) { // if it was not a mine, calculate how many mines are around



					for (Point mineCoordinate : mineCoordinates) { // positions around the location
						for(int x = -1; x < 2; x++) {
							for(int y = -1; y < 2; y++) {
								if(x == 0 && y == 0) {
									continue;
								}
								if(j + x == mineCoordinate.x && i + y == mineCoordinate.y ) {
									mineTotal++;

								}
							}
						}
						numOfMines[i][j] = mineTotal; // give each space a value
					}
				}
			}
		}
		for (int[] numOfMine : numOfMines) { // Prints out board with mine positions
			for (int b = 0; b < numOfMines.length; b++) {
				System.out.print(numOfMine[b] + "     ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void check(int x, int y) { // displays what is at the position clicked on

		boolean isGameOver = false;

		if(!buttons[x][y].getText().equals("O") && numOfMines[x][y] == 99) { // bomb is clicked ... oops :(
			buttons[x][y].setText("X"); // if a mine is flagged and clicked the text will convert from a flag to a mine!
			gameOver(false);
			isGameOver = true;
		}

		if(!isGameOver) { // if game is not over

			if(numOfMines[x][y] == 0) { // if zero spread until bombs are discovered
				zeroCoordinate.setLocation(x,y);

				isZero = true;
				display(zeroCoordinate);

			}

			if(!buttons[x][y].getText().equals("O")) { // if not tagged and does not equal a mine or 0
				displayNumbers(x,y);
			}

			isWinner(); // check if you have won

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
		if(spaces == 0) { // if the remaining spaces count equal the bomb count then the player wins
			gameOver(true);
		}
	}

	public void displayNumbers(int x, int y) { // after clicking a non-mine space, the specific number will appear depending on bomb location
		if(numOfMines[x][y] != 99) {
			buttons[x][y].setForeground(new Color(255, 165, 0));
			buttons[x][y].setBackground((new Color(70, 70, 70)));
			buttons[x][y].setText(String.valueOf(numOfMines[x][y]));
			buttons[x][y].setEnabled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Load chosen difficulty depending on dropdown box choice
		if(e.getSource() == dropdownBox) {
			if(dropdownBox.getSelectedItem() == "Easy") {
				gameFrame.dispose();
				new Game(6, 10, 0);
				dropdownBox.setSelectedIndex(0);
			} else if (dropdownBox.getSelectedItem() == "Medium") {
				gameFrame.dispose();
				new Game(8, 20, 1);
				dropdownBox.setSelectedIndex(1);

			} else {
				gameFrame.dispose();
				new Game(10, 30, 2);
				dropdownBox.setSelectedIndex(2);

			}
		}else {
			// Display position clicked on
			for (int i = 0; i < buttons.length; i++) {
				for (int j = 0; j < buttons.length; j++) {
					if (e.getSource() == buttons[i][j]) {
						check(i, j);
					}
				}
			}
		}




	}

	public void display(Point zeroCoordinate) {

		if(isZero) { // if the position = 0 (true)
			for(int x = -1;  x < 2; x++) {
				for (int y = -1; y < 2; y++) { // check if space is in bounds first
					if ((zeroCoordinate.x + x) >= 0 && (zeroCoordinate.y + y) >= 0 &&
							(zeroCoordinate.x + x) < boardSize && (zeroCoordinate.y + y < boardSize)) {
						System.out.println(zeroCoordinate.x + x);
						displayNumbers(zeroCoordinate.x + x, zeroCoordinate.y + y);
					}
				}
			}
		}

		for(int i = 0; i < buttons.length; i++) { // loop through all spaces
			for(int j = 0; j < buttons.length; j++) {

				if(buttons[i][j].getText().equals("0")) { // check if the space has a zero
					if(i-1 >= 0) { // check to see in scope
						displayNumbers(i-1,j);
					}

					if(i+1 < boardSize) {
						displayNumbers(i+1,j);
					}

					if(j-1 >= 0) {
						displayNumbers(i,j-1);
					}

					if(j+1 < boardSize) {
						displayNumbers(i,j+1);
					}

					if(i-1 >= 0 && j-1 >= 0) {
						displayNumbers(i-1,j-1);
					}

					if(i+1 < boardSize && j+1 < boardSize) {
						displayNumbers(i+1,j+1);
					}

					if(i+1 < boardSize && j-1 >= 0) {
						displayNumbers(i+1,j-1);
					}

					if(i-1 >= 0 && j+1 < boardSize) {
						displayNumbers(i-1,j+1);
					}
				}
			}
		}
	}


}
