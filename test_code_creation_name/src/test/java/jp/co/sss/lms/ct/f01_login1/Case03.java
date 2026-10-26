package jp.co.sss.lms.ct.f01_login1;

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
 * 結合テスト ログイン機能①
 * ケース03
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース03 受講生 ログイン 正常系")
public class Case03 {

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

}
