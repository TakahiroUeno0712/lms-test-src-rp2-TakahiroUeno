package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
		// ボタンの要素を取得し押下する
		WebElement loginButton = webDriver.findElement(By.cssSelector("input[type='submit']"));
		loginButton.click();

		// リンクの要素を取得し押下する
		WebElement linkButton = webDriver.findElement(By.linkText("ようこそ受講生ＡＡ３さん"));
		linkButton.click();

		// 見出しを取得し、遷移できていることを確認する
		WebElement legendElement = webDriver.findElement(By.tagName("h2"));
		assertEquals("ユーザー詳細", legendElement.getText());

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// 「修正する」ボタンを取得して押下する
		WebElement detailsButton = webDriver.findElement(
				By.xpath(
						"//tr[td[contains(normalize-space(), '2022年10月2日(日)')]]//input[@type='submit' and @value='修正する']"));
		detailsButton.click();

		// セクション詳細画面が表示されるまで待機する
		visibilityTimeout(By.cssSelector("button[type='submit']"), 10);

		// 見出しを取得し、セクション詳細画面に遷移出来ていることを確認する
		WebElement reportNameElement = webDriver.findElement(By.cssSelector("input[name='reportName']"));
		assertEquals("週報【デモ】", reportNameElement.getAttribute("value"));

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {
		// 正常値を入力する
		inputNormalValues();

		// 学習項目を未入力にする
		WebElement learningItem = webDriver.findElement(By.id("intFieldName_0"));
		learningItem.clear();

		// 「提出する」ボタンを押下する
		submitReport();

		// 学習項目が入力エラーになっていることを確認する
		assertFieldError("intFieldName_0");

		// エビデンスを取得する
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {
		// 正常値を入力する
		inputNormalValues();

		// 理解度を未選択にする
		Select understanding = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		understanding.selectByIndex(0);

		// 「提出する」ボタンを押下する
		submitReport();

		// 理解度が入力エラーになっていることを確認する
		assertFieldError("intFieldValue_0");

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {
		// 正常値を入力する
		inputNormalValues();

		// 目標の達成度に数値以外を入力する
		WebElement goalAchievement = webDriver.findElement(By.id("content_0"));
		goalAchievement.clear();
		goalAchievement.sendKeys("あ");

		// 「提出する」ボタンを押下する
		submitReport();

		// 目標の達成度が入力エラーになっていることを確認する
		assertFieldError("content_0");

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {
		// 正常値を入力する
		inputNormalValues();

		// 目標の達成度に範囲外の値を入力する
		WebElement goalAchievement = webDriver.findElement(By.id("content_0"));
		goalAchievement.clear();
		goalAchievement.sendKeys("11");

		// 「提出する」ボタンを押下する
		submitReport();

		// 目標の達成度が入力エラーになっていることを確認する
		assertFieldError("content_0");

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
		// 正常値を入力する
		inputNormalValues();

		// 目標の達成度を未入力にする
		WebElement goalAchievement = webDriver.findElement(By.id("content_0"));
		goalAchievement.clear();

		// 所感を未入力にする
		WebElement impression = webDriver.findElement(By.id("content_1"));
		impression.clear();

		// 「提出する」ボタンを押下する
		submitReport();

		// 目標の達成度と所感が入力エラーになっていることを確認する
		assertFieldError("content_0");
		assertFieldError("content_1");

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
		String over2000CharacterString = "あ".repeat(2001);

		// 正常値を入力する
		inputNormalValues();

		// 所感に2001文字を入力する
		WebElement impression = webDriver.findElement(By.id("content_1"));
		impression.clear();
		impression.sendKeys(over2000CharacterString);

		// 一週間の振り返りに2001文字を入力する
		WebElement weeklyReflection = webDriver.findElement(By.id("content_2"));
		weeklyReflection.clear();
		weeklyReflection.sendKeys(over2000CharacterString);

		// 「提出する」ボタンを押下する
		submitReport();

		// 所感が入力エラーになっていることを確認する
		assertFieldError("content_1");

		// 一週間の振り返りが入力エラーになっていることを確認する
		assertFieldError("content_2");

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	/**
	 * 各入力欄に正常値を設定する
	 */
	private void inputNormalValues() {

		// 学習項目
		WebElement learningItem = webDriver.findElement(By.id("intFieldName_0"));
		learningItem.clear();
		learningItem.sendKeys("テスト");

		// 理解度
		Select understanding = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		understanding.selectByVisibleText("2");

		// 目標の達成度
		WebElement goalAchievement = webDriver.findElement(By.id("content_0"));
		goalAchievement.clear();
		goalAchievement.sendKeys("8");

		// 所感
		WebElement impression = webDriver.findElement(By.id("content_1"));
		impression.clear();
		impression.sendKeys("週報入力テスト");

		// 一週間の振り返り
		WebElement weeklyReflection = webDriver.findElement(By.id("content_2"));
		weeklyReflection.clear();
		weeklyReflection.sendKeys("今週でrp2終了");
	}

	/**
	 * 「提出する」ボタンを押下する
	 */
	private void submitReport() {
		WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		submitButton.click();
	}

	/**
	 * 指定した項目が入力エラーになっていることを確認する
	 */
	private void assertFieldError(String fieldId) {

		// 入力エラーを示すclassが付与されるまで待機する
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

		// DOM更新で要素が古くなった場合は再取得して確認する
		wait.ignoring(StaleElementReferenceException.class);

		// 指定した要素を取得し、errorInputが付与されているか確認する
		boolean result = wait.until(Driver -> {

			// 対象の入力項目を取得する
			WebElement field = Driver.findElement(By.id(fieldId));

			// エラー時に付与されるclassを取得する
			String classNameString = field.getAttribute("class");
			return classNameString != null && classNameString.contains("errorInput");
		});

		// エラーが画面に表示されていることを確認する
		assertTrue(result);
	}

}
