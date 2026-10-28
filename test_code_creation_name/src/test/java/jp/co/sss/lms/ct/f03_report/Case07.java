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

/**
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		// 「未提出」ステータスの研修日にある「詳細」ボタンを特定する
		String xpathExpression = "//tr[.//span[normalize-space() = '未提出']]//input[@type='submit' and @value='詳細']";

		// 「詳細」ボタンの要素を取得し押下する
		WebElement detailsButton = webDriver.findElement(By.xpath(xpathExpression));
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
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {

		// ボタンの要素を取得し押下する
		WebElement reportButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='日報【デモ】を提出する']"));
		reportButton.click();

		// 見出しを取得しレポート登録画面に遷移できていることを確認する
		WebElement legendElement = webDriver.findElement(By.tagName("legend"));
		assertEquals("報告レポート", legendElement.getText());

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {

		//レポート入力欄の要素を取得し、内容を入力する
		WebElement textInput = webDriver.findElement(By.id("content_0"));
		textInput.sendKeys("本日も頑張った。");

		// 「提出する」ボタンの要素を取得し押下する
		WebElement loginButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		loginButton.click();

		// 提出済みの日報確認ボタンを取得し、更新されていることを確認する
		WebElement inputElement = webDriver
				.findElement(By.cssSelector("input[type='submit'][value='提出済み日報【デモ】を確認する']"));
		String value = inputElement.getAttribute("value");
		assertEquals("提出済み日報【デモ】を確認する", value);

		// エビデンスを取得する
		getEvidence(new Object() {
		});
	}

}
