//package com.kw.hct;
//
//public final class Hct {
//    //色相
//    private double hue;
//    //彩色
//    private double chroma;
//    //明暗
//    private double tone;
//    private int argb;
//
//    public static Hct from(double hue, double chroma, double tone) {
//        int argb = HctSolver.solveToInt(hue, chroma, tone);
//        return new Hct(argb);
//    }
//
//    /**
//     * Create an HCT color from a color.
//     *
//     * @param argb ARGB representation of a color.
//     * @return HCT representation of a color in default viewing conditions
//     */
//    public static Hct fromInt(int argb) {
//        return new Hct(argb);
//    }
//
//    private Hct(int argb) {
//        setInternalState(argb);
//    }
//
//    public double getHue() {
//        return hue;
//    }
//
//    public double getChroma() {
//        return chroma;
//    }
//
//    public double getTone() {
//        return tone;
//    }
//
//    public int toInt() {
//        return argb;
//    }
//
//    /**
//     * Set the hue of this color. Chroma may decrease because chroma has a different maximum for any
//     * given hue and tone.
//     *
//     * @param newHue 0 <= newHue < 360; invalid values are corrected.
//     */
//    public void setHue(double newHue) {
//        setInternalState(HctSolver.solveToInt(newHue, chroma, tone));
//    }
//
//    /**
//     * Set the chroma of this color. Chroma may decrease because chroma has a different maximum for
//     * any given hue and tone.
//     *
//     * @param newChroma 0 <= newChroma < ?
//     */
//    public void setChroma(double newChroma) {
//        setInternalState(HctSolver.solveToInt(hue, newChroma, tone));
//    }
//
//    /**
//     * Set the tone of this color. Chroma may decrease because chroma has a different maximum for any
//     * given hue and tone.
//     *
//     * @param newTone 0 <= newTone <= 100; invalid valids are corrected.
//     */
//    public void setTone(double newTone) {
//        setInternalState(HctSolver.solveToInt(hue, chroma, newTone));
//    }
//
//    @Override
//    public String toString() {
//        return "HCT("
//                + (int) Math.round(hue)
//                + ", "
//                + (int) Math.round(chroma)
//                + ", "
//                + (int) Math.round(tone)
//                + ")";
//    }
//
//    public static boolean isBlue(double hue) {
//        return hue >= 250 && hue < 270;
//    }
//
//    public static boolean isYellow(double hue) {
//        return hue >= 105 && hue < 125;
//    }
//
//    public static boolean isCyan(double hue) {
//        return hue >= 170 && hue < 207;
//    }
//
//    /**
//     * Translate a color into different ViewingConditions.
//     *
//     * <p>Colors change appearance. They look different with lights on versus off, the same color, as
//     * in hex code, on white looks different when on black. This is called color relativity, most
//     * famously explicated by Josef Albers in Interaction of Color.
//     *
//     * <p>In color science, color appearance models can account for this and calculate the appearance
//     * of a color in different settings. HCT is based on CAM16, a color appearance model, and uses it
//     * to make these calculations.
//     *
//     * <p>See ViewingConditions.make for parameters affecting color appearance.
//     */
//    public Hct inViewingConditions(ViewingConditions vc) {
//        // 1. Use CAM16 to find XYZ coordinates of color in specified VC.
//        Cam16 cam16 = Cam16.fromInt(toInt());
//        double[] viewedInVc = cam16.xyzInViewingConditions(vc, null);
//
//        // 2. Create CAM16 of those XYZ coordinates in default VC.
//        Cam16 recastInVc =
//                Cam16.fromXyzInViewingConditions(
//                        viewedInVc[0], viewedInVc[1], viewedInVc[2], ViewingConditions.DEFAULT);
//
//        // 3. Create HCT from:
//        // - CAM16 using default VC with XYZ coordinates in specified VC.
//        // - L* converted from Y in XYZ coordinates in specified VC.
//        return Hct.from(
//                recastInVc.getHue(), recastInVc.getChroma(), ColorUtils.lstarFromY(viewedInVc[1]));
//    }
//
//    private void setInternalState(int argb) {
//        this.argb = argb;
//        Cam16 cam = Cam16.fromInt(argb);
//        hue = cam.getHue();
//        chroma = cam.getChroma();
//        this.tone = ColorUtils.lstarFromArgb(argb);
//    }
//}
