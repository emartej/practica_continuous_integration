package es.codeurjc.ais.tictactoe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BoardTestParamsWhenNoWinnerTest extends BoardTest {

	@Parameters(name = "{index}: No winner with checkDraw = {3}")
	public static Collection<Object[]> data() {
		Object[][] values = { 
				{ "X", new String[] { "X", "O", "X", "X", "O", "X", "O", "X", "O" }, null, true },
				{ "O", new String[] { "O", "X", "O", "O", "X", "O", "X", "O", "X" }, null, true } };

		return Arrays.asList(values);
	}

	@Test
	public void givenNoPlayerWinsWhenGetCellsIfWinnerThenWinnerPositionIsNullTest() {

		super.givenACertainBoardWhenGetCellsIfWinnerThenTheWinnerPositionOrNullIsObtainedTest();
	}
	
	@Test
	public void givenNoPlayerWinsWhenCheckDrawThenTrueIsObtainedTest() {

		super.givenACertainBoardWhenCheckDrawThenCorrectBooleanIsObtainedTest();
	}
}
