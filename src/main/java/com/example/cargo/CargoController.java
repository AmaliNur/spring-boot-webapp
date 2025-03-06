package com.example.cargo;

import com.example.cargo.annotation.RequireRole;
import com.example.cargo.model.Role;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CargoController {

    @Autowired
    private com.example.cargo.CargoService service;

    @RequestMapping("/")
    public String viewHomePage(Model model, @Param("keyword") String keyword) {
        List<Cargo> listCargos = service.listAll(keyword);
        model.addAttribute("listCargos", listCargos);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    @RequireRole(Role.ADMIN)
    @RequestMapping("/new")
    public String showNewCargoForm(Model model, RedirectAttributes redirectAttributes) {
        Cargo cargo = new Cargo();
        model.addAttribute("cargo", cargo);
        return "new_cargo";
    }

    @RequireRole(Role.ADMIN)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveCargo(@ModelAttribute("cargo") Cargo cargo, RedirectAttributes redirectAttributes) {
        service.save(cargo);
        return "redirect:/";
    }

    @RequireRole(Role.ADMIN)
    @RequestMapping("/edit/{id}")
    public String showEditSCargoForm(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Cargo cargo = service.get(id);
        model.addAttribute("cargo", cargo);
        return "edit_cargo";
    }

    @RequireRole(Role.ADMIN)
    @RequestMapping("/delete/{id}")
    public String deleteCargo(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        service.delete(id);
        return "redirect:/";
    }

    @ResponseBody
    @GetMapping("/strings")
    public Map<String, Object> getStrings() {
        String dbUrl = "jdbc:mysql://localhost:3306/shipping";
        String username = "root";
        String password = "root";
        List<String> formattedDates = new ArrayList<>();
        List<Integer> countDates = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            String query = "SELECT cargo.date1, COUNT(cargo.date1) AS count FROM cargo GROUP BY cargo.date1";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String originalDateFormat = "dd.MM.yy";
                String targetDateFormat = "yyyy-MM-dd";
                SimpleDateFormat originalFormat = new SimpleDateFormat(originalDateFormat);
                Date date;
                date = originalFormat.parse(rs.getString("date1"));
                SimpleDateFormat targetFormat = new SimpleDateFormat(targetDateFormat);
                String formattedDate = targetFormat.format(date);
                formattedDates.add(formattedDate);
                countDates.add(rs.getInt("count"));
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> result = new HashMap<>();
        result.put("dates", formattedDates);
        result.put("counts", countDates);
        return result;
    }

    @RequestMapping("/sort")
    public String viewHomePage2(Model model) {
        List<Cargo> sortCargos = service.sortAll();
        model.addAttribute("sortCargos", sortCargos);
//        model.addAttribute("keyword", keyword);
        return "index";
    }


    @ResponseBody
    @RequestMapping("/day")
    public int countCargos(String keyword) {
        int result = service.countByDay(keyword);
        return result;
    }
}
