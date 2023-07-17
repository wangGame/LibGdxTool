package com.libGdx.test.json;

import com.badlogic.gdx.math.Vector2;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/27 16:58
 */

public class TestLevel {
    public int popNum = 5;
    public int speed = 50;
    public int num = 50;
    public Vector2[] pos;
    public int shootType = 0;
    public int ballNum;//颜色数量
    public int coinNum;//颜色数量
    public int coinMultiple;//颜色数量
    public int shellNum;//颜色数量
    public int shellTargetCount;//颜色数量
    public int collectId;//颜色数量
    public boolean shellIsTarget;//颜色数量
    /** 具体颜色序号 0-7: 红,蓝,黄,紫,绿,橙,粉,青 */
    public int[] ballBaseColors;

    public int[] collectProbability;

    public int levelType;
    public int time;
    public int star1Score;
    public int star2Score;
    public int star3Score;
    public int scoreTarget;
    public int bossTargetId;

    public int energyMax;

    public int stoneNum;
    public boolean stoneIsTarget;
    public int[] stonePos;
    public int[] stoneIces;

    public int iceNum;
    public boolean iceIsTarget;
    public int[] icePos;
    public int[] ices;

    public int cageNum;
    public boolean cageIsTarget;
    public int[] cagePos;

    public int coverNum;
    public boolean coverIsTarget;
    public int[] coverPos;

    public int birdNum;
    public boolean birdIsTarget;
    public int[] birdPos;
    public int[] birdIces;

    public int keyNum;
    public boolean keyIsTarget;
    public int[] keyPos;
    public int[] keyBirdPos;

    public int lavaNum;
    public boolean lavaIsTarget;
    public int[] lavaPos;
    public int[] lavaIces;

    /** 被用作蝴蝶球 */
    public int tricolorNum;
    public boolean tricolorIsTarget;
    public int[] tricolorPos;
    public int[] tricolorIces;

    /** 鸟蛋球 */
    public int butterFlyNum;
    public boolean butterFlyIsTarget;
    public int[] butterFlyPos;
    public int[] butterFlyIces;

    public int discolorNum;
    public int discolorTime;
    public int[] discolorPos;
    public int[] discolorIces;

    public int lightningNum;
    public int[] lightningPos;

    public int blueStoneNum;
    public boolean blueStoneIsTarget;
    public int[] blueStonePos;

    public int beeNum;
    public boolean beeIsTarget;
    public int[] beePos;
    public int[] beeIces;

    public int spiderNum;
    public boolean spiderIsTarget;
    public int[] spiderPos;

    public int woodNum;
    public int[] woodPos;

    public int[] magicCounts;
    public int[] magicPRs;

    public int[] splitBubbleCounts;
    public int[] splitBubblePRs;

    public int[] glowwormCounts;
    public int[] glowwormPRs;

    //罩子,网球,冰球,蝙蝠球,变色球,闪电球

    public int[] coverCounts;
    public int[] coverPRs;

    public int[] cageCounts;
    public int[] cagePRs;

    public int[] iceCounts;
    public int[] iceLayerPRs;//2层概率,3层概率. 默认最大值100,缺省概率为1层冰
    public int[] icePRs;

    public int[] lavaCounts;
    public int[] lavaPRs;
    public int[] lavaIceLayerPRs;

    public int[] colorChangeCounts;
    public int[] colorChangePRs;
    public int[] colorChangeIceLayerPRs;//1层概率,2层概率,3层概率. 默认最大值100,缺省概率为0层冰

    public int[] lightningCounts;
    public int[] lightningPRs;

    public int[] ballPRs;
    public boolean isDifficult;
    public Vector2[] rail1;
    public Vector2[] rail2;
}

