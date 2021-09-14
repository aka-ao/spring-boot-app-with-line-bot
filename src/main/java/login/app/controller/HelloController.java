package login.app.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Random;


/**
 * hello画面のコントローラクラス
 * 今回はログイン済みユーザのユーザ名をビューに渡す処理のみを行う
 * @author aoi
 *
 */
@Controller
public class HelloController {
	
	
	/**
	 * ログイン成功時に呼び出されるメソッド
	 * SecurityContextHolderから認証済みユーザの情報を取得しモデルへ追加する
	 * @param model リクエストスコープ上にオブジェクトを載せるためのmap
	 * @return helloページのViewName
	 */
	@RequestMapping("/hello")
	private String init(Model model, HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//Principalからログインユーザの情報を取得
		String userName = auth.getName();
		model.addAttribute("userName", userName);
		String linkUrl = session.getAttribute("linkUrl").toString();
		model.addAttribute("linkUrl", linkUrl);
		return "hello";
	}

	@RequestMapping("/liff")
	public String liff(@RequestParam(required = false) String id, Model model) {
		// [[${test}]] の部分を Hello... で書き換えて、liff.htmlを表示する
		System.out.println(id);
		model.addAttribute("test", "Hello Tymeleaf!");
		return "liff";
	}

	@RequestMapping("/login")
	public String login(@RequestParam(name = "linkToken", required = false) String linkToken,
									Model model, HttpSession session) {
		Random random = new Random();
		int nonce = random.nextInt(999999999);
		String linkUriStr = "https://access.line.me/dialog/bot/accountLink?linkToken=" + linkToken + "&nonce=" + String.format("%10s", nonce).replaceAll(" ", "0");
		session.setAttribute("linkUrl", linkUriStr);
		return "login";
	}
	


}