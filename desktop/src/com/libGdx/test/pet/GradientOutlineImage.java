package com.libGdx.test.pet;

//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.glutils.ShaderProgram;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//
//public class OutlineImage extends Image {
//
//    private final Texture texture;
//    private final ShaderProgram outlineShader;
//
//    private final Color outlineColor = new Color(1f, 0.92f, 0f, 1f);
//    private float outlineWidth = 15f;
//    private float alphaThreshold = 0.08f;
//
//    public OutlineImage(Texture texture) {
//        super(texture);
//        this.texture = texture;
//
//        ShaderProgram.pedantic = false;
//        outlineShader = new ShaderProgram(VERT, FRAG);
//
//        if (!outlineShader.isCompiled()) {
//            throw new RuntimeException(outlineShader.getLog());
//        }
//
//        setSize(texture.getWidth(), texture.getHeight());
//    }
//
//    public void setOutlineWidth(float outlineWidth) {
//        this.outlineWidth = outlineWidth;
//    }
//
//    public void setOutlineColor(Color color) {
//        this.outlineColor.set(color);
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        ShaderProgram oldShader = batch.getShader();
//
//        batch.setShader(outlineShader);
//
//        outlineShader.setUniformf(
//                "u_texelSize",
//                1f / texture.getWidth(),
//                1f / texture.getHeight()
//        );
//
//        outlineShader.setUniformf("u_radius", outlineWidth);
//        outlineShader.setUniformf("u_alphaThreshold", alphaThreshold);
//
//        outlineShader.setUniformf(
//                "u_outlineColor",
//                outlineColor.r,
//                outlineColor.g,
//                outlineColor.b,
//                outlineColor.a
//        );
//
//        super.draw(batch, parentAlpha);
//
//        batch.setShader(oldShader);
//    }
//
//    public void dispose() {
//        outlineShader.dispose();
//    }
//
//    private static final String VERT =
//            "attribute vec4 a_position;\n" +
//                    "attribute vec4 a_color;\n" +
//                    "attribute vec2 a_texCoord0;\n" +
//                    "\n" +
//                    "uniform mat4 u_projTrans;\n" +
//                    "\n" +
//                    "varying vec4 v_color;\n" +
//                    "varying vec2 v_texCoords;\n" +
//                    "\n" +
//                    "void main() {\n" +
//                    "    v_color = a_color;\n" +
//                    "    v_texCoords = a_texCoord0;\n" +
//                    "    gl_Position = u_projTrans * a_position;\n" +
//                    "}";
//
//    private static final String FRAG =
//            "#ifdef GL_ES\n" +
//                    "precision mediump float;\n" +
//                    "#endif\n" +
//                    "\n" +
//                    "varying vec4 v_color;\n" +
//                    "varying vec2 v_texCoords;\n" +
//                    "\n" +
//                    "uniform sampler2D u_texture;\n" +
//                    "uniform vec2 u_texelSize;\n" +
//                    "uniform float u_radius;\n" +
//                    "uniform vec4 u_outlineColor;\n" +
//                    "uniform float u_alphaThreshold;\n" +
//                    "\n" +
//                    "const int MAX_RADIUS = 10;\n" +
//                    "\n" +
//                    "void main() {\n" +
//                    "    vec4 baseColor = texture2D(u_texture, v_texCoords);\n" +
//                    "\n" +
//                    "    if (baseColor.a > u_alphaThreshold) {\n" +
//                    "        gl_FragColor = baseColor * v_color;\n" +
//                    "        return;\n" +
//                    "    }\n" +
//                    "\n" +
//                    "    float maxAlpha = 0.0;\n" +
//                    "\n" +
//                    "    for (int x = -MAX_RADIUS; x <= MAX_RADIUS; x++) {\n" +
//                    "        for (int y = -MAX_RADIUS; y <= MAX_RADIUS; y++) {\n" +
//                    "            float fx = float(x);\n" +
//                    "            float fy = float(y);\n" +
//                    "            float dist = sqrt(fx * fx + fy * fy);\n" +
//                    "\n" +
//                    "            if (dist <= u_radius) {\n" +
//                    "                vec2 offset = vec2(fx, fy) * u_texelSize;\n" +
//                    "                float sampleAlpha = texture2D(u_texture, v_texCoords + offset).a;\n" +
//                    "                maxAlpha = max(maxAlpha, sampleAlpha);\n" +
//                    "            }\n" +
//                    "        }\n" +
//                    "    }\n" +
//                    "\n" +
//                    "    if (maxAlpha > u_alphaThreshold) {\n" +
//                    "        float edgeAlpha = smoothstep(u_alphaThreshold, 1.0, maxAlpha);\n" +
//                    "        gl_FragColor = vec4(\n" +
//                    "            u_outlineColor.rgb,\n" +
//                    "            u_outlineColor.a * edgeAlpha * v_color.a\n" +
//                    "        );\n" +
//                    "    } else {\n" +
//                    "        gl_FragColor = vec4(0.0);\n" +
//                    "    }\n" +
//                    "}";
//    }

