package com.fdmgroup.Lettuce.Controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fdmgroup.Lettuce.Exceptions.InsufficientFundsException;
import com.fdmgroup.Lettuce.Models.Currency;
import com.fdmgroup.Lettuce.Models.User;
import com.fdmgroup.Lettuce.Repo.CurrencyRepo;
import com.fdmgroup.Lettuce.Service.PortfolioServiceImpl;

@Controller
@SessionAttributes({"user"})
public class PortfolioController {
	@Autowired
	private PortfolioServiceImpl psi = new PortfolioServiceImpl();
	@Autowired
	private CurrencyRepo cr;
	
	public static final Logger actLogger=LogManager.getLogger("ActLogging");
	public static final Logger dbLogger=LogManager.getLogger("DBLogging");
	
	@RequestMapping("/addfund")
	public String addFundPage() {
		return "addfund";
	}

	@RequestMapping(value = "/fundHandler",params = "top-up",method=RequestMethod.POST)
	public String addFundHandler(@RequestParam double amount, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		int pid = user.getPortfolio().getPortfolioId();
		Currency aud = cr.getById("AUD");
		psi.increaseCurrency(aud, amount, pid);
		return "redirect:/profile";
	}

	@RequestMapping(value = "/fundHandler", params = "withdraw", method = RequestMethod.POST)
	public String withdrawFundHandler(@RequestParam double amount, HttpServletRequest request, RedirectAttributes redir) throws InsufficientFundsException {
		User user = (User) request.getSession().getAttribute("user");
		int pid = user.getPortfolio().getPortfolioId();
		Currency aud = cr.getById("AUD");
		try {
			psi.decreaseCurrency(aud, amount, pid);
			return "redirect:/profile";
		} catch(InsufficientFundsException e) {
			redir.addFlashAttribute("noFunds", "You do not have sufficient funds to withdraw!");
			return "redirect:/addfund";
		}
	}

	

	
}
