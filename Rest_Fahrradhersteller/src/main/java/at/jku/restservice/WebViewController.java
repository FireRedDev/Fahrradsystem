package at.jku.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/view/")
public class WebViewController {
    @Autowired
    HandlebarConfigController controller;
    @GetMapping("/orderview")
    public String greetingForm(Model model) {
        model.addAttribute("handlebarconfig", new HandlebarConfig());
        return "orderview";
    }

    @PostMapping("/ordersubmit")
    @ResponseBody
    public String greetingSubmit(@ModelAttribute HandlebarConfig config, Model model) {
        return controller.order(config.getHandlebarType(),config.getHandlebarMaterial(),config.getHandlebarGearshift(),config.getHandleMaterial()).toString();
    }
}
