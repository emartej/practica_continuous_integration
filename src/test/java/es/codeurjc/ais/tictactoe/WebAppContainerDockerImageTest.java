package es.codeurjc.ais.tictactoe;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode;


import es.codeurjc.ais.tictactoe.WebApp;
import io.github.bonigarcia.wdm.WebDriverManager;

@RunWith(Parameterized.class)
public class WebAppContainerDockerImageTest {

	@Parameters(name = "{index}: {3}")
	public static Collection<Object[]> data() {
		Object[][] values = { 
				{ "J1", "J2", new int[] { 8, 6, 3, 2, 1, 4 }, "J2 wins! J1 looses." },
				{ "J1", "J2", new int[] { 1, 3, 2, 4, 5, 7, 6, 8, 0 }, "J1 wins! J2 looses." },
				{ "J1", "J2", new int[] { 3, 4, 5, 6, 7, 8, 0, 1, 2 }, "Draw!" } 
	};
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

	private RemoteWebDriver driver1;
	private RemoteWebDriver driver2;

	@Rule
	public BrowserWebDriverContainer chrome1 = new BrowserWebDriverContainer()
	.withDesiredCapabilities(DesiredCapabilities.chrome())
	.withRecordingMode(VncRecordingMode.RECORD_ALL, new File("target"));

	@Rule
	public BrowserWebDriverContainer chrome2 = new BrowserWebDriverContainer()
	.withDesiredCapabilities(DesiredCapabilities.chrome())
	.withRecordingMode(VncRecordingMode.RECORD_ALL, new File("target"));

	@BeforeClass
	public static void setupClass() {
		//WebApp.start();
		Testcontainers.exposeHostPorts(8089);
		try {
			BufferedReader bufferedReader;
			bufferedReader = new BufferedReader(new FileReader ("container-ip.txt"));
			String linea = bufferedReader.readLine();
			bufferedReader.close();
			System.setProperty("hostIP", linea);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@AfterClass
	public static void teardownClass() {
		//WebApp.stop();
	}

	@Before
	public void setupTest() throws InterruptedException {
		Thread.sleep(15000);
		driver1 = chrome1.getWebDriver();
		Thread.sleep(5000);
		driver2 = chrome2.getWebDriver();
		driver1.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver2.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@After
	public void teardown() throws InterruptedException {
		if (driver1 != null) {
			driver1.quit();
		}
		if (driver2 != null) {
			driver2.quit();
		}
		Thread.sleep(15000);
	}

	@Test
	public void test() throws InterruptedException {

		// Given
		String hostIP = System.getProperty("hostIP");
		String appURL = "http://" + hostIP + ":8089";
		// Given
		driver1.get(appURL);
		Thread.sleep(5000);
		driver2.get(appURL);

		Thread.sleep(5000);

		driver1.findElement(By.id("nickname")).sendKeys(nameJ1);
		driver1.findElement(By.id("startBtn")).click();

		driver2.findElement(By.id("nickname")).sendKeys(nameJ2);
		driver2.findElement(By.id("startBtn")).click();

		Thread.sleep(9000);

		// When
		int indexPlayersMovements = 0;
		do {
			Thread.sleep(9000);
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
