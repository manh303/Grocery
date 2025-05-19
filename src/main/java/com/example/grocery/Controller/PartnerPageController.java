package com.example.grocery.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.grocery.Entity.Partner;
import com.example.grocery.Service.PartnerService;

@Controller
@RequestMapping("/partners")
public class PartnerPageController {

    @Autowired
    private PartnerService partnerService;

    // Hiển thị trang đối tác
    @GetMapping
    public String showPartnerPage(Model model) {
        // Lấy danh sách đối tác từ cơ sở dữ liệu
        model.addAttribute("partners", partnerService.getAllPartners());
        return "partners"; // templates/partners.html
    }

    // Thêm các phương thức khác nếu cần thiết, chẳng hạn như tạo, cập nhật hoặc xóa đối tác

    @PostMapping("/create")
    public String createPartner(@ModelAttribute("partner") Partner partner) {
        partnerService.savePartner(partner);
        return "redirect:/partners"; // Quay lại trang danh sách đối tác
    }

    @GetMapping("/edit/{id}")
    public String editPartner(@PathVariable Long id, Model model) {
        Optional<Partner> partner = partnerService.getPartnerById(id);
        if (partner.isPresent()) {
            model.addAttribute("partner", partner.get());
            return "edit_partner"; // templates/edit_partner.html
        }
        model.addAttribute("error", "Đối tác không tồn tại!");
        return "redirect:/partners"; // Quay lại trang danh sách đối tác
    }

    @PostMapping("/update")
    public String updatePartner(@ModelAttribute("partner") Partner partner) {
        partnerService.savePartner(partner);
        return "redirect:/partners"; // Quay lại trang danh sách đối tác
    }
    @GetMapping("/delete/{id}")
    public String deletePartner(@PathVariable Long id) {
        partnerService.deletePartner(id);
        return "redirect:/partners"; // Quay lại trang danh sách đối tác
    }
}