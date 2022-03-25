package com.company.skt;

import com.company.skt.controller.Menu;
import com.company.skt.lib.ScreenController;
import com.company.skt.model.Local;

public class Skt extends ScreenController {

	
	@Override
	public void create () {
		super.create();
		Local.initiate("assets/local/");
		setActiveScreen(new Menu());
	}
	
}
