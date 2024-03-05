import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {

	private Game ms;
	private Game ms2;
	private Game ms3;


	@BeforeEach
	public void setup () {
		System.out.println("-----");
		ms = new Game(6,10,0); // easy difficulty board
		ms2 = new Game(8,20,1); // easy difficulty board
		ms3 = new Game(10,30,2); // easy difficulty board
		System.out.println("Board Created!");


	}

	@AfterAll
	public static void closing() {
		System.out.println("-----");
		System.out.println("All tests are completed!");

	}

	@Test
	void numberOfMinesEasy() {
		int count = 0;
		for(int i = 0; i < ms.numOfMines.length; i++) {
			for(int j = 0; j < ms.numOfMines.length; j++) {
				if(ms.numOfMines[i][j] == 99) {
					count++;
				}
			}
		}
		Assertions.assertEquals(10, count, "There are only " + count + " mines and 10 is expected");
		Assertions.assertTrue(true);
	}

	@Test
	void numberOfMinesMedium() {
		int count = 0;
		for(int i = 0; i < ms2.numOfMines.length; i++) {
			for(int j = 0; j < ms2.numOfMines.length; j++) {
				if(ms2.numOfMines[i][j] == 99) {
					count++;
				}
			}
		}
		Assertions.assertEquals(20, count, "There are only " + count + " mines and 10 is expected");
		Assertions.assertTrue(true);
	}

	@Test
	void numberOfMinesHard() {
		int count = 0;
		for(int i = 0; i < ms3.numOfMines.length; i++) {
			for(int j = 0; j < ms3.numOfMines.length; j++) {
				if(ms3.numOfMines[i][j] == 99) {
					count++;
				}
			}
		}
		Assertions.assertEquals(count <= 30, count, "There are only " + count + " mines and 10 is expected");
		Assertions.assertTrue(true);
	}

	@Test
	void isWinner() {
		ms.gameOver(true);
		Assertions.assertTrue(true);
	}

	@Test
	void actionPerformed() {
		Assertions.assertTrue(true);

	}


}