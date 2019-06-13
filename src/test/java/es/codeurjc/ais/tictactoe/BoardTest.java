package es.codeurjc.ais.tictactoe;

import org.junit.runners.Parameterized.Parameter;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

import es.codeurjc.ais.tictactoe.Board;

public class BoardTest {

	@Parameter(0)
	public String label;
	@Parameter(1)
	public String[] boardCells;
	@Parameter(2)
	public List<Integer> expectedWinPos;
	@Parameter(3)
	public boolean expectedCheckDraw;

	private void fillBoard(Board board, String[] cells) {
		for (int i = 0; i < cells.length; i++) {
			board.getCell(i).value = cells[i];
		}
	}

	public void givenACertainBoardWhenGetCellsIfWinnerThenTheWinnerPositionOrNullIsObtainedTest() {

		// Given
		Board board = new Board();
		fillBoard(board, boardCells);

		// When
		int[] winPos = board.getCellsIfWinner(label);
		
		// Then
		if (winPos != null)
			assertArrayEquals(expectedWinPos.stream().mapToInt(i -> i).toArray(), winPos);
		else
			assertEquals(expectedWinPos, winPos);

	}

	public void givenACertainBoardWhenCheckDrawThenCorrectBooleanIsObtainedTest() {

		// Given
		Board board = new Board();
		fillBoard(board, boardCells);

		// When
		boolean checkDraw = board.checkDraw();

		// Then
		assertEquals(expectedCheckDraw, checkDraw);
	}
}
