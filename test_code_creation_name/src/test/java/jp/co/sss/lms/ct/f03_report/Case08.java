package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// 「詳細」ボタンを取得して押下する
		WebElement detailsButton = webDriver.findElement(
				By.xpath("//tr[td[contains(normalize-space(), '2022年10月2日')]]//input[@type='submit' and @value='詳細']"));
		detailsButton.click();

		// セクション詳細画面が表示されるまで待機する
		visibilityTimeout(By.id("sectionDetail"), 60);

		// 見出しを取得し、セクション詳細画面に遷移出来ていることを確認する
		WebElement h3Element1 = webDriver.findElement(By.tagName("h3"));
		assertEquals("本日の内容", h3Element1.getText());

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// ボタンの要素を取得し押下する
		WebElement reportButton = webDriver
				.findElement(By.cssSelector("input[type='submit'][value='提出済み週報【デモ】を確認する']"));
		reportButton.click();

		// 週報であることを確認する
		WebElement reportNameElement = webDriver.findElement(By.cssSelector("input[name='reportName']"));
		assertEquals("週報【デモ】", reportNameElement.getAttribute("value"));

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {
		// 登録済みの内容を取得し、内容を消去して、新しい内容を入力する
		WebElement leaningItem = webDriver.findElement(By.id("intFieldName_0"));
		leaningItem.clear();
		leaningItem.sendKeys("テスト");

		// 理解度を変更する
		Select understanding = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		understanding.selectByVisibleText("2");

		// 目標の達成度を取得し、内容を削除し、新しい内容を取得する
		WebElement goalAchievement = webDriver.findElement(By.id("content_0"));
		goalAchievement.clear();
		goalAchievement.sendKeys("8");

		// 所感を取得し、内容を削除し、新しい内容を取得する
		WebElement impression = webDriver.findElement(By.id("content_1"));
		impression.clear();
		impression.sendKeys("週報入力テスト");

		// 所感を取得し、内容を削除し、新しい内容を取得する
		WebElement weeklyReflection = webDriver.findElement(By.id("content_2"));
		weeklyReflection.clear();
		weeklyReflection.sendKeys("今週でrp2終了");

		// 「提出する」ボタンの要素を取得し押下する
		WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		submitButton.click();

		visibilityTimeout(By.id("sectionDetail"), 60);

		// 提出済みの週報確認ボタンを取得し、更新されていることを確認する
		WebElement inputElement = webDriver
				.findElement(By.cssSelector("input[type='submit'][value='提出済み週報【デモ】を確認する']"));
		String value = inputElement.getAttribute("value");
		assertEquals("提出済み週報【デモ】を確認する", value);

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {

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
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {

		// 日付とレポート名を条件に、該当レポートの「詳細」ボタンの要素を取得し押下する
		WebElement detailBtn = webDriver
				.findElement(
						By.xpath("//tr[td[normalize-space()='2022年10月2日(日)']"
								+ " and td[normalize-space()='週報【デモ】']]"
								+ "//input[@type='submit'and@value='詳細']"));
		detailBtn.click();

		// 読み込まれるまで待機する
		pageLoadTimeout(10);

		// 修正した所感を取得し確認する
		WebElement impression = webDriver
				.findElement(By.xpath("//th[normalize-space()='所感']/following-sibling::td"));
		assertEquals("週報入力テスト", impression.getText().trim());

		// 修正した一週間の振り返りを取得し確認する
		WebElement weeklyReflection = webDriver
				.findElement(By.xpath("//th[normalize-space()='一週間の振り返り']/following-sibling::td"));
		assertEquals("今週でrp2終了", weeklyReflection.getText().trim());

		// エビデンスを取得する
		getEvidence(new Object() {
		});

	}

}
