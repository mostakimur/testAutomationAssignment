package rokomari;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class RokomariWebSearchAndPlaceOrder {

	//private String browserType = "";
	public String baseUrl = "https://www.rokomari.com/book";
	// Just need to change the runOnBrowser before executing the TestScript
	public String runOnBrowser = "Edge";
	public WebDriver driver;

	// Print Method
	public void print(String str) {
		System.out.println(str);
	}

	@BeforeTest
	public void setUp() throws Exception {

		// Check if parameter passed as 'chrome'
		if (runOnBrowser.equalsIgnoreCase("chrome")) {
			// set path to chromedriver.exe
			System.setProperty("webdriver.chrome.driver", ".\\chromedriver.exe");
			// create chrome instance
			driver = new ChromeDriver();
		}
		// Check if parameter passed as 'Edge'
		else if (runOnBrowser.equalsIgnoreCase("Edge")) {
			// set path to Edge.exe
			System.setProperty("webdriver.edge.driver", ".\\msedgedriver.exe");
			// create Edge instance
			driver = new EdgeDriver();
		} else {
			// If no browser passed throw exception
			throw new Exception("Browser is not correct");
		}

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//WebDriverWait wait = new WebDriverWait(driver, 20);
		driver.manage().window().maximize();

	}

	@Test
	public void SearchAndPlaceOrder() {

		print("launching "+runOnBrowser+" browser");
		driver.get(baseUrl);

		// Verify we are in Rokomari.com
		String expectedTitle = "Buy Book Online - Best Online Book Shop in Bangladesh | Rokomari.com";
		String actualTitle = driver.getTitle();
		Assert.assertEquals(actualTitle, expectedTitle);
		print("Verified we are in Rokomari.com");

		// Close Advertise pop-up window if appears
		boolean isDisplayed = driver.findElement(By.xpath("(//i[@class='ion-close-round'])[3]")).isDisplayed();
		if (isDisplayed) {
			driver.findElement(By.xpath("(//i[@class='ion-close-round'])[3]")).click();
		}

		// Enter keyword in search field
		driver.findElement(By.cssSelector("input[name='term']")).sendKeys("Billion Dollar Startup");
		print("Enter keyword-Billion Dollar Startup in search field");

		// Click on Search button
		driver.findElement(By.cssSelector("i.ion-ios-search-strong")).click();
		print("Clicked on Search Button");

		// View Details of the Book
		String oldTab = driver.getWindowHandle();
		driver.findElement(By.xpath("(//div[@class='book-list-wrapper ']//a)[1]")).click();
		print("Navigate to View Details of the Book");
		ArrayList<String> newTab = new ArrayList<String>(driver.getWindowHandles());
		// newTab.remove(oldTab);
		// change focus to new tab
		driver.switchTo().window(newTab.get(1));
		// Switch back to original window
		// driver.switchTo().window(mainWindowHandle);

		// View Details in new window
		String bookPageTitle = driver.getTitle();
		print("We are in: " + bookPageTitle);

		// Click on Go To Cart button
		WebDriverWait wait = new WebDriverWait(driver, 40);
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector("div#js--details-btn-area>a:nth-of-type(2)>span")));

		driver.findElement(By.xpath("//*[@id=\"js--details-btn-area\"]/a[2]/span")).click();
		print("Clicked on Go To Cart button");

		driver.findElement(By.cssSelector("span.js--cart-quantity")).click();
		print("Clicked on Cart Quantity Count!");

		// Click on Place Order button
		driver.findElement(By.xpath("//a[@id='js-continue-to-shipping']")).click();
		print("Clicked on Place Order button");

		// Veify Enter your phone number text appears in screen
		String getPhoneNumberText = driver.findElement(By.xpath("//p[text()='Enter your phone number']")).getText();
		String expectedText = "Enter your phone number";
		Assert.assertEquals(getPhoneNumberText, expectedText);
		print("Enter your phone number page appears");
	}

	@AfterTest
	public void afterTest() {
		driver.quit();
		print("Close the window");
		print("Test Passed Successfully!");

	}

}