import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Game {

	private int boardSize; // board size
	private int bombAmount; // amount of bombs on board
	private ArrayList<Point> bombCoordinates = new ArrayList<>(); // bomb's x & y positions
	private Random random;
	private JFrame gameFrame;
	private JPanel textPanel;
	private JPanel buttonPanel;
	private JButton[][] buttons; // Multidimensional Array of buttons
	private JLabel textField;

	public Game() {

		random = new Random();
		int count = 0;

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

	}


}
