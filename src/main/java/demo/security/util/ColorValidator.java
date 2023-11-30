package demo.security.util;

import org.springframework.stereotype.Component;

@Component
public class ColorValidator {

    public String validate(String color) {
        if(!color.startsWith("#")) { return "Hex color must starts with #"; }
        if(!color.matches("^#(?:[0-9a-fA-F]{3}){1,2}$")) { return "Chars must be hexadecimal numbers"; }
        if(color.length() > 7) { return "To long"; }
        return null;
    }

}
