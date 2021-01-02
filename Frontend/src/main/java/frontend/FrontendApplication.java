package frontend;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;

@Theme("valo")
public class FrontendApplication extends UI {

	public static void main(String[] args) {
		SpringApplication.run(FrontendApplication.class, args);
	}

}
