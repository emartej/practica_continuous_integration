package es.codeurjc.ais.tictactoe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BoardTestParamWhenWinnerTest extends BoardTest {

	@Parameters(name = "{index}: Player {0} wins at {2} with checkDraw = {3}")
	public static Collection<Object[]> data() {
		Object[][] values = { { "X", new String[] { "X", "X", "X", "O", "O" }, Arrays.asList(0, 1, 2), false },
				{ "X", new String[] { "X", null, "O", "X", "O", null, "X" }, Arrays.asList(0, 3, 6), false },
				{ "O", new String[] { "X", "O", "X", "O", "O", "O", "X" }, Arrays.asList(3, 4, 5), false },
				{ "O", new String[] { "X", "O", "O", null, "O", "X", "X", "O" }, Arrays.asList(1, 4, 7), false },
				{ "X", new String[] { null, null, "O", null, "O", null, "X", "X", "X" }, Arrays.asList(6, 7, 8), false },
				{ "O", new String[] { "O", "X", "O", null, "O", null, "X", "X", "O" }, Arrays.asList(0, 4, 8), false },
				{ "X", new String[] { null, null, "X", null, "O", "X", "O", "O", "X" }, Arrays.asList(2, 5, 8), false },
				{ "O", new String[] { "X", "X", "O", null, "O", null, "O", "X", "O" }, Arrays.asList(6, 4, 2), false },
				{ "X", new String[] { "X", "X", "X", "O", "O", "X", "X", "O", "O" }, Arrays.asList(0, 1, 2), true },
				{ "X", new String[] { "X", "X", "O", "X", "O", "O", "X", "O", "X" }, Arrays.asList(0, 3, 6), true },
				{ "O", new String[] { "X", "O", "X", "O", "O", "O", "X", "X", "O" }, Arrays.asList(3, 4, 5), true },
				{ "O", new String[] { "X", "O", "O", "O", "O", "X", "X", "O", "X" }, Arrays.asList(1, 4, 7), true },
				{ "X", new String[] { "X", "O", "O", "O", "O", "X", "X", "X", "X" }, Arrays.asList(6, 7, 8), true },
				{ "O", new String[] { "O", "X", "X", "X", "O", "O", "X", "X", "O" }, Arrays.asList(0, 4, 8), true },
				{ "X", new String[] { "O", "X", "X", "O", "O", "X", "X", "O", "X" }, Arrays.asList(2, 5, 8), true },
				{ "O", new String[] { "X", "X", "O", "X", "O", "X", "O", "X", "O" }, Arrays.asList(6, 4, 2), true }, };

		return Arrays.asList(values);
	}

	@Test
	public void givenAPlayerWinsWhenGetCellsIfWinnerThenWinnerPositionIsObtainedTest() {

		super.givenACertainBoardWhenGetCellsIfWinnerThenTheWinnerPositionOrNullIsObtainedTest();
	}

	@Test
	public void givenAPlayerWinsWhenCheckDrawThenFalseIsObtainedTest() {

		super.givenACertainBoardWhenCheckDrawThenCorrectBooleanIsObtainedTest();
	}
}
