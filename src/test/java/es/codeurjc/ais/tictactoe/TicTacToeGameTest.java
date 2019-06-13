package es.codeurjc.ais.tictactoe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;

import es.codeurjc.ais.tictactoe.TicTacToeGame;
import es.codeurjc.ais.tictactoe.TicTacToeGame.CellMarkedValue;
import es.codeurjc.ais.tictactoe.TicTacToeGame.EventType;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerValue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.reset;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TicTacToeGameTest {

	@Parameters(name = "{index}: Player with label {4} wins with winnerPos {5}")
	public static Collection<Object[]> data() {
		Object[][] values = { 
				{ "X", "O", new int[] { 8, 6, 3, 2, 1, 4 }, true, "O", Arrays.asList(6, 4, 2) },
				{ "X", "O", new int[] { 1, 3, 2, 4, 5, 7, 6, 8, 0 }, true, "X", Arrays.asList(0, 1, 2) },
				{ "X", "O", new int[] { 3, 4, 5, 6, 7, 8, 0, 1, 2 }, false, "none", Arrays.asList() }
				};
		return Arrays.asList(values);
	}

	@Parameter(0)
	public String labelJ1;
	@Parameter(1)
	public String labelJ2;
	@Parameter(2)
	public int[] playersMovements;
	@Parameter(3)
	public boolean thereIsAnyWinner;
	@Parameter(4)
	public String labelWinner;
	@Parameter(5)
	public List<Integer> winnerPos;

	@Test
	public void ticTacToeGameTest() {

		// Given
		TicTacToeGame game = new TicTacToeGame();

		Connection conn1 = mock(Connection.class);
		Connection conn2 = mock(Connection.class);

		game.addConnection(conn1);
		game.addConnection(conn2);

		Player player1 = new Player(0, labelJ1, "J_label"+labelJ1);
		Player player2 = new Player(1, labelJ2, "J_label"+labelJ2);

		// When
		game.addPlayer(player1);

		// Then
		verify(conn1).sendEvent(Matchers.eq(EventType.JOIN_GAME), argThat(hasItems(player1)));
		verify(conn2).sendEvent(Matchers.eq(EventType.JOIN_GAME), argThat(hasItems(player1)));

		reset(conn1);
		reset(conn2);

		// When
		game.addPlayer(player2);

		// Then
		verify(conn1).sendEvent(Matchers.eq(EventType.JOIN_GAME), argThat(hasItems(player2)));
		verify(conn2).sendEvent(Matchers.eq(EventType.JOIN_GAME), argThat(hasItems(player2)));

		verify(conn1).sendEvent(Matchers.eq(EventType.SET_TURN), Matchers.eq(game.getPlayers().get(player1.getId())));
		verify(conn2).sendEvent(Matchers.eq(EventType.SET_TURN), Matchers.eq(game.getPlayers().get(player1.getId())));

		Player nextPlayer = null;
		CellMarkedValue value = new CellMarkedValue();
		value.cellId = 99;

		int indexPlayersMovements = 0;

		for (indexPlayersMovements = 0; indexPlayersMovements < (playersMovements.length
				- 1); indexPlayersMovements++) {

			value.cellId = playersMovements[indexPlayersMovements];

			if (game.checkTurn(player1.getId())) {
				value.player = player1;
				nextPlayer = player2;
			} else if (game.checkTurn(player2.getId())) {
				value.player = player2;
				nextPlayer = player1;
			}

			// When
			game.mark(value.cellId);

			// Then
			ArgumentCaptor<CellMarkedValue> argumentCellMarkedValue = ArgumentCaptor.forClass(CellMarkedValue.class);
			CellMarkedValue captured_value = null;

			verify(conn1).sendEvent(Matchers.eq(EventType.MARK), argumentCellMarkedValue.capture());
			captured_value = argumentCellMarkedValue.getValue();
			assertEquals(value.cellId, captured_value.cellId);
			assertEquals(value.player, captured_value.player);

			verify(conn1).sendEvent(Matchers.eq(EventType.SET_TURN),
					Matchers.eq(game.getPlayers().get(nextPlayer.getId())));

			verify(conn2).sendEvent(Matchers.eq(EventType.MARK), argumentCellMarkedValue.capture());
			captured_value = argumentCellMarkedValue.getValue();
			assertEquals(value.cellId, captured_value.cellId);
			assertEquals(value.player, captured_value.player);

			verify(conn2).sendEvent(Matchers.eq(EventType.SET_TURN),
					Matchers.eq(game.getPlayers().get(nextPlayer.getId())));

			reset(conn1);
			reset(conn2);
		}

		value.player = nextPlayer;
		value.cellId = playersMovements[indexPlayersMovements];

		// When
		game.mark(value.cellId);

		// Then

		if (thereIsAnyWinner) {
			ArgumentCaptor<WinnerValue> argumentWinnerValue = ArgumentCaptor.forClass(WinnerValue.class);
			WinnerValue captured_winner = null;

			verify(conn1).sendEvent(Matchers.eq(EventType.GAME_OVER), argumentWinnerValue.capture());
			captured_winner = argumentWinnerValue.getValue();
			assertEquals(nextPlayer, captured_winner.player);
			assertArrayEquals(winnerPos.stream().mapToInt(i -> i).toArray(), captured_winner.pos);
			assertEquals(labelWinner, captured_winner.player.getLabel());

			verify(conn2).sendEvent(Matchers.eq(EventType.GAME_OVER), argumentWinnerValue.capture());
			captured_winner = argumentWinnerValue.getValue();
			assertEquals(nextPlayer, captured_winner.player);
			assertArrayEquals(winnerPos.stream().mapToInt(i -> i).toArray(), captured_winner.pos);
		}
		else
		{
			verify(conn1).sendEvent(Matchers.eq(EventType.GAME_OVER), Matchers.eq(null));
			verify(conn2).sendEvent(Matchers.eq(EventType.GAME_OVER), Matchers.eq(null));
		}
	}
}
