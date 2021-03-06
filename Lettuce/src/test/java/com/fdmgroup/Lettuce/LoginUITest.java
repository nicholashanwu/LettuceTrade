package com.fdmgroup.Lettuce;


import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
public class LoginUITest {
	@Test
	public void successLogin() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8006/login");

		WebElement user = driver.findElement(By.id("user-email"));
		WebElement pass = driver.findElement(By.id("user-password"));
		WebElement loginButton = driver.findElement(By.id("sign-in"));
		user.sendKeys("wassimkhan3@gmail.com");
		pass.sendKeys("123");
		Thread.sleep(3000);
		
		loginButton.click();
		Thread.sleep(3000);
		String g = driver.getCurrentUrl();
		String actual = g.split(";")[0];
		System.out.println(actual);
		String expected = "http://localhost:8006/dashboard";
		assertEquals(expected, actual);
		driver.close();

	}

	@Test
	public void unsuccessfulLogin() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8006/login");

		WebElement user = driver.findElement(By.id("user-email"));
		WebElement pass = driver.findElement(By.id("user-password"));
		WebElement loginButton = driver.findElement(By.id("sign-in"));
		user.sendKeys("123");
		pass.sendKeys("123");
		Thread.sleep(3000);
		loginButton.click();
		Thread.sleep(3000);

		String actual = driver.getCurrentUrl();
		String expected = "http://localhost:8006/login";
		assertEquals(expected, actual);
		driver.close();
	}

	@Test
	public void registerBlankMessage() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8006/register");

		WebElement registerButton = driver.findElement(By.id("sign-up"));
		registerButton.click();
		Thread.sleep(3000);

		WebElement validation = driver.findElement(By.id("user-firstName-message"));
		String message = validation.getText();
		assertEquals("Valid first name is required.", message);
		driver.close();
	}

	@Test
	public void registerEmailInvalid() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8006/register");

		WebElement email = driver.findElement(By.id("user-email"));
		WebElement registerButton = driver.findElement(By.id("sign-up"));
		email.sendKeys("Not a valid email");
		Thread.sleep(3000);
		registerButton.click();
		Thread.sleep(3000);

		WebElement emailValidation = driver.findElement(By.id("user-email-message"));
		String message = emailValidation.getText();
		assertEquals("Valid email is required.", message);
		driver.close();
	}

	@Test
	public void registerPassMismatch() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8006/register");
		WebElement fname = driver.findElement(By.id("user-firstName"));
		WebElement lname = driver.findElement(By.id("user-lastName"));
		WebElement email = driver.findElement(By.id("user-email"));
		WebElement pass = driver.findElement(By.id("user-password"));
		WebElement passConf = driver.findElement(By.id("user-confirm-password"));
		WebElement registerButton = driver.findElement(By.id("sign-up"));
		fname.sendKeys("J");
		lname.sendKeys("D");
		email.sendKeys("jd@gmail.com");
		pass.sendKeys("123");
		passConf.sendKeys("123456789");
		Thread.sleep(3000);
		registerButton.click();
		Thread.sleep(3000);
		WebElement passValidation = driver.findElement(By.id("user-confirm-password-message"));
		String message = passValidation.getText();
		assertEquals(message, "The confirmation password does not match.");
		driver.close();
	}
	@Test
	public void ratesPage() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8006/login");

		WebElement user = driver.findElement(By.id("user-email"));
		WebElement pass = driver.findElement(By.id("user-password"));
		WebElement loginButton = driver.findElement(By.id("sign-in"));
		user.sendKeys("wassimkhan3@gmail.com");
		pass.sendKeys("123");
		loginButton.click();
		
		WebElement rates = driver.findElement(By.id("rates"));
		rates.click();
		Thread.sleep(3000);
		String actual = driver.getCurrentUrl();
		String expected = "http://localhost:8006/rates";
		assertEquals(expected, actual);
		driver.close();
	}
	@Test
	public void profilePage() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8006/login");

		WebElement user = driver.findElement(By.id("user-email"));
		WebElement pass = driver.findElement(By.id("user-password"));
		WebElement loginButton = driver.findElement(By.id("sign-in"));
		user.sendKeys("wassimkhan3@gmail.com");
		pass.sendKeys("123");
		loginButton.click();
		
		WebElement profile = driver.findElement(By.id("profile"));
		profile.click();
		Thread.sleep(3000);
		String actual = driver.getCurrentUrl();
		String expected = "http://localhost:8006/profile";
		assertEquals(expected, actual);
		driver.close();
	}
	@Test
	public void orderPage() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8006/login");

		WebElement user = driver.findElement(By.id("user-email"));
		WebElement pass = driver.findElement(By.id("user-password"));
		WebElement loginButton = driver.findElement(By.id("sign-in"));
		user.sendKeys("wassimkhan3@gmail.com");
		pass.sendKeys("123");
		loginButton.click();
		
		WebElement order = driver.findElement(By.id("order"));
		order.click();
		Thread.sleep(3000);
		String actual = driver.getCurrentUrl();
		String expected = "http://localhost:8006/order";
		assertEquals(expected, actual);
		driver.close();
	}
	@Test
	public void historyPage() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8006/login");

		WebElement user = driver.findElement(By.id("user-email"));
		WebElement pass = driver.findElement(By.id("user-password"));
		WebElement loginButton = driver.findElement(By.id("sign-in"));
		user.sendKeys("wassimkhan3@gmail.com");
		pass.sendKeys("123");
		loginButton.click();
		
		WebElement history = driver.findElement(By.id("history"));
		history.click();
		Thread.sleep(3000);
		String actual = driver.getCurrentUrl();
		String expected = "http://localhost:8006/history";
		assertEquals(expected, actual);
		driver.close();
	}
	@Test
	public void marketplacePage() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8006/login");

		WebElement user = driver.findElement(By.id("user-email"));
		WebElement pass = driver.findElement(By.id("user-password"));
		WebElement loginButton = driver.findElement(By.id("sign-in"));
		user.sendKeys("wassimkhan3@gmail.com");
		pass.sendKeys("123");
		loginButton.click();
		
		WebElement marketplace = driver.findElement(By.id("marketplace"));
		marketplace.click();
		Thread.sleep(3000);
		String actual = driver.getCurrentUrl();
		String expected = "http://localhost:8006/outstandingOrder";
		assertEquals(expected, actual);
		driver.close();
	}

}
