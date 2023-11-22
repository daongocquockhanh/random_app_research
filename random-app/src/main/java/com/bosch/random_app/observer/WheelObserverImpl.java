package com.bosch.random_app.observer;

import com.bosch.random_app.view.components.AlertBox;

public class WheelObserverImpl implements WheelObserver {

	@Override
	public void onWheelStopped(String data) {
        AlertBox.display("Current Data Random", data);
//        SpinningWheel.removeElement(data);
	}

}
