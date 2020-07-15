package servlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Victor Gambier
 *
 */
@Controller
@RequestMapping(value = "/dashboard")
public class MainController {

    @GetMapping
    public ModelAndView getTestData() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("dashboard");
        mv.getModel().put("data", "Welcome home man");

        return mv;
    }
}