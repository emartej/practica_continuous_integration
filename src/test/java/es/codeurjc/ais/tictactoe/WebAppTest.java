package es.codeurjc.ais.tictactoe;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import es.codeurjc.ais.tictactoe.WebApp;
import io.github.bonigarcia.wdm.WebDriverManager;

@RunWith(Parameterized.class)
public class WebAppTest {

	@Parameters(name = "{index}: {3}")
	public static Collection<Object[]> data() {
		Object[][] values = { 
				{ "J1", "J2", new int[] { 8, 6, 3, 2, 1, 4 }, "J2 wins! J1 looses." },
				{ "J1", "J2", new int[] { 1, 3, 2, 4, 5, 7, 6, 8, 0 }, "J1 wins! J2 looses." },
				{ "J1", "J2", new int[] { 3, 4, 5, 6, 7, 8, 0, 1, 2 }, "Draw!" } };
		return Arrays.asList(values);
	}

	@Parameter(0)
	public String nameJ1;
	@Parameter(1)
	public String nameJ2;
	@Parameter(2)
	public int[] playersMovements;
	@Parameter(3)
	public String expectedFinalMessage;

	private WebDriver driver1;
	private WebDriver driver2;

	@BeforeClass
	public static void setupClass() {
		WebDriverManager.chromedriver().setup();
		WebApp.start();
	}

	@AfterClass
	public static void teardownClass() {
		WebApp.stop();
	}

	@Before
	public void setupTest() {
		driver1 = new ChromeDriver();
		driver2 = new ChromeDriver();
	}

	@After
	public void teardown() {
		if (driver1 != null) {
			driver1.quit();
		}
		if (driver2 != null) {
			driver2.quit();
		}
	}

	@Test
	public void test() throws InterruptedException {

		// Given
		driver1.get("http://localhost:8089/");
		driver2.get("http://localhost:8089/");

		driver1.findElement(By.id("nickname")).sendKeys(nameJ1);
		driver1.findElement(By.id("startBtn")).click();

		driver2.findElement(By.id("nickname")).sendKeys(nameJ2);
		driver2.findElement(By.id("startBtn")).click();
		Thread.sleep(3000);

		// When
		int indexPlayersMovements = 0;
		do {
			if (indexPlayersMovements % 2 == 0) {
				driver1.findElement(By.id("cell-" + playersMovements[indexPlayersMovements])).click();
				indexPlayersMovements++;
			} else {
				driver2.findElement(By.id("cell-" + playersMovements[indexPlayersMovements])).click();
				indexPlayersMovements++;
			}

		} while (indexPlayersMovements < (playersMovements.length));

		Thread.sleep(3000);
		
		//Then
		String finalMessageReceivedDriver1 = driver1.switchTo().alert().getText();
		String finalMessageReceivedDriver2 = driver2.switchTo().alert().getText();

		assertEquals(expectedFinalMessage, finalMessageReceivedDriver1);
		assertEquals(expectedFinalMessage, finalMessageReceivedDriver2);
	}

}
