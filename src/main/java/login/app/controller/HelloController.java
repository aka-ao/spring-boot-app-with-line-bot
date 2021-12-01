package login.app.controller;


import login.app.mapper.LineMapper;
import login.app.service.LineAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Random;
import java.util.UUID;


/**
 * hello画面のコントローラクラス
 * 今回はログイン済みユーザのユーザ名をビューに渡す処理のみを行う
 * @author aoi
 *
 */
@Controller
public class HelloController {
	@Value("${liff-id}")
	private String liffId;

	@Autowired
	LineMapper mapper;

	@Autowired
	LineAuthService authService;
	
	/**
	 * ログイン成功時に呼び出されるメソッド
	 * SecurityContextHolderから認証済みユーザの情報を取得しモデルへ追加する
	 * @param model リクエストスコープ上にオブジェクトを載せるためのmap
	 * @return helloページのViewName
	 */
	@RequestMapping(value = {"/", "/hello"})
	private String init(Model model, HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//Principalからログインユーザの情報を取得
		String userName = auth.getName();
		model.addAttribute("userName", userName);
		String linkToken = session.getAttribute("linkToken").toString();
		String nonce = UUID.randomUUID().toString();
		String linkUriStr = "https://access.line.me/dialog/bot/accountLink?linkToken=" + linkToken + "&nonce=" + nonce;

		// ユーザIDとnonceをセットにして保存
		mapper.insertLine(userName, nonce);
		model.addAttribute("linkUrl", linkUriStr);
		return "hello";
	}

	@RequestMapping("/liff")
	public String liff(@RequestParam(required = false) String id, Model model) {
		// [[${test}]] の部分を Hello... で書き換えて、liff.htmlを表示する
		model.addAttribute("test", "Hello Tymeleaf!");
		return "liff";
	}

	@RequestMapping("/login")
	public String login(
			@RequestParam(name = "linkToken", required = false) String linkToken,
			Model model,
			HttpSession session
	) {
		session.setAttribute("linkToken", linkToken);
		return "login";
	}
}