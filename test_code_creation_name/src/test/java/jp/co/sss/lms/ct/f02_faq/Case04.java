package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト よくある質問機能
 * ケース04
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// トップページへアクセスする
		goTo("http://localhost:8080/lms");

		// ページタイトルが「ログイン | LMS」であることを確認する
		assertEquals("ログイン | LMS", webDriver.getTitle());

		// ログインボタンの要素、value属性（入力値）を取得し表示欄が「ログイン」であることを確認する
		WebElement inputElement = webDriver.findElement(By.cssSelector("input[type='submit']"));
		String value = inputElement.getAttribute("value");
		assertEquals("ログイン", value);

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// ログインIDとパスワードの入力欄の要素を取得する
		WebElement idInput = webDriver.findElement(By.id("loginId"));
		WebElement passwordInput = webDriver.findElement(By.id("password"));

		// 初回ログイン済みの受講生ユーザーのログインIDとパスワードを入力する
		idInput.sendKeys("StudentAA03");
		passwordInput.sendKeys("StudentAA033");

		// ログインボタンの要素を取得し押下する
		WebElement loginButton = webDriver.findElement(By.cssSelector("input[type='submit']"));
		loginButton.click();

		// ログイン後、要素が表示されるまで待機する
		visibilityTimeout(By.cssSelector("a>small"), 60);

		// 表示要素を取得し、正常にログイン出来ていることを確認する
		WebElement smallElement = webDriver.findElement(By.tagName("small"));
		assertEquals("ようこそ受講生ＡＡ３さん", smallElement.getText());

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {

		// ドロップダウンを開くための要素をクリック
		WebElement dropdownTrigger = webDriver.findElement(By.cssSelector(".dropdown-toggle"));
		dropdownTrigger.click();

		// プルダウン内の選択肢が表示されるまで待機して取得
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		List<WebElement> options = wait.until(
				ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//ul[@class='dropdown-menu']/li")));

		// 選択肢から「ヘルプ」リンクを探して押下する
		for (WebElement option : options) {
			if (option.getText().trim().equals("ヘルプ")) {
				option.click();
				break;
			}
		}

		// 見出しを取得し、ヘルプ画面に遷移出来ていることを確認する
		WebElement h2Element = webDriver.findElement(By.tagName("h2"));
		assertEquals("ヘルプ", h2Element.getText());

		// エビデンスを取得する
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {

		// 元のウィンドウハンドルを保持
		String originalWindow = webDriver.getWindowHandle();

		// 「よくある質問」リンクの要素を取得し押下する
		WebElement linkButton = webDriver.findElement(By.linkText("よくある質問"));
		linkButton.click();

		// 別タブを開く操作の後、全ハンドルを取得
		Set<String> allWindowHandles = webDriver.getWindowHandles();

		// 新しいタブのハンドルへ切り替え
		for (String handle : allWindowHandles) {
			if (!handle.equals(originalWindow)) {
				webDriver.switchTo().window(handle);
				break;
			}
		}

		// 見出しを取得し、よくある質問画面に遷移出来ていることを確認する
		WebElement h2Element = webDriver.findElement(By.tagName("h2"));
		assertEquals("よくある質問", h2Element.getText());

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

}
