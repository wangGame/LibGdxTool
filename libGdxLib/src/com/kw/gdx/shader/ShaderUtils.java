package com.kw.gdx.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaderUtils {
    private static ArrayMap<Integer,ShaderProgram> shaderPrograms;
    public static ShaderProgram program(String filv,String fileF){
        if (shaderPrograms == null){
            shaderPrograms = new ArrayMap<>();
        }
        int uniqueId = getUniqueId(fileF);
        if (shaderPrograms.containsKey(uniqueId)){
            return shaderPrograms.get(uniqueId);
        }
        ShaderProgram program = new ShaderProgram(
                    Gdx.files.internal(filv),
                    Gdx.files.internal(fileF));
        shaderPrograms.put(uniqueId,program);
        return program;
    }

    public static void dispose(){
        if (shaderPrograms!=null) {
            try {
                for (ObjectMap.Entry<Integer, ShaderProgram> shaderProgram : shaderPrograms) {
                    shaderProgram.value.dispose();
                }
            }catch (Exception e){
                System.out.println("dispose shader");
            }finally {
                if(shaderPrograms!=null) {
                    shaderPrograms.clear();
                }
                shaderPrograms = null;
            }
        }
    }

    public static int getUniqueId(String filePath) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(filePath.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString().hashCode();

    }
}