//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.glutils.ShaderProgram;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.InputListener;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//
//public class GradientOutlineImage extends Image {
//
//    private final Texture texture;
//    private final ShaderProgram outlineShader;
//
//    private boolean outlineEnabled = false;
//
//    /**
//     * 描边宽度，单位是像素
//     */
//    private float outlineRadius = 16f;
//
//    /**
//     * alpha 判断阈值
//     * PNG 透明区域一般 alpha = 0
//     * 角色区域 alpha = 1
//     */
//    private float alphaThreshold = 0.08f;
//
//    /**
//     * 内圈颜色：靠近角色边缘的位置
//     */
//    private final Color innerColor = new Color(1f, 0f, 0f, 1f);
//
//    /**
//     * 外圈颜色：最外层渐隐的位置
//     */
//    private final Color outerColor = new Color(1f, 0f, 0f, 0.1f);
//
//    public GradientOutlineImage(Texture texture) {
//        super(texture);
//        this.texture = texture;
//
//        ShaderProgram.pedantic = false;
//        outlineShader = new ShaderProgram(VERT_SHADER, FRAG_SHADER);
//
//        if (!outlineShader.isCompiled()) {
//            throw new RuntimeException("Outline shader compile failed:\n" + outlineShader.getLog());
//        }
//
//        setSize(texture.getWidth(), texture.getHeight());
//
//        addHoverListener();
//    }
//
//    private void addHoverListener() {
//        addListener(new InputListener() {
//            @Override
//            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
//                setOutlineEnabled(true);
//            }
//
//            @Override
//            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
//                setOutlineEnabled(false);
//            }
//        });
//    }
//
//    public void setOutlineEnabled(boolean outlineEnabled) {
//        this.outlineEnabled = outlineEnabled;
//    }
//
//    public boolean isOutlineEnabled() {
//        return outlineEnabled;
//    }
//
//    public void setOutlineRadius(float outlineRadius) {
//        this.outlineRadius = outlineRadius;
//    }
//
//    public void setAlphaThreshold(float alphaThreshold) {
//        this.alphaThreshold = alphaThreshold;
//    }
//
//    public void setInnerColor(Color color) {
//        this.innerColor.set(color);
//    }
//
//    public void setOuterColor(Color color) {
//        this.outerColor.set(color);
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        if (!outlineEnabled) {
//            super.draw(batch, parentAlpha);
//            return;
//        }
//
//        ShaderProgram oldShader = batch.getShader();
//
//        batch.setShader(outlineShader);
//
//        outlineShader.setUniformf(
//                "u_texelSize",
//                1f / texture.getWidth(),
//                1f / texture.getHeight()
//        );
//
//        outlineShader.setUniformf("u_radius", outlineRadius);
//        outlineShader.setUniformf("u_alphaThreshold", alphaThreshold);
//
//        outlineShader.setUniformf(
//                "u_innerColor",
//                innerColor.r,
//                innerColor.g,
//                innerColor.b,
//                innerColor.a
//        );
//
//        outlineShader.setUniformf(
//                "u_outerColor",
//                outerColor.r,
//                outerColor.g,
//                outerColor.b,
//                outerColor.a
//        );
//
//        super.draw(batch, parentAlpha);
//
//        batch.setShader(oldShader);
//    }
//
//    public void dispose() {
//        outlineShader.dispose();
//    }
//
//    private static final String VERT_SHADER =
//            "attribute vec4 a_position;\n" +
//                    "attribute vec4 a_color;\n" +
//                    "attribute vec2 a_texCoord0;\n" +
//                    "\n" +
//                    "uniform mat4 u_projTrans;\n" +
//                    "\n" +
//                    "varying vec4 v_color;\n" +
//                    "varying vec2 v_texCoords;\n" +
//                    "\n" +
//                    "void main() {\n" +
//                    "    v_color = a_color;\n" +
//                    "    v_texCoords = a_texCoord0;\n" +
//                    "    gl_Position = u_projTrans * a_position;\n" +
//                    "}";
//
//    private static final String FRAG_SHADER =
//            "#ifdef GL_ES\n" +
//                    "precision mediump float;\n" +
//                    "#endif\n" +
//                    "\n" +
//                    "varying vec4 v_color;\n" +
//                    "varying vec2 v_texCoords;\n" +
//                    "\n" +
//                    "uniform sampler2D u_texture;\n" +
//                    "uniform vec2 u_texelSize;\n" +
//                    "uniform float u_radius;\n" +
//                    "uniform float u_alphaThreshold;\n" +
//                    "uniform vec4 u_innerColor;\n" +
//                    "uniform vec4 u_outerColor;\n" +
//                    "\n" +
//                    "const int MAX_RADIUS = 12;\n" +
//                    "\n" +
//                    "void main() {\n" +
//                    "    vec4 baseColor = texture2D(u_texture, v_texCoords);\n" +
//                    "\n" +
//                    "    if (baseColor.a > u_alphaThreshold) {\n" +
//                    "        gl_FragColor = baseColor * v_color;\n" +
//                    "        return;\n" +
//                    "    }\n" +
//                    "\n" +
//                    "    float nearestDist = 9999.0;\n" +
//                    "    float found = 0.0;\n" +
//                    "\n" +
//                    "    for (int x = -MAX_RADIUS; x <= MAX_RADIUS; x++) {\n" +
//                    "        for (int y = -MAX_RADIUS; y <= MAX_RADIUS; y++) {\n" +
//                    "            float fx = float(x);\n" +
//                    "            float fy = float(y);\n" +
//                    "            float dist = sqrt(fx * fx + fy * fy);\n" +
//                    "\n" +
//                    "            if (dist <= u_radius) {\n" +
//                    "                vec2 offset = vec2(fx, fy) * u_texelSize;\n" +
//                    "                float sampleAlpha = texture2D(u_texture, v_texCoords + offset).a;\n" +
//                    "\n" +
//                    "                if (sampleAlpha > u_alphaThreshold) {\n" +
//                    "                    found = 1.0;\n" +
//                    "                    nearestDist = min(nearestDist, dist);\n" +
//                    "                }\n" +
//                    "            }\n" +
//                    "        }\n" +
//                    "    }\n" +
//                    "\n" +
//                    "    if (found > 0.5) {\n" +
//                    "        float t = 1.0 - clamp(nearestDist / u_radius, 0.0, 1.0);\n" +
//                    "\n" +
//                    "        float alpha = smoothstep(0.0, 1.0, t);\n" +
//                    "\n" +
//                    "        vec4 outlineColor = mix(u_outerColor, u_innerColor, alpha);\n" +
//                    "\n" +
//                    "        gl_FragColor = vec4(\n" +
//                    "            outlineColor.rgb,\n" +
//                    "            outlineColor.a * alpha * v_color.a\n" +
//                    "        );\n" +
//                    "    } else {\n" +
//                    "        gl_FragColor = vec4(0.0);\n" +
//                    "    }\n" +
//                    "}";
//}


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GradientOutlineImage extends Image {

    private final Texture texture;
    private final ShaderProgram outlineShader;

    private boolean outlineEnabled = false;

    /** 外部渐变半径 */
    private float outlineRadius = 8f;

    /** 向图片内部扩展多少像素，防止和图片之间有缝 */
    private float innerExpand = 2f;

    /** 向内部扩展部分的强度，建议 0.2 ~ 0.5 */
    private float innerStrength = 0.5f;

    /** alpha 阈值 */
    private float alphaThreshold = 0.05f;

    /** 内圈颜色（靠近图片边缘） */
    private final Color innerColor = new Color(1f, 0.15f, 0.15f, 1f);

    /** 外圈颜色（最外层） */
    private final Color outerColor = new Color(1f, 0f, 0f, 0.12f);

    public GradientOutlineImage(Texture texture) {
        super(texture);
        this.texture = texture;

        ShaderProgram.pedantic = false;
        outlineShader = new ShaderProgram(VERT_SHADER, FRAG_SHADER);

        if (!outlineShader.isCompiled()) {
            throw new RuntimeException("Outline shader compile failed:\n" + outlineShader.getLog());
        }

        setSize(texture.getWidth(), texture.getHeight());
        addHoverListener();
    }

    private void addHoverListener() {
        addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                setOutlineEnabled(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                setOutlineEnabled(false);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!outlineEnabled) {
            super.draw(batch, parentAlpha);
            return;
        }

        ShaderProgram oldShader = batch.getShader();
        batch.setShader(outlineShader);

        outlineShader.setUniformf("u_texelSize",
                1f / texture.getWidth(),
                1f / texture.getHeight());

        outlineShader.setUniformf("u_radius", outlineRadius);
        outlineShader.setUniformf("u_innerWidth", -innerExpand);
        outlineShader.setUniformf("u_innerStrength", innerStrength);
        outlineShader.setUniformf("u_alphaThreshold", alphaThreshold);

        outlineShader.setUniformf("u_innerColor",
                innerColor.r, innerColor.g, innerColor.b, innerColor.a);

        outlineShader.setUniformf("u_outerColor",
                outerColor.r, outerColor.g, outerColor.b, outerColor.a);

        super.draw(batch, parentAlpha);

        batch.setShader(oldShader);
    }

    public void dispose() {
        outlineShader.dispose();
    }

    // =========================
    // setters / getters
    // =========================

    public void setOutlineEnabled(boolean outlineEnabled) {
        this.outlineEnabled = outlineEnabled;
    }

    public boolean isOutlineEnabled() {
        return outlineEnabled;
    }

    public void setOutlineRadius(float outlineRadius) {
        this.outlineRadius = outlineRadius;
    }

    public float getOutlineRadius() {
        return outlineRadius;
    }

    public void setInnerExpand(float innerExpand) {
        this.innerExpand = innerExpand;
    }

    public float getInnerExpand() {
        return innerExpand;
    }

    public void setInnerStrength(float innerStrength) {
        this.innerStrength = innerStrength;
    }

    public float getInnerStrength() {
        return innerStrength;
    }

    public void setAlphaThreshold(float alphaThreshold) {
        this.alphaThreshold = alphaThreshold;
    }

    public float getAlphaThreshold() {
        return alphaThreshold;
    }

    public void setInnerColor(Color color) {
        this.innerColor.set(color);
    }

    public void setOuterColor(Color color) {
        this.outerColor.set(color);
    }

    public Color getInnerColor() {
        return innerColor;
    }

    public Color getOuterColor() {
        return outerColor;
    }

    // =========================
    // shaders
    // =========================

    private static final String VERT_SHADER =
            "attribute vec4 a_position;\n" +
                    "attribute vec4 a_color;\n" +
                    "attribute vec2 a_texCoord0;\n" +
                    "\n" +
                    "uniform mat4 u_projTrans;\n" +
                    "\n" +
                    "varying vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    v_color = a_color;\n" +
                    "    v_texCoords = a_texCoord0;\n" +
                    "    gl_Position = u_projTrans * a_position;\n" +
                    "}";

    private static final String FRAG_SHADER =
            "#ifdef GL_ES\n" +
                    "precision mediump float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "\n" +
                    "uniform sampler2D u_texture;\n" +
                    "uniform vec2 u_texelSize;\n" +
                    "uniform float u_radius;\n" +
                    "uniform float u_innerWidth;\n" +
                    "uniform float u_innerStrength;\n" +
                    "uniform float u_alphaThreshold;\n" +
                    "uniform vec4 u_innerColor;\n" +
                    "uniform vec4 u_outerColor;\n" +
                    "\n" +
                    "const int MAX_RADIUS = 16;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec4 baseColor = texture2D(u_texture, v_texCoords);\n" +
                    "\n" +
                    "    // =========================\n" +
//                    "    // 1) 图片本体区域：支持向内扩一点 glow，填掉缝隙\n" +
                    "    // =========================\n" +
                    "    if (baseColor.a > u_alphaThreshold) {\n" +
                    "        vec4 finalColor = baseColor * v_color;\n" +
                    "\n" +
                    "        if (u_innerWidth > 0.0) {\n" +
                    "            float nearestTransparentDist = 9999.0;\n" +
                    "            float foundTransparent = 0.0;\n" +
                    "\n" +
                    "            for (int x = -MAX_RADIUS; x <= MAX_RADIUS; x++) {\n" +
                    "                for (int y = -MAX_RADIUS; y <= MAX_RADIUS; y++) {\n" +
                    "                    float fx = float(x);\n" +
                    "                    float fy = float(y);\n" +
                    "                    float dist = sqrt(fx * fx + fy * fy);\n" +
                    "\n" +
                    "                    if (dist <= u_innerWidth) {\n" +
                    "                        vec2 offset = vec2(fx, fy) * u_texelSize;\n" +
                    "                        float sampleAlpha = texture2D(u_texture, v_texCoords + offset).a;\n" +
                    "\n" +
                    "                        if (sampleAlpha <= u_alphaThreshold) {\n" +
                    "                            foundTransparent = 1.0;\n" +
                    "                            nearestTransparentDist = min(nearestTransparentDist, dist);\n" +
                    "                        }\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            }\n" +
                    "\n" +
                    "            if (foundTransparent > 0.5) {\n" +
                    "                float tIn = 1.0 - clamp(nearestTransparentDist / u_innerWidth, 0.0, 1.0);\n" +
                    "                float innerAlpha = smoothstep(0.0, 2.0, tIn) * u_innerStrength;\n" +
                    "\n" +
//                    "                // 用 mix 柔和叠进去，不会一下子把图染得太红\n" +
                    "                finalColor.rgb = mix(finalColor.rgb, u_innerColor.rgb, innerAlpha);\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        gl_FragColor = finalColor;\n" +
                    "        return;\n" +
                    "    }\n" +
                    "\n" +
                    "    // =========================\n" +
                    "    // 2) 图片外部区域：正常画向外渐变 glow\n" +
                    "    // =========================\n" +
                    "    float nearestDist = 9999.0;\n" +
                    "    float found = 0.0;\n" +
                    "\n" +
                    "    for (int x = -MAX_RADIUS; x <= MAX_RADIUS; x++) {\n" +
                    "        for (int y = -MAX_RADIUS; y <= MAX_RADIUS; y++) {\n" +
                    "            float fx = float(x);\n" +
                    "            float fy = float(y);\n" +
                    "            float dist = sqrt(fx * fx + fy * fy);\n" +
                    "\n" +
                    "            if (dist <= u_radius) {\n" +
                    "                vec2 offset = vec2(fx, fy) * u_texelSize;\n" +
                    "                float sampleAlpha = texture2D(u_texture, v_texCoords + offset).a;\n" +
                    "\n" +
                    "                if (sampleAlpha > u_alphaThreshold) {\n" +
                    "                    found = 1.0;\n" +
                    "                    nearestDist = min(nearestDist, dist);\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    if (found > 0.5) {\n" +
                    "        float t = 1.0 - clamp(nearestDist / u_radius, 0.0, 1.0);\n" +
                    "        float alpha = smoothstep(0.0, 1.0, t);\n" +
                    "\n" +
                    "        vec4 outlineColor = mix(u_outerColor, u_innerColor, alpha);\n" +
                    "\n" +
                    "        gl_FragColor = vec4(\n" +
                    "            outlineColor.rgb,\n" +
                    "            outlineColor.a * alpha * v_color.a\n" +
                    "        );\n" +
                    "    } else {\n" +
                    "        gl_FragColor = vec4(0.0);\n" +
                    "    }\n" +
                    "}";
}