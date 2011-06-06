package y11.GUI;

import java.awt.Font;

/**
 * GUI constraints for the PatternExplorer histogram display. Most values can be
 * freely tweaked, noted if otherwise. Used
 * http://www.colorschemer.com/online.html to generate color pallets. Easily
 * changed to PatternExplorer operator parameters.
 * 
 * @author Caleb Sotelo
 * 
 */
public abstract class GUISettings {

	/*
	 * Tool tips.
	 */
	public static final String RESET_BTN_TIP = "Clear history and reset to first state";
	public static final String BACK_BTN_TIP = "Move to previous state";
	public static final String FORWARD_BTN_TIP = "Move to next state";
	public static final String SIGMA_SLIDER_TIP = "Change the threshold for displaying statistical significance";
	public static final String TARGET_SELECT_TIP = "Change the target attribute";
	public static final String ZOOM_SLIDER_TIP = "Change the size of the histogram display";
	//public static final String HIST_CHECKBOX_TIP = "Select to enable / disable history traversal (disable to conserve memory)";
	public static final String HIST_DFLT_TIP = "Select to enable default history caching";
	public static final String HIST_TIME_TIP = "Select to enable faster history browsing (uses more memory)";
	public static final String HIST_MEM_TIP = "Select to conserve memory (slower history browsing)";
	public static final String DETAIL_TIP = "Click to show detailed view";

	/*
	 * Colors
	 */
	public static final int LABEL_OUTLINE_COLOR = 0xFFE18F;
	public static final int DETAIL_PANEL_BG_COLOR = 0xFFFFFF;
	public static final int GUI_BORDER_COLOR = 0xCCCCCC;
	public static final int HISTOGRAM_DEFAULT_BORDER_COLOR = 0xA3A3A3;
	public static final int HISTOGRAM_META_BORDER_COLOR = 0xB0B0B0;
	public static final int HISTOGRAM_DETAIL_BORDER_COLOR = 0xA3A3A3;
	public static final int HISTOGRAM_DEFAULT_BAR_COLOR = 0x295EFF;
	public static final int HISTOGRAM_META_BAR_COLOR = 0xA8BFFF;
	public static final int HISTOGRAM_SIGMA_BAR_COLOR = 0xFF8C66;
	public static final int HISTOGRAM_BACKGROUND_COLOR = 0xFFFFFF;
	public static final int LABEL_COLOR = 0x636363;
	public static final int TITLE_LABEL_COLOR = 0x3D3D3D;
	public static final int SIGMA_LABEL_COLOR = 0x6633FF;
	public static final int MATRIX_BG_COLOR = 0xFFFFFF;

	/*
	 * Button and label text
	 */
	public static final String DELETE_VALUE_TXT = "x";
	public static final String HIST_LABEL_TXT = "History";
	public static final String HIST_BACK_BTN_TXT = "<<";
	public static final String HIST_RESET_BTN_TXT = "Reset";
	public static final String HIST_FRWD_BTN_TXT = ">>";
	public static final String ZOOM_LABEL_TXT = "- Zoom +";
	public static final String DFLT_MEM_BTN_TXT = "Default";
	public static final String ECON_MEM_BTN_TXT = "Conserve memory";
	public static final String ECON_TIME_BTN_TXT = "Faster";
	public static final String SIGMA_LABEL_TXT = "- Significance Level +";
	public static final String TARGET_LABEL_TXT = "Target";
	public static final String SIGMA_CHAR = " \u03C3";
	public static final String EMPTY_TOOLTIP_TXT = "";
	public static final String EXPANDED_BUTTON_TEXT = "<<";
	public static final String COMPACTED_BUTTON_TEXT = ">>";

	/*
	 * Dimensions
	 */
	public static final int MAIN_DIVIDER_WIDTH = 12;
	public static final int HISTOGRAM_DEFAULT_SIZE = 100;
	public static final int HISTOGRAM_BORDER_WIDTH = 2;
	public static final int HISTOGRAM_DEFAULT_BAR_WIDTH = 35;
	public static final int HISTOGRAM_DETAIL_HEIGHT = 210;
	public static final int HISTOGRAM_DETAIL_WIDTH = 250;
	public static final double HISTOGRAM_DETAIL_CATEGORY_MARGIN = 0.05;
	
	/*
	 * Spacing and padding
	 */
	public static final int SIDE_PANEL_AREA_SPACING = 20;
	public static final int SIDE_PANEL_LINE_SPACING = 3;
	public static final int SIDE_PANEL_PADDING = 4;
	public static final int ATTRIB_LABEL_PADDING = 40;
	public static final int ROW_LABEL_PADDING = 90;
	public static final int DELETE_BTN_PADDING = 30;
	public static final int SIGMA_LABEL_PADDING = 3;
	public static final int LABEL_INSIDE_PADDING = 14;
	public static final int MATRIX_OUTER_PADDING = 20;
	public static final int MATRIX_TITLE_PADDING = 35;
	public static final int HISTOGRAM_PADDING_X = 25;
	public static final int HISTOGRAM_PADDING_Y = 25;
	public static final int MATRIX_MIN_PADDING = 3;
	public static final int HISTOGRAM_INTERNAL_SIDE_PADDING = 10;
	public static final int HISTOGRAM_INTERNAL_BAR_PADDING = 3;
	public static final int HISTOGRAM_INTERNAL_TOP_PADDING = 1;
	public static final int HISTOGRAM_TARGET_SIDE_PADDING = 6;	
	
	/*
	 * Misc
	 */
	public static final int V_SCROLL_AMOUNT = 50;
	public static final int H_SCROLL_AMOUNT = 80;
		
	/*
	 * Label fonts
	 */
	public static final double LABEL_MIN_SCALE = 0.54;
	public static final String LABEL_FONT_FAM = "Arial";
	public static final int LABEL_FONT_STYLE = Font.BOLD;
	public static final int LABEL_FONT_SIZE = 16;
	public static final int SIGMA_LABEL_FONT_SIZE = 11;
	public static final int TITLE_FONT_SIZE = 26;
	public static final int MAX_LABEL_FONT_SIZE = 22;
	
	/*
	 * Position	
	 */
	public static final int MATRIX_TITLE_X = 3 * MATRIX_OUTER_PADDING;
	public static final int MATRIX_TITLE_Y = 2 * MATRIX_OUTER_PADDING;
	
	/*
	 * Sigma slider settings. Please see comments.
	 */
	public static final int SIGMA_SLIDER_MIN = 100;	// must be greater than 0
	public static final int SIGMA_SLIDER_MAX = 400;
	public static final int SIGMA_SLIDER_INIT = 300;
	public static final int SIGMA_SLIDER_EXT = 0;
	public static final int SIGMA_SLIDER_MINOR_TICK_SPACING = 100;
	
	/*
	 * Zoom slider settings. Please don't tweak.
	 */
	public static final int ZOOM_SLIDER_MIN = -90;
	public static final int ZOOM_SLIDER_MAX = 90;
	public static final int ZOOM_SLIDER_INIT = 0;
	public static final int ZOOM_SLIDER_EXT = 0;
	public static final int ZOOM_SLIDER_MINOR_TICK_SPACING = 10;
	public static final int ZOOM_PADDING = 10;
	public static final int ZOOM_DEFAULT_SCALE = 100;

}
