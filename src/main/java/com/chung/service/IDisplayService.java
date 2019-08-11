package com.chung.service;

import com.chung.dto.display.DisplayInfo;
import com.chung.dto.display.DisplayInfoImage;

public interface IDisplayService {
	public DisplayInfo getDisplayInfo(int displayInfoId);

	public DisplayInfoImage getDisplayInfoImage(int displayInfoId);
}
