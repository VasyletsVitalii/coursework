package com.joreijarr.studycontrol.controllers;
import com.joreijarr.studycontrol.models.Competition;
import com.joreijarr.studycontrol.repo.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class CompetitionController {

    @Autowired
    private CompetitionRepository competitionRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("competitions", competitionRepository.findAll());
        return "index";
    }

    @GetMapping("/add")
    public String addCompetitionForm(HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return "add-competition";
        }
        return "redirect:/login";
    }

    @PostMapping("/add")
    public String addCompetition(@RequestParam String name, @RequestParam String location, @RequestParam String date, @RequestParam String result, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            Competition competition = new Competition();
            competition.setName(name);
            competition.setLocation(location);
            competition.setDate(date);
            competition.setResult(result);
            competitionRepository.save(competition);
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @PostMapping("/delete")
    public String deleteCompetition(@RequestParam Long id, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            competitionRepository.deleteById(id);
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @GetMapping("/edit")
    public String editCompetitionForm(@RequestParam Long id, Model model, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            Competition competition = competitionRepository.findById(id).orElse(null);
            if (competition != null) {
                model.addAttribute("competition", competition);
                return "edit-competition";
            }
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @PostMapping("/edit")
    public String editCompetition(@RequestParam Long id, @RequestParam String name, @RequestParam String location, @RequestParam String date, @RequestParam String result, HttpSession session) {
        if (Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            Competition competition = competitionRepository.findById(id).orElse(null);
            if (competition != null) {
                competition.setName(name);
                competition.setLocation(location);
                competition.setDate(date);
                competition.setResult(result);
                competitionRepository.save(competition);
            }
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        if ("admin".equals(username) && "admin".equals(password)) {
            session.setAttribute("loggedIn", true);
            return "redirect:/";
        }
        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}