package com.vv.shenhua3.pcdd.manager;

public class AppGuideManager {
	
	public static final String GUIDE_1 = "shop_classify";
	public static final String GUIDE_2 = "choose_prod";
	public static final String GUIDE_3 = "to_cart";
	public static final String GUIDE_4 = "add_cart";

	private static boolean G1 = false;
	private static boolean G2 = false;
	private static boolean G3 = false;
	private static boolean G4 = false;

	public static boolean getGuide1() {
		return G1;
//		return PreferenceUtils.getPrefBoolean(PcddApp.applicationContext, GUIDE_1, false);
	}
	
	public static void setGuide1() {
		G1 = true;
//		PreferenceUtils.setPrefBoolean(PcddApp.applicationContext, GUIDE_1, true);
	}
	
	public static boolean getGuide2() {
		return false;
//		return G2;
//		return PreferenceUtils.getPrefBoolean(PcddApp.applicationContext, GUIDE_2, false);
	}

	public static void setGuide2() {
		G2 = true;
//		PreferenceUtils.setPrefBoolean(PcddApp.applicationContext, GUIDE_2, true);
	}
	
	public static boolean getGuide3() {
		return G3;
//		return PreferenceUtils.getPrefBoolean(PcddApp.applicationContext, GUIDE_3, false);
	}

	public static void setGuide3() {
		G3 = true;
//		PreferenceUtils.setPrefBoolean(PcddApp.applicationContext, GUIDE_3, true);
	}
	
	public static boolean getGuide4() {
		return G4;
//		return PreferenceUtils.getPrefBoolean(PcddApp.applicationContext, GUIDE_4, false);
	}

	public static void setGuide4() {
		G4 = true;
//		PreferenceUtils.setPrefBoolean(PcddApp.applicationContext, GUIDE_4, true);
	}

	public static void clear() {
		G1 = false;
		G2 = false;
		G3 = false;
		G4 = false;
	}
}
