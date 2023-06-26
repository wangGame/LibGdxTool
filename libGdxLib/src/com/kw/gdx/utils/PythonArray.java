package com.kw.gdx.utils;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class PythonArray extends Array<Object> implements Json.Serializable{
   @Override
   public void write(Json json) {
      json.writeArrayStart("items");
      for(int i=0;i<this.size;i++){
         json.writeValue(this.get(i));
      }
      json.writeArrayEnd();
   }

   @Override
   public void read(Json json, JsonValue jsonData) {
      JsonValue jv=jsonData.child();
      while(jv!=null) {
         if(jv.isObject()){
            this.add(json.readValue(PythonDict.class,jv));
         }else if(jv.isArray()){
            this.add(json.readValue(PythonArray.class,jv));
         }else {
            this.add(json.readValue(null,jv));
         }
         jv=jv.next();
      }
   }
}
