package temp;


import entities.TicketEntity;
import org.xml.sax.SAXException;
import parsers.TicketsParser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

public class TestSelenium {
    public static void main(String[] args) throws AWTException, IOException, SAXException {

		String xmlString =
				"<tickets>\n" +
				"  <ticket>\n" +
				"    <id>375352</id>\n" +
				"    <gameId>11</gameId>\n" +
				"    <price>100</price>\n" +
				"    <context>{100,1,6,5,2,7,7,7,100}</context>\n" +
				"    <win>100</win>\n" +
				"  </ticket>\n" +
				"</tickets>";

		TicketEntity ticketEntity = TicketsParser.getInstance().getRundomTicket(new ByteArrayInputStream(xmlString.getBytes(Charset.forName("UTF-8"))));

		System.out.println();
/*				ChromeOptions options = new ChromeOptions();
				options.addArguments("disable-infobars");
				options.addArguments("--kiosk");

				options.setExperimentalOption("useAutomationExtension", false);
				options.setExperimentalOption("excludeSwitches",Collections.singletonList("enable-automation"));

//				System.setProperty("webdriver.chrome.driver","chromedriver");
				System.setProperty("webdriver.chrome.driver","C:\\lotteryServer\\fullLotteryServer\\src\\main\\resources\\chromedriver.exe");

				WebDriver driver = new ChromeDriver(options);
				//WebDriver driver2 = new ChromeDriver(options);
				driver.manage().window().setPosition(new Point(100,100));
				driver.manage().window().fullscreen();
				driver.get("about:blank");
				driver.manage().window().setPosition(new Point(2200,100));*/


/*				Set<String> windows = driver2.getWindowHandles();
				String adminToolHandle = driver2.getWindowHandle();
				driver2.manage().window().setPosition(new Point(2000,100));
				driver2.manage().window().fullscreen();
				((JavascriptExecutor) driver2).executeScript("window.open('http://www.google.com');");
				System.out.println();*/
        // robot.mouseMove(2000,100);
        //  robot.mousePress(InputEvent.BUTTON1_MASK);
        //  robot.keyPress(KeyEvent.VK_F11);


/*				WebDriver driver = new ChromeDriver();
				driver.get(adminToolURL);
				Set<String> windows = driver.getWindowHandles();
				String adminToolHandle = driver.getWindowHandle();
				((JavascriptExecutor)driver).executeScript("window.open();");
				Set<String> customerWindow = driver.getWindowHandles();
				customerWindow.removeAll(windows);
				String customerSiteHandle = ((String)customerWindow.toArray()[0]);
				driver.switchTo().window(customerSiteHandle);
				driver.get(customerSiteURL);
				driver.switchTo().window(adminToolHandle);*/

    }
}