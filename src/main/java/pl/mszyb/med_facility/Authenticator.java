package pl.mszyb.med_facility;

import javax.servlet.http.HttpServletRequest;

public class Authenticator {

    public static String redirectLoggedUsersOrReturnViewName(HttpServletRequest request, String viewName) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/admin/homepage?page=0";
        }
        if (request.isUserInRole("ROLE_USER")) {
            return "redirect:/user/homepage";
        }
        if (request.isUserInRole("ROLE_PHYSICIAN")) {
            return "redirect:/doc/homepage";
        }
        return viewName;
    }
}
