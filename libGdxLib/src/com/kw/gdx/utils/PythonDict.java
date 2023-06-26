package com.kw.gdx.utils;


import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class PythonDict extends ArrayMap<String,Object> implements Json.Serializable{
   @Override
   public void write(Json json) {
//        json.writeObjectStart();
      for(String k:this.keys()){
         Object value = this.get(k);
         if(value instanceof PythonArray) {
            PythonArray pa=(PythonArray) value;
            json.writeArrayStart(k);
            for(int i=0;i<pa.size;i++){
               json.writeValue(pa.get(i));
            }
            json.writeArrayEnd();
         }
         else {
            json.writeValue(k, value);
         }
      }
//        json.writeObjectEnd();
   }

   @Override
   public void read(Json json, JsonValue jsonData) {
      JsonValue jv=jsonData.child();
      while(jv!=null) {
         if(jv.isObject()){
            this.put(jv.name,json.readValue(PythonDict.class,jv));
         }else if(jv.isArray()){
            this.put(jv.name,json.readValue(PythonArray.class,jv));
         }else {
            this.put(jv.name,json.readValue(null,jv));
         }
         jv=jv.next();
      }
   }

   public <T> T get(String key,Class<T> type) {
      return (T)get(key);
   }
}
