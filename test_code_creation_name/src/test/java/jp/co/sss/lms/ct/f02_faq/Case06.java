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
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {

		// 「研修関係」リンクの要素を取得し押下する
		WebElement categoryLink = webDriver.findElement(By.linkText("【研修関係】"));
		categoryLink.click();

		// カテゴリ検索結果が表示されるまで待機する
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("dl[id^='question-h']")));

		// 表示された質問要素をすべて取得する
		List<WebElement> resultItems = webDriver.findElements(By.cssSelector("dl[id^='question-h']"));

		// 検索結果の数・質問が合っているかを確認する
		assertEquals(2, resultItems.size());
		assertEquals("Q.キャンセル料・途中退校について", resultItems.get(0).getText());
		assertEquals("Q.研修の申し込みはどのようにすれば良いですか？", resultItems.get(1).getText());

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {

		// 「キャンセル料・途中退校について」の質問要素を取得し押下する
		WebElement questionElement = webDriver
				.findElement(By.xpath("//dl[starts-with(@id,'question-h')][.//span[text()='キャンセル料・途中退校について']]"));
		questionElement.click();

		// カテゴリ検索結果が表示されるまで待機する
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("dd[id^='answer-h']")));

		// 表示された質問要素をすべて取得する
		List<WebElement> resultItems = webDriver.findElements(By.cssSelector("dd[id^='answer-h']"));

		// 検索結果の数・質問が合っているかを確認する
		assertEquals("A. 受講者の退職や解雇等、やむを得ない事情による途中終了に関してなど、事情をお伺いした上で、協議という形を取らせて頂きます。 弊社営業担当までご相談下さい。",
				resultItems.get(0).getText());

		// エビデンスを取得する
		getEvidence(new Object() {
		});

	}

}
