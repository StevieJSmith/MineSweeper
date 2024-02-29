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

	public Game() {

		random = new Random();
		int count = 0;
		boardSize = 12;
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
		textField.setForeground(Color.GREEN);
		textField.setText("Watch out sweeper, there are " + bombAmount + " Bombs");

		buttons = new JButton[boardSize][boardSize];
		for(int i = 0; i < buttons.length; i++){
			for(int j = 0; j < buttons.length; j++){
				buttons[i][j] = new JButton(); // current button
				buttonPanel.add(buttons[i][j]);
				buttons[i][j].setFont(new Font("Monospaced", Font.BOLD, 10));
				buttons[i][j].setText("");
				buttons[i][j].setBackground(new Color(50,50,50));
				buttons[i][j].setFocusable(false);
				buttons[i][j].addActionListener(this);
			}
		}

		textPanel.add(textField);
		gameFrame.add(textPanel, BorderLayout.NORTH); // assign 'textPanel' to the north of the Frame!
		gameFrame.add(buttonPanel);
		gameFrame.setSize(600,600);
		gameFrame.revalidate();
		gameFrame.setLocationRelativeTo(null); // centers the frame in the middle of the screen

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(int i = 0; i < buttons.length; i++){
			for(int j = 0; j < buttons.length; j++){
				if(e.getSource() == buttons[i][j]) {
					System.out.println("Clicked!!!");
				}
			}
		}
	}

}
